package com.example.project;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.Observer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class admin_home_page_fragment extends Fragment {

    private static final String ARG_USERNAME = "username";
    private static final String ARG_HOSTEL_BLOCK = "hostelBlock";

    private String username;
    private String hostelBlock;


    public admin_home_page_fragment() {

    }
    public static admin_home_page_fragment newInstance(String username, String hostelBlock) {
        admin_home_page_fragment fragment = new admin_home_page_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        args.putString(ARG_HOSTEL_BLOCK, hostelBlock);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(ARG_USERNAME);
            hostelBlock = getArguments().getString(ARG_HOSTEL_BLOCK);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_home_page_fragment, container, false);
        TextView usernameTextView = view.findViewById(R.id.name_home);
        TextView hostelBlockTextView = view.findViewById(R.id.hostelBlock_home);
        usernameTextView.setText(username);
        hostelBlockTextView.setText(hostelBlock);
        Button attendanceButton = view.findViewById(R.id.attendance_button);
        attendanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("attendance");
                reference.setValue(true);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {

            String hostelBlock = getArguments().getString(ARG_HOSTEL_BLOCK);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            TextView presentDaysNoTextView = view.findViewById(R.id.presentDaysNo);
            TextView absentDaysNoTextView = view.findViewById(R.id.absentDaysNo);

            database.getReference("attendance").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int presentCount = 0;
                    int absentCount = 0;

                    String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    if (snapshot.child(currentDate).child(hostelBlock).exists()) {
                        DataSnapshot hostelBlockSnapshot = snapshot.child(currentDate).child(hostelBlock);


                        for (DataSnapshot childSnapshot : hostelBlockSnapshot.getChildren()) {
                            String status = childSnapshot.child("status").getValue(String.class);
                            if (status != null && status.equals("present")) {
                                presentCount++;
                            } else {
                                absentCount++;
                            }
                        }
                    }

                    // Update the UI with the present and absent counts
                    presentDaysNoTextView.setText(String.valueOf(presentCount));
                    absentDaysNoTextView.setText(String.valueOf(absentCount));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle errors
                }
            });
        }
    }
}
