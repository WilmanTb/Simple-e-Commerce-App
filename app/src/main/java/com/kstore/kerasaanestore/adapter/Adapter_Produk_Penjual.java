package com.kstore.kerasaanestore.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kstore.kerasaanestore.activity.penjual.Activity_Detail_Produk_Penjual;
import com.kstore.kerasaanestore.R;
import com.kstore.kerasaanestore.jclass.Data_Value;
import com.kstore.kerasaanestore.model.Model_Katalog;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Adapter_Produk_Penjual extends RecyclerView.Adapter<Adapter_Produk_Penjual.ViewHolder> {

    Context context;
    ArrayList<Model_Katalog> listItem;

    DatabaseReference dbProduct = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();

    public Adapter_Produk_Penjual(Context context, ArrayList<Model_Katalog> listItem) {
        this.context = context;
        this.listItem = listItem;
    }

    @NonNull
    @Override
    public Adapter_Produk_Penjual.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_produk_penjual, parent, false);
        return new Adapter_Produk_Penjual.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Produk_Penjual.ViewHolder holder, int position) {
        Model_Katalog modelKatalog = listItem.get(position);
        holder.nama_produk.setText(modelKatalog.getNama());
        holder.stokProduk.setText("X"+modelKatalog.getStok());
        holder.hargaProduk.setText(formatRupiah(Double.parseDouble(modelKatalog.getHarga())));
        Glide.with(context).load(listItem.get(position).getGambar()).into(holder.img_produk);
        holder.delete_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(context)
                        .setMessage("Yakin menghapus produk ?")
                        .setPositiveButton("Hapus", null)
                        .setNegativeButton("Batal", null)
                        .show();
                Button positiveButton = alertDialog.getButton(alertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dbProduct.child("Produk").child("Data").child(listItem.get(holder.getAdapterPosition()).getIdProduk()).setValue(null);
                        Toast.makeText(context, "Produk berhasil dihapus", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nama_produk, stokProduk, hargaProduk;
        ImageView img_produk, delete_product;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nama_produk = itemView.findViewById(R.id.nama_produk);
            stokProduk = itemView.findViewById(R.id.stokProduk);
            hargaProduk = itemView.findViewById(R.id.hargaProduk);
            img_produk = itemView.findViewById(R.id.img_produk);
            delete_product = itemView.findViewById(R.id.delete_product);
        }
    }

    private String formatRupiah(Double number){
        Locale locale = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(locale);
        return formatRupiah.format(number);
    }
}
