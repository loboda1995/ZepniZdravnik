package com.example.tpoteam.zepnizdravnik;

import java.io.Serializable;

/**
 * Created by Luka Loboda on 16-Nov-16.
 */

public class MedicineNotification implements Serializable{

    public String medicineName;

    public boolean dailyInterval;
    public boolean[] times = new boolean[24];
    public String comments;

    public MedicineNotification(String medicineName, boolean isDaily, boolean[] t, String comm){
        this.medicineName = medicineName;

        this.dailyInterval = isDaily;
        this.times = t;
        this.comments = comm;
        // TODO: dodati dodatne atribute, kolicina, intervali doz, opombe
    }

}
