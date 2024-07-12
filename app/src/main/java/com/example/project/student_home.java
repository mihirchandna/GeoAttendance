//package com.example.project;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.TextView;
//
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//
//public class student_home extends AppCompatActivity {
//
//
//    TextView fullName,hostelBlock;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_student_home);
//
//
//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
//        bottomNavigationView.setSelectedItemId(R.id.home_screen);
//
//        bottomNavigationView.setOnItemSelectedListener(item -> {
//
//            int itemId = item.getItemId();
//            if (itemId == R.id.home_screen) {
//                return true;
//            }
////            else if (itemId == R.id.records_screen) {
////                startActivity(new Intent(getApplicationContext(), Dashboard.class));
////                finish();
////                return true;
////
////            }
//            else if (itemId == R.id.profile_screen) {
//                startActivity(new Intent(getApplicationContext(), UserProfile.class));
//                finish();
//                return true;
//            }
//            return false;
//
//        });
//
//        fullName = findViewById(R.id.name_home);
//        hostelBlock = findViewById(R.id.hostelBlock_home);
//
//        showUserHome();
//
//
//    }
//
//    private void showUserHome() {
//
//        Intent intent = getIntent();
//        String user_name_home = intent.getStringExtra("name");
//        String user_hostelBlock_home = intent.getStringExtra("hostelBlock");
//
//
//        fullName.setText(user_name_home);
//        hostelBlock.setText(user_hostelBlock_home);
//
//    }
//}
//
//
//
