package com.example.tpoteam.zepnizdravnik;

/**
 * Created by Thinkpad on 21. 11. 2016.
 */

public class Zdravnik {
    private String ime;
    private String priimek;



    public Zdravnik(String i, String p){
        this.ime = i;
        this.priimek = p;
    }

    public Zdravnik(){
        this.ime = this.priimek ="";
    }


    public String getPriimek() {
        return priimek;
    }

    public String getIme() {
        return ime;
    }



    public void setIme(String ime) {
        this.ime = ime;
    }

    public void setPriimek(String priimek) {
        this.priimek = priimek;
    }

    @Override
    public String toString() {
        return (this.ime + " " + this.priimek);
    }
}
