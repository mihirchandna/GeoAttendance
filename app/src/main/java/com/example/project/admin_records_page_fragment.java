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


public class admin_records_page_fragment extends Fragment {

    private static final String ARG_USERNAME = "username";
    private static final String ARG_HOSTEL_BLOCK = "hostelBlock";
    private static final String ARG_ID = "id";

    private String username;
    private String hostelBlock;
    private String id;

//    AdminHomePageViewModel viewModel;

    public admin_records_page_fragment() {

    }
    public static admin_records_page_fragment newInstance(String username, String hostelBlock, String id) {
        admin_records_page_fragment fragment = new admin_records_page_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        args.putString(ARG_HOSTEL_BLOCK, hostelBlock);
        args.putString(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(ARG_USERNAME);
            hostelBlock = getArguments().getString(ARG_HOSTEL_BLOCK);
            id = getArguments().getString(ARG_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_records_page_fragment, container, false);
        TextView usernameTextView = view.findViewById(R.id.profileUsername);
        TextView hostelBlockTextView = view.findViewById(R.id.profileHostelBlock);
        TextView idTextView = view.findViewById(R.id.profileId);
        usernameTextView.setText(username);
        hostelBlockTextView.setText(hostelBlock);
        idTextView.setText(id);
    return view;
    }

}