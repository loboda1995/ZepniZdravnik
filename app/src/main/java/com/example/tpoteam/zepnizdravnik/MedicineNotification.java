package com.example.tpoteam.zepnizdravnik;

import android.app.PendingIntent;

import java.io.Serializable;

/**
 * Created by Luka Loboda on 16-Nov-16.
 */

public class MedicineNotification implements Serializable{

    public String medicineName;
    public String medicineQuantity;
    public int idOfColor;
    public boolean dailyInterval;
    public int[] times = new int[24];
    public String comments;
    public int idOfNoti;


    public MedicineNotification(String medicineName, String medicineQuantity, int idColor, boolean isDaily, int[] t, String comm, int id){
        this.medicineName = medicineName;
        this.medicineQuantity = medicineQuantity;
        this.idOfColor = idColor;
        this.dailyInterval = isDaily;
        this.times = t;
        this.comments = comm;
        this.idOfNoti = id;
    }

}
