package com.kstore.kerasaanestore.activity.penjual;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.kstore.kerasaanestore.R;
import com.kstore.kerasaanestore.jclass.Data_Value;
import com.kstore.kerasaanestore.model.Model_Katalog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.http.Url;

public class Activity_Tambah_Produk_Penjual extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ImageView arrow_back;
    private EditText editText_nama_produk, editText_harga_produk, editText_berat_produk, editText_stok_produk;
    private AppCompatEditText editText_deskripsi_produk;
    private Spinner spin_kategori;
    private RelativeLayout rlFoto;
    private TextView txt_pilih_foto;
    private DatabaseReference dbProduk;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private static final int PICK_IMAGE_REQUEST = 1;
    private StorageReference storageFoto;
    private StorageTask mUploadTask;
    private String NamaProduk, HargaProduk, BeratProduk, StokProduk, DeskripsiProduk, UrlFoto, UID, KategoriProduk;
    private Button btn_submit;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayList<String> arrayListKategori = new ArrayList<>();
    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_produk_penjual);

        initComponents();
        getKategori();

        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEditTextValue();
            }
        });

        rlFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFoto(v);
            }
        });
    }

    private void initComponents() {
        arrow_back = findViewById(R.id.btn_back);
        editText_nama_produk = findViewById(R.id.editText_nama_produk);
        editText_harga_produk = findViewById(R.id.editText_harga_produk);
        editText_berat_produk = findViewById(R.id.editText_berat_produk);
        editText_stok_produk = findViewById(R.id.editText_stok_produk);
        editText_deskripsi_produk = findViewById(R.id.editText_deskripsi_produk);
        spin_kategori = findViewById(R.id.spin_kategori);
        rlFoto = findViewById(R.id.rlfoto);
        txt_pilih_foto = findViewById(R.id.txt_pilih_foto);
        btn_submit = findViewById(R.id.btn_submit);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser= firebaseAuth.getCurrentUser();
        UID = firebaseUser.getUid();
        dbProduk = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();
        storageFoto = FirebaseStorage.getInstance().getReference();
    }

    private void getText(){
        NamaProduk = editText_nama_produk.getText().toString();
        HargaProduk = editText_harga_produk.getText().toString();
        BeratProduk = editText_berat_produk.getText().toString();
        StokProduk = editText_stok_produk.getText().toString();
        DeskripsiProduk = editText_deskripsi_produk.getText().toString();

    }

    private void checkEditTextValue(){
        getText();
        if (NamaProduk.isEmpty() || HargaProduk.isEmpty() || BeratProduk.isEmpty() || StokProduk.isEmpty() || DeskripsiProduk.isEmpty() || KategoriProduk.isEmpty()){
            Toast.makeText(this, "Form tidak boleh kosong", Toast.LENGTH_SHORT).show();
        } else {
            createNewProduct();
        }
    }
    private void createNewProduct(){
        uploadFile(new CallbackFoto() {
            @Override
            public void onCallback(String URL) {
                if (!URL.equals("Tidak ada")) {
                    String idProduk = dbProduk.push().getKey();
                    Model_Katalog model_katalog = new Model_Katalog(idProduk, NamaProduk, HargaProduk, DeskripsiProduk, URL, BeratProduk, KategoriProduk, UID, StokProduk);
                    dbProduk.child("Produk").child("Data").child(idProduk).setValue(model_katalog);
                    startActivity(new Intent(Activity_Tambah_Produk_Penjual.this, Activity_Produk_Penjual.class));
                    finish();
                    Toast.makeText(Activity_Tambah_Produk_Penjual.this, "Produk baru berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Activity_Tambah_Produk_Penjual.this, "Gambar produk tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getKategori(){
        spin_kategori.setPrompt("Pilih kategori");
        dbProduk.child("Kategori").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                arrayList.add(0, "Pilih kategori");
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    arrayList.add(dataSnapshot.child("nama").getValue().toString());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Activity_Tambah_Produk_Penjual.this, android.R.layout.simple_spinner_item, arrayList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin_kategori.setAdapter(adapter);
                spin_kategori.setOnItemSelectedListener(Activity_Tambah_Produk_Penjual.this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spin_kategori =(Spinner) parent;
        if (!parent.getItemAtPosition(position).equals("Pilih kategori")){
            KategoriProduk = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void uploadFoto(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent, PICK_IMAGE_REQUEST);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.get().load(mImageUri);
            txt_pilih_foto.setText(String.valueOf(mImageUri));
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(CallbackFoto callbackFoto) {
        if (mImageUri != null) {
            StorageReference fileReference = storageFoto.child("Produk").child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    UrlFoto = uri.toString();
                                    callbackFoto.onCallback(UrlFoto);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Activity_Tambah_Produk_Penjual.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            UrlFoto = "Tidak ada";
            callbackFoto.onCallback(UrlFoto);
        }
    }

    private interface CallbackFoto{
        void onCallback(String URL);
    }
}