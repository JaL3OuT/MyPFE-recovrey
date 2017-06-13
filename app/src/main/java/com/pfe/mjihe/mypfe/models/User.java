package com.pfe.mjihe.mypfe.models;

import java.io.Serializable;

/**
 * Created by Mjihe on 04/03/2017.
 */
public class User implements Serializable {
    private String nom;
    private String prenom;
    private String email;
    private String gouvernorat;
    private String comunn;
    private String localite;
    private String type;
    private String CIN;
    private Wallet wallet;
    public User() {
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getPrenom() {
        return prenom;
    }
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getGouvernorat() {
        return gouvernorat;
    }
    public void setGouvernorat(String gouvernorat) {
        this.gouvernorat = gouvernorat;
    }
    public String getComunn() {
        return comunn;
    }
    public void setComunn(String comunn) {
        this.comunn = comunn;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public com.pfe.mjihe.mypfe.models.Wallet getWallet() {
        return wallet;
    }
    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }
    public String getLocalite() {
        return localite;
    }
    public void setLocalite(String localite) {
        this.localite = localite;
    }
    public String getCIN() {
        return CIN;
    }
    public void setCIN(String CIN) {
        this.CIN = CIN;
    }

}
