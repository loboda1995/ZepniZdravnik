package com.example.tpoteam.zepnizdravnik;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Luka Loboda on 16-Nov-16.
 */

public class NotificationAdapter extends ArrayAdapter<MedicineNotification> {



    public NotificationAdapter(Context context, ArrayList<MedicineNotification> notifications) {
        super(context, 0, notifications);
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        MedicineNotification notification = getItem(position);

        // Ce je notification==null potem gre za element v seznamu, ki predstavlja gumb za dodajanje opomnikov
        if(notification == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.notification_add, parent, false);
        }else{
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.notification_display, parent, false);

            LinearLayout noti = (LinearLayout) convertView.findViewById(R.id.notificationDisplay);
            noti.setBackgroundColor(getContext().getResources().getIntArray(R.array.notificationcolors)[notification.idOfColor]);
            TextView medicineNameDisplay = (TextView) convertView.findViewById(R.id.medicineName);
            medicineNameDisplay.setText(notification.medicineName);
            TextView medicineQunatityDisplay = (TextView) convertView.findViewById(R.id.medicineQuantity);
            medicineQunatityDisplay.setText(Integer.toString(notification.medicineQuantity));
        }

        return convertView;
    }
}