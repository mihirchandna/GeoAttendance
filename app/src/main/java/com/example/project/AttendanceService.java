package com.example.project;
// AttendanceService.java
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.project.Student_HomePage;

public class AttendanceService extends Worker {

    public AttendanceService(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Call the markAllStudentsAbsent() method
        Student_HomePage.markAllStudentsAbsent();
        return Result.success();
    }
}
