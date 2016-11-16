package com.example.tpoteam.zepnizdravnik;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Luka Loboda on 16-Nov-16.
 */

public class NotificationAdapter extends ArrayAdapter<MedicineNotification> {

    private MedicineNotification[] objects;

    public NotificationAdapter(Context context, ArrayList<MedicineNotification> notifications) {
        super(context, 0, notifications);
        objects = notifications.toArray(new MedicineNotification[0]);
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        MedicineNotification notification = objects[position];

        if(notification.medicineNoticiationID == -1){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.notification_add, null);
        }else{
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.notification_display, null);

            TextView medicineNameDisplay = (TextView) convertView.findViewById(R.id.medicineName);
            medicineNameDisplay.setText(notification.medicineName);
            // TODO: dodati je potrebno se prikaz ostalih podatkov
        }

        return convertView;
    }
}