package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.project.databinding.ActivityDashboardBinding;

public class Dashboard extends AppCompatActivity {

    ActivityDashboardBinding binding;
    String Name;
    String HostelBlock;
    String RoomNo;
    String RegNo;
    String Email;
    String TAG = "location";

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private Location lastLocation;
    private Context context;

    private static final String GEOFENCE_ID = "student_geofence";
    private static final float GEOFENCE_RADIUS = 1.0f; // Radius in meters
    private static final double GEOFENCE_LATITUDE = 13.125046;
    private static final double GEOFENCE_LONGITUDE = 77.590459;
    private GeofencingClient geofencingClient;
    private PendingIntent geofencePendingIntent;
    private static final int REQUEST_LOCATION_PERMISSION = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Name = getIntent().getStringExtra("name");
        HostelBlock = getIntent().getStringExtra("hostelBlock");
        RoomNo = getIntent().getStringExtra("roomNo");
        RegNo = getIntent().getStringExtra("regNo");
        Email = getIntent().getStringExtra("email");
        context = getApplicationContext();
        checkLocationPermission();
        init();
        geofencingClient = LocationServices.getGeofencingClient(this);

        if (savedInstanceState == null) {
            replaceFragment(Student_HomePage.newInstance(Name, HostelBlock, RegNo));
        }

        binding.studentBottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home_screen) {
                replaceFragment(Student_HomePage.newInstance(Name, HostelBlock, RegNo));
            } else if (itemId == R.id.profile_screen) {
                replaceFragment(Student_ProfilePage.newInstance(Name, HostelBlock, RoomNo, RegNo, Email));
            }
            return true;
        });
    }

    private void checkLocationPermission() {
        Log.d(TAG, "checkLocationPermission: ");

        if (ContextCompat.checkSelfPermission(Dashboard.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Dashboard.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(Dashboard.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                ActivityCompat.requestPermissions(Dashboard.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        } else {
            if (geofencingClient != null) {
                addGeofence();
            } else {

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(Dashboard.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Granted ", Toast.LENGTH_SHORT).show();
                        if (geofencingClient != null) {
                            addGeofence();
                        } else {
                            // Handle the case where geofencingClient is still null
                        }
                    }
                }
                return;
        }
    }

    private void addGeofence() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Location permissions granted, proceed with adding the geofence
            Geofence geofence = new Geofence.Builder()
                    .setRequestId(GEOFENCE_ID)
                    .setCircularRegion(GEOFENCE_LATITUDE, GEOFENCE_LONGITUDE, GEOFENCE_RADIUS)
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build();

            GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
            builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
            builder.addGeofence(geofence);

            try {
                geofencingClient.addGeofences(builder.build(), getGeofencePendingIntent())
                        .addOnSuccessListener(aVoid -> {
                            // Geofence added successfully
                        })
                        .addOnFailureListener(e -> {
                            // Failed to add geofence
                        });
            } catch (SecurityException e) {
                Toast.makeText(this, "Failed to add geofence: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            // Location permissions not granted, request them again
            requestLocationPermissions();
        }
    }

    private PendingIntent getGeofencePendingIntent() {
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }

        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
        geofencePendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        return geofencePendingIntent;
    }

    private void requestLocationPermissions() {
        // Request location permissions
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION}, REQUEST_LOCATION_PERMISSION);
    }

    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // Handle location updates
                if (locationResult != null) {
                    for (Location location : locationResult.getLocations()) {
                        Log.d("Location Update", "Location: " + location.getLatitude() + ", " + location.getLongitude());
                    }
                } else {
                    Log.d("Location Update", "Location: null");
                }
            }
        };

        mLocationRequest = LocationRequest.create()
                .setInterval(5000)
                .setFastestInterval(500)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(100);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(locationSettingsResponse -> {
                    Log.d(TAG, "location settings are okay: ");
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                })
                .addOnFailureListener(e -> {
                    int statusCode = ((ApiException) e).getStatusCode();
                    Log.d(TAG, "inside error: " + statusCode);
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(task -> {
                    Log.d(TAG, "stop location updates ");
                });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (fragment instanceof Student_HomePage) {
            ((Student_HomePage) fragment).setGeofencingClient(geofencingClient,mFusedLocationClient);
        }

        fragmentTransaction.replace(R.id.student_frame_layout, fragment);
        fragmentTransaction.commit();
    }
}