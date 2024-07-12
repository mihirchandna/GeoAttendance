//package com.example.project;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.location.LocationManager;
//
//import com.google.android.gms.location.Geofence;
//import com.google.android.gms.location.GeofencingClient;
//import com.google.android.gms.location.GeofencingRequest;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class GeofenceHelper {
//    private static final String GEOFENCE_REQUEST_ID = "GEOFENCE_REQUEST";
//    private GeofencingClient geofencingClient;
//    private Context context;
//
//    public GeofenceHelper(Context context) {
//        this.context = context;
//        geofencingClient = LocationServices.getGeofencingClient(context);
//    }
//
//    public void addGeofences(PendingIntent pendingIntent, String hostelBlock) {
//        if (!isLocationServicesEnabled(context)) {
//            showLocationServicesDisabledError();
//            return;
//        }
//
//        List<Geofence> geofenceList = new ArrayList<>();
//        List<LatLng> polygonBoundaries = getHostelBlockGeofenceBoundaries(hostelBlock);
//
//        if (polygonBoundaries.isEmpty()) {
//            showGeofencingError(new Exception("Invalid hostel block"));
//            return;
//        }
//
//        geofenceList.add(
//                new Geofence.Builder()
//                        .setRequestId(hostelBlock)
//                        .setCircularRegion(
//                                polygonBoundaries.get(0).latitude,
//                                polygonBoundaries.get(0).longitude,
//                                100 // Set an appropriate radius in meters
//                        )
//                        .setExpirationDuration(Geofence.NEVER_EXPIRE)
//                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
//                        .build()
//        );
//
//        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
//        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
//        builder.addGeofences(geofenceList);
//        GeofencingRequest geofencingRequest = builder.build();
//
//        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        // Geofences added successfully
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(Exception e) {
//                        showGeofencingError(e);
//                    }
//                });
//    }
//
//    private boolean isLocationServicesEnabled(Context context) {
//        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
//                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//    }
//
//    private void showLocationServicesDisabledError() {
//        // Show an error message or prompt the user to enable location services
//    }
//
//    private void showGeofencingError(Exception e) {
//        // Show an error message or handle the geofencing error
//    }
//
//    private List<LatLng> getHostelBlockGeofenceBoundaries(String hostelBlock) {
//        switch (hostelBlock) {
//            case "hostel_block_1":
//                return Arrays.asList(
//                        new LatLng(18.5204, 73.8567), // polygon coordinates for hostel block 1
//                        new LatLng(18.5206, 73.8569),
//                        // ...
//                        new LatLng(18.5204, 73.8567) // Close the polygon loop
//                );
//            case "hostel_block_2":
//                return Arrays.asList(
//                        new LatLng(18.5210, 73.8575), // polygon coordinates for hostel block 2
//                        new LatLng(18.5212, 73.8577),
//                        // ...
//                        new LatLng(18.5210, 73.8575) // Close the polygon loop
//                );
//            // Add more cases for other hostel blocks
//            default:
//                return new ArrayList<>();
//        }
//    }
//
//    public static boolean isPointInPolygon(LatLng point, List<LatLng> polygon) {
//        int j = polygon.size() - 1;
//        boolean odd = false;
//
//        for (int i = 0; i < polygon.size(); j = i++) {
//            LatLng pi = polygon.get(i);
//            LatLng pj = polygon.get(j);
//
//            if ((pi.latitude < point.latitude && pj.latitude >= point.latitude || pj.latitude < point.latitude && pi.latitude >= point.latitude) &&
//                    (pi.longitude <= point.longitude || pj.longitude <= point.longitude)) {
//                if (pi.latitude + (point.latitude - pi.latitude) / (pj.latitude - pi.latitude) * (pj.longitude - pi.longitude) < point.longitude) {
//                    odd = !odd;
//                }
//            }
//        }
//
//        return odd;
//    }
//}