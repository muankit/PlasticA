package com.example.muank.greenathon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout mRegisterEmail;
    private TextInputLayout mRegisterPassword;
    private TextInputLayout mRegisterDisplayName;
    private Button mRegisterBtn;
    private ProgressDialog mProgress;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mRegisterDisplayName = (TextInputLayout) findViewById(R.id.register_display_name);
        mRegisterEmail = (TextInputLayout) findViewById(R.id.register_email_field);
        mRegisterPassword = (TextInputLayout) findViewById(R.id.register_password_field);
        mRegisterBtn = (Button) findViewById(R.id.register_Btn);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mProgress = new ProgressDialog(this);

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgress.setTitle("Registering User");
                mProgress.setMessage("Please wait while we create your account");
                mProgress.show();

                String display_name = mRegisterDisplayName.getEditText().getText().toString();
                String email = mRegisterEmail.getEditText().getText().toString();
                String password = mRegisterPassword.getEditText().getText().toString();

                if(!TextUtils.isEmpty(display_name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    registerUser(display_name, email, password);
                }else{
                    mRegisterDisplayName.setError("Name Can't be empty");
                    mRegisterEmail.setError("Email Can't be empty");
                    mRegisterPassword.setError("Password Can't be empty");
                    mProgress.hide();
                }
            }
        });
    }

    private void registerUser(String display_name, String email, String password) {

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    mProgress.dismiss();
                    Intent mainIntent = new Intent(RegisterActivity.this , MainActivity.class);
                    startActivity(mainIntent);
                    finish();

                }else{
                    Toast.makeText(RegisterActivity.this, "Check Your Internet Connection", Toast.LENGTH_LONG).show();
                    mProgress.hide();
                }
            }
        });

    }
}
