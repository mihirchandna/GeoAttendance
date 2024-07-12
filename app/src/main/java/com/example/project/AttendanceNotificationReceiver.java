package com.example.project;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class AttendanceNotificationReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            if (action.equals("attendance_started")) {
                createNotificationChannel(context); // Call the method to create the notification channel
                sendAttendanceStartedNotification(context);
            } else if (action.equals("attendance_ending_soon")) {
                createNotificationChannel(context); // Call the method to create the notification channel
                sendAttendanceEndingSoonNotification(context);
            }
        }
    }

    private void createNotificationChannel(Context context) {
        // Check if the notification channel exists
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = notificationManager.getNotificationChannel("attendance_channel");
            if (channel == null) {
                // Create the notification channel
                channel = new NotificationChannel("attendance_channel", "Attendance Notifications", NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
    private void sendAttendanceStartedNotification(Context context) {
        // Create and display the "Attendance has started" notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "attendance_channel")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Attendance has started")
                .setContentText("You can now mark your attendance.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager.notify(1, builder.build());
    }


    private void sendAttendanceEndingSoonNotification(Context context) {
        // Create and display the "Attendance is ending soon" notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "attendance_channel")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Attendance is ending soon")
                .setContentText("You have 5 minutes left to mark your attendance.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager.notify(2, builder.build());
    }


}
