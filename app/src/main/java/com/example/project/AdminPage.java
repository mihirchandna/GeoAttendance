package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.databinding.ActivityAdminPage2Binding;
import com.example.project.databinding.ActivityAdminPageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AdminPage extends AppCompatActivity {

    TextView callstudentPage;
    TextInputLayout regUsername,regPassword;
    Button calladminHomePage;

    private DatabaseReference adminsRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        regUsername = findViewById(R.id.username);
        regPassword = findViewById(R.id.password);

        callstudentPage = findViewById(R.id.studentLogin_screen);
        callstudentPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminPage.this,Login.class);
                startActivity(intent);
                finish();
            }
        });

        adminsRef = FirebaseDatabase.getInstance().getReference("adminReg");

        calladminHomePage = findViewById(R.id.admin_login_button);
        calladminHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userEnteredUsername = regUsername.getEditText().getText().toString().trim();
                String userEnteredPassword = regPassword.getEditText().getText().toString().trim();

                if (!validateUsername() || !validatePassword()){

                    return;
                }
                LoginAdmin(userEnteredUsername,userEnteredPassword);
            }

        });
    }

    private Boolean validateUsername(){

        String val = regUsername.getEditText().getText().toString();

        if(val.isEmpty()){
            regUsername.setError("Field cannot be empty");
            return false;
        }
        else {
            regUsername.setError(null);
            regUsername.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean validatePassword() {
        String val = regPassword.getEditText().getText().toString();
//        String passwordPattern = "^"+
//                                "(?=.*[a-zA-Z])"+           //any letter
//                                "(?=.*[@#$%^&+=])"+         //at least 1 special character
//                                "(?=\\s+$)"+                // no white spaces
//                                ".{4,}"+                    //at least 4 character
//                                "$";

        if (val.isEmpty()) {
            regPassword.setError("Field cannot be empty");
            return false;
        }
//        else if (!val.matches(passwordPattern)) {
//            regPassword.setError("password is too weak");
//            return false;
//        }
        else {
            regPassword.setError(null);
            regPassword.setErrorEnabled(false);
            return true;
        }
    }


    private void LoginAdmin(String userEnteredUsername, String userEnteredPassword) {

        adminsRef.orderByChild("ID").equalTo(userEnteredUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean adminFound = false;

                if (snapshot.exists()){

                    for (DataSnapshot adminSnapshot : snapshot.getChildren()){

                        String storedPassword = adminSnapshot.child("Password").getValue(String.class);
                        if (storedPassword != null && storedPassword.equals(userEnteredPassword)){

                            String uid = adminSnapshot.getKey();
                            fetchAdminData(uid);
                            return;
                        }
                    }
                    if (!adminFound){
                        Toast.makeText(AdminPage.this,"Incorrect Password",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(AdminPage.this,"Admin not found",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void fetchAdminData(String uid) {

    adminsRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()){

                String username = snapshot.child("Username").getValue(String.class);
                String hostelBlock = snapshot.child("HostelBlock").getValue(String.class);
                String id = snapshot.child("ID").getValue(String.class);

                Intent intent = new Intent(AdminPage.this, Admin_page.class);
                intent.putExtra("Username",username);
                intent.putExtra("HostelBlock",hostelBlock);
                intent.putExtra("ID",id);
                startActivity(intent);
                finish();
            }
            else {
                Toast.makeText(AdminPage.this,"Admin data not found", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

            Toast.makeText(AdminPage.this,"Database Error", Toast.LENGTH_SHORT).show();

        }
    });

    }


//
//    public void LoginAdmin(View v){
//        if(!validateUsername() | !validatePassword()){
//            return ;
//        }
//        else{
//            isUser();
//
//        }
//    }
//
//    private void isUser() {
//
//
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("adminReg");
//
//        Query checkUser = reference.orderByChild("ID").equalTo(userEnteredUsername);
//
//        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//
//                    regUsername.setError(null);
//                    regUsername.setErrorEnabled(false);
//
//
//                    String passwordFromDB = snapshot.child(userEnteredUsername).child("Password").getValue(String.class);
//
//
//                    if(passwordFromDB.equals(userEnteredPassword)){
//
//                        regUsername.setError(null);
//                        regUsername.setErrorEnabled(false);
//
//                        String usernameFromDB = snapshot.child(userEnteredUsername).child("Username").getValue(String.class);
//                        String hostelBlockFromDB = snapshot.child(userEnteredUsername).child("HostelBlock").getValue(String.class);
//
////                        admin_home_page_fragment fragment = new admin_home_page_fragment();
//
////                        fragment.setArguments(bundle);
////
////                        FragmentManager fragmentManager = getSupportFragmentManager();
////                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
////                        fragmentTransaction.replace(R.id.frame_layout,fragment);
////                        fragmentTransaction.commit();
//
//                        Intent intent = new Intent(getApplicationContext(), Admin_page.class);
////                        Bundle bundle = new Bundle();
////                        bundle.putString("Username",usernameFromDB);
////                        bundle.putString("HostelBlock",hostelBlockFromDB);
////                        intent.putExtras(bundle);
//                        intent.putExtra("Username",usernameFromDB);
//                        intent.putExtra("HostelBlock",hostelBlockFromDB);
//
//                        startActivity(intent);
//                        finish();
//
//                    }
//                    else {
//
//                        regPassword.setError("Wrong Password");
//                        regPassword.requestFocus();
//                    }
//
//                }
//                else {
//                    regUsername.setError("No such User exist");
//                    regUsername.requestFocus();
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//                Log.e("Firebase", "Database error " + error.getMessage() );
//
//            }
//        });
//











//        //Creating Admin Database
//
//
//        // Get an instance of Firebase Database
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//
//// Create a reference to the location you want to write data
//        DatabaseReference myRef = database.getReference("adminReg");
//
////        String userID = myRef.push().getKey();
//
//
//        String username = "Admin3";
//        String password = "Admin3";
//        String userID = "25032002";
//        String hostelBlock = "2";
//
//
//
//
//
//// Create the data object (e.g., a HashMap or custom class)
//        Map<String, Object> data = new HashMap<>();
//        data.put("Username", username);
//        data.put("Password", password);
//        data.put("ID", userID);
//        data.put("HostelBlock",hostelBlock);
//
//// Write the data to the database
//        myRef.child(userID).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//                    // Data written successfully
//                    Log.d("TAG", "Data written successfully!");
//                } else {
//                    // Handle write failure
//                    Log.w("TAG", "Error writing data:", task.getException());
//                }
//            }
//        });
//










//        callstudentPage = findViewById(R.id.studentLogin_screen);
//        callstudentPage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(AdminPage.this, SignUp.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//
//
//        regUsername = findViewById(R.id.username);
//        regPassword= findViewById(R.id.password);
//
//        calladminHomePage = findViewById(R.id.admin_login_button);
//        calladminHomePage.setOnClickListener(new View.OnClickListener() {
//
//            private Boolean validateUsername() {
//
//                String val = regUsername.getEditText().getText().toString();
//
//                if (val.isEmpty()) {
//                    regUsername.setError("Field cannot be empty");
//                    return false;
//                } else {
//                    regUsername.setError(null);
//                    regUsername.setErrorEnabled(false);
//                    return true;
//                }
//
//            }
//
//            private Boolean validatePassword() {
//                String val = regPassword.getEditText().getText().toString();
////        String passwordPattern = "^"+
////                                "(?=.*[a-zA-Z])"+           //any letter
////                                "(?=.*[@#$%^&+=])"+         //at least 1 special character
////                                "(?=\\s+$)"+                // no white spaces
////                                ".{4,}"+                    //at least 4 character
////                                "$";
//
//                if (val.isEmpty()) {
//                    regPassword.setError("Field cannot be empty");
//                    return false;
//                }
////        else if (!val.matches(passwordPattern)) {
////            regPassword.setError("password is too weak");
////            return false;
////        }
//                else {
//                    regPassword.setError(null);
//                    regPassword.setErrorEnabled(false);
//                    return true;
//                }
//            }
//
//
//            @Override
//            public void onClick(View v) {
//
//                rootNode = FirebaseDatabase.getInstance();
//                reference = rootNode.getReference("adminReg");
//
//
//                if(!validateUsername() | !validatePassword())
//
//                    return;
//
//                String Username = regUsername.getEditText().getText().toString();
//                String Password = regPassword.getEditText().getText().toString();
//
//
//
//                UserHelper user = new UserHelper( Username,Password);
//
//                reference.child(Username).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if(task.isSuccessful()){
//
//                        }
//                        else {
//                            Log.e("AdminPage", "Error writing to database",task.getException() );
//
//                        }
//                    }
//                });
//
//
//                Intent intent = new Intent(AdminPage.this, Login.class);
//                startActivity(intent);
//
//
//            }
//        });




    }



