package com.example.project;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            // Handle geofence error
            return;
        }
        int geofenceTransition = geofencingEvent.getGeofenceTransition();
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
                    Student_HomePage.enableAttendanceButton();
                } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
                    Student_HomePage.disableAttendanceButton();
                }
            }
        });
    }
}