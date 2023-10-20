package com.kstore.kerasaanestore.activity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kstore.kerasaanestore.R;
import com.kstore.kerasaanestore.adapter.Adapter_Checkout;
import com.kstore.kerasaanestore.jclass.Data_Value;
import com.kstore.kerasaanestore.model.Model_Keranjang;
import com.kstore.kerasaanestore.model.Model_Ongkir;
import com.kstore.kerasaanestore.model.Model_Pesanan;
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Activity_Checkout extends AppCompatActivity implements TransactionFinishedCallback {

    private ImageView arrow_back, img_produk;
    private TextView nama_noHp, alamat_lengkap, kode_pos, txt_service, txt_ongkir, txt_estimasi, txt_total_pembayaran,
            nama_toko, nama_produk, harga_produk, jumlah_produk, txt_service1, txt_ongkir1, txt_estimasi1, txt_opsi, txt_opsi1;
    private CardView cv_item2, cv_item, cv_item1;
    private Button btn_create_order;
    private DatabaseReference dbCheckout;
    private Adapter_Checkout adapter_checkout;
    private ArrayList<Model_Keranjang> model_keranjangArrayList;
    private FirebaseAuth userAuth;
    private FirebaseUser firebaseUser;
    public static String UID, hargaproduk, jumlahProduk, idPenjual, ongkirProduk, estimasiPengiriman, currentDate, NamaUser, EmailUser, NoHp, idKeranjang, transactionId, idProduk;
    public static int totalBayar = 0;
    private RecyclerView rc_checkout_item;
    private Model_Ongkir model_ongkir;
    private static long randomInteger;
    private int BUY_REQUEST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        BUY_REQUEST = Activity_Detail_Produk_User.BUY_REQUEST;

        initComponents();
        setUserDetail();
        setVisibility();
        if (BUY_REQUEST == 0) {
            getKeranjangData();
        }else {
            getInstantBuyData();
        }
        getCurrentDate();
        getUserData();
        transactionID();

        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_create_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model_Pesanan model_pesanan = new Model_Pesanan(transactionId, UID, idPenjual, jumlahProduk, ongkirProduk, "JNE", estimasiPengiriman, "Transfer bank", "belum dibayar", currentDate, idProduk, String.valueOf(totalBayar));
                dbCheckout.child("Pesanan").child(transactionId).setValue(model_pesanan);
                if (BUY_REQUEST==0)
                    dbCheckout.child("Keranjang").child(idKeranjang).setValue(null);
                if (transactionId != null) {
                    MidtransSDK.getInstance().setTransactionRequest(transactionRequest(transactionId, Double.parseDouble(String.valueOf(totalBayar)), 1, "Pembayaran pesanan"));
                    MidtransSDK.getInstance().startPaymentUiFlow(Activity_Checkout.this);
                } else {
                    Toast.makeText(Activity_Checkout.this, "pepepk", Toast.LENGTH_SHORT).show();
                }
            }
        });
        createPayment();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Activity_Checkout.this, Activity_Dashboard_User.class));
        finish();
    }

    private void initComponents() {

        nama_toko = findViewById(R.id.nama_toko);
        nama_produk = findViewById(R.id.nama_produk);
        harga_produk = findViewById(R.id.harga_produk);
        jumlah_produk = findViewById(R.id.jumlah_produk);
        txt_service1 = findViewById(R.id.tvType1);
        txt_ongkir1 = findViewById(R.id.tvPrice1);
        txt_estimasi1 = findViewById(R.id.tvEst1);
        txt_opsi = findViewById(R.id.txt_opsi);
        txt_opsi1 = findViewById(R.id.txt_opsi1);
        img_produk = findViewById(R.id.img_produk);
        cv_item = findViewById(R.id.cv_item);
        cv_item1 = findViewById(R.id.cv_item1);
        cv_item2 = findViewById(R.id.cv_item2);
        img_produk = findViewById(R.id.img_produk);


        txt_service = findViewById(R.id.tvType);
        txt_ongkir = findViewById(R.id.tvPrice);
        txt_estimasi = findViewById(R.id.tvEst);

        txt_total_pembayaran = findViewById(R.id.total_pembayaran);

        arrow_back = findViewById(R.id.btn_back);
        nama_noHp = findViewById(R.id.nama_noHp);
        alamat_lengkap = findViewById(R.id.alamat_lengkap);
        kode_pos = findViewById(R.id.kode_pos);
        btn_create_order = findViewById(R.id.btn_cek_ongkir);
        dbCheckout = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();
        userAuth = FirebaseAuth.getInstance();
        firebaseUser = userAuth.getCurrentUser();
        UID = firebaseUser.getUid();

        rc_checkout_item = findViewById(R.id.rc_checkout_item);
        rc_checkout_item.setHasFixedSize(true);
        rc_checkout_item.setLayoutManager(new LinearLayoutManager(this));
        model_keranjangArrayList = new ArrayList<>();
        adapter_checkout = new Adapter_Checkout(this, model_keranjangArrayList);
        rc_checkout_item.setAdapter(adapter_checkout);
    }

    private void setUserDetail() {
        dbCheckout.child("Users").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nama_noHp.setText(snapshot.child("nama").getValue().toString() + " | " + snapshot.child("handphone").getValue().toString());
                alamat_lengkap.setText(snapshot.child("alamat").getValue().toString());
                kode_pos.setText("ID :  " + UID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getInstantBuyData(){
        hargaproduk = Data_Value.hargaProduk;
        jumlahProduk = String.valueOf(Data_Value.jlhItem);
        idPenjual = Data_Value.idPenjual;
        idProduk = Data_Value.idProduk;
        nama_produk.setText(Data_Value.namaProduk);
        Glide.with(getApplicationContext()).load(Data_Value.gambarProduk).into(img_produk);
        harga_produk.setText(formatRupiah(Double.parseDouble(hargaproduk)));
        jumlah_produk.setText("X"+jumlahProduk);
        getNamaTokoInstantBuy();

        getJasaKirim(new CallbackJasa() {
            @Override
            public void onCallback(String Ongkir) {
                totalBayar = (Integer.parseInt(hargaproduk) * Integer.parseInt(jumlahProduk)) + Integer.parseInt(Ongkir);
                txt_total_pembayaran.setText(formatRupiah(Double.parseDouble(String.valueOf(totalBayar))));
            }
        });
    }

    private void getNamaTokoInstantBuy(){
        idPenjual = Data_Value.idPenjual;

        dbCheckout.child("Toko").orderByChild("id_penjual").equalTo(idPenjual).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        nama_toko.setText(dataSnapshot.child("nama_toko").getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getKeranjangData() {
        dbCheckout.child("Keranjang").orderByChild("id_user").equalTo(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    model_keranjangArrayList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Model_Keranjang itemList = dataSnapshot.getValue(Model_Keranjang.class);
                        model_keranjangArrayList.add(itemList);
                        hargaproduk = itemList.getHarga();
                        jumlahProduk = String.valueOf(itemList.getJumlahProduk());
                        idPenjual = itemList.getId_penjual();
                        idKeranjang = itemList.getId_keranjang();
                        idProduk = itemList.getId_produk();

                    }
                    adapter_checkout.notifyDataSetChanged();
                    getJasaKirim(new CallbackJasa() {
                        @Override
                        public void onCallback(String Ongkir) {
                            totalBayar = (Integer.parseInt(hargaproduk) * Integer.parseInt(jumlahProduk)) + Integer.parseInt(Ongkir);
                            txt_total_pembayaran.setText(formatRupiah(Double.parseDouble(String.valueOf(totalBayar))));
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getUserData() {
        dbCheckout.child("Users").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NamaUser = snapshot.child("nama").getValue().toString();
                EmailUser = snapshot.child("email").getValue().toString();
                NoHp = snapshot.child("handphone").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getJasaKirim(CallbackJasa callback) {
        Object object = getIntent().getSerializableExtra("deskripsi");
        if (object instanceof Model_Ongkir) {
            model_ongkir = (Model_Ongkir) object;
        }

        if (model_ongkir != null) {
            ongkirProduk = String.valueOf(model_ongkir.getValue());
            estimasiPengiriman = model_ongkir.getEtd();
            if (BUY_REQUEST == 0) {
                txt_estimasi.setText(model_ongkir.getEtd() + " Hari");
                txt_service.setText(model_ongkir.getService());
                txt_ongkir.setText(String.valueOf(formatRupiah(Double.parseDouble(String.valueOf(model_ongkir.getValue())))));
                callback.onCallback(ongkirProduk);
            } else {
                txt_estimasi1.setText(model_ongkir.getEtd() + " Hari");
                txt_service1.setText(model_ongkir.getService());
                txt_ongkir1.setText(String.valueOf(formatRupiah(Double.parseDouble(String.valueOf(model_ongkir.getValue())))));
                callback.onCallback(ongkirProduk);
            }
        }
    }

    private String formatRupiah(Double number) {
        Locale locale = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(locale);
        return formatRupiah.format(number);
    }

    private interface CallbackJasa {
        void onCallback(String Ongkir);
    }

    private void getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        currentDate = simpleDateFormat.format(calendar.getTime());
    }

    private void transactionID() {
        Random random = new Random();
        randomInteger = (random.nextLong() % 9000000000L) + 1000000000L;
        transactionId = String.valueOf(randomInteger);
        Log.d("ID1", transactionId);
    }

    private void createPayment() {
        SdkUIFlowBuilder.init()
                .setContext(this)
                .setMerchantBaseUrl("https://jenswifi.000webhostapp.com/response.php/")
                .setClientKey("SB-Mid-client-Q-FGg6jCULeq1jXG")
                .setTransactionFinishedCallback(this)
                .enableLog(true)
                .buildSDK();
    }

    public static CustomerDetails customerDetails() {
        Activity_Checkout activity_detail_pembayaran_user = new Activity_Checkout();
        String nama = activity_detail_pembayaran_user.NamaUser;
        String email = activity_detail_pembayaran_user.EmailUser;
        String hp = activity_detail_pembayaran_user.NoHp;
        CustomerDetails cd = new CustomerDetails();
        cd.setCustomerIdentifier(nama);
        cd.setFirstName(nama);
        cd.setEmail(email);
        cd.setPhone(hp);
        return cd;
    }

    public static TransactionRequest transactionRequest(String id, double price, int qty, String name) {
        Activity_Checkout activity_checkout = new Activity_Checkout();
        double amount = Double.parseDouble(String.valueOf(activity_checkout.totalBayar));
        String orderID = String.valueOf(activity_checkout.randomInteger);
        TransactionRequest request = new TransactionRequest("Kerasaan eStore-" + orderID + " ", amount);
        request.setCustomerDetails(customerDetails());
        ItemDetails details = new ItemDetails(id, price, qty, name);

        ArrayList<ItemDetails> itemDetails = new ArrayList<>();
        itemDetails.add(details);
        request.setItemDetails(itemDetails);
        return request;
    }

    @Override
    public void onTransactionFinished(TransactionResult transactionResult) {
        if (transactionResult.getStatus().equals("pending")) {
            String basurl = "https://api.sandbox.midtrans.com/v2/";
            String transId = transactionResult.getResponse().getTransactionId();
            String status = "/status";
            String Url = basurl + transId + status;
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(Url)
                    .addHeader("accept", "application/json")
                    .addHeader("authorization", "Basic U0ItTWlkLXNlcnZlci1tRTVJZU9LZnh4RjNmLW5zOWpaMjYtbjQ6")
                    .build();

            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        try {
                            String responseBody = response.body().string();
                            JSONObject jsonObject = new JSONObject(responseBody);
                            final String statusCode = jsonObject.getString("status_code");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String statusTransaksi = statusCode;
                                    if (statusTransaksi.equals("200")) {
                                        dbCheckout.child("Pesanan").child(transactionId).child("status_pembayaran").setValue("dibayar");
                                        Toast.makeText(Activity_Checkout.this, "Pesanan berhasil dibayar", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Activity_Checkout.this, Activity_Dashboard_User.class));
                                    } else {
                                        Toast.makeText(Activity_Checkout.this, "Tagihan pembayaran menunggu dibayarkan", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        // Handle the error response
                    }

                    // Close the response body
                    response.body().close();
                }
            });

        } else {
            // Payment failed
            Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void setVisibility(){
        if (BUY_REQUEST == 0){
            rc_checkout_item.setVisibility(View.VISIBLE);
            cv_item.setVisibility(View.VISIBLE);
            txt_opsi.setVisibility(View.VISIBLE);
        }else{
            cv_item1.setVisibility(View.VISIBLE);
            cv_item2.setVisibility(View.VISIBLE);
            nama_toko.setVisibility(View.VISIBLE);
            nama_produk.setVisibility(View.VISIBLE);
            harga_produk.setVisibility(View.VISIBLE);
            jumlah_produk.setVisibility(View.VISIBLE);
            txt_opsi1.setVisibility(View.VISIBLE);
        }
    }
}