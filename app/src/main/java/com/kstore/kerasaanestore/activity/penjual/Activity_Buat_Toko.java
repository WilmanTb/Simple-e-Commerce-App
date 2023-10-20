package com.kstore.kerasaanestore.activity.penjual;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kstore.kerasaanestore.R;
import com.kstore.kerasaanestore.jclass.Data_Value;
import com.kstore.kerasaanestore.model.Model_Toko;

public class Activity_Buat_Toko extends AppCompatActivity {

    private EditText editText_nama_toko;
    private Button btn_buat_toko;
    private DatabaseReference dbToko;
    private String namaToko;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buat_toko);

        initComponenst();
        btn_buat_toko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                namaToko = editText_nama_toko.getText().toString();
                if (namaToko.isEmpty()){
                    Toast.makeText(Activity_Buat_Toko.this, "Form tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    String idToko = dbToko.push().getKey();
                    Model_Toko model_toko = new Model_Toko(UID, idToko, namaToko);
                    dbToko.child("Toko").child(idToko).setValue(model_toko);
                    Toast.makeText(Activity_Buat_Toko.this, "Toko berhasil dibuat", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Activity_Buat_Toko.this, Dashboard_Penjual.class));
                    finish();
                }
            }
        });
    }

    private void initComponenst() {
        editText_nama_toko = findViewById(R.id.edit_user_name);
        btn_buat_toko = findViewById(R.id.btn_kirim);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        UID = firebaseUser.getUid();;
        dbToko = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();
    }
}