package com.example.tpoteam.zepnizdravnik;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
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
        list = (ListView) findViewById(R.id.list);
        list.setOnItemClickListener(movieDetailsListener);

        /*
        medicineNotifications.add(new MedicineNotification(1, "Zdravilo 1"));
        medicineNotifications.add(new MedicineNotification(2, "Zdravilo 2"));
        medicineNotifications.add(new MedicineNotification(3, "Zdravilo 3"));
        medicineNotifications.add(new MedicineNotification(4, "Zdravilo 4"));
        medicineNotifications.add(new MedicineNotification(5, "Zdravilo 5"));
        medicineNotifications.add(new MedicineNotification(6, "Zdravilo 6"));
        medicineNotifications.add(new MedicineNotification(7, "Zdravilo 7"));
        */
        medicineNotifications = getMedicineNotifications();
        adapter = new NotificationAdapter(this, medicineNotifications);
        list.setAdapter(adapter);
    }

    // Ustvarimo meni
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    AdapterView.OnItemClickListener movieDetailsListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            final Intent details = new Intent(MainActivity.this, NotificationOverview.class);
            details.putExtra(nameOfExtra1, medicineNotifications);
            details.putExtra(nameOfExtra2, arg2);
            startActivity(details);
        }
    };

    protected void onResume(){
        super.onResume();

        medicineNotifications = getMedicineNotifications();
        adapter = new NotificationAdapter(this, medicineNotifications);
        list.setAdapter(adapter);
    }

    private ArrayList<MedicineNotification> getMedicineNotifications()
    {
        InputStream fis = null;
        ObjectInputStream ois = null;
        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), fileNameWithNotifications);
        ArrayList<MedicineNotification> noti = new ArrayList<>();
        try {

            fis = new FileInputStream(f);
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
