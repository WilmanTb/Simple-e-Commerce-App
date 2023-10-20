package com.kstore.kerasaanestore.model;

import java.io.Serializable;

public class Model_Ongkir implements Serializable {
    private String service;
    private String description;
    private int value;
    private String etd;

    public Model_Ongkir(String service, String description, int value, String etd) {
        this.service = service;
        this.description = description;
        this.value = value;
        this.etd = etd;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getEtd() {
        return etd;
    }

    public void setEtd(String etd) {
        this.etd = etd;
    }
}
