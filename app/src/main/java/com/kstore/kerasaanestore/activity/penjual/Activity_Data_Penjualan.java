package com.kstore.kerasaanestore.activity.penjual;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kstore.kerasaanestore.R;
import com.kstore.kerasaanestore.jclass.Data_Value;
import com.kstore.kerasaanestore.model.Model_Pesanan;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Activity_Data_Penjualan extends AppCompatActivity {

    private TextView txt_penghasilan, txt_terjual, txt_jumlah_produk;
    private DatabaseReference dbPenjualan;
    private FloatingActionButton download_data;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String UID;
    private ArrayList<Model_Pesanan> model_pesananArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_penjualan);

        initComponents();
        getDataPenjualan();
        getPesananData();

        download_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download_laporan(model_pesananArrayList);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Activity_Data_Penjualan.this, Dashboard_Penjual.class));
        finish();
    }

    private void initComponents(){
        txt_penghasilan = findViewById(R.id.txt_penghasilan);
        txt_terjual = findViewById(R.id.txt_terjual);
        txt_jumlah_produk = findViewById(R.id.txt_jumlah_produk);
        download_data = findViewById(R.id.download_data);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        UID = firebaseUser.getUid();
        model_pesananArrayList = new ArrayList<>();
        dbPenjualan = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();
    }

    private void getDataPenjualan(){
        dbPenjualan.child("Pesanan").orderByChild("id_penjual").equalTo(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int totalPenghasilan = 0;
                    int totalTerjual = 0;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String statusBayar = dataSnapshot.child("status_pembayaran").getValue().toString();
                        if (statusBayar.equals("dibayar")) {
                            String totalBayar = dataSnapshot.child("total_bayar").getValue().toString();
                            int penghasilan = Integer.parseInt(totalBayar);
                            totalPenghasilan += penghasilan;

                            String jumlahProduk = dataSnapshot.child("jumlah").getValue().toString();
                            int terjual = Integer.parseInt(jumlahProduk);
                            totalTerjual += terjual;
                        }
                    }
                    txt_penghasilan.setText(formatRupiah(Double.parseDouble(String.valueOf(totalPenghasilan))));
                    txt_terjual.setText(totalTerjual + " terjual");
                } else {
                    txt_penghasilan.setText("0");
                    txt_terjual.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dbPenjualan.child("Produk").child("Data").orderByChild("penjual").equalTo(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String jumlahProduk = String.valueOf(snapshot.getChildrenCount());
                    txt_jumlah_produk.setText(jumlahProduk + " produk");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getPesananData(){
        dbPenjualan.child("Pesanan").orderByChild("id_penjual").equalTo(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    model_pesananArrayList.clear();
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        String statusBayar = dataSnapshot.child("status_pembayaran").getValue().toString();
                        if (statusBayar.equals("dibayar")){
                            Model_Pesanan model_pesanan = dataSnapshot.getValue(Model_Pesanan.class);
                            model_pesananArrayList.add(model_pesanan);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void download_laporan(ArrayList<Model_Pesanan> mergedLists) {
        try {
            String strDate = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss", Locale.getDefault()).format(new Date());
            File root = new File(Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Data Penjualan");
            if (!root.exists())
                root.mkdirs();
            File path = new File(root, "/" + strDate + ".xlsx");

            XSSFWorkbook workbook = new XSSFWorkbook();
            FileOutputStream outputStream = new FileOutputStream(path);

            XSSFCellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderTop(BorderStyle.MEDIUM);
            headerStyle.setBorderBottom(BorderStyle.MEDIUM);
            headerStyle.setBorderRight(BorderStyle.MEDIUM);
            headerStyle.setBorderLeft(BorderStyle.MEDIUM);

            XSSFFont font = workbook.createFont();
            font.setFontHeightInPoints((short) 12);
            font.setColor(IndexedColors.WHITE.getIndex());
            font.setBold(true);
            headerStyle.setFont(font);

            XSSFSheet sheet = workbook.createSheet("Data Laporan Penjualan");
            XSSFRow row = sheet.createRow(0);

            XSSFCell cell = row.createCell(0);
            cell.setCellValue("ID Pesanan");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(1);
            cell.setCellValue("Id Pembeli");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(2);
            cell.setCellValue("Id Penjual");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(3);
            cell.setCellValue("Id Produk");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(4);
            cell.setCellValue("Jumlah");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(5);
            cell.setCellValue("Layanan");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(6);
            cell.setCellValue("Estimasi");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(7);
            cell.setCellValue("Ongkir");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(8);
            cell.setCellValue("Total pembayaran");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(9);
            cell.setCellValue("Tanggal pesanan");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(10);
            cell.setCellValue("Status bayar");
            cell.setCellStyle(headerStyle);


            for (int i = 0; i < mergedLists.size(); i++) {
                row = sheet.createRow(i + 1);

                cell = row.createCell(0);
                cell.setCellValue(mergedLists.get(i).getId_pesanan());
                sheet.setColumnWidth(0, (mergedLists.get(i).getId_pesanan().length() + 30) * 256);

                cell = row.createCell(1);
                cell.setCellValue(mergedLists.get(i).getId_user());
                sheet.setColumnWidth(1, mergedLists.get(i).getId_user().length() * 400);

                cell = row.createCell(2);
                cell.setCellValue(mergedLists.get(i).getId_penjual());
                sheet.setColumnWidth(2, mergedLists.get(i).getId_penjual().length() * 400);

                cell = row.createCell(3);
                cell.setCellValue(mergedLists.get(i).getId_produk());
                sheet.setColumnWidth(3, mergedLists.get(i).getId_produk().length() * 400);

                cell = row.createCell(4);
                cell.setCellValue(mergedLists.get(i).getJumlah());
                sheet.setColumnWidth(4, 400);

                cell = row.createCell(5);
                cell.setCellValue(mergedLists.get(i).getLayanan());
                sheet.setColumnWidth(5, mergedLists.get(i).getLayanan().length() * 400);

                cell = row.createCell(6);
                cell.setCellValue(mergedLists.get(i).getEstimasi());
                sheet.setColumnWidth(6, mergedLists.get(i).getEstimasi().length() * 400);

                cell = row.createCell(7);
                cell.setCellValue(mergedLists.get(i).getOngkir());
                sheet.setColumnWidth(7, mergedLists.get(i).getOngkir().length() * 400);

                cell = row.createCell(8);
                cell.setCellValue(mergedLists.get(i).getTotal_bayar());
                sheet.setColumnWidth(8, mergedLists.get(i).getTotal_bayar().length() * 400);

                cell = row.createCell(9);
                cell.setCellValue(mergedLists.get(i).getTanggal_pesanan());
                sheet.setColumnWidth(9, mergedLists.get(i).getTanggal_pesanan().length() * 400);

                cell = row.createCell(10);
                cell.setCellValue(mergedLists.get(i).getStatus_pembayaran());
                sheet.setColumnWidth(10, mergedLists.get(i).getStatus_pembayaran().length() * 400);


            }
            workbook.write(outputStream);
            outputStream.close();
            Toast.makeText(Activity_Data_Penjualan.this, "Data berhasil di ekspor!", Toast.LENGTH_SHORT).show();

            Uri uri = FileProvider.getUriForFile(Activity_Data_Penjualan.this, "com.kstore.kerasaanestore.provider", path);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String mimeType = getContentResolver().getType(uri);
            intent.setDataAndType(uri, mimeType);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Intent chooser = Intent.createChooser(intent, "Buka dengan...");
//            if (intent.resolveActivity(getPackageManager()) != null) {
//                startActivity(chooser);
//            }
            startActivity(chooser);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static String formatRupiah(double amount) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setCurrencySymbol("Rp ");
        symbols.setGroupingSeparator('.');

        DecimalFormat rupiahFormat = (DecimalFormat) DecimalFormat.getCurrencyInstance(Locale.getDefault());
        rupiahFormat.setDecimalFormatSymbols(symbols);
        rupiahFormat.setMaximumFractionDigits(0);

        return rupiahFormat.format(amount);
    }
}