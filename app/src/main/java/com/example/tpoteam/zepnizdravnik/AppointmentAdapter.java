package com.example.tpoteam.zepnizdravnik;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Luka Loboda on 14-Dec-16.
 */

public class AppointmentAdapter extends ArrayAdapter<AppointmentNotification> {

    private String notification_not_set = "ni nastavljen";

    public AppointmentAdapter(Context context, ArrayList<AppointmentNotification> notifications) {
        super(context, 0, notifications);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        AppointmentNotification appointment = getItem(position);

        // Ce je notification==null potem gre za element v seznamu, ki predstavlja gumb za dodajanje opomnikov
        if(appointment == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.notification_add, parent, false);
            LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.addButton);
            layout.getBackground().setColorFilter(getContext().getResources().getColor(R.color.addListItem), PorterDuff.Mode.ADD);
        }else{
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.appointment_notification_display, parent, false);
            LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.notificationDisplay);
            layout.getBackground().setColorFilter(getContext().getResources().getIntArray(R.array.notificationcolors)[appointment.idOfColor], PorterDuff.Mode.ADD);

            TextView timeOfAppoint = (TextView) convertView.findViewById(R.id.timeOfAppointment);
            TextView locOfAppoint = (TextView) convertView.findViewById(R.id.locOfAppointment);
            TextView doctor = (TextView) convertView.findViewById(R.id.docOfAppointment);

            if(appointment.timeOfAppointment != -1) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(appointment.timeOfAppointment);
                Date time = cal.getTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy' ob 'HH:mm");
                timeOfAppoint.setText(simpleDateFormat.format(time));
            }
            else {
                timeOfAppoint.setText(notification_not_set);
            }

            locOfAppoint.setText(appointment.location);
            doctor.setText(appointment.doctor);

        }

        return convertView;
    }
}
