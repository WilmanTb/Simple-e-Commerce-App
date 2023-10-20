package com.kstore.kerasaanestore.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kstore.kerasaanestore.activity.auth.Login_Activity;
import com.kstore.kerasaanestore.activity.user.Activity_Check_Ongkir;
import com.kstore.kerasaanestore.activity.user.Activity_Checkout;
import com.kstore.kerasaanestore.R;
import com.kstore.kerasaanestore.adapter.Adapter_Keranjang;
import com.kstore.kerasaanestore.jclass.Data_Value;
import com.kstore.kerasaanestore.model.Model_Keranjang;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Cart_Fragment extends Fragment implements Adapter_Keranjang.TotalPriceListener {

    View view;
    RecyclerView rc_keranjang;
    Adapter_Keranjang adapter_keranjang;
    ArrayList<Model_Keranjang> model_keranjang;
    DatabaseReference dbKeranjang;
    FirebaseAuth userAuth;
    FirebaseUser firebaseUser;
    String UID, idPenjual;
    TextView total_harga;
    Button btn_checkout;
    ImageView arrow_back;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cart_, container, false);

        initComponents();
        getKeranjangData();

        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCartList();
            }
        });


        return view;
    }

    private void initComponents() {
        arrow_back = view.findViewById(R.id.btn_back);
        btn_checkout = view.findViewById(R.id.btn_checkout);
        total_harga = view.findViewById(R.id.total_harga);
        rc_keranjang = view.findViewById(R.id.rc_keranjang);
        rc_keranjang.setHasFixedSize(true);
        rc_keranjang.setLayoutManager(new LinearLayoutManager(view.getContext()));
        model_keranjang = new ArrayList<>();
        adapter_keranjang = new Adapter_Keranjang(view.getContext(), model_keranjang, this);
        rc_keranjang.setAdapter(adapter_keranjang);
        userAuth = FirebaseAuth.getInstance();
        firebaseUser = userAuth.getCurrentUser();
        dbKeranjang = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();
    }

    private void getKeranjangData() {
        if (firebaseUser != null) {
            UID = firebaseUser.getUid();
            dbKeranjang.child("Keranjang").orderByChild("id_user").equalTo(UID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        model_keranjang.clear();
                        double total = 0;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Model_Keranjang listItem = dataSnapshot.getValue(Model_Keranjang.class);
                            model_keranjang.add(listItem);
                            total += Double.parseDouble(listItem.getHarga()) * listItem.getJumlahProduk();
                            idPenjual = listItem.getId_penjual();
                        }

                        total_harga.setText(formatRupiah(total));
                        adapter_keranjang.notifyDataSetChanged();


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    public void onUpdateTotalPrice() {
        updateTotalPrice(); // Call the method to update the total price
    }

    private void updateTotalPrice() {
        double total = 0;
        for (Model_Keranjang item : model_keranjang) {
            total += Double.parseDouble(item.getHarga()) * item.getJumlahProduk();
        }
        total_harga.setText(formatRupiah(total)); // Update the total price TextView
    }

    private String formatRupiah(Double number) {
        Locale locale = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(locale);
        return formatRupiah.format(number);
    }

    private void checkCartList() {
        if (model_keranjang.isEmpty()) {
            Toast.makeText(view.getContext(), "Belum ada item untuk di checkout", Toast.LENGTH_SHORT).show();
        } else {

//            startActivity(new Intent(getContext(), Activity_Checkout.class));
            if (firebaseUser!=null){
                UID = firebaseUser.getUid();

                dbKeranjang.child("Users").child(UID).child("alamat").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            startActivity(new Intent(view.getContext(), Activity_Check_Ongkir.class));
                        } else {
                            Toast.makeText(view.getContext(), "Anda belum melengkapi data\nSilahkan melengkapi data pada menu profil", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } else {
                Dialog dialog = new Dialog(view.getContext());
                dialog.setContentView(R.layout.popup_autentication);
                dialog.show();

                Button btn_ok = dialog.findViewById(R.id.btn_okay);

                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), Login_Activity.class));
                    }
                });
            }
        }
    }
}