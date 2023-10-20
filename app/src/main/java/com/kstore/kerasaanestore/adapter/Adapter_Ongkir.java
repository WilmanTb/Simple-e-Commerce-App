package com.kstore.kerasaanestore.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.kstore.kerasaanestore.R;
import com.kstore.kerasaanestore.activity.user.Activity_Checkout;
import com.kstore.kerasaanestore.model.Model_Ongkir;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Adapter_Ongkir extends RecyclerView.Adapter<Adapter_Ongkir.ViewHolder> {

    Context context;
    ArrayList<Model_Ongkir> model_ongkirs;

    public Adapter_Ongkir(Context context, ArrayList<Model_Ongkir> model_ongkirs) {
        this.context = context;
        this.model_ongkirs = model_ongkirs;
    }

    @NonNull
    @Override
    public Adapter_Ongkir.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_opsi_kirim, parent, false);
        return new Adapter_Ongkir.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Ongkir.ViewHolder holder, int position) {
        Model_Ongkir model_ongkir = model_ongkirs.get(position);
        holder.txt_ongkir.setText(String.valueOf(formatRupiah(Double.parseDouble(String.valueOf(model_ongkir.getValue())))));
        holder.tipe_servis.setText(model_ongkir.getService());
        holder.txt_estimasi.setText(model_ongkir.getEtd() +  " Hari");
        holder.cv_jasa_ongkir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Activity_Checkout.class).putExtra("deskripsi", model_ongkirs.get(holder.getAdapterPosition())));
            }
        });
    }

    @Override
    public int getItemCount() {
        return model_ongkirs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_ongkir, tipe_servis, txt_estimasi;
        CardView cv_jasa_ongkir;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_ongkir = itemView.findViewById(R.id.tvPrice);
            tipe_servis = itemView.findViewById(R.id.tvType);
            txt_estimasi = itemView.findViewById(R.id.tvEst);
            cv_jasa_ongkir = itemView.findViewById(R.id.cv_item);
        }
    }
    private String formatRupiah(Double number){
        Locale locale = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(locale);
        return formatRupiah.format(number);
    }
}

