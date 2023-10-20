package com.kstore.kerasaanestore.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kstore.kerasaanestore.activity.user.Activity_Dashboard_User;
import com.kstore.kerasaanestore.activity.user.Activity_Edit_Profil;
import com.kstore.kerasaanestore.activity.user.Activity_Search_Product;
import com.kstore.kerasaanestore.adapter.Adapter_Katalog;
import com.kstore.kerasaanestore.adapter.Adapter_Kategori;
import com.kstore.kerasaanestore.model.Model_Katalog;
import com.kstore.kerasaanestore.model.Model_Kategori;
import com.kstore.kerasaanestore.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home_Fragment extends Fragment {

    View view;
    private RecyclerView rc_kategori, rc_katalog;
    private CircleImageView img_profilUser;
    private Adapter_Kategori adapterKategori;
    private Adapter_Katalog adapterKatalog;
    private ArrayList<Model_Katalog> model_katalog;
    private ArrayList<Model_Kategori> model_kategori;
    private ArrayList<Model_Kategori> productNameList;
    private DatabaseReference dbRef;
    private EditText search_product;
    private Button btn_cari;
    public static String namaProduct;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String UID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_, container, false);


        initComponents();
        getDataKategori();
        getDataKatalog();
        getUserProfilePicture();


//        search_product.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                String query = s.toString().toLowerCase();
//                searchItems(query);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });


        btn_cari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                namaProduct = search_product.getText().toString();
                startActivity(new Intent(getActivity(), Activity_Search_Product.class));
            }
        });

        img_profilUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Activity_Edit_Profil.class));
            }
        });

        return view;
    }

    private void initComponents() {
        img_profilUser = view.findViewById(R.id.img_profilUser);
        btn_cari = view.findViewById(R.id.btn_cari);
        productNameList = new ArrayList<>();
        search_product = view.findViewById(R.id.search_product);
        rc_katalog = view.findViewById(R.id.rc_katalog);
        rc_kategori = view.findViewById(R.id.rc_kategori);
        rc_kategori.setHasFixedSize(true);
        rc_katalog.setHasFixedSize(true);
        rc_katalog.setLayoutManager(new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false));
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        rc_kategori.setLayoutManager(layoutManager);
        model_kategori = new ArrayList<>();
        model_katalog = new ArrayList<>();
        adapterKatalog = new Adapter_Katalog(view.getContext(), model_katalog);
        adapterKategori = new Adapter_Kategori(view.getContext(), model_kategori);
        rc_kategori.setAdapter(adapterKategori);
        rc_katalog.setAdapter(adapterKatalog);
        dbRef = FirebaseDatabase.getInstance("https://kerasaan-estore-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null)
            UID = firebaseUser.getUid();
    }

    private void getDataKategori() {
        dbRef.child("Kategori").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    model_kategori.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Model_Kategori modelKategori = dataSnapshot.getValue(Model_Kategori.class);
                        model_kategori.add(modelKategori);
                    }
                    adapterKategori.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getDataKatalog() {
        dbRef.child("Produk").child("Data").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    model_katalog.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Model_Katalog modelKatalog = dataSnapshot.getValue(Model_Katalog.class);
                        model_katalog.add(modelKatalog);
                    }
                    adapterKatalog.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getUserProfilePicture() {
        if (firebaseUser != null) {
            dbRef.child("Users").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String foto = snapshot.child("foto").getValue().toString();
                    if (isAdded() && getActivity() != null && !getActivity().isFinishing() && !foto.equals("empty"))
                        Glide.with(requireContext()).load(foto).into(img_profilUser);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

}