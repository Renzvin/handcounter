package com.banana.handcounter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private int mCount = 0;
    Button plus,min,logout;
    TextView tvCounter;
    private FirebaseAuth mAuth;
    public String counter;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReference  = FirebaseDatabase.getInstance().getReference("ALL_USER_COUNTER_DATA");

        mAuth = FirebaseAuth.getInstance();
        plus = findViewById(R.id.buttonplus);
        min = findViewById(R.id.buttonminus);
        logout = findViewById(R.id.logout);
        tvCounter = findViewById(R.id.number);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCount++;
                databaseClass();
            }
        });
        min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCount--;
                databaseClass();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                gotoLogin();
            }
        });
    }

    public void databaseClass(){
        counter = String.valueOf(mCount);

        String id = "NANTI_DI_GANTI_LIST_USER";
        CounterClass counterClass = new CounterClass(id,counter);
        databaseReference.child(id).setValue(counterClass);
        tvCounter.setText(Integer.toString(mCount));
    }

    public void gotoLogin(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

}
