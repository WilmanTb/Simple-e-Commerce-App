package com.kstore.kerasaanestore.model;

public class Model_Pesanan {
    public String id_pesanan;
    public String id_user;
    public String id_penjual;
    public String jumlah;
    public String ongkir;
    public String layanan;
    public String estimasi;
    public String jenis_pembayaran;
    public String status_pembayaran;
    public String tanggal_pesanan;
    public String total_bayar;

    public String getTotal_bayar() {
        return total_bayar;
    }

    public void setTotal_bayar(String total_bayar) {
        this.total_bayar = total_bayar;
    }

    public String getId_produk() {
        return id_produk;
    }

    public void setId_produk(String id_produk) {
        this.id_produk = id_produk;
    }

    public String id_produk;

    public String getId_pesanan() {
        return id_pesanan;
    }

    public void setId_pesanan(String id_pesanan) {
        this.id_pesanan = id_pesanan;
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

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getOngkir() {
        return ongkir;
    }

    public void setOngkir(String ongkir) {
        this.ongkir = ongkir;
    }

    public String getLayanan() {
        return layanan;
    }

    public void setLayanan(String layanan) {
        this.layanan = layanan;
    }

    public String getEstimasi() {
        return estimasi;
    }

    public void setEstimasi(String estimasi) {
        this.estimasi = estimasi;
    }

    public String getJenis_pembayaran() {
        return jenis_pembayaran;
    }

    public void setJenis_pembayaran(String jenis_pembayaran) {
        this.jenis_pembayaran = jenis_pembayaran;
    }

    public String getStatus_pembayaran() {
        return status_pembayaran;
    }

    public void setStatus_pembayaran(String status_pembayaran) {
        this.status_pembayaran = status_pembayaran;
    }

    public String getTanggal_pesanan() {
        return tanggal_pesanan;
    }

    public void setTanggal_pesanan(String tanggal_pesanan) {
        this.tanggal_pesanan = tanggal_pesanan;
    }

    public Model_Pesanan() {
    }

    public Model_Pesanan(String id_pesanan, String id_user, String id_penjual, String jumlah, String ongkir, String layanan, String estimasi, String jenis_pembayaran, String status_pembayaran, String tanggal_pesanan, String id_produk, String total_bayar) {
        this.id_pesanan = id_pesanan;
        this.id_user = id_user;
        this.id_penjual = id_penjual;
        this.jumlah = jumlah;
        this.ongkir = ongkir;
        this.layanan = layanan;
        this.estimasi = estimasi;
        this.jenis_pembayaran = jenis_pembayaran;
        this.status_pembayaran = status_pembayaran;
        this.tanggal_pesanan = tanggal_pesanan;
        this.id_produk = id_produk;
        this.total_bayar = total_bayar;
    }
}
