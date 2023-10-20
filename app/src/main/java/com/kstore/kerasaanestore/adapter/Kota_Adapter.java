package com.kstore.kerasaanestore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kstore.kerasaanestore.model.Model_Kota;
import com.kstore.kerasaanestore.model.Model_Provinsi;

import java.util.ArrayList;

public class Kota_Adapter extends ArrayAdapter<Model_Kota> {
    private Context context;
    private ArrayList<Model_Kota> cityList;

    public Kota_Adapter(Context context, int resource, ArrayList<Model_Kota> cityList) {
        super(context, resource, cityList);
        this.context = context;
        this.cityList = cityList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(cityList.get(position).getCity_name());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(cityList.get(position).getCity_name());

        return convertView;
    }
}
