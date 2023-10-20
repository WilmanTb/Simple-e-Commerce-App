package com.kstore.kerasaanestore.activity.penjual;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kstore.kerasaanestore.R;
import com.kstore.kerasaanestore.adapter.Adapter_Produk_Penjual;
import com.kstore.kerasaanestore.jclass.Data_Value;
import com.kstore.kerasaanestore.model.Model_Katalog;

import java.util.ArrayList;

public class Activity_Produk_Penjual extends AppCompatActivity {

    private ImageView arrow_back;
    private Button btn_tambah_produk;
    private RecyclerView rc_produk_penjual;
    private Adapter_Produk_Penjual adapter_produk_penjual;
    private ArrayList<Model_Katalog> model_katalogs;
    private DatabaseReference dbProduk;
    private FirebaseAuth userAuth;
    private FirebaseUser firebaseUser;
    private String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produk_penjual);

        initComponenst();
        getPenjualProduk();

        btn_tambah_produk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Produk_Penjual.this, Activity_Tambah_Produk_Penjual.class));
            }
        });

        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Activity_Produk_Penjual.this, Dashboard_Penjual.class));
        finish();
    }

    private void initComponenst() {
        arrow_back = findViewById(R.id.btn_back);
        btn_tambah_produk = findViewById(R.id.btn_tambah_produk);
        userAuth = FirebaseAuth.getInstance();
        firebaseUser = userAuth.getCurrentUser();
        UID = firebaseUser.getUid();
        rc_produk_penjual = findViewById(R.id.rc_produk_penjual);
        rc_produk_penjual.setHasFixedSize(true);
        rc_produk_penjual.setLayoutManager(new LinearLayoutManager(this));
        model_katalogs = new ArrayList<>();
        adapter_produk_penjual = new Adapter_Produk_Penjual(this, model_katalogs);
        rc_produk_penjual.setAdapter(adapter_produk_penjual);
        dbProduk = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();
    }

    private void getPenjualProduk(){
        dbProduk.child("Produk").child("Data").orderByChild("penjual").equalTo(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    model_katalogs.clear();
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        Model_Katalog itemList = dataSnapshot.getValue(Model_Katalog.class);
                        model_katalogs.add(itemList);
                    }
                    adapter_produk_penjual.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}