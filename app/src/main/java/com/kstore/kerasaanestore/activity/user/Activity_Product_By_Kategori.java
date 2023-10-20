package com.kstore.kerasaanestore.activity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kstore.kerasaanestore.R;
import com.kstore.kerasaanestore.adapter.Adapter_Katalog;
import com.kstore.kerasaanestore.adapter.Adapter_Kategori;
import com.kstore.kerasaanestore.jclass.Data_Value;
import com.kstore.kerasaanestore.model.Model_Katalog;
import com.kstore.kerasaanestore.model.Model_Kategori;

import java.util.ArrayList;

public class Activity_Product_By_Kategori extends AppCompatActivity {

    private ImageView arrow_back;
    private TextView txt_layanan;
    private RecyclerView rc_product_by_kategori;
    private Adapter_Katalog adapter_katalog;
    private ArrayList<Model_Katalog> model_katalog_arrayList;
    private DatabaseReference dbProduct;
    private String kategoriProduct;
    private Model_Kategori modelKategori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_by_kategori);

        initComponents();
        getKategori();
        showProductByKategori();

        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initComponents() {
        arrow_back = findViewById(R.id.btn_back);
        txt_layanan = findViewById(R.id.txt_layanan);
        rc_product_by_kategori = findViewById(R.id.rc_product_by_kategori);
        rc_product_by_kategori.setHasFixedSize(true);
        rc_product_by_kategori.setLayoutManager(new GridLayoutManager(this, 2));
        model_katalog_arrayList = new ArrayList<>();
        adapter_katalog = new Adapter_Katalog(this, model_katalog_arrayList);
        rc_product_by_kategori.setAdapter(adapter_katalog);
        dbProduct = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();
    }

    private void getKategori() {
        Object object = getIntent().getSerializableExtra("kategori");
        if (object instanceof Model_Kategori) {
            modelKategori = (Model_Kategori) object;
        }

        if (modelKategori != null) {
            kategoriProduct = modelKategori.getNama();
            txt_layanan.setText(kategoriProduct);
        }
    }

    private void showProductByKategori(){
        dbProduct.child("Produk").child("Data").orderByChild("kategori").equalTo(kategoriProduct).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    model_katalog_arrayList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Model_Katalog modelKatalog = dataSnapshot.getValue(Model_Katalog.class);
                        model_katalog_arrayList.add(modelKatalog);
                    }
                    adapter_katalog.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled if needed
            }
        });
    }
}