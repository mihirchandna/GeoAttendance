package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Student_ProfilePage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Student_ProfilePage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_NAME = "Name";
    private static final String ARG_HOSTEL_BLOCK = "HostelBlock";
    private static final String ARG_ROOMNO = "RoomNo";
    private static final String ARG_REGNO = "RegNo";
    private static final String ARG_EMAIL = "Email";

    // TODO: Rename and change types of parameters
    private String Name;
    private String HostelBlock;
    private String RoomNo;
    private String RegNo;
    private String Email;

    public Student_ProfilePage() {
        // Required empty public constructor
    }
    public static Student_ProfilePage newInstance(String Name, String HostelBlock, String RoomNo, String RegNo, String Email) {
        Student_ProfilePage fragment = new Student_ProfilePage();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, Name);
        args.putString(ARG_HOSTEL_BLOCK, HostelBlock);
        args.putString(ARG_ROOMNO, RoomNo);
        args.putString(ARG_REGNO, RegNo);
        args.putString(ARG_EMAIL, Email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Name = getArguments().getString(ARG_NAME);
            HostelBlock = getArguments().getString(ARG_HOSTEL_BLOCK);
            RoomNo = getArguments().getString(ARG_ROOMNO);
            RegNo = getArguments().getString(ARG_REGNO);
            Email = getArguments().getString(ARG_EMAIL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student__profile_page,container,false);
        TextInputLayout nameTextInputLayout = view.findViewById(R.id.name_profile);
        TextInputLayout hostelBlockTextInputLayout = view.findViewById(R.id.hostelBlock_profile);
        TextInputLayout roomNoTextInputLayout = view.findViewById(R.id.roomNo_profile);
        TextInputLayout regNoTextInputLayout = view.findViewById(R.id.RegNo_profile);
        TextInputLayout emailTextInputLayout = view.findViewById(R.id.email_profile);

        nameTextInputLayout.getEditText().setText(Name);
        hostelBlockTextInputLayout.getEditText().setText(HostelBlock);
        roomNoTextInputLayout.getEditText().setText(RoomNo);
        regNoTextInputLayout.getEditText().setText(RegNo);
        emailTextInputLayout.getEditText().setText(Email);

        Button logoutButton = view.findViewById(R.id.logout_button);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        return view;
    }

    private void logout() {
        // Clear Firebase Authentication state
        FirebaseAuth.getInstance().signOut();

        // Clear any cached user data or tokens
        // (Assuming you're using SharedPreferences to store user data)
        SharedPreferences preferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        // Clear any other cached data or session information

        // Navigate back to the login screen or the main activity
        Intent intent = new Intent(getActivity(), Login.class);
        startActivity(intent);
        requireActivity().finish();
    }

}