package com.example.tpoteam.zepnizdravnik;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

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

        medicineNotifications.add(new MedicineNotification(1, "Lekadol"));
        medicineNotifications.add(new MedicineNotification(2, "Aspirin"));
        medicineNotifications.add(new MedicineNotification(1, "Lekadol"));
        medicineNotifications.add(new MedicineNotification(2, "Aspirin"));
        medicineNotifications.add(new MedicineNotification(1, "Lekadol"));
        medicineNotifications.add(new MedicineNotification(2, "Aspirin"));
        medicineNotifications.add(new MedicineNotification(1, "Lekadol"));
        medicineNotifications.add(new MedicineNotification(2, "Aspirin"));
        medicineNotifications.add(new MedicineNotification(1, "Lekadol"));
        medicineNotifications.add(new MedicineNotification(2, "Aspirin"));
        medicineNotifications.add(null);

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
            MedicineNotification izbrani = medicineNotifications.get(arg2);
            details.putExtra("Notification", izbrani);
            startActivity(details);
        }
    };

}
