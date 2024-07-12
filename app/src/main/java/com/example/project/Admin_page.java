package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.example.project.databinding.ActivityAdminPage2Binding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Admin_page extends AppCompatActivity {

    ActivityAdminPage2Binding binding;
//    AdminHomePageViewModel viewModel;
    String username;
    String hostelBlock;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminPage2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        username = getIntent().getStringExtra("Username");
        hostelBlock = getIntent().getStringExtra("HostelBlock");
        id = getIntent().getStringExtra("ID");

        if(savedInstanceState == null){

            replaceFragment(admin_home_page_fragment.newInstance(username,hostelBlock));

        }


        binding.adminBottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();
            if (itemId == R.id.admin_home_page) {
                replaceFragment(admin_home_page_fragment.newInstance(username, hostelBlock));
            } else if (itemId == R.id.student_records) {
                replaceFragment(admin_records_page_fragment.newInstance(username, hostelBlock, id));
            }

            return true;
        });




    }

    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();

    }

}