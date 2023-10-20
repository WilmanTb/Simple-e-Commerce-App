package com.kstore.kerasaanestore.activity.penjual;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kstore.kerasaanestore.R;
import com.kstore.kerasaanestore.jclass.Data_Value;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Activity_Kelola_Toko extends AppCompatActivity {

    private ImageView arrow_back;
    private TextView nama_toko, jumlah_produk, produk_terjual;
    private Button btn_edit_nama_toko;
    private DatabaseReference dbToko;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String UID;
    public static String IdToko;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelola_toko);

        initComponenst();
        getDataToko();

        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_edit_nama_toko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Kelola_Toko.this, Activity_Edit_Nama_Toko.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Activity_Kelola_Toko.this, Dashboard_Penjual.class));
        finish();
    }

    private void initComponenst() {
        arrow_back = findViewById(R.id.btn_back);
        nama_toko = findViewById(R.id.nama_toko);
        jumlah_produk = findViewById(R.id.jumlah_produk);
        produk_terjual = findViewById(R.id.produk_terjual);
        btn_edit_nama_toko = findViewById(R.id.btn_edit_nama_toko);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        UID = firebaseUser.getUid();
        dbToko = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();
    }

    private void getDataToko(){
        dbToko.child("Toko").orderByChild("id_penjual").equalTo(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        nama_toko.setText(dataSnapshot.child("nama_toko").getValue().toString());
                        IdToko = dataSnapshot.child("id_toko").getValue().toString();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        dbToko.child("Pesanan").orderByChild("id_penjual").equalTo(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int totalTerjual = 0;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String statusBayar = dataSnapshot.child("status_pembayaran").getValue().toString();
                        if (statusBayar.equals("dibayar")) {
                            String jumlahProduk = dataSnapshot.child("jumlah").getValue().toString();
                            int terjual = Integer.parseInt(jumlahProduk);
                            totalTerjual += terjual;
                        }
                    }
                    produk_terjual.setText(String.valueOf(totalTerjual));
                } else {
                    produk_terjual.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dbToko.child("Produk").child("Data").orderByChild("penjual").equalTo(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String jumlahProduk = String.valueOf(snapshot.getChildrenCount());
                    jumlah_produk.setText(jumlahProduk);
                } else {
                    jumlah_produk.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static String formatRupiah(double amount) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setCurrencySymbol("Rp ");
        symbols.setGroupingSeparator('.');

        DecimalFormat rupiahFormat = (DecimalFormat) DecimalFormat.getCurrencyInstance(Locale.getDefault());
        rupiahFormat.setDecimalFormatSymbols(symbols);
        rupiahFormat.setMaximumFractionDigits(0);

        return rupiahFormat.format(amount);
    }
}