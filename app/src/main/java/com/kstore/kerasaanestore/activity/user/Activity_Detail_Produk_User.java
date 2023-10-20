package com.kstore.kerasaanestore.activity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kstore.kerasaanestore.R;
import com.kstore.kerasaanestore.activity.auth.Login_Activity;
import com.kstore.kerasaanestore.activity.user.Activity_Dashboard_User;
import com.kstore.kerasaanestore.jclass.Data_Value;
import com.kstore.kerasaanestore.model.Model_Katalog;
import com.kstore.kerasaanestore.model.Model_Keranjang;

import java.text.NumberFormat;
import java.util.Locale;

public class Activity_Detail_Produk_User extends AppCompatActivity {

    private ImageView arrow_back, img_produk;
    private TextView nama_produk, deskripsi_produk, harga_produk, amount_of_item;
    private Button btn_decreaseAmount, btn_increaseAmount, btn_beli;
    private AppCompatButton btn_keranjang;
    private DatabaseReference dbProduk;
    private FirebaseAuth userAuth;
    private FirebaseUser firebaseUser;
    private String UID = "", productKey;
    private Model_Katalog model_katalog;
    public static int BUY_REQUEST;
    private int itemCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_produk_user);

        BUY_REQUEST = 0;

        initComponents();
        getDetailProduk();
        amount_of_item.setText("0");

        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_decreaseAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseItemCount();
            }
        });

        btn_increaseAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseItemCount();
            }
        });

        btn_keranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemCount < 1)
                    Toast.makeText(Activity_Detail_Produk_User.this, "Masukkan jumlah item", Toast.LENGTH_SHORT).show();
                else
                    addToCart();
            }
        });

        btn_beli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemCount < 1)
                    Toast.makeText(Activity_Detail_Produk_User.this, "Masukkan jumlah item", Toast.LENGTH_SHORT).show();
                else
                    buyItem();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Activity_Dashboard_User.class));
        finish();
    }

    private void initComponents(){
        arrow_back = findViewById(R.id.btn_back);
        img_produk = findViewById(R.id.img_produk);
        nama_produk = findViewById(R.id.nama_produk);
        deskripsi_produk = findViewById(R.id.deskripsi_produk);
        harga_produk = findViewById(R.id.harga_produk);
        amount_of_item = findViewById(R.id.amount_of_item);
        btn_beli = findViewById(R.id.btn_beli);
        btn_keranjang = findViewById(R.id.btn_keranjang);
        btn_increaseAmount = findViewById(R.id.btn_increaseAmount);
        btn_decreaseAmount = findViewById(R.id.btn_decreaseAmount);
        amount_of_item = findViewById(R.id.amount_of_item);
        userAuth = FirebaseAuth.getInstance();
        firebaseUser = userAuth.getCurrentUser();
        dbProduk = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();
    }

    private void getDetailProduk(){
        Object object = getIntent().getSerializableExtra("detail");
        if (object instanceof Model_Katalog){
            model_katalog = (Model_Katalog) object;
        }

        if (model_katalog!=null){
            Glide.with(getApplicationContext()).load(model_katalog.getGambar()).into(img_produk);
            nama_produk.setText(model_katalog.getNama());
            harga_produk.setText(formatRupiah(Double.parseDouble(model_katalog.getHarga())));
            deskripsi_produk.setText(model_katalog.getDeskripsi());
        }
    }

    private String formatRupiah(Double number){
        Locale locale = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(locale);
        return formatRupiah.format(number);
    }

    private void increaseItemCount() {
        itemCount++;
        updateItemCount();
    }

    private void decreaseItemCount() {
        if (itemCount > 0) {
            itemCount--;
            updateItemCount();
        }
    }

    private void updateItemCount() {
        amount_of_item.setText(String.valueOf(itemCount));
    }

    private void addToCart(){
        if (firebaseUser == null){
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.popup_autentication);
            dialog.show();

            Button btn_okay = dialog.findViewById(R.id.btn_okay);
            btn_okay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Activity_Detail_Produk_User.this, Login_Activity.class));
                    finish();
                }
            });
        } else {
            String idKeranjang = dbProduk.push().getKey();
            String idProduk = model_katalog.getIdProduk();
            String namaProduk = model_katalog.getNama();
            String idPenjual = model_katalog.getPenjual();
            String deskripsiProduk = model_katalog.getDeskripsi();
            String gambarProduk = model_katalog.getGambar();
            String hargaProduk = model_katalog.getHarga();
            String kategoriProduk = model_katalog.getKatergori();
            String beratProduk = model_katalog.getBerat();
            UID = firebaseUser.getUid();

            Model_Keranjang model_keranjang = new Model_Keranjang(idKeranjang, UID, idPenjual, namaProduk, idProduk, beratProduk,
                    deskripsiProduk, gambarProduk,hargaProduk, kategoriProduk, itemCount);

            dbProduk.child("Keranjang").orderByChild("id_produk").equalTo(idProduk).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                            if (dataSnapshot.child("id_user").getValue().toString().equals(UID)){
                                productKey = dataSnapshot.getKey();
                                dbProduk.child("Keranjang").child(productKey).child("jumlahProduk").setValue(itemCount);
                            } else {
                                dbProduk.child("Keranjang").child(idKeranjang).setValue(model_keranjang);
                            }
                        }
                    } else {
                        dbProduk.child("Keranjang").child(idKeranjang).setValue(model_keranjang);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            Toast.makeText(this, "Produk berhasil ditambah ke keranjang", Toast.LENGTH_SHORT).show();
        }
    }

    private void buyItem(){
        if (firebaseUser == null){
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.popup_autentication);
            dialog.show();

            Button btn_okay = dialog.findViewById(R.id.btn_okay);
            btn_okay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Activity_Detail_Produk_User.this, Login_Activity.class));
                    finish();
                }
            });
        } else {
            UID = firebaseUser.getUid();
            BUY_REQUEST = 1;
            Data_Value.idProduk = model_katalog.getIdProduk();
            Data_Value.namaProduk = model_katalog.getNama();
            Data_Value.idPenjual = model_katalog.getPenjual();
            Data_Value.deskripsiProduk = model_katalog.getDeskripsi();
            Data_Value.gambarProduk = model_katalog.getGambar();
            Data_Value.hargaProduk = model_katalog.getHarga();
            Data_Value.kategoriProduk = model_katalog.getKatergori();
            Data_Value.beratProduk = model_katalog.getBerat();
            Data_Value.jlhItem = itemCount;
            startActivity(new Intent(this, Activity_Check_Ongkir.class));
            finish();
        }
    }

}