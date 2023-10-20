package com.kstore.kerasaanestore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kstore.kerasaanestore.R;
import com.kstore.kerasaanestore.jclass.Data_Value;
import com.kstore.kerasaanestore.model.Model_Keranjang;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Adapter_Keranjang extends RecyclerView.Adapter<Adapter_Keranjang.ViewHolder> {

    Context context;
    ArrayList<Model_Keranjang> list;
    TotalPriceListener totalPriceListener;

    public interface TotalPriceListener {
        void onUpdateTotalPrice();
    }

    int itemCount = 0;

    public Adapter_Keranjang(Context context, ArrayList<Model_Keranjang> list, TotalPriceListener listener) {
        this.context = context;
        this.list = list;
        this.totalPriceListener = listener;
    }

    @NonNull
    @Override
    public Adapter_Keranjang.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_keranjang,parent,false);
        return new Adapter_Keranjang.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Keranjang.ViewHolder holder, int position) {
        Model_Keranjang model_keranjang = list.get(position);
        holder.nama_produk.setText(model_keranjang.getNama_produk());
        holder.harga_produk.setText(formatRupiah(Double.parseDouble(model_keranjang.getHarga())));
        holder.jumlah_produk.setText(String.valueOf(model_keranjang.getJumlahProduk()));
        Glide.with(context).load(list.get(position).getGambar()).into(holder.img_produk);
        DatabaseReference dbKeranjang = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();

        holder.btn_increaseAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemCount = model_keranjang.getJumlahProduk();
                itemCount++;
                holder.jumlah_produk.setText(String.valueOf(itemCount));
                model_keranjang.setJumlahProduk(itemCount);
                notifyDataSetChanged();
                totalPriceListener.onUpdateTotalPrice(); // Notify the Fragment to update total price
                dbKeranjang.child("Keranjang").child(model_keranjang.getId_keranjang()).child("jumlahProduk").setValue(itemCount);
            }
        });

        holder.btn_decreaseAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemCount = model_keranjang.getJumlahProduk();
                if (itemCount > 1) {
                    itemCount--;
                    holder.jumlah_produk.setText(String.valueOf(itemCount));
                    model_keranjang.setJumlahProduk(itemCount);
                    notifyDataSetChanged();
                    totalPriceListener.onUpdateTotalPrice(); // Notify the Fragment to update total price
                    dbKeranjang.child("Keranjang").child(model_keranjang.getId_keranjang()).child("jumlahProduk").setValue(itemCount);
                } else {
                    dbKeranjang.child("Keranjang").child(model_keranjang.getId_keranjang()).setValue(null);
                    totalPriceListener.onUpdateTotalPrice();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nama_produk, harga_produk, jumlah_produk;
        ImageView img_produk;
        Button btn_increaseAmount, btn_decreaseAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nama_produk = itemView.findViewById(R.id.nama_produk);
            harga_produk = itemView.findViewById(R.id.harga_produk);
            jumlah_produk = itemView.findViewById(R.id.jumlah_produk);
            img_produk = itemView.findViewById(R.id.img_produk);
            btn_increaseAmount = itemView.findViewById(R.id.btn_increaseAmount);
            btn_decreaseAmount = itemView.findViewById(R.id.btn_decreaseAmount);
        }
    }

    private String formatRupiah(Double number){
        Locale locale = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(locale);
        return formatRupiah.format(number);
    }

}
