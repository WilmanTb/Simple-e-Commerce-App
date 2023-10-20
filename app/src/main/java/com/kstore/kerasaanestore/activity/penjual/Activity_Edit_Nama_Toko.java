package com.kstore.kerasaanestore.activity.penjual;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kstore.kerasaanestore.R;
import com.kstore.kerasaanestore.jclass.Data_Value;

public class Activity_Edit_Nama_Toko extends AppCompatActivity {

    private ImageView arrow_back;
    private EditText edit_user_name;
    private Button btn_kirim;
    private FirebaseAuth userAuth;
    private DatabaseReference dbUser;
    private String UID, NamaToko, IdToko;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_nama_toko);

        IdToko = Activity_Kelola_Toko.IdToko;

        initComponents();
        getData();

        btn_kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NamaToko = edit_user_name.getText().toString();
                dbUser.child("Toko").child(IdToko).child("nama_toko").setValue(NamaToko);
                Toast.makeText(Activity_Edit_Nama_Toko.this, "Nama toko berhasil diganti", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Activity_Edit_Nama_Toko.this, Activity_Kelola_Toko.class));
                finish();
            }
        });
    }
    private void initComponents(){
        arrow_back = findViewById(R.id.btn_back);
        edit_user_name = findViewById(R.id.edit_user_name);
        btn_kirim = findViewById(R.id.btn_kirim);
        userAuth = FirebaseAuth.getInstance();
        dbUser = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();
        FirebaseUser firebaseUser = userAuth.getCurrentUser();
        UID = firebaseUser.getUid();
    }

    private void getData(){
        dbUser.child("Toko").child(IdToko).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    edit_user_name.setText(snapshot.child("nama_toko").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}