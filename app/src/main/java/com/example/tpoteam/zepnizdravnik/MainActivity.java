package com.example.tpoteam.zepnizdravnik;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
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

        list = (ListView) findViewById(R.id.list);

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
        medicineNotifications.add(new MedicineNotification(-1, "Aspirin"));



        adapter = new NotificationAdapter(this, medicineNotifications);
        list.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
