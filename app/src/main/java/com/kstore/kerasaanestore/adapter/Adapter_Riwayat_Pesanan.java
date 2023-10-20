package com.kstore.kerasaanestore.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kstore.kerasaanestore.R;
import com.kstore.kerasaanestore.jclass.Data_Value;
import com.kstore.kerasaanestore.model.Model_Pesanan;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Adapter_Riwayat_Pesanan extends RecyclerView.Adapter<Adapter_Riwayat_Pesanan.ViewHolder> {

    Context context;
    ArrayList<Model_Pesanan> listRiwayat;

    public Adapter_Riwayat_Pesanan(Context context, ArrayList<Model_Pesanan> listRiwayat) {
        this.context = context;
        this.listRiwayat = listRiwayat;
    }

    @NonNull
    @Override
    public Adapter_Riwayat_Pesanan.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.lsit_riwayat_pesanan, parent, false);
        return new Adapter_Riwayat_Pesanan.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Riwayat_Pesanan.ViewHolder holder, int position) {
        Model_Pesanan model_pesanan = listRiwayat.get(position);
        holder.harga_produk.setText(formatRupiah(Double.parseDouble(model_pesanan.getTotal_bayar())));
        holder.tanggal_pesanan.setText(model_pesanan.getTanggal_pesanan());

        String idProduk = model_pesanan.getId_produk();
        DatabaseReference dbProduk = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();

        dbProduk.child("Produk").child("Data").child(idProduk).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                holder.nama_produk.setText(snapshot.child("nama").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return listRiwayat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nama_produk, harga_produk, tanggal_pesanan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nama_produk = itemView.findViewById(R.id.nama_produk);
            harga_produk = itemView.findViewById(R.id.harga_produk);
            tanggal_pesanan = itemView.findViewById(R.id.tanggal_pesanan);
        }
    }

    private String formatRupiah(Double number){
        Locale locale = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(locale);
        return formatRupiah.format(number);
    }
}
