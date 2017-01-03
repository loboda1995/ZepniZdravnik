package com.example.tpoteam.zepnizdravnik;

import java.io.Serializable;

/**
 * Created by Luka Loboda on 14-Dec-16.
 */

public class AppointmentNotification implements Serializable{

    public int idOfColor;
    public String institution;
    public String location;
    public String doctor;
    public long timeOfNotification;
    public long timeOfAppointment;
    public String comments;
    public int idOfNoti;
    public boolean removeOld;

    public AppointmentNotification(int c, String inst, String loc, String doc, long tN, long tA, String comm, int id, boolean r){
        this.idOfColor = c;
        this.institution = inst;
        this.location = loc;
        this.doctor = doc;
        this.timeOfNotification = tN;
        this.timeOfAppointment = tA;
        this.comments = comm;
        this.idOfNoti = id;
        this.removeOld = r;
    }

}
