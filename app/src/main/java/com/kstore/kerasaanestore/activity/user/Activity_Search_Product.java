package com.kstore.kerasaanestore.activity.user;

import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kstore.kerasaanestore.R;
import com.kstore.kerasaanestore.activity.user.Activity_Dashboard_User;
import com.kstore.kerasaanestore.adapter.Adapter_Katalog;
import com.kstore.kerasaanestore.fragment.Home_Fragment;
import com.kstore.kerasaanestore.jclass.Data_Value;
import com.kstore.kerasaanestore.model.Model_Katalog;

import java.util.ArrayList;

public class Activity_Search_Product extends AppCompatActivity {

    private String namaProduct;
    private DatabaseReference dbProduct;
    private RecyclerView rc_pencarian_produk;
    private ImageView arrow_back;
    private Adapter_Katalog adapter_katalog;
    private ArrayList<Model_Katalog> model_katalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);

        initComponents();
        namaProduct = Home_Fragment.namaProduct;
        searchItems(namaProduct);

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

        startActivity(new Intent(this, Activity_Dashboard_User.class));
        finish();
    }

    private void initComponents() {
        dbProduct = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();
        arrow_back = findViewById(R.id.btn_back);
        rc_pencarian_produk = findViewById(R.id.rc_pencarian_produk);
        rc_pencarian_produk.setHasFixedSize(true);
        rc_pencarian_produk.setLayoutManager(new GridLayoutManager(this, 2));
        model_katalog = new ArrayList<>();
        adapter_katalog = new Adapter_Katalog(this, model_katalog);
        rc_pencarian_produk.setAdapter(adapter_katalog);
    }

    private void searchItems(String query) {
        // Get the first word from the query
        String[] keywords = query.split("\\s+");
        String firstKeyword = keywords[0];

        Query searchQuery = dbProduct.child("Produk").child("Data")
                .orderByChild("nama")
                .startAt(firstKeyword)
                .endAt(firstKeyword + "\uf8ff");

        searchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                model_katalog.clear();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    Model_Katalog modelKatalog = itemSnapshot.getValue(Model_Katalog.class);
                    model_katalog.add(modelKatalog);
                }
                adapter_katalog.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseSearch", "Search canceled", databaseError.toException());
            }
        });
    }

}