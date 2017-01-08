package com.example.tpoteam.zepnizdravnik;

import java.io.Serializable;

/**
 * Created by Thinkpad on 3. 12. 2016.
 */

public class Dan implements Serializable{
    String ime;
    String delovnik;

    public Dan(String i, String u){
        this.ime = i;
        this.delovnik= u;

    }

    public String getIme() {
        return ime;
    }

    public String getDelovnik() {
        return delovnik;
    }
}
