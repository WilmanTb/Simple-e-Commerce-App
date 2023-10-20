package com.kstore.kerasaanestore.model;

import androidx.annotation.NonNull;

public class Model_Provinsi {
   public String province;
   public String province_id;

    public Model_Provinsi() {
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }

    public Model_Provinsi(String province, String province_id) {
        this.province = province;
        this.province_id = province_id;
    }
}
