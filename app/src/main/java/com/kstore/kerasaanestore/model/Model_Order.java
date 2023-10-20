package com.kstore.kerasaanestore.model;

public class Model_Order {
    public String id_pesanan;
    public String id_user;
    public String id_penjual;
    public String ongkir;
    public String layanan;
    public String estimasi;
    public String jenis_pembayaran;
    public String status_pembayaran;
    public String tanggal_pesanan;
    public String tanggal_pembayaran;
    public String status_kirim;

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

    public String getTanggal_pembayaran() {
        return tanggal_pembayaran;
    }

    public void setTanggal_pembayaran(String tanggal_pembayaran) {
        this.tanggal_pembayaran = tanggal_pembayaran;
    }

    public String getStatus_kirim() {
        return status_kirim;
    }

    public void setStatus_kirim(String status_kirim) {
        this.status_kirim = status_kirim;
    }

    public String getTotal_pembayaran() {
        return total_pembayaran;
    }

    public void setTotal_pembayaran(String total_pembayaran) {
        this.total_pembayaran = total_pembayaran;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public Model_Order() {
    }


    public String total_pembayaran;
    public String nama_produk;
    public String harga_produk;
    public String gambar;

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getHarga_produk() {
        return harga_produk;
    }

    public void setHarga_produk(String harga_produk) {
        this.harga_produk = harga_produk;
    }


    public String getNama_produk() {
        return nama_produk;
    }

    public void setNama_produk(String nama_produk) {
        this.nama_produk = nama_produk;
    }

    public int jumlah;

    public Model_Order(String id_pesanan, String id_user, String id_penjual, String ongkir, String layanan, String estimasi, String jenis_pembayaran, String status_pembayaran, String tanggal_pesanan, String tanggal_pembayaran, String status_kirim, String total_pembayaran, String nama_produk, String harga_produk, String gambar, int jumlah) {
        this.id_pesanan = id_pesanan;
        this.id_user = id_user;
        this.id_penjual = id_penjual;
        this.ongkir = ongkir;
        this.layanan = layanan;
        this.estimasi = estimasi;
        this.jenis_pembayaran = jenis_pembayaran;
        this.status_pembayaran = status_pembayaran;
        this.tanggal_pesanan = tanggal_pesanan;
        this.tanggal_pembayaran = tanggal_pembayaran;
        this.status_kirim = status_kirim;
        this.total_pembayaran = total_pembayaran;
        this.nama_produk = nama_produk;
        this.harga_produk = harga_produk;
        this.gambar = gambar;
        this.jumlah = jumlah;
    }
}
