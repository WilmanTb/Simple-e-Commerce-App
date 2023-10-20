package com.kstore.kerasaanestore.model;

import java.io.Serializable;

public class Model_Keranjang implements Serializable {
    public String id_produk;

    public String getId_produk() {
        return id_produk;
    }

    public void setId_produk(String id_produk) {
        this.id_produk = id_produk;
    }

    public String id_keranjang;
    public String id_user;
    public String id_penjual;
    public String nama_produk;
    public String berat;
    public String deskripsi;
    public String gambar;
    public String harga;
    public String kategori;
    public int jumlahProduk;

    public int getJumlahProduk() {
        return jumlahProduk;
    }

    public void setJumlahProduk(int jumlahProduk) {
        this.jumlahProduk = jumlahProduk;
    }

    public String getId_keranjang() {
        return id_keranjang;
    }

    public void setId_keranjang(String id_keranjang) {
        this.id_keranjang = id_keranjang;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getId_penjual() {
        return id_penjual;
    }

    public void setId_penjual(String id_penjual) {
        this.id_penjual = id_penjual;
    }

    public String getNama_produk() {
        return nama_produk;
    }

    public void setNama_produk(String nama_produk) {
        this.nama_produk = nama_produk;
    }

    public String getBerat() {
        return berat;
    }

    public void setBerat(String berat) {
        this.berat = berat;
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

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public Model_Keranjang() {
    }

    public Model_Keranjang(String id_keranjang, String id_user, String id_penjual, String nama_produk,String id_produk, String berat, String deskripsi, String gambar, String harga, String kategori, int jumlahProduk) {
        this.id_keranjang = id_keranjang;
        this.id_user = id_user;
        this.id_penjual = id_penjual;
        this.nama_produk = nama_produk;
        this.berat = berat;
        this.deskripsi = deskripsi;
        this.gambar = gambar;
        this.harga = harga;
        this.kategori = kategori;
        this.jumlahProduk = jumlahProduk;
        this.id_produk = id_produk;
    }
}
