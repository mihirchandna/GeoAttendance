//package com.example.project;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.google.android.material.textfield.TextInputLayout;
//
//public class UserProfile extends AppCompatActivity {
//
//    TextInputLayout fullName,hostelBlock,roomNo,regNo,email,password;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_user_profile);
//
//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
//        bottomNavigationView.setSelectedItemId(R.id.profile_screen);
//
//        bottomNavigationView.setOnItemSelectedListener(item -> {
//
//            int itemId = item.getItemId();
//            if (itemId == R.id.home_screen) {
//                startActivity(new Intent(getApplicationContext(), student_home.class));
//                finish();
//                return true;
//            }
////            else if (itemId == R.id.records_screen) {
////                startActivity(new Intent(getApplicationContext(), Dashboard.class));
////                finish();
////                return true;
////            }
//            else if (itemId == R.id.profile_screen) {
//                return true;
//            }
//            return false;
//
//        });
//
//        fullName = findViewById(R.id.name_profile);
//        hostelBlock = findViewById(R.id.hostelBlock_profile);
//        roomNo = findViewById(R.id.roomNo_profile);
//        regNo = findViewById(R.id.RegNo_profile);
//        email = findViewById(R.id.email_profile);
//        password = findViewById(R.id.password_profile);
//
//
//        showAllUserData();
//
//
//    }
//
//    private void showAllUserData() {
//
//        Intent intent = getIntent();
//        String user_name = intent.getStringExtra("name");
//        String user_hostelBlock = intent.getStringExtra("hostelBlock");
//        String user_roomNo = intent.getStringExtra("roomNo");
//        String user_regNo = intent.getStringExtra("regNo");
//        String user_email = intent.getStringExtra("email");
//        String user_password = intent.getStringExtra("password");
//
//        fullName.getEditText().setText(user_name);
//        hostelBlock.getEditText().setText(user_hostelBlock);
//        roomNo.getEditText().setText(user_roomNo);
//        regNo.getEditText().setText(user_regNo);
//        email.getEditText().setText(user_email);
//        password.getEditText().setText(user_password);
//
//
//
//    }
//
//}