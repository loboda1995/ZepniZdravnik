package com.example.tpoteam.zepnizdravnik;

import android.app.PendingIntent;

import java.io.Serializable;

/**
 * Created by Luka Loboda on 16-Nov-16.
 */

public class MedicineNotification implements Serializable{

    public String medicineName;
    public int medicineQuantity;
    public int idOfColor;
    public boolean dailyInterval;
    public int[] times = new int[24];
    public String comments;


    public MedicineNotification(String medicineName, int medicineQuantity, int id, boolean isDaily, int[] t, String comm){
        this.medicineName = medicineName;
        this.medicineQuantity = medicineQuantity;
        this.idOfColor = id;
        this.dailyInterval = isDaily;
        this.times = t;
        this.comments = comm;
    }

}
