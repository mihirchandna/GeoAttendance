package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    TextView calladminPage;
    Button callSignUpPage, login;
    private DatabaseReference usersRef;
    TextInputLayout regNo, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        regNo = findViewById(R.id.registrationNo);
        password = findViewById(R.id.password);

        calladminPage = findViewById(R.id.adminPage_screen);
        calladminPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, AdminPage.class);
                startActivity(intent);
                finish();

            }
        });


        callSignUpPage = findViewById(R.id.alreadyUser);
        callSignUpPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
                finish();
            }
        });

        usersRef = FirebaseDatabase.getInstance().getReference("usersReg");

        login = findViewById(R.id.studentHome);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String regNoValue = Login.this.regNo.getEditText().getText().toString().trim();
                String passwordValue = Login.this.password.getEditText().getText().toString().trim();

                if (!validateRegNo() || !validatePassword()) {
                    return;
                }
                loginUser(regNoValue, passwordValue);
            }
        });


    }


    private Boolean validateRegNo() {

        String val = regNo.getEditText().getText().toString();

        if (val.isEmpty()) {
            regNo.setError("Field cannot be empty");
            return false;
        } else {
            regNo.setError(null);
            regNo.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean validatePassword() {
        String val = password.getEditText().getText().toString();
//        String passwordPattern = "^"+
//                                "(?=.*[a-zA-Z])"+           //any letter
//                                "(?=.*[@#$%^&+=])"+         //at least 1 special character
//                                "(?=\\s+$)"+                // no white spaces
//                                ".{4,}"+                    //at least 4 character
//                                "$";

        if (val.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        }
//        else if (!val.matches(passwordPattern)) {
//            regPassword.setError("password is too weak");
//            return false;
//        }
        else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    private void loginUser(String regNo, String password) {
        usersRef.orderByChild("regNo").equalTo(regNo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean userFound = false;

                if(snapshot.exists()){

                    for (DataSnapshot userSnapshot : snapshot.getChildren()){

                        String storedPassword = userSnapshot.child("password").getValue(String.class);
                        if(storedPassword != null&& storedPassword.equals(password)){

                            String uid = userSnapshot.getKey();
                            fetchUserData(uid);
                            userFound = true;
                            return;

                        }

                    }
                    if (!userFound) {
                        Toast.makeText(Login.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(Login.this,"User not found",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(Login.this,"Database error: "+error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void fetchUserData(String uid) {

        usersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    String name = snapshot.child("name").getValue(String.class);
                    String hostelBlock = snapshot.child("hostelBlock").getValue(String.class);
                    String roomNo = snapshot.child("roomNo").getValue(String.class);
                    String regNo = snapshot.child("regNo").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);

                    Intent intent = new Intent(Login.this, Dashboard.class);
                    intent.putExtra("name", name);
                    intent.putExtra("hostelBlock", hostelBlock);
                    intent.putExtra("roomNo", roomNo);
                    intent.putExtra("regNo", regNo);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();

                }else {
                    Toast.makeText(Login.this,"User data not found", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(Login.this,"Database Error: "+error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }
}