package com.example.project;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executor;

public class Student_HomePage extends Fragment {

    private static final String ARG_NAME = "Name";
    private static final String ARG_HOSTEL_BLOCK = "HostelBlock";
    private static final String ARG_REG_NO = "RegNo";

    private String Name;
    private String HostelBlock;
    private String RegNo;
    static Button attendanceButton;

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private GeofencingClient geofencingClient;
    private FusedLocationProviderClient mFusedLocationClient;
    public static boolean isAttendanceButtonEnabled = false;
    private TextView presentDaysTextView;
    private TextView absentDaysTextView;

    private static final String GEOFENCE_ID = "student_geofence";
    private static final float GEOFENCE_RADIUS = 1.0f; // Radius in meters
    private static final double GEOFENCE_LATITUDE = 13.125046;
    private static final double GEOFENCE_LONGITUDE = 77.590459;


    public Student_HomePage() {
    }

    public static Student_HomePage newInstance(String Name, String HostelBlock, String RegNo) {
        Student_HomePage fragment = new Student_HomePage();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, Name);
        args.putString(ARG_HOSTEL_BLOCK, HostelBlock);
        args.putString(ARG_REG_NO, RegNo);
        fragment.setArguments(args);
        return fragment;
    }

    public void setGeofencingClient(GeofencingClient geofencingClient, FusedLocationProviderClient mFusedLocationClient) {
        this.geofencingClient = geofencingClient;
        this.mFusedLocationClient = mFusedLocationClient;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Name = getArguments().getString(ARG_NAME);
            HostelBlock = getArguments().getString(ARG_HOSTEL_BLOCK);
            RegNo = getArguments().getString(ARG_REG_NO);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student__home_page, container, false);
        TextView nameTextView = view.findViewById(R.id.student_name_home);
        TextView hostelBlockTextView = view.findViewById(R.id.student_hostelBlock_home);

        nameTextView.setText(Name);
        hostelBlockTextView.setText(HostelBlock);
        attendanceButton = view.findViewById(R.id.attendance_button);

        geofencingClient = LocationServices.getGeofencingClient(requireContext());
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        presentDaysTextView = view.findViewById(R.id.presentDaysNo);
        absentDaysTextView = view.findViewById(R.id.absentDaysNo);


        fetchAndUpdateAttendanceData();

        // Enable/disable the attendance button based on time
        DatabaseReference attendanceRef = FirebaseDatabase.getInstance().getReference("attendance");
        attendanceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // Get the current time
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                Log.d("Time Check", "Current hour: " + hour);

                isInsideGeofence(new OnGeofenceStatusListener() {
                    @Override
                    public void onGeofenceStatus(boolean isInside) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (hour == 11 && minute == 20) {
                                    sendAttendanceStartedBroadcast();
                                } else if (hour == 11 && minute == 21) {
                                    sendAttendanceEndingSoonBroadcast();
                                }

                                if (isInside && hour >=10 && hour <1630) {
                                    enableAttendanceButton();
                                    Log.d("Attendance Button", "Attendance button enabled");
                                } else {
                                    if (!isInside) {
                                        Log.d("Attendance Button", "Attendance button disabled (not inside geofence)");
                                    } else {
                                        Log.d("Attendance Button", "Attendance button disabled (outside time window)");
                                    }
                                    disableAttendanceButton();
                                }
                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        // Initialize the BiometricPrompt
        executor = ContextCompat.getMainExecutor(requireContext());
        biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                // Handle authentication error
                Toast.makeText(requireContext(), "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                // Proceed with marking attendance
                markAttendance();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                // Handle authentication failure
                Toast.makeText(requireContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });

        // Set up the PromptInfo
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Authenticate to mark attendance")
                .setNegativeButtonText("cancel")
                .build();

        attendanceButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {
                // Check if the button is enabled
                Log.d("Button Status", "Is attendance button enabled: " + isAttendanceButtonEnabled);
                if (isAttendanceButtonEnabled) {
                    // Show the BiometricPrompt
                    biometricPrompt.authenticate(promptInfo);
                }
                else {

                    Toast.makeText(requireContext(), "Attendance marking is not allowed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
    private void sendAttendanceStartedBroadcast() {
        Intent intent = new Intent("attendance_started");
        requireContext().sendBroadcast(intent);
    }

    private void sendAttendanceEndingSoonBroadcast() {
        Intent intent = new Intent("attendance_ending_soon");
        requireContext().sendBroadcast(intent);
    }
    private void fetchAndUpdateAttendanceData() {
        // Get the current date
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Get a reference to the attendance data for the current date and the student's hostel block
        DatabaseReference attendanceRef = FirebaseDatabase.getInstance().getReference("attendance")
                .child(currentDate)
                .child(HostelBlock);

        // Fetch the attendance data and update the UI
        attendanceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int presentCount = 0;
                int absentCount = 0;

                for (DataSnapshot studentSnapshot : snapshot.getChildren()) {
                    String status = studentSnapshot.child("status").getValue(String.class);
                    if (status != null && status.equals("present")) {
                        presentCount++;
                    } else {
                        absentCount++;
                    }
                }

                presentDaysTextView.setText(String.valueOf(presentCount));
                absentDaysTextView.setText(String.valueOf(absentCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors
            }
        });
    }

    private void isInsideGeofence(OnGeofenceStatusListener listener) {
        if (getContext() != null && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<Location> locationTask = mFusedLocationClient.getLastLocation();
            locationTask.addOnSuccessListener(location -> {
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    float[] distance = new float[1];
                    Location.distanceBetween(latitude, longitude, GEOFENCE_LATITUDE, GEOFENCE_LONGITUDE, distance);
                    boolean isInsideGeofence = distance[0] <= GEOFENCE_RADIUS;
                    Log.d("Geofence Status", "Is inside geofence: " + isInsideGeofence);
                    // Check if the fragment is attached to an activity before updating the UI
                    if (getActivity() != null && isAdded()) {
                        getActivity().runOnUiThread(() -> listener.onGeofenceStatus(isInsideGeofence));
                    } else {
                        // Fragment is not attached or added, handle the case accordingly
                        listener.onGeofenceStatus(false);
                        Log.w("Student_HomePage", "Fragment not attached or added to activity, unable to update UI");
                    }
                } else {
                    // Location is not available
                    listener.onGeofenceStatus(false);
                }
            }).addOnFailureListener(e -> {
                // Handle any errors retrieving the user's location
                listener.onGeofenceStatus(false);
            }).addOnCompleteListener(task -> {
                // Wait for the task to complete before returning the result
            });
        } else {
            // Location permission is not granted
            listener.onGeofenceStatus(false);
        }
    }

    interface OnGeofenceStatusListener {
        void onGeofenceStatus(boolean isInside);
    }

    public static void enableAttendanceButton() {
        isAttendanceButtonEnabled = true;
        attendanceButton.setEnabled(true);
        // Enable the attendance button
    }

    public static void disableAttendanceButton() {
        isAttendanceButtonEnabled = false;
        attendanceButton.setEnabled(false);
        // Disable the attendance button
    }


    private void markAttendance() {

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            // Store the attendance data in the "attendance" node
            DatabaseReference attendanceRef = FirebaseDatabase.getInstance().getReference("attendance").child(currentDate).child(HostelBlock).child(RegNo);
            Map<String, Object> attendanceData = new HashMap<>();
            attendanceData.put("name", Name);
            attendanceData.put("regNo", RegNo);
            attendanceData.put("hostelBlock", HostelBlock);
            attendanceData.put("status", "present");
            attendanceData.put("time", System.currentTimeMillis());
            attendanceRef.setValue(attendanceData);
            fetchAndUpdateAttendanceData();
        if (getActivity() != null) {
            attendanceRef.setValue(attendanceData).addOnSuccessListener(aVoid -> {

                fetchAndUpdateAttendanceData();
                getActivity().runOnUiThread(() -> Toast.makeText(requireContext(), "Attendance is marked", Toast.LENGTH_SHORT).show());
            }).addOnFailureListener(e -> {

                getActivity().runOnUiThread(() -> Toast.makeText(requireContext(), "Error marking attendance", Toast.LENGTH_SHORT).show());
            });
        } else {
            Log.w("Student_HomePage", "Fragment not attached to activity, unable to mark attendance");
        }
    }

    public static void markAllStudentsAbsent() {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("usersReg");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String regNo = userSnapshot.child("regNo").getValue(String.class);
                        String name = userSnapshot.child("name").getValue(String.class);
                        String hostelBlock = userSnapshot.child("hostelBlock").getValue(String.class);

                        // Check if the student has marked their attendance
                        DatabaseReference attendanceRef = FirebaseDatabase.getInstance().getReference("attendance").child(currentDate).child(hostelBlock).child(regNo);
                        attendanceRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (!snapshot.exists()) {
                                    // Student has not marked their attendance, mark them as absent
                                    Map<String, Object> attendanceData = new HashMap<>();
                                    attendanceData.put("name", name);
                                    attendanceData.put("regNo", regNo);
                                    attendanceData.put("hostelBlock", hostelBlock);
                                    attendanceData.put("status", "absent");
                                    attendanceRef.setValue(attendanceData);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("Database Error", error.getMessage());
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Database Error", error.getMessage());
            }
        });
    }
}