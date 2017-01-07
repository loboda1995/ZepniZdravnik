package com.example.tpoteam.zepnizdravnik;

/**
 * Created by Žiga on 12.12.2016.
 */

public class Dom {
    String ime;
    String mail;
    String naslov;
    String telefon;
    String postna_st;
    String kraj;

    public Dom(){
        this.ime = "";
    }

    public Dom(String ime, String naslov, String kraj, String postna){
        this.ime = ime;
        this.naslov = naslov;
        this.postna_st = postna;
        this.kraj = kraj;
    }

    public Dom(String ime, String naslov, String mail,String postna_st, String telefon){
        this.ime = ime;
        this.naslov = naslov;
        this.mail = mail;
        this.telefon = telefon;
        this.postna_st = postna_st;
    }

    public Dom(String ime, String naslov, String kraj, String mail,String postna_st, String telefon){
        this.ime = ime;
        this.naslov = naslov;
        this.mail = mail;
        this.telefon = telefon;
        this.kraj = kraj;
        this.postna_st = postna_st;
    }

    public String getNaslov(){return this.naslov;}

    public String getIme() {return this.ime;}

    public String getMail(){return this.mail;}

    public String getTelefon(){return this.telefon;}

    public String getKraj() {return this.kraj;};
}