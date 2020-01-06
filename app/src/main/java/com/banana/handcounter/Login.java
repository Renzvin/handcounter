package com.banana.handcounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.*;

public class Login extends AppCompatActivity {
    private static final String TAG = "Login";
    Button btnLogin,btnRegister;
    EditText etEmail,etPassword;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private boolean loggedIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        btnRegister = findViewById(R.id.register);
        btnLogin = findViewById(R.id.btnLogin);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if(user!=null)
        {
            if(user.isEmailVerified())
            {
                loggedIn = isLoggedIn();
                if(loggedIn){
                    startCounter();
                }
            }
        }
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                //panggil fungsi login

                login(email, password);
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(Login.this,Register.class);
                startActivity(register);
            }
        });
    }
    private void startCounter(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    public boolean isLoggedIn() {
        if (mAuth.getCurrentUser() != null) {
            //  user logged in
            return true;
        } else {
            return false;
        }
    }
    private void login(final String email, final String password)
    {
        if (TextUtils.isEmpty(email)) {
            Snackbar.make(findViewById(android.R.id.content), "Masukan Alamat Surel yang valid", Snackbar.LENGTH_LONG)
                    .show();
        } else if (TextUtils.isEmpty(password)) {
            Snackbar.make(findViewById(android.R.id.content), "Masukan Kata Sandi yang valid", Snackbar.LENGTH_LONG)
                    .show();
        } else {
            //  do login
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //  login sucess
                                //  go to mainactivity
                                user = mAuth.getCurrentUser();

                                //System.out.println(user.getEmail()+" "+user.isEmailVerified());
                                if(user.isEmailVerified())
                                {
                                    startCounter();
                                    //proses data profil disini

                                    Toast.makeText(Login.this, "Login Sukses", Toast.LENGTH_SHORT).show();
                                }else{
                                    sendEmail(user);
                                    Toast.makeText(Login.this, "Email Verifikasi Telah Dikirim", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                //  login failed
                                Toast.makeText(Login.this, "Login Gagal", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
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
                    showMessageBox("Akun anda belum terverifikasi. Email verifikasi telah terkirim ke "+firebase_user.getEmail()+
                            ". Login ulang setelah melakukan verifikasi akun.");
                } else {
                    Log.e(TAG, "sendEmailVerification", task.getException());
                    Toast.makeText(Login.this,
                            "Failed to send verification email.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void showMessageBox(String message)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Login");
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialogBuilder.show();
    }
}
