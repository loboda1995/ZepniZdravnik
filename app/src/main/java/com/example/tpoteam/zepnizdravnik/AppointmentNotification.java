package com.example.tpoteam.zepnizdravnik;

import java.io.Serializable;

/**
 * Created by Luka Loboda on 14-Dec-16.
 */

public class AppointmentNotification implements Serializable{

    public int idOfColor;
    public String location;
    public String doctor;
    public long timeOfNotification;
    public long timeOfAppointment;
    public String comments;

    public AppointmentNotification(int c, String loc, String doc, long tN, long tA, String comm){
        this.idOfColor = c;
        this.location = loc;
        this.doctor = doc;
        this.timeOfNotification = tN;
        this.timeOfAppointment = tA;
        this.comments = comm;
    }

}
