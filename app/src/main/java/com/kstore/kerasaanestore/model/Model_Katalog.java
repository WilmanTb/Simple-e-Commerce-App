package com.kstore.kerasaanestore.model;

import java.io.Serializable;

public class Model_Katalog implements Serializable {
    public String idProduk;
    public String nama;
    public String harga;
    public String deskripsi;
    public String gambar;
    public String berat;
    public String kategori;
    public String penjual;

    public String getStok() {
        return stok;
    }

    public void setStok(String stok) {
        this.stok = stok;
    }

    public String stok;

    public String getIdProduk() {
        return idProduk;
    }

    public void setIdProduk(String idProduk) {
        this.idProduk = idProduk;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getBerat() {
        return berat;
    }

    public void setBerat(String berat) {
        this.berat = berat;
    }

    public String getKatergori() {
        return kategori;
    }

    public void setKatergori(String katergori) {
        this.kategori = katergori;
    }

    public String getPenjual() {
        return penjual;
    }

    public void setPenjual(String penjual) {
        this.penjual = penjual;
    }

    public Model_Katalog() {
    }

    public Model_Katalog(String idProduk, String nama, String harga, String deskripsi, String gambar, String berat, String kategori, String penjual, String stok) {
        this.nama = nama;
        this.harga = harga;
        this.deskripsi = deskripsi;
        this.gambar = gambar;
        this.berat = berat;
        this.kategori = kategori;
        this.penjual = penjual;
        this.idProduk = idProduk;
        this.stok = stok;
    }
}
