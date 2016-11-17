package com.example.tpoteam.zepnizdravnik;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by Luka Loboda on 16-Nov-16.
 */

public class NotificationOverview extends AppCompatActivity {

    private ArrayList<MedicineNotification>  medicineNotifications =  new ArrayList<>();
    private int IDselected;
    private MedicineNotification selectedNotification;

    private EditText medicineNameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_overview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Pridobimo vnosna polja
        medicineNameInput = (EditText) findViewById(R.id.medicineName);

        medicineNotifications = (ArrayList<MedicineNotification>) this.getIntent().getSerializableExtra(MainActivity.nameOfExtra1);
        IDselected = this.getIntent().getIntExtra(MainActivity.nameOfExtra2, 0);

        // Ce je IDselected kaze na zadnji element pomeni, da ustvarjamo nov opomnik, sicer
        // spreminajmo ze obstojecega
        if(IDselected < medicineNotifications.size()-1){
            selectedNotification = medicineNotifications.get(IDselected);

            medicineNameInput.setText(selectedNotification.medicineName);
            //TODO: pridobiti in prikazati se ostale atribute
        }else{

        }

        // Odzivi na klike na gumbe
        Button gumb = (Button) findViewById(R.id.deleteButton);
        gumb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                delete();
            }
        });
        gumb = (Button) findViewById(R.id.saveButton);
        gumb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                save();
            }
        });
    }

    // Izbrise izbrani opomnik
    private void delete(){
        if(selectedNotification != null){
            //TODO: prikaz sporocila, "Ali ste prepricani?"
            deleteThisNotification();
            finish();
        }
    }

    // Shrani novoustvarjen opomnik ali pa shrani spremenjen opomnik
    private void save(){
        if(allInputIsValid()){
            saveThisNotification();
            //TODO: prikaz sporocila, "Uspesno shranjeno"
        }
    }

    // Preveri ali so vrednosti vseh vnosnih polj podane in regularne
    private boolean allInputIsValid(){
        //TODO: implementacija preverjanja
        return true;
    }

    private void saveThisNotification() {
        String newMedicineName = medicineNameInput.getText().toString();
        medicineNotifications.remove(medicineNotifications.size()-1);

        // Ce je izbrani Notification == null, pomeni da ustvarjamo novega, sicer spreminajmo ze
        // obstojecega
        if(selectedNotification != null){
            selectedNotification.medicineName = newMedicineName;
        }else{
            selectedNotification = new MedicineNotification(10, newMedicineName);
            medicineNotifications.add(selectedNotification);
        }

        // Shranimo trenutne opomnike
        writeObjectToFile(medicineNotifications, MainActivity.fileNameWithNotifications);
    }

    private void deleteThisNotification() {
        medicineNotifications.remove(medicineNotifications.size()-1);
        medicineNotifications.remove(IDselected);

        // Shranimo trenutne opomnike
        writeObjectToFile(medicineNotifications, MainActivity.fileNameWithNotifications);
    }

    private void writeObjectToFile(Object o, String fileName){
        try {
            File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(f);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(o);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
