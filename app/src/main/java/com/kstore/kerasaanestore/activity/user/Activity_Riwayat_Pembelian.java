package com.kstore.kerasaanestore.activity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kstore.kerasaanestore.R;
import com.kstore.kerasaanestore.adapter.Adapter_Riwayat_Pesanan;
import com.kstore.kerasaanestore.jclass.Data_Value;
import com.kstore.kerasaanestore.model.Model_Pesanan;

import java.util.ArrayList;

public class Activity_Riwayat_Pembelian extends AppCompatActivity {

    private ImageView arrow_back;
    private RecyclerView rc_riwayat_pesanan;
    private DatabaseReference dbRiwayatPesanan;
    private Adapter_Riwayat_Pesanan adapter_riwayat_pesanan;
    private ArrayList<Model_Pesanan> model_pesananArrayList;
    private FirebaseAuth userAuth;
    private FirebaseUser firebaseUser;
    private String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_pembelian);

        initComponents();
        getRiwayatPesanan();
    }

    private void initComponents(){
        arrow_back = findViewById(R.id.btn_back);
        dbRiwayatPesanan = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();
        userAuth = FirebaseAuth.getInstance();
        firebaseUser = userAuth.getCurrentUser();
        UID = firebaseUser.getUid();
        rc_riwayat_pesanan = findViewById(R.id.rc_riwayat_pesanan);
        rc_riwayat_pesanan.setHasFixedSize(true);
        rc_riwayat_pesanan.setLayoutManager(new LinearLayoutManager(this));
        model_pesananArrayList = new ArrayList<>();
        adapter_riwayat_pesanan = new Adapter_Riwayat_Pesanan(this, model_pesananArrayList);
        rc_riwayat_pesanan.setAdapter(adapter_riwayat_pesanan);
    }

    private void getRiwayatPesanan(){
        dbRiwayatPesanan.child("Pesanan").orderByChild("id_user").equalTo(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    model_pesananArrayList.clear();
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        Model_Pesanan itemList = dataSnapshot.getValue(Model_Pesanan.class);
                        model_pesananArrayList.add(itemList);
                    }
                    adapter_riwayat_pesanan.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}