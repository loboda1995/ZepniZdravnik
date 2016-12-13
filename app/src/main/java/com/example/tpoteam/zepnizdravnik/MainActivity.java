package com.example.tpoteam.zepnizdravnik;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String fileNameWithNotifications = "notificationsFile";
    public static final String nameOfExtra1 = "Notifications";
    public static final String nameOfExtra2 = "ID";

    private ListView list;
    private LinearLayout startScreen;
    private NotificationAdapter adapter;

    private int numberOfNotifications;
    private ArrayList<MedicineNotification> medicineNotifications = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Pridobimo ListView in mu dodamo ClickListener ter ustvarimo in dodamo adapter
        startScreen = (LinearLayout) findViewById(R.id.startScreen);
        list = (ListView) findViewById(R.id.list);
        list.setOnItemClickListener(movieDetailsListener);

        showScreen();

        // Ob kliku na zacetni zaslon prikazemo okno za dodajanje opomnika
        startScreen.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startAddNotificationActivity(0);
            }
        });
    }

    // Ustvarimo meni
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    // Odziv na uporabnikovo izbiro iz menija
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_appointment:
                showAppointments();
                return true;
            case R.id.action_contacts:
                showContacts();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    // Prikaze aktivnost s pregledi
    private void showAppointments(){

    }

    // Prikaze aktivnost s kontakti
    private void showContacts(){
        final Intent doctors = new Intent(MainActivity.this, IzbiraPrikaza.class);
        startActivity(doctors);
    }

    AdapterView.OnItemClickListener movieDetailsListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            startAddNotificationActivity(arg2);
        }
    };

    private void startAddNotificationActivity(int id){
        final Intent details = new Intent(MainActivity.this, NotificationOverview.class);
        details.putExtra(nameOfExtra1, medicineNotifications);
        details.putExtra(nameOfExtra2, id);
        startActivity(details);
    }

    protected void onResume(){
        super.onResume();

        showScreen();
    }

    private void showScreen(){
        // Pridobimo shranjene opomnike
        medicineNotifications = getMedicineNotifications();
        adapter = new NotificationAdapter(this, medicineNotifications);
        list.setAdapter(adapter);

        // Ce uporabnik se ni ustvaril opomnikov, prikazemo zacetni zaslon, sicer prikazemo seznam
        if(medicineNotifications.size() > 1){
            list.setVisibility(View.VISIBLE);
            startScreen.setVisibility(View.GONE);
        }else{
            list.setVisibility(View.GONE);
            startScreen.setVisibility(View.VISIBLE);
        }
    }

    private ArrayList<MedicineNotification> getMedicineNotifications()
    {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        ArrayList<MedicineNotification> noti = new ArrayList<>();
        try {
            fis = openFileInput(fileNameWithNotifications);;
            ois = new ObjectInputStream(fis);
            noti = (ArrayList<MedicineNotification>)ois.readObject();
            ois.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        noti.add(null);
        return noti;
    }

}
