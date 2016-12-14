package com.example.tpoteam.zepnizdravnik;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Luka Loboda on 14-Dec-16.
 */

public class AppointmentActivity extends AppCompatActivity {

    public static final String nameOfExtra1 = "Appointments";
    public static final String nameOfExtra2 = "ID";

    private AppointmentAdapter adapter;
    private ArrayList<AppointmentNotification> appointmentNotifications = new ArrayList<>();

    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list = (ListView) findViewById(R.id.list);
        list.setOnItemClickListener(overviewListener);

        showAppointments();

    }

    protected void onResume(){
        super.onResume();

        showAppointments();
    }

    private void showAppointments(){
        // Pridobimo shranjene opomnike za preglede
        appointmentNotifications = getAppointmentNotifications();
        adapter = new AppointmentAdapter(this, appointmentNotifications);
        list.setAdapter(adapter);

        // TODO: Ce uporabnik se ni ustvaril opomnikov, prikazemo zacetni zaslon, sicer prikazemo seznam
        if(appointmentNotifications.size() > 0){
            list.setVisibility(View.VISIBLE);
        }else{
            list.setVisibility(View.GONE);
        }
    }

    AdapterView.OnItemClickListener overviewListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            startAddNotificationActivity(arg2);
        }
    };

    private void startAddNotificationActivity(int id){
        final Intent details = new Intent(AppointmentActivity.this, AppointmentOverview.class);
        details.putExtra(nameOfExtra1, appointmentNotifications);
        details.putExtra(nameOfExtra2, id);
        startActivity(details);
    }

    private ArrayList<AppointmentNotification> getAppointmentNotifications()
    {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        ArrayList<AppointmentNotification> noti = new ArrayList<>();
        try {
            fis = openFileInput(MainActivity.fileNameWithAppointments);
            ois = new ObjectInputStream(fis);
            noti = (ArrayList<AppointmentNotification>)ois.readObject();
            ois.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        noti.add(null);
        return noti;
    }

}
