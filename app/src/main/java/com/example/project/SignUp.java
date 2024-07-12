package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    TextInputLayout regName,regHostelBlock,regRoomNo,regRegNo,regEmail,regPassword;
    Button regBtn;

    TextView callLoginPage;

    FirebaseDatabase rootNode;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        callLoginPage = findViewById(R.id.alreadyUser);
        callLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        regName = findViewById(R.id.name);
        regHostelBlock = findViewById(R.id.hostelBlock);
        regRoomNo = findViewById(R.id.roomNo);
        regRegNo = findViewById(R.id.RegNo);
        regEmail = findViewById(R.id.email);
        regPassword = findViewById(R.id.password);
        regBtn = findViewById(R.id.signUp);



        regBtn.setOnClickListener(new View.OnClickListener() {

            private Boolean validateName(){
                String val = regName.getEditText().getText().toString();

                if(val.isEmpty()){
                    regName.setError("Field cannot be empty");
                    return false;
                }
                else {
                    regName.setError(null);
                    regName.setErrorEnabled(false);
                    return true;
                }

            }

            private Boolean validateHostelBlock(){
                String val = regHostelBlock.getEditText().getText().toString();

                if(val.isEmpty()){
                    regHostelBlock.setError("Field cannot be empty");
                    return false;
                }
                else if (val.length()>=2) {
                    regHostelBlock.setError("Choose b/w 1,2,3");
                    return false;

                }
                else {
                    regHostelBlock.setError(null);
                    regHostelBlock.setErrorEnabled(false);
                    return true;
                }

            }

            private Boolean validateRoomNo(){
                String val = regRoomNo.getEditText().getText().toString();

                if(val.isEmpty()){
                    regRoomNo.setError("Field cannot be empty");
                    return false;
                } else if (val.length()>=4) {
                    regRoomNo.setError("Enter a valid room no.");
                    return false;

                } else {
                    regRoomNo.setError(null);
                    regRoomNo.setErrorEnabled(false);
                    return true;
                }

            }
            private Boolean validateRegNo(){
                String val = regRegNo.getEditText().getText().toString();

                if(val.isEmpty()){
                    regRegNo.setError("Field cannot be empty");
                    return false;
                }
                else {
                    regRegNo.setError(null);
                    regRegNo.setErrorEnabled(false);
                    return true;
                }

            }

            private Boolean validateEmail(){
                String val = regEmail.getEditText().getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if(val.isEmpty()){
                    regEmail.setError("Field cannot be empty");
                    return false;
                } else if (!val.matches(emailPattern)) {
                    regEmail.setError("Invalid email address");
                    return false;

                } else {
                    regEmail.setError(null);
                    regEmail.setErrorEnabled(false);
                    return true;
                }

            }

            private Boolean validatePassword(){
                String val = regPassword.getEditText().getText().toString();
//        String passwordPattern = "^"+
//                                "(?=.*[a-zA-Z])"+           //any letter
//                                "(?=.*[@#$%^&+=])"+         //at least 1 special character
//                                "(?=\\s+$)"+                // no white spaces
//                                ".{4,}"+                    //at least 4 character
//                                "$";

                if(val.isEmpty()){
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


            @Override
            public void onClick(View v) {



                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("usersReg");


                if(!validateName() | !validateHostelBlock() | !validateRoomNo() | !validateRegNo() | !validateEmail() | !validatePassword())

                    return;

                String name = regName.getEditText().getText().toString();
                String hostelBlock = regHostelBlock.getEditText().getText().toString();
                String roomNo = regRoomNo.getEditText().getText().toString();
                String RegNo = regRegNo.getEditText().getText().toString();
                String email = regEmail.getEditText().getText().toString();
                String password = regPassword.getEditText().getText().toString();



                UserHelper user = new UserHelper( name, hostelBlock, roomNo, RegNo, email, password);

                reference.child(RegNo).setValue(user);


                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);


            }
        });




    }



}