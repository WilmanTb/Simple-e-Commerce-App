package com.kstore.kerasaanestore.model;

import java.io.Serializable;

public class Model_Kategori implements Serializable {
    public String nama, gambar;

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public Model_Kategori() {
    }

    public Model_Kategori(String nama, String gambar) {
        this.nama = nama;
        this.gambar = gambar;
    }
}
