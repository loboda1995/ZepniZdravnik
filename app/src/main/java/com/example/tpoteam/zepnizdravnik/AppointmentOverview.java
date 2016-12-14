package com.example.tpoteam.zepnizdravnik;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Luka Loboda on 14-Dec-16.
 */

public class AppointmentOverview extends AppCompatActivity{

    private ArrayList<AppointmentNotification> appointmentNotifications =  new ArrayList<>();
    private int IDselected;
    private AppointmentNotification selectedNotification;

    private EditText inputDoctor;
    private EditText inputLocation;
    private Spinner colorPicker;
    private EditText comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_notification_overview);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Pridobimo vnosna polja
        getAllInputs();


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


        // Pridobimo in prikazemo podatke, ce opomnik ze obstaja
        appointmentNotifications = (ArrayList<AppointmentNotification>) this.getIntent().getSerializableExtra(AppointmentActivity.nameOfExtra1);
        IDselected = this.getIntent().getIntExtra(AppointmentActivity.nameOfExtra2, 0);

        // Ce je IDselected kaze na zadnji element pomeni, da ustvarjamo nov opomnik, sicer
        // spreminajmo ze obstojecega
        if(IDselected < appointmentNotifications.size()-1){
            selectedNotification = appointmentNotifications.get(IDselected);


            colorPicker.setSelection(selectedNotification.idOfColor);

            comments.setText(selectedNotification.comments);
        }
    }

    // Pridobimo in shranimo vsa vnosna polja
    private void getAllInputs(){
        inputDoctor = (EditText) findViewById(R.id.appointDoctorName);
        inputLocation = (EditText) findViewById(R.id.appointLocation);

        // Ustvarimo color picker za izbiro barve ozadja opomnika
        colorPicker = (Spinner)findViewById(R.id.colorPicker);
        colorPicker.setAdapter(new ColorPickerAdapter(this));

        comments = (EditText) findViewById(R.id.commentsInput);
    }

    // Izbrise izbrani opomnik
    private void delete(){
        if(selectedNotification != null){
            new AlertDialog.Builder(this)
                    .setTitle(R.string.notificationRemovalTitle)
                    .setMessage(R.string.notificationRemoveMessage)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            boolean b = deleteThisNotification();
                            Toast.makeText(AppointmentOverview.this, b ? R.string.notifcationRemoved : R.string.notifcationNotRemoved, Toast.LENGTH_LONG).show();
                            if(b){
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        AppointmentOverview.this.finish();
                                    }
                                }, 500);
                            }
                        }})
                    .setNegativeButton(R.string.no, null).show();
        }
    }

    // Shrani novoustvarjen opomnik ali pa shrani spremenjen opomnik
    private void save(){
        // TODO: preverjanje vnosa
        boolean b = saveThisNotification();
        Toast.makeText(this, b ? R.string.notificationSaved : R.string.notificationNotSaved, Toast.LENGTH_LONG).show();
        if(b){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    AppointmentOverview.this.finish();
                }
            }, 500);
        }
    }

    private boolean saveThisNotification() {
        String doctorName = inputDoctor.getText().toString();
        String location = inputLocation.getText().toString();
        int newColorId = colorPicker.getSelectedItemPosition();
        String comm = comments.getText().toString();

        AppointmentNotification copy = null;
        if(selectedNotification != null){
            copy = new AppointmentNotification(newColorId, location, doctorName, 0, 0, comm);
        }

        // Odstranimo element, ki sluzi dodajanju novih opomnikov
        appointmentNotifications.remove(appointmentNotifications.size()-1);

        // Ce je izbrani Notification == null, pomeni da ustvarjamo novega, sicer spreminajmo ze
        // obstojecega
        if(selectedNotification != null){
            selectedNotification.doctor = doctorName;
            selectedNotification.location = location;
            selectedNotification.idOfColor = newColorId;
            selectedNotification.comments = comm;
        }else{
            selectedNotification = new AppointmentNotification(newColorId, location, doctorName, 0, 0, comm);
            appointmentNotifications.add(selectedNotification);
        }

        // Shranimo trenutne opomnike, ce shranjevanje uspe vrnemo true, sicer ponastavimo vrednosti
        // trenutnega opomnika na stare in vrnemo false
        boolean b = writeObjectToFile(appointmentNotifications, MainActivity.fileNameWithAppointments);
        if(b){
            return true;
        }else{
            selectedNotification = copy;
            return false;
        }
    }

    private boolean deleteThisNotification() {
        // Ne moremo izbrisati opomnika, ko ustvarjamo novega
        if(selectedNotification == null){
            return false;
        }
        appointmentNotifications.remove(appointmentNotifications.size()-1);
        appointmentNotifications.remove(IDselected);

        // Shranimo trenutne opomnike
        return writeObjectToFile(appointmentNotifications, MainActivity.fileNameWithAppointments);
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
