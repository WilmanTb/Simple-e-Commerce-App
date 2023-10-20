package com.kstore.kerasaanestore.activity.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kstore.kerasaanestore.activity.user.Activity_Dashboard_User;
import com.kstore.kerasaanestore.R;
import com.kstore.kerasaanestore.adapter.Kota_Adapter;
import com.kstore.kerasaanestore.adapter.Province_Adapter;
import com.kstore.kerasaanestore.model.Model_Kota;
import com.kstore.kerasaanestore.model.Model_Provinsi;
import com.kstore.kerasaanestore.model.Register_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Register_Activity extends AppCompatActivity {

    private ImageView arrow_back;
    private EditText et_uName, et_email, et_hp, et_password, et_alamat;
    private Button btn_daftar;
    private Spinner spin_provinsi, spin_kota;
    private TextView txt_login;
    private String UserName = "", Email = "", Handphone = "", Password = "", Alamat = "";
    private DatabaseReference dbRef;
    private FirebaseAuth userAuth;
    private ProgressDialog progressDialog;
    private String provinceID, cityID;
    private Province_Adapter provinceAdapter;
    private Kota_Adapter cityAdapter;
    private ArrayList<Model_Provinsi> provincesList = new ArrayList<>();
    private ArrayList<Model_Kota> citiesList = new ArrayList<>();

    private String apiKey = "b0dd632d6e78fdc1f0bffbb5a6ec6fef"; // Gantilah dengan API Key RajaOngkir Anda

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initComponents();

        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register_Activity.this, LandingPage.class));
                finish();
            }
        });

        btn_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmpty();
            }
        });

        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register_Activity.this, Login_Activity.class));
                finish();
            }
        });

        fetchProvinceData();

        provinceAdapter = new Province_Adapter(this, android.R.layout.simple_spinner_item, provincesList);
        cityAdapter = new Kota_Adapter(this, android.R.layout.simple_spinner_item, citiesList);
        spin_provinsi.setAdapter(provinceAdapter);
        spin_kota.setAdapter(cityAdapter);

        spin_provinsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Model_Provinsi selectedProvince = (Model_Provinsi) spin_provinsi.getSelectedItem();
                String selectedProvinceName = selectedProvince.getProvince();

                // Get the corresponding province ID
                provinceID = getProvinceID(selectedProvinceName);

                // Fetch cities data based on the selected province ID
                fetchCitiesData(provinceID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin_kota.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Model_Kota selectedCity = (Model_Kota) spin_kota.getSelectedItem();
                String selectedCityName = selectedCity.getCity_name();

                // Get the corresponding province ID
                cityID = getCitiesID(selectedCityName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Login_Activity.class));
        finish();
    }

    private void initComponents() {
        spin_kota = findViewById(R.id.spin_kota);
        spin_provinsi = findViewById(R.id.spin_provinsi);
        arrow_back = findViewById(R.id.arrow_back);
        et_alamat = findViewById(R.id.et_alamat);
        et_uName = findViewById(R.id.et_uName);
        et_email = findViewById(R.id.et_email);
        et_hp = findViewById(R.id.et_hp);
        et_password = findViewById(R.id.et_password);
        txt_login = findViewById(R.id.txt_login);
        btn_daftar = findViewById(R.id.btn_daftar);
        dbRef = FirebaseDatabase.getInstance("https://kerasaan-estore-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
        userAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(Register_Activity.this);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
    }

    private void getText() {
        UserName = et_uName.getText().toString();
        Email = et_email.getText().toString();
        Handphone = et_hp.getText().toString();
        Password = et_password.getText().toString();
        Alamat = et_alamat.getText().toString();

    }

    private void checkEmpty() {
        getText();
        progressDialog.show();
        if (UserName.isEmpty() && Email.isEmpty() && Handphone.isEmpty() && Password.isEmpty()) {
            Toast.makeText(this, "Form tidak boleh kosong", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } else {
            registerUser(UserName, Email, Handphone, Password, Alamat, provinceID, cityID);
        }
    }

    private void registerUser(String userName, String email, String handphone, String password, String alamat, String provinsi, String kota) {
        userAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = userAuth.getCurrentUser();
                    String UID = firebaseUser.getUid();
                    Register_Model registerModel = new Register_Model(userName, email, handphone, password, alamat, provinsi, kota, "empty");
                    dbRef.child(UID).setValue(registerModel);
                    startActivity(new Intent(Register_Activity.this, Activity_Dashboard_User.class));
                    finish();
                    progressDialog.dismiss();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(Register_Activity.this, "Registrasi gagal\nMohon coba beberapa saat lagi", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchProvinceData() {
        String url = "https://storeunika.000webhostapp.com/store/provinsi.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray results = response.getJSONArray("results");
                            for (int i = 0; i < results.length(); i++) {
                                JSONObject provinceObject = results.getJSONObject(i);
                                String provinceName = provinceObject.getString("province");
                                String ID = provinceObject.getString("province_id");
                                Model_Provinsi modelProvinsi = new Model_Provinsi(provinceName, ID);
                                provincesList.add(modelProvinsi);
                                Log.d("ProvinceData", "Name: " + provinceName + ", ID: " + ID);
                            }
                            provinceAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    private void fetchCitiesData(String provinceID) {
        String url = "https://storeunika.000webhostapp.com/store/kota.php?province=" + provinceID;
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray results = response.getJSONArray("results");
                            citiesList.clear();
                            for (int i = 0; i < results.length(); i++) {
                                JSONObject provinceObject = results.getJSONObject(i);
                                String citiesName = provinceObject.getString("city_name");
                                String citiesID = provinceObject.getString("city_id");
                                Model_Kota model_kota = new Model_Kota(citiesID, citiesName);
                                citiesList.add(model_kota);
                            }
                            cityAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    private String getProvinceID(String selectedProvinceName) {
        for (int i = 0; i < provincesList.size(); i++) {
            Model_Provinsi province = provincesList.get(i);
            if (province.getProvince().equalsIgnoreCase(selectedProvinceName)) {
                return province.getProvince_id();
            }
        }
        return ""; // Return a default value or handle the case where the province is not found
    }

    private String getCitiesID(String selectedCityName){
        for (int i = 0; i < citiesList.size(); i++) {
            Model_Kota kota = citiesList.get(i);
            if (kota.getCity_name().equalsIgnoreCase(selectedCityName)) {
                return kota.getCity_id();
            }
        }
        return "";
    }


}
