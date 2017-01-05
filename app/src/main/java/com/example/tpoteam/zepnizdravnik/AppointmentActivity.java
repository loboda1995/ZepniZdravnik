package com.example.tpoteam.zepnizdravnik;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    private LinearLayout startScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.BLACK);

        startScreen = (LinearLayout) findViewById(R.id.apointmentsScreen);
        list = (ListView) findViewById(R.id.listApointments);
        list.setOnItemClickListener(overviewListener);

        showAppointments();

        // Ob kliku na zacetni zaslon prikazemo okno za dodajanje opomnika
        startScreen.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startAddNotificationActivity(0);
            }
        });

        // Ce je podan extra potem smo odprli aktivnost ob kliku na notification in odpremo ustrezen pregled
        int id = this.getIntent().getIntExtra("notificationID", -1);
        Log.i("ID", id+"");
        if(id != -1){
            for(int i = 0; i < appointmentNotifications.size()-1; i++){
                if(appointmentNotifications.get(i).idOfNoti == id)
                    startAddNotificationActivity(i);
            }
        }

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

        // Ce uporabnik se ni ustvaril opomnikov, prikazemo zacetni zaslon, sicer prikazemo seznam
        if(appointmentNotifications.size() > 1){
            list.setVisibility(View.VISIBLE);
            startScreen.setVisibility(View.GONE);
        }else{
            list.setVisibility(View.GONE);
            startScreen.setVisibility(View.VISIBLE);
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
        appointmentNotifications.remove(appointmentNotifications.size()-1);
        details.putExtra(nameOfExtra1, appointmentNotifications);
        details.putExtra(nameOfExtra2, id);
        startActivity(details);
    }

    private ArrayList<AppointmentNotification> getAppointmentNotifications()
    {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        ArrayList<AppointmentNotification> noti = new ArrayList<>();
        ArrayList<AppointmentNotification> notiActive = new ArrayList<>();
        try {
            fis = openFileInput(MainActivity.fileNameWithAppointments);
            ois = new ObjectInputStream(fis);
            noti = (ArrayList<AppointmentNotification>)ois.readObject();
            ois.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(AppointmentNotification n : noti){
            if(!n.removeOld || (n.removeOld && n.timeOfAppointment > System.currentTimeMillis())){
                notiActive.add(n);
            }
        }
        if(noti.size() != notiActive.size())
            writeObjectToFile(notiActive, MainActivity.fileNameWithAppointments);
        notiActive.add(null);
        return notiActive;
    }

    private boolean writeObjectToFile(Object o, String fileName){
        try {
            FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fos);
            objectOutputStream.writeObject(o);
            objectOutputStream.close();
            fos.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
