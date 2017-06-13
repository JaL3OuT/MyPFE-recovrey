package com.pfe.mjihe.mypfe.models;

import java.io.Serializable;

/**
 * Created by Mjihe on 12/05/2017.
 */

public class Rapport implements Serializable {
    private String ruid;
    private String url;
    private String rapport;
    private String date;
    private double lati, langi;

    public Rapport() {
    }

    public Rapport(String uid, String url, String rapportcontent, String date, double lati, double langi) {
        this.ruid = uid;
        this.url = url;
        this.rapport = rapportcontent;
        this.date = date;
        this.lati = lati;
        this.langi = langi;
    }

    public String getRapport() {
        return rapport;
    }

    public void setRapport(String rapport) {
        this.rapport = rapport;
    }

    public String getRuidUid() {
        return ruid;
    }

    public void setRuid(String uid) {
        this.ruid = uid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getLati() {
        return lati;
    }

    public void setLati(double lati) {
        this.lati = lati;
    }

    public double getLangi() {
        return langi;
    }

    public void setLangi(double langi) {
        this.langi = langi;
    }
}
