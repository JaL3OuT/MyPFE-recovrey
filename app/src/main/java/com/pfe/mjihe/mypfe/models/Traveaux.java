package com.pfe.mjihe.mypfe.models;

import java.io.Serializable;

/**
 * Created by Mjihe on 10/06/2017.
 */

public class Traveaux implements Serializable {
    private String idtraveau;
    private String date;
    private double budget;
    private String duré;
    private double traLat, traLang;

    public Traveaux(String idtraveau, String date, double budget, String duré, double traLat, double traLang) {
        this.idtraveau = idtraveau;
        this.date = date;
        this.budget = budget;
        this.duré = duré;
        this.traLat = traLat;
        this.traLang = traLang;
    }

    public Traveaux() {
    }

    public String getIdtraveau() {
        return idtraveau;
    }

    public void setIdtraveau(String idtraveau) {
        this.idtraveau = idtraveau;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public String getDuré() {
        return duré;
    }

    public void setDuré(String duré) {
        this.duré = duré;
    }

    public double getTraLat() {
        return traLat;
    }

    public void setTraLat(double traLat) {
        this.traLat = traLat;
    }

    public double getTraLang() {
        return traLang;
    }

    public void setTraLang(double traLang) {
        this.traLang = traLang;
    }
}
