//package com.example.project;
//
//import android.app.IntentService;
//import android.content.Intent;
//import android.util.Log;
//
//import com.google.android.gms.location.Geofence;
//import com.google.android.gms.location.GeofencingEvent;
//
//import java.util.Calendar;
//
//public class GeofenceTransitionService extends IntentService {
//    public static final String ACTION_PROCESS_GEOFENCE = "com.example.project.action.PROCESS_GEOFENCE";
//
//    public GeofenceTransitionService() {
//
//        super("GeofenceTransitionService");
//    }
//
//    @Override
//    protected void onHandleIntent(Intent intent) {
//        if (intent != null && ACTION_PROCESS_GEOFENCE.equals(intent.getAction())) {
//            handleGeofenceTransition(intent);
//        }
//    }
//
//    private void handleGeofenceTransition(Intent intent) {
//        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
//        if (geofencingEvent.hasError()) {
//            Log.e("GeofenceTransitionService", "Geofence transition error: " + geofencingEvent.getErrorCode());
//            return;
//        }
//
//        int geofenceTransition = geofencingEvent.getGeofenceTransition();
//
//        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
//            // Student is inside the geofence, enable the attendance button
//            Student_HomePage.enableAttendanceButton();
//        } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
//
//            Student_HomePage.disableAttendanceButton();
//            // Student is outside the geofence, check the time and mark them absent if necessary
//            checkTimeAndMarkAbsent();
//        }
//    }
//
//    private void checkTimeAndMarkAbsent() {
//        // Get the current time
//        Calendar calendar = Calendar.getInstance();
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//
//
//        if (hour > 16) {
//            // Mark the student as absent
//            Student_HomePage.markAllStudentsAbsent();
//        }
//    }
//}