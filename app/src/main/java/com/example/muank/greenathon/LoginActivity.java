package com.example.muank.greenathon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private TextInputLayout mloginEmail;
    private TextInputLayout mLoginPassword;
    private Button mLoginBtn;
    private ProgressDialog mProgress;
    private Button mloginSignUpBtn;
    private Button mLoginMemberLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mProgress = new ProgressDialog(this);

        mloginEmail = (TextInputLayout) findViewById(R.id.login_email);
        mLoginPassword = (TextInputLayout) findViewById(R.id.login_password);
        mLoginBtn = (Button) findViewById(R.id.login_Btn);
        mloginSignUpBtn = (Button) findViewById(R.id.login_Sign_up_Btn);
        mLoginMemberLoginBtn = (Button) findViewById(R.id.login_member_login_Btn);

        mLoginMemberLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mloginSignUpBtn.setVisibility(View.INVISIBLE);
                mLoginMemberLoginBtn.setVisibility(View.INVISIBLE);


                mLoginBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String email = mloginEmail.getEditText().getText().toString();
                        String password = mLoginPassword.getEditText().getText().toString();

                        loginMemberUser(email,password);

                        Intent verifyIntent = new Intent(LoginActivity.this, MemberAadharVerify.class);
                        startActivity(verifyIntent);
                        finish();
                    }
                });
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = mloginEmail.getEditText().getText().toString();
                String password = mLoginPassword.getEditText().getText().toString();

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
                    mProgress.setTitle("Logging In");
                    mProgress.setMessage("Please wait while we check your credentials");
                    mProgress.show();

                    loginInUser(email,password);
                }
                else {
                    mProgress.hide();
                    Toast.makeText(LoginActivity.this, "Login Error ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mloginSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });
    }

    private void loginMemberUser(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Succesfull", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(LoginActivity.this, "UnSuccessful", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loginInUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent mainIntent = new Intent(LoginActivity.this , MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, "No Credentials Found", Toast.LENGTH_LONG).show();
                    Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(registerIntent);
                    finish();
                }
                mProgress.dismiss();

            }
        });
    }


}
