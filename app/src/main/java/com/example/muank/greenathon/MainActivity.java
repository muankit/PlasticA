package com.example.muank.greenathon;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    private EditText mLocationField;
    private EditText mAmountField;
    private EditText mStoreField;
    private Button mSubmitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mUserDatabase = FirebaseDatabase.getInstance().getReference();
        
        mLocationField = (EditText) findViewById(R.id.main_location_field);
        mAmountField = (EditText) findViewById(R.id.main_amount_field);
        mStoreField = (EditText) findViewById(R.id.main_store_field);

        mSubmitBtn = (Button) findViewById(R.id.main_submit_btn);

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                String loaction = mLocationField.getText().toString();
                String amount = mAmountField.getText().toString();
                String store_loaction = mStoreField.getText().toString();

                if (!TextUtils.isEmpty(loaction) && !TextUtils.isEmpty(amount)){

                    String currentDate = DateFormat.getDateTimeInstance().format(new Date());

                    Map<String, String> plasticMap = new HashMap<>();
                    plasticMap.put("location", loaction);
                    plasticMap.put("amount", amount);
                    plasticMap.put("storeLocation", store_loaction);
                    plasticMap.put("date" , currentDate);

                    mUserDatabase.child("PlasticData").push().setValue(plasticMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Report Successfully submitted", Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(MainActivity.this, "Error Submitting", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    mLocationField.setError("Loaction Can't be empty");
                    mAmountField.setError("Amount Can't be empty");
                }
                mLocationField.setText("");
                mAmountField.setText("");
                mStoreField.setText("");
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.main_settings) {

        }

        if (item.getItemId() == R.id.main_logout) {
            mAuth.signOut();
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }

        return true;
    }
}
