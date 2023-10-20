package com.kstore.kerasaanestore.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kstore.kerasaanestore.activity.user.Activity_Product_By_Kategori;
import com.kstore.kerasaanestore.model.Model_Kategori;
import com.kstore.kerasaanestore.R;

import java.util.ArrayList;

public class Adapter_Kategori extends RecyclerView.Adapter<Adapter_Kategori.MyViewHolder> {

    Context context;
    ArrayList<Model_Kategori> list;

    public Adapter_Kategori(Context context, ArrayList<Model_Kategori> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Adapter_Kategori.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_kategori, parent, false);
        return new Adapter_Kategori.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Kategori.MyViewHolder holder, int position) {
        Model_Kategori modelKategori = list.get(position);
        Glide.with(context).load(list.get(position).getGambar()).into(holder.gambar_kategori);
        holder.cv_kategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Activity_Product_By_Kategori.class).putExtra("kategori",list.get(holder.getAdapterPosition())));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView gambar_kategori;
        CardView cv_kategori;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            gambar_kategori = itemView.findViewById(R.id.gambar_kategori);
            cv_kategori = itemView.findViewById(R.id.cv_kategori);
        }
    }
}
