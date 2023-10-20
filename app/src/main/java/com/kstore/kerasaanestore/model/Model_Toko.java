package com.kstore.kerasaanestore.model;

public class Model_Toko {
    public String id_penjual, id_toko, nama_toko;

    public String getId_penjual() {
        return id_penjual;
    }

    public void setId_penjual(String id_penjual) {
        this.id_penjual = id_penjual;
    }

    public String getId_toko() {
        return id_toko;
    }

    public void setId_toko(String id_toko) {
        this.id_toko = id_toko;
    }

    public String getNama_toko() {
        return nama_toko;
    }

    public void setNama_toko(String nama_toko) {
        this.nama_toko = nama_toko;
    }

    public Model_Toko() {
    }

    public Model_Toko(String id_penjual, String id_toko, String nama_toko) {
        this.id_penjual = id_penjual;
        this.id_toko = id_toko;
        this.nama_toko = nama_toko;
    }
}
