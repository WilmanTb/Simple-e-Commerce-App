package com.kstore.kerasaanestore.activity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.kstore.kerasaanestore.R;
import com.kstore.kerasaanestore.activity.auth.LandingPage;
import com.kstore.kerasaanestore.fragment.Cart_Fragment;
import com.kstore.kerasaanestore.fragment.Home_Fragment;
import com.kstore.kerasaanestore.fragment.Profile_Fragment;

public class Activity_Dashboard_User extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;
    private BottomNavigationView bottomNavigationView;

    private int backPressCount;
    private Home_Fragment homeFragment = new Home_Fragment();
    private Profile_Fragment profileFragment = new Profile_Fragment();
    private Cart_Fragment cartFragment = new Cart_Fragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_user);

        mAuth = FirebaseAuth.getInstance();
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,homeFragment).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, homeFragment).commit();
                        return true;
                    case R.id.person:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, profileFragment).commit();
                        return true;
                    case R.id.cart:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, cartFragment).commit();
                        return true;
                }
                return false;

            }
        });

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser firebaseUser = mAuth.getCurrentUser();
//        if (firebaseUser == null) {
//            startActivity(new Intent(Activity_Dashboard_User.this, LandingPage.class));
//            finish();
//        }
//    }

    @Override
    public void onBackPressed() {
        backPressCount++;

        if (backPressCount == 1) {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

            // Reset the back press count after a certain duration (e.g., 2 seconds)
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backPressCount = 0;
                }
            }, 2000);
        } else if (backPressCount == 2) {
            // If the back button is pressed twice within the specified duration, exit the app
            super.onBackPressed();
        }

    }


}