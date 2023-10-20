package com.kstore.kerasaanestore.model;

public class Register_Model {
    public String nama;
    public String email;
    public String handphone;
    public String password;
    public String alamat;
    public String provinsi;
    public String foto;

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Register_Model(String nama, String email, String handphone, String password, String alamat, String provinsi, String kota, String foto) {
        this.nama = nama;
        this.email = email;
        this.handphone = handphone;
        this.password = password;
        this.alamat = alamat;
        this.provinsi = provinsi;
        this.kota = kota;
        this.foto = foto;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getProvinsi() {
        return provinsi;
    }

    public void setProvinsi(String provinsi) {
        this.provinsi = provinsi;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public String kota;

    public Register_Model() {
    }

    public Register_Model(String nama, String email, String handphone, String password) {
        this.nama = nama;
        this.email = email;
        this.handphone = handphone;
        this.password = password;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHandphone() {
        return handphone;
    }

    public void setHandphone(String handphone) {
        this.handphone = handphone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
