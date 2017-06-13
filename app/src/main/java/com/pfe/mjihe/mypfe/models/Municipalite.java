package com.pfe.mjihe.mypfe.models;

import java.io.Serializable;

/**
 * Created by Mjihe on 31/05/2017.
 */

public class Municipalite implements Serializable {
    private String Nom, logo, info;
    private Double Lat;
    private Double lang;

    public Municipalite() {

    }

    public Municipalite(String Nom, String logo, String info, double lat, double lang) {
        this.Nom = Nom;
        this.logo = logo;
        this.info = info;
        this.Lat = lat;
        this.lang = lang;
    }


    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        this.Nom = nom;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }


    public Double getLat() {
        return Lat;
    }

    public void setLat(Double lat) {
        this.Lat = lat;
    }

    public Double getLang() {
        return lang;
    }

    public void setLang(Double lang) {
        this.lang = lang;
    }

    public String toString() {
        return "Municipalite{" +
                "nom" + Nom +
                "info" + info +
                "logo" + logo +
                "Lat" + Lat +
                "lang" + lang + " }";
    }
}