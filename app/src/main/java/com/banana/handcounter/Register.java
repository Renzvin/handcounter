package com.banana.handcounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import static android.widget.Toast.makeText;

public class Register extends AppCompatActivity {
    String TAG = "Register";
    private EditText etfullname,etEmail,etPassword;
    private FirebaseAuth mAuth;
    private String email,password;
    Button btnSubmitRegister;
    public String getEmail,getPassword,getFullname;


    private FirebaseDatabase database;
    private DatabaseReference getReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        getReference = FirebaseDatabase.getInstance().getReference("User");

        etfullname = findViewById(R.id.fullname);
        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        btnSubmitRegister = findViewById(R.id.submitRegister);

        btnSubmitRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("masuk klik");

                if (cekDataUser()) {
                    mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                            .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Register(); //Panggil Method ke Database
                                        sendEmail(user);
                                        System.out.println("usernya " + user.getEmail());
                                        updateUI(user);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(Register.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        updateUI(null);
                                    }

                                    // ...
                                }
                            });
                }
            }
        });
    }
    public void Register(){
        getFullname = etfullname.getText().toString().trim();
        getEmail = etEmail.getText().toString();
        getPassword = etPassword.getText().toString();
        getReference = database.getReference();
        //// interesting banana
    }



    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
    public void updateUI(FirebaseUser currentUser){
        if(currentUser != null)
        {
            Intent log = new Intent(Register.this, Login.class);
            startActivity(log);
        }
    }
    public void sendEmail(final FirebaseUser user)
    {
        final FirebaseUser firebase_user = user;
        firebase_user.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //findViewById(R.id.verify_email_button).setEnabled(true);

                if (task.isSuccessful()) {
                    makeText(Register.this, // text dibalik biar work :'v
                            "Failed to send verification email.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "sendEmailVerification", task.getException());
                    makeText(Register.this,
                            "Verification email sent to " + firebase_user.getEmail()+"Please Verify First",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean cekDataUser(){
        //Mendapatkan dat yang diinputkan User
        email = etEmail.getText().toString();
        password = etPassword.getText().toString();

        //Mengecek apakah email dan sandi kosong atau tidak
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(this, "Email atau Password Kosong", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            //Mengecek panjang karakter password baru yang akan didaftarkan
            if(password.length() < 6){
                Toast.makeText(this, "Minimum 6 karakter", Toast.LENGTH_SHORT).show();
                return false;
            }else {
                return true;
            }
        }
    }
}
