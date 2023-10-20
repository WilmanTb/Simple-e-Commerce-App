package com.kstore.kerasaanestore.activity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kstore.kerasaanestore.R;
import com.kstore.kerasaanestore.adapter.Adapter_Ongkir;
import com.kstore.kerasaanestore.jclass.Data_Value;
import com.kstore.kerasaanestore.model.Model_Ongkir;

import org.apache.xmlbeans.impl.soap.Detail;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Activity_Check_Ongkir extends AppCompatActivity {

    private ImageView arrow_back;
    private RecyclerView rc_jasaKirim;
    private Adapter_Ongkir adapter_ongkir;
    private ArrayList<Model_Ongkir> model_ongkirArrayList;
    private DatabaseReference dbCheckOngkir;
    private FirebaseAuth userAuth;
    private FirebaseUser firebaseUser;
    private String UID, idPenjual, idKotaPenjual, idKotaPembeli, berat_item, berat_total, jlhItem;
    private String etd = "";
    private String service = "";
    private String description = "";
    private int BUY_REQUEST;
    private int value = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_ongkir);

        BUY_REQUEST = Activity_Detail_Produk_User.BUY_REQUEST;

        initComponents();
        if (BUY_REQUEST == 0)
            getSellerId();
        else
            getSellerIdInstantBuy();

        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void initComponents() {
        arrow_back = findViewById(R.id.btn_back);
//        rc_jasaKirim = findViewById(R.id.rc_jasaKirim);
//        rc_jasaKirim.setHasFixedSize(true);
//        rc_jasaKirim.setLayoutManager(new LinearLayoutManager(this));
        model_ongkirArrayList = new ArrayList<>();
//        adapter_ongkir = new Adapter_Ongkir(this, model_ongkirArrayList);
//        rc_jasaKirim.setAdapter(adapter_ongkir);
        userAuth = FirebaseAuth.getInstance();
        firebaseUser = userAuth.getCurrentUser();
        UID = firebaseUser.getUid();
        dbCheckOngkir = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();
    }

    private void getSellerIdInstantBuy(){
        idPenjual = Data_Value.idPenjual;
        berat_item = Data_Value.beratProduk.replaceAll("[^0-9]", "");
        jlhItem = String.valueOf(Data_Value.jlhItem);
        getSellerandBuyerAddress(new SellerandUserCallBack() {
            @Override
            public void onCallback(String IDPENJUAL, String IDPEMBELI) {
                getJasaPengirimanList(IDPENJUAL, IDPEMBELI);
            }
        });
    }

    private void getSellerId() {
        dbCheckOngkir.child("Keranjang").orderByChild("id_user").equalTo(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        idPenjual = dataSnapshot.child("id_penjual").getValue().toString();
                        berat_item = dataSnapshot.child("berat").getValue().toString().replaceAll("[^0-9]", "");
                        jlhItem = dataSnapshot.child("jumlahProduk").getValue().toString();
                    }
                    getSellerandBuyerAddress(new SellerandUserCallBack() {
                        @Override
                        public void onCallback(String IDPENJUAL, String IDPEMBELI) {
                            getJasaPengirimanList(IDPENJUAL, IDPEMBELI);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getSellerandBuyerAddress(SellerandUserCallBack sellerandUserCallBack) {
        dbCheckOngkir.child("Users").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                idKotaPembeli = snapshot.child("kota").getValue().toString();
                dbCheckOngkir.child("Users").child(idPenjual).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        idKotaPenjual = snapshot.child("kota").getValue().toString();
                        sellerandUserCallBack.onCallback(idKotaPenjual, idKotaPembeli);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getJasaPengirimanList(String idKotaPenjual, String idKotaPembeli) {
        berat_total = String.valueOf(Integer.parseInt(berat_item) * Integer.parseInt(jlhItem));
        String url = "https://storeunika.000webhostapp.com/store/ongkir.php?province=" + idKotaPenjual + "&kota=" + idKotaPembeli + "&berat=" + berat_total;
        RequestQueue req = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest ongkir = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray arr = response.getJSONArray("costs");

                            for (int k = 0; k < arr.length(); k++) {
                                JSONObject job = arr.getJSONObject(k);
                                service = job.getString("service");
                                description = job.getString("description");

                                JSONArray arrs = job.getJSONArray("cost");

                                for (int j = 0; j < 1; j++) {
                                    JSONObject jobs = arrs.getJSONObject(j);
                                    value = jobs.getInt("value");
                                    etd = jobs.getString("etd");
                                }
                                model_ongkirArrayList.add(new Model_Ongkir(service, description, value, etd));
                                Adapter_Ongkir adapter = new Adapter_Ongkir(Activity_Check_Ongkir.this, model_ongkirArrayList);
                                RecyclerView rvcost = findViewById(R.id.rc_jasaKirim);
                                rvcost.setHasFixedSize(true);
                                rvcost.setLayoutManager(new LinearLayoutManager(Activity_Check_Ongkir.this));
                                rvcost.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                error -> {

                }
        );
        req.add(ongkir);

    }

    private interface SellerandUserCallBack {
        void onCallback(String IDPENJUAL, String IDPEMBELI);
    }
}