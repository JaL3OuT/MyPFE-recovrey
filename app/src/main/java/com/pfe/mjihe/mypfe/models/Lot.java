package com.pfe.mjihe.mypfe.models;

import java.io.Serializable;

/**
 * Created by Mjihe on 02/06/2017.
 */

public class Lot implements Serializable {
    private String cin, numlot;
    private Double latlot, laglot, taxe;
    private Boolean payment;

    public Lot() {
    }
    public Lot(String cin, String numlot, Double latlot, Double laglot, Double taxe, Boolean payment) {
        this.cin = cin;
        this.numlot = numlot;
        this.latlot = latlot;
        this.laglot = laglot;
        this.taxe = taxe;
        this.payment = payment;
    }
    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public Double getLatlot() {
        return latlot;
    }

    public void setLatlot(Double latlot) {
        this.latlot = latlot;
    }

    public Double getLaglot() {
        return laglot;
    }

    public void setLaglot(Double laglot) {
        this.laglot = laglot;
    }

    public Double getTaxe() {
        return taxe;
    }

    public void setTaxe(Double taxe) {
        this.taxe = taxe;
    }

    public Boolean getPayment() {
        return payment;
    }

    public void setPayment(Boolean payment) {
        this.payment = payment;
    }

    public String getNumlot() {
        return numlot;
    }

    public void setNumlot(String numlot) {
        this.numlot = numlot;
    }
}
