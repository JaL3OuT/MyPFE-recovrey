package com.pfe.mjihe.mypfe.models;

import java.io.Serializable;

/**
 * Created by Mjihe on 23/04/2017.
 */

public class Wallet implements Serializable {

    private String cin;
    private String pin;
    private String codeWallet;
    private String codeEdinar;
    private Double solde;
    private String existe;
    private String pinWallet;

    public Wallet() {
        setExiste("false");
    }


    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getCodeWallet() {
        return codeWallet;
    }

    public void setCodeWallet(String codeWallet) {
        this.codeWallet = codeWallet;
    }

    public String getCodeEdinar() {
        return codeEdinar;
    }

    public void setCodeEdinar(String codeEdinar) {
        this.codeEdinar = codeEdinar;
    }

    public Double getSolde() {
        return solde;
    }

    public void setSolde(Double solde) {
        this.solde = solde;
    }

    public String getExiste() {
        return existe;
    }

    public void setExiste(String existe) {
        this.existe = existe;
    }

    public String getPinWallet() {
        return pinWallet;
    }

    public void setPinWallet(String pinWallet) {
        this.pinWallet = pinWallet;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "cin='" + cin + '\'' +
                ", pin='" + pin + '\'' +
                ", codeWallet='" + codeWallet + '\'' +
                ", codeEdinar='" + codeEdinar + '\'' +
                ", solde=" + solde +
                ", existe='" + existe + '\'' +
                ", pinWallet='" + pinWallet + '\'' +
                '}';
    }
}
