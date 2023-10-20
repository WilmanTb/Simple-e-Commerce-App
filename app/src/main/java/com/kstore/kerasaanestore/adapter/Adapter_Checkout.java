package com.kstore.kerasaanestore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kstore.kerasaanestore.R;
import com.kstore.kerasaanestore.jclass.Data_Value;
import com.kstore.kerasaanestore.model.Model_Keranjang;
import com.kstore.kerasaanestore.model.Model_Order;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Adapter_Checkout extends RecyclerView.Adapter<Adapter_Checkout.ViewHolder> {

    Context context;
    ArrayList<Model_Keranjang> list;

    public Adapter_Checkout(Context context, ArrayList<Model_Keranjang> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Adapter_Checkout.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_checkout, parent, false);
        return new Adapter_Checkout.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Checkout.ViewHolder holder, int position) {
        Model_Keranjang model_keranjang = list.get(position);
        holder.nama_produk.setText(model_keranjang.getNama_produk());
        holder.harga_produk.setText(formatRupiah(Double.parseDouble(model_keranjang.getHarga())));
        holder.jumlah_produk.setText("x"+model_keranjang.getJumlahProduk());
        Glide.with(context).load(list.get(position).getGambar()).into(holder.img_produk);

        DatabaseReference dbToko = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();

        dbToko.child("Toko").orderByChild("id_penjual").equalTo(model_keranjang.getId_penjual()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()) {
                        holder.nama_toko.setText(dataSnapshot.child("nama_toko").getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nama_toko, nama_produk, harga_produk, jumlah_produk;
        ImageView img_produk;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nama_produk = itemView.findViewById(R.id.nama_produk);
            nama_toko = itemView.findViewById(R.id.nama_toko);
            harga_produk = itemView.findViewById(R.id.harga_produk);
            jumlah_produk = itemView.findViewById(R.id.jumlah_produk);
            img_produk = itemView.findViewById(R.id.img_produk);
        }
    }

    private String formatRupiah(Double number){
        Locale locale = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(locale);
        return formatRupiah.format(number);
    }
}
