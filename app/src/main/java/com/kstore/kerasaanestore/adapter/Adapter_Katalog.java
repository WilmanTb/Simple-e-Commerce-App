package com.kstore.kerasaanestore.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kstore.kerasaanestore.activity.user.Activity_Detail_Produk_User;
import com.kstore.kerasaanestore.model.Model_Katalog;
import com.kstore.kerasaanestore.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Adapter_Katalog extends RecyclerView.Adapter<Adapter_Katalog.MyViewHolder> {

    ArrayList<Model_Katalog> itemList = new ArrayList<>();

    Context context;
    ArrayList<Model_Katalog> list;

    public Adapter_Katalog(Context context, ArrayList<Model_Katalog> list) {
        this.context = context;
        this.list = list;
    }

    public void setItemList(ArrayList<Model_Katalog> items) {
        itemList = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Adapter_Katalog.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_katalog_dashboard,parent, false);
        return new Adapter_Katalog.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Katalog.MyViewHolder holder, int position) {
        Model_Katalog modelKatalog = list.get(position);
        holder.nama_produk.setText(modelKatalog.getNama());
        holder.harga_produk.setText(formatRupiah(Double.parseDouble(modelKatalog.getHarga())));
        Glide.with(context).load(list.get(position).getGambar()).into(holder.gambar_produk);
        holder.cv_katalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Activity_Detail_Produk_User.class).putExtra("detail", list.get(holder.getAdapterPosition())));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView gambar_produk;
        TextView nama_produk, harga_produk;
        CardView cv_katalog;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            gambar_produk = itemView.findViewById(R.id.gambar_produk);
            nama_produk = itemView.findViewById(R.id.nama_produk);
            harga_produk = itemView.findViewById(R.id.harga_produk);
            cv_katalog = itemView.findViewById(R.id.cv_katalog);
        }
    }
    private String formatRupiah(Double number){
        Locale locale = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(locale);
        return formatRupiah.format(number);
    }
}
