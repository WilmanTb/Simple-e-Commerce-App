package com.kstore.kerasaanestore.activity.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kstore.kerasaanestore.R;
import com.kstore.kerasaanestore.activity.user.Activity_Dashboard_User;

public class LandingPage extends AppCompatActivity {

    private AppCompatButton btn_login, btn_register;
    private int backPressCount;
    private TextView txt_guest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landingpage);

        initComponents();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LandingPage.this, Login_Activity.class));
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LandingPage.this, Register_Activity.class));
            }
        });

        txt_guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LandingPage.this, Activity_Dashboard_User.class));
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser1 = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser1 != null){
            startActivity(new Intent(LandingPage.this, Activity_Dashboard_User.class));
            finish();
        }
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

    private void initComponents(){
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        txt_guest = findViewById(R.id.txt_guest);
    }
}