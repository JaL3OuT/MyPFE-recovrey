package com.pfe.mjihe.mypfe.models;

import java.io.Serializable;

/**
 * Created by Mjihe on 11/06/2017.
 */

public class Factures implements Serializable {
    private String nLot;
    private String date;
    private boolean paymentF;
    private double somme, flang, flat;

    public Factures() {

    }

    public Factures(String nLot, String date, boolean paymentF, double somme, double flang, double flat) {
        this.nLot = nLot;

        this.date = date;
        this.paymentF = paymentF;
        this.somme = somme;
        this.flang = flang;
        this.flat = flat;
    }

    public String getnLot() {
        return nLot;
    }

    public void setnLot(String nLot) {
        this.nLot = nLot;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isPaymentF() {
        return paymentF;
    }

    public void setPaymentF(boolean paymentF) {
        this.paymentF = paymentF;
    }

    public double getSomme() {
        return somme;
    }

    public void setSomme(double somme) {
        this.somme = somme;
    }

    public double getFlang() {
        return flang;
    }

    public void setFlang(double flang) {
        this.flang = flang;
    }

    public double getFlat() {
        return flat;
    }

    public void setFlat(double flat) {
        this.flat = flat;
    }

}
