package com.kstore.kerasaanestore.activity.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kstore.kerasaanestore.activity.user.Activity_Dashboard_User;
import com.kstore.kerasaanestore.R;

public class Login_Activity extends AppCompatActivity {

    private ImageView arrow_back;
    private EditText et_email, et_password;
    private Button btn_login;
    private TextView txt_daftar;
    private String Email, Password, KEY, passUser;
    private DatabaseReference dbLogin;
    private FirebaseAuth userAuth;
    private ProgressDialog progressDialog;
    private int backPressCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initComponents();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                Email = et_email.getText().toString();
                Password = et_password.getText().toString();
                if (Email.isEmpty() || Password.isEmpty()){
                    Toast.makeText(Login_Activity.this, "Form tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else {
                    checkEmail(new UIDCallBack() {
                        @Override
                        public void onCallBack(String UID) {
                            checkPassword(UID);
                        }
                    });
                }
            }
        });

        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login_Activity.this, LandingPage.class));
                finish();
            }
        });

        txt_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login_Activity.this, Register_Activity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        backPressCount++;

        if (backPressCount == 1){
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backPressCount = 0;
                }
            }, 2000);
        } else if (backPressCount == 2){
            super.onBackPressed();
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
            finishAffinity();
        }
    }

    private void initComponents() {
        arrow_back = findViewById(R.id.arrow_back);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        txt_daftar = findViewById(R.id.txt_daftar);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        userAuth = FirebaseAuth.getInstance();
        dbLogin = FirebaseDatabase.getInstance("https://kerasaan-estore-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
    }

    private void checkEmail(UIDCallBack uidCallBack){
        Email = et_email.getText().toString();
        dbLogin.orderByChild("email").equalTo(Email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        if (dataSnapshot.exists()) {
                            KEY = dataSnapshot.getKey();
                        }
                        break;
                    }
                    uidCallBack.onCallBack(KEY);
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(Login_Activity.this, "Email tidak terdaftar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkPassword(String Key){
        Password = et_password.getText().toString();
        dbLogin.child(Key).child("password").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    passUser = snapshot.getValue().toString();
                    if (passUser.equals(Password)){
                        loginUser();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(Login_Activity.this, "Password salah!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loginUser(){
        userAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(Login_Activity.this, Activity_Dashboard_User.class));
                    finish();
                    progressDialog.dismiss();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(Login_Activity.this, "Login gagal\nMohon cek koneksi internet anda", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private interface UIDCallBack{
        void onCallBack(String UID);
    }
}