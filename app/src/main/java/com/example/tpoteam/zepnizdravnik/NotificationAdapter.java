package com.example.tpoteam.zepnizdravnik;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Luka Loboda on 16-Nov-16.
 */

public class NotificationAdapter extends ArrayAdapter<MedicineNotification> {

    private String times[] = {"00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00","24:00"};
    private String days[] = {"ponedeljek", "torek", "sreda", "četrtek", "petek", "sobota", "nedelja"};

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
            TextView medicineTimesDisplay = (TextView) convertView.findViewById(R.id.medicineTimes);
            String sporocilo = "";
            for (int i = 0; i < notification.times.length; i++) {
                if(notification.times[i] != 0)
                    if(notification.dailyInterval)
                        sporocilo += times[i] + " ";
                    else
                        sporocilo += days[i] + " ";
            }
            medicineTimesDisplay.setText(sporocilo);
        }

        return convertView;
    }
}