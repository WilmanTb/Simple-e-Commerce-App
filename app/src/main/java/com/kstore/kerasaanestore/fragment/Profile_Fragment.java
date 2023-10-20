package com.kstore.kerasaanestore.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kstore.kerasaanestore.activity.penjual.Dashboard_Penjual;
import com.kstore.kerasaanestore.activity.user.Activity_Detail_Produk_User;
import com.kstore.kerasaanestore.activity.user.Activity_Edit_Profil;
import com.kstore.kerasaanestore.R;
import com.kstore.kerasaanestore.activity.auth.Login_Activity;
import com.kstore.kerasaanestore.activity.user.Activity_Riwayat_Pembelian;
import com.kstore.kerasaanestore.jclass.Data_Value;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_Fragment extends Fragment {

    private Button btn_logout;
    private CircleImageView img_profil_user;
    private View view;
    private DatabaseReference dbUser;
    private FirebaseAuth userAuth;
    private FirebaseUser firebaseUser;
    private String UID;
    private TextView txt_nama_user, txt_email_user;
    private CardView cv_edit_profil, cv_riwayat_pembelian, cv_beralih_penjual;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_, container, false);

        initComponents();
        if (firebaseUser != null) {
            getDataUser();
            cv_edit_profil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), Activity_Edit_Profil.class));
                }
            });

            cv_riwayat_pembelian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), Activity_Riwayat_Pembelian.class));
                }
            });

            cv_beralih_penjual.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(),Dashboard_Penjual.class));
                }
            });
        }

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAuth.signOut();
                startActivity(new Intent(getActivity(), Login_Activity.class));
            }
        });

        return view;
    }

    private void initComponents() {
        cv_beralih_penjual = view.findViewById(R.id.cv_beralih_penjual);
        img_profil_user = view.findViewById(R.id.img_profilUser);
        txt_email_user = view.findViewById(R.id.txt_email_user);
        txt_nama_user = view.findViewById(R.id.txt_nama_user);
        btn_logout = view.findViewById(R.id.btn_logout);
        userAuth = FirebaseAuth.getInstance();
        firebaseUser = userAuth.getCurrentUser();
        if (firebaseUser != null) {
            UID = firebaseUser.getUid();
        } else {
            Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.popup_autentication);
            dialog.show();

            Button btn_okay = dialog.findViewById(R.id.btn_okay);
            btn_okay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), Login_Activity.class));
                }
            });
        }
        dbUser = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();
        cv_edit_profil = view.findViewById(R.id.cv_edit_profil);
        cv_riwayat_pembelian = view.findViewById(R.id.cv_history_pembelian);
    }

    private void getDataUser() {
        dbUser.child("Users").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    txt_nama_user.setText(snapshot.child("nama").getValue().toString());
                    txt_email_user.setText(snapshot.child("email").getValue().toString());
                    if (isAdded() && getActivity() != null && !getActivity().isFinishing() && !snapshot.child("foto").getValue().toString().equals("empty"))
                        Glide.with(requireContext()).load(snapshot.child("foto").getValue().toString()).into(img_profil_user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}