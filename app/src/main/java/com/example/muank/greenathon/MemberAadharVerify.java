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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.TooManyListenersException;

public class MemberAadharVerify extends AppCompatActivity {

    private TextInputLayout mAadharverifyUsername;
    private TextInputLayout mAadharVerifyAadharNumber;
    private EditText mAadharverifyCurrentAdddress;
    private Button mAadharVerifyNextBtn;
    private CheckBox mCheckBox;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_aadhar_verify);

        mAadharverifyUsername = (TextInputLayout) findViewById(R.id.aadhar_verify_username);
        mAadharVerifyAadharNumber = (TextInputLayout) findViewById(R.id.aadhar_verify_aadhar_number);
        mAadharverifyCurrentAdddress = (EditText) findViewById(R.id.aadhar_verify_current_address);
        mAadharVerifyNextBtn = (Button) findViewById(R.id.aadhar_verify_next_btn);
        mCheckBox = (CheckBox) findViewById(R.id.aadhar_verify_checkBox);

        mAuth  = FirebaseAuth.getInstance();
        mUserDatabase = FirebaseDatabase.getInstance().getReference();
        mProgress = new ProgressDialog(this);

        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    mAadharverifyCurrentAdddress.setVisibility(View.VISIBLE);
                }else {
                    mAadharverifyCurrentAdddress.setVisibility(View.INVISIBLE);
                }
            }
        });


        mAadharVerifyNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mProgress.setTitle("Member Logging In");
                mProgress.setMessage("Please wait while we logging you in");
                mProgress.setCanceledOnTouchOutside(false);
                mProgress.show();

                String username = mAadharverifyUsername.getEditText().getText().toString();
                String aadhar = mAadharVerifyAadharNumber.getEditText().getText().toString();
                String current_address = mAadharverifyCurrentAdddress.getText().toString();

                if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(aadhar)){
                    Map<String,String> membermap = new HashMap<>();
                    membermap.put("username",username);
                    membermap.put("currentAddress", current_address);
                    membermap.put("aadharNumber" , aadhar);

                    mUserDatabase.child("members").push().setValue(membermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                mProgress.dismiss();
                                Intent mainMemberIntent = new Intent(MemberAadharVerify.this,MemberMainActivity.class);
                                startActivity(mainMemberIntent);
                                finish();
                            }else{
                                mProgress.hide();
                                Toast.makeText(MemberAadharVerify.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                          }
                    });

                }
            }
        });
    }
}
