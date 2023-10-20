package com.kstore.kerasaanestore.activity.penjual;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Dashboard_Penjual extends AppCompatActivity {

    private TextView txt_penghasilan, txt_terjual, txt_jlhProduk;
    private CardView cv_produkSaya, cv_kelolaToko, cv_dataPenjualan;
    private DatabaseReference dbToko;
    private FirebaseAuth userAuth;
    private FirebaseUser firebaseUser;
    private String UID;
    private int backPressCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_penjual);

        initComponents();
        checkTokoExist();

        cv_produkSaya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard_Penjual.this, Activity_Produk_Penjual.class));
                finish();
            }
        });

        cv_kelolaToko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard_Penjual.this, Activity_Kelola_Toko.class));
                finish();
            }
        });

        cv_dataPenjualan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard_Penjual.this, Activity_Data_Penjualan.class));
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
        txt_penghasilan = findViewById(R.id.txt_penghasilan);
        txt_terjual = findViewById(R.id.txt_terjual);
        txt_jlhProduk = findViewById(R.id.txt_jlhProduk);
        cv_dataPenjualan = findViewById(R.id.cv_dataPenjualan);
        cv_kelolaToko = findViewById(R.id.cv_kelolaToko);
        cv_produkSaya = findViewById(R.id.cv_produkSaya);
        userAuth = FirebaseAuth.getInstance();
        firebaseUser = userAuth.getCurrentUser();
        UID = firebaseUser.getUid();
        dbToko = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();
    }

    private void checkTokoExist() {
        dbToko.child("Toko").orderByChild("id_penjual").equalTo(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    dbToko.child("Pesanan").orderByChild("id_penjual").equalTo(UID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                int totalPenghasilan = 0;
                                int totalTerjual = 0;
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    String statusBayar = dataSnapshot.child("status_pembayaran").getValue().toString();
                                    if (statusBayar.equals("dibayar")) {
                                        String totalBayar = dataSnapshot.child("total_bayar").getValue().toString();
                                        int penghasilan = Integer.parseInt(totalBayar);
                                        totalPenghasilan += penghasilan;

                                        String jumlahProduk = dataSnapshot.child("jumlah").getValue().toString();
                                        int terjual = Integer.parseInt(jumlahProduk);
                                        totalTerjual += terjual;
                                    }
                                }
                                txt_penghasilan.setText(formatRupiah(Double.parseDouble(String.valueOf(totalPenghasilan))));
                                txt_terjual.setText(String.valueOf(totalTerjual));
                            } else {
                                txt_penghasilan.setText(formatRupiah(Double.parseDouble("0")));
                                txt_terjual.setText("0");
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
                                txt_jlhProduk.setText(jumlahProduk);
                            } else {
                                txt_jlhProduk.setText("0");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } else {
                    Dialog dialog = new Dialog(Dashboard_Penjual.this);
                    dialog.setContentView(R.layout.pop_up_penjual);
                    dialog.setCancelable(false);
                    dialog.show();

                    Button btn_ok = dialog.findViewById(R.id.btn_okay);
                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            startActivity(new Intent(Dashboard_Penjual.this, Activity_Buat_Toko.class));
                        }
                    });
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