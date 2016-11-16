package com.example.tpoteam.zepnizdravnik;

import java.io.Serializable;

/**
 * Created by Luka Loboda on 16-Nov-16.
 */

public class MedicineNotification implements Serializable{

    public int medicineNoticiationID;
    public String medicineName;

    public MedicineNotification(int notiID, String medicineName){
        this.medicineNoticiationID = notiID;
        this.medicineName = medicineName;
        // TODO: dodati dodatne atribute, kolicina, intervali doz, opombe
    }

}
