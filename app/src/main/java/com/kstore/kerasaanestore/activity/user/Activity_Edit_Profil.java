package com.kstore.kerasaanestore.activity.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Activity_Edit_Profil extends AppCompatActivity {

    private CardView cv_foto_profil, cv_username;
    private ImageView arrow_back;
    private TextView txt_username;
    private CircleImageView foto_profil;
    private FirebaseAuth userAuth;
    private DatabaseReference dbUser;
    private StorageReference storageFoto;
    private StorageTask mUploadTask;
    private String UID, Url;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);

        initComponents();
        getUserData();

        cv_foto_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPicture(v);
            }
        });

        cv_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Edit_Profil.this, Activity_Ganti_Username.class));
                finish();
            }
        });

        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initComponents(){
        arrow_back = findViewById(R.id.btn_back);
        cv_foto_profil = findViewById(R.id.cv_foto_profil);
        cv_username = findViewById(R.id.cv_user_name);
        txt_username = findViewById(R.id.txt_username);
        foto_profil = findViewById(R.id.foto_profil);
        userAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = userAuth.getCurrentUser();
        UID = firebaseUser.getUid();
        dbUser = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();
        storageFoto = FirebaseStorage.getInstance().getReference();
    }

    private void getUserData(){
        dbUser.child("Users").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    txt_username.setText(snapshot.child("nama").getValue().toString());
                    Glide.with(getApplicationContext()).load(snapshot.child("foto").getValue().toString()).into(foto_profil);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void selectPicture(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();

            Picasso.get().load(imageUri);
            uploadFoto(new CallbackFoto() {
                @Override
                public void onCallBackFoto(String URL) {
                    if (URL.equals("empty")){
                        Toast.makeText(Activity_Edit_Profil.this, "Foto tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    } else {
                        Handler handler = new Handler();
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                dbUser.child("Users").child(UID).child("foto").setValue(URL);
                                Glide.with(getApplicationContext()).load(URL).into(foto_profil);
                                Toast.makeText(Activity_Edit_Profil.this, "Foto profil berhasil di ganti", Toast.LENGTH_SHORT).show();
                            }
                        };
                        handler.postDelayed(runnable, 2000);

                    }
                }
            });
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadFoto(CallbackFoto callbackFoto){
        if (imageUri != null){
            StorageReference storageReference = storageFoto.child("foto_profil").child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            mUploadTask = storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Url = uri.toString();
                            callbackFoto.onCallBackFoto(Url);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Activity_Edit_Profil.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Url = "empty";
            callbackFoto.onCallBackFoto(Url);
        }
    }

    private interface CallbackFoto {
        void onCallBackFoto(String URL);
    }


}