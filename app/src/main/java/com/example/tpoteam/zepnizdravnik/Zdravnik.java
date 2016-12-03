package com.example.tpoteam.zepnizdravnik;

<<<<<<< HEAD
import java.util.ArrayList;

=======
>>>>>>> f6b982eaea758d6db77761344baf91eddc547688
/**
 * Created by Thinkpad on 21. 11. 2016.
 */

public class Zdravnik {
<<<<<<< HEAD
    String ime;
    String priimek;
    String ime_doma;
    String Mail_zdravnika;
    String Telefon_zdravnika;
    String Naziv;
    String ID_urnika;
    ArrayList<Dan> urnik;

//    String Ponedeljek;
//    String Torek;
//    String Sreda;
//    String Cetrtek;
//    String Petek;
//    String Sobota;
//    String Nedelja;

    public Zdravnik(){

    }
    public Zdravnik(String i, String p, String d, String m, String t, String N, String u){
        this.ime = i;
        this.priimek = p;
        this.ime_doma = d;
        this.Mail_zdravnika = m;
        this.Telefon_zdravnika = t;
        this.Naziv = N;
        this.ID_urnika = u;

    }

    public ArrayList<Dan> getUrnik() {
        return urnik;
    }

    public void setUrnik(ArrayList<Dan> urnik) {
        this.urnik = urnik;
    }
=======
    private String ime;
    private String priimek;


>>>>>>> f6b982eaea758d6db77761344baf91eddc547688

    public Zdravnik(String i, String p){
        this.ime = i;
        this.priimek = p;
    }

<<<<<<< HEAD

    public String getTelefon_zdravnika() {
        return Telefon_zdravnika;
    }

    public String getIme_doma() {
        return ime_doma;
    }

    public String getMail_zdravnika() {
        return Mail_zdravnika;
    }

    public String getNaziv() {
        return Naziv;
    }

    public String getID_urnika() {
        return ID_urnika;
    }



=======
    public Zdravnik(){
        this.ime = this.priimek ="";
    }


>>>>>>> f6b982eaea758d6db77761344baf91eddc547688
    public String getPriimek() {
        return priimek;
    }

    public String getIme() {
        return ime;
    }


<<<<<<< HEAD
=======

>>>>>>> f6b982eaea758d6db77761344baf91eddc547688
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
