package com.example.tpoteam.zepnizdravnik;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Luka Loboda on 16-Nov-16.
 */

public class NotificationOverview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_overview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MedicineNotification noti = (MedicineNotification) this.getIntent().getSerializableExtra("Notification");

        // Ce je noti == null pomeni, da ustvarjamo nov opomnik, sicer spreminajmo ze obstojecega
        if(noti != null){
            TextView medicineNameDisplay = (TextView) findViewById(R.id.medicineName);
            medicineNameDisplay.setText(noti.medicineName);
            //TODO: pridobiti in prikazati se ostale atribute
        }
    }

}
