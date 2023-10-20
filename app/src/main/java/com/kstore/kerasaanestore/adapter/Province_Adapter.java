package com.kstore.kerasaanestore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kstore.kerasaanestore.model.Model_Provinsi;

import java.util.ArrayList;

public class Province_Adapter extends ArrayAdapter<Model_Provinsi> {

    private Context context;
    private ArrayList<Model_Provinsi> provinceList;

    public Province_Adapter(Context context, int resource, ArrayList<Model_Provinsi> provinceList) {
        super(context, resource, provinceList);
        this.context = context;
        this.provinceList = provinceList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(provinceList.get(position).getProvince());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(provinceList.get(position).getProvince());

        return convertView;
    }
}
