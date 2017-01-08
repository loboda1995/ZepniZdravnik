package com.example.tpoteam.zepnizdravnik;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Luka Loboda on 14-Dec-16.
 */

public class AppointmentOverview extends AppCompatActivity{

    private AppointmentAlarmReceiver alarm = new AppointmentAlarmReceiver();

    private ArrayList<AppointmentNotification> appointmentNotifications =  new ArrayList<>();
    private int IDselected;
    private AppointmentNotification selectedNotification;

    private EditText inputDoctor;
    private EditText inputInstitution;
    private EditText inputLocation;
    private Spinner colorPicker;
    private TextView displayAlarmTime;
    private TextView displayAppointmentTime;
    private CheckBox checkRemove;
    private EditText comments;

    private long alarmTime = -1;
    private long appointmentTime = -1;

    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy 'ob' HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_notification_overview);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.BLACK);

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
        if(IDselected < appointmentNotifications.size()){
            selectedNotification = appointmentNotifications.get(IDselected);

            inputDoctor.setText(selectedNotification.doctor);
            inputInstitution.setText(selectedNotification.institution);
            inputLocation.setText(selectedNotification.location);

            colorPicker.setSelection(selectedNotification.idOfColor);

            checkRemove.setChecked(selectedNotification.removeOld);

            comments.setText(selectedNotification.comments);

            alarmTime = selectedNotification.timeOfNotification;
            appointmentTime = selectedNotification.timeOfAppointment;
            // Odstranimo alarm ce je ze minil
            if(selectedNotification != null && alarmTime != -1 && alarmTime < Calendar.getInstance().getTimeInMillis()) {
                alarmTime = -1;
                saveThisNotification();
            }
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(alarmTime);
            displayAlarmTime.setText(alarmTime == -1 ? getResources().getString(R.string.setTimeText) : sdf.format(cal.getTime()));
            cal.setTimeInMillis(appointmentTime);
            displayAppointmentTime.setText(appointmentTime == -1 ? getResources().getString(R.string.setTimeText) : sdf.format(cal.getTime()));
        }
    }

    // Pridobimo in shranimo vsa vnosna polja
    private void getAllInputs(){
        inputDoctor = (EditText) findViewById(R.id.appointDoctorName);
        inputInstitution = (EditText) findViewById(R.id.appointInstitution);
        inputLocation = (EditText) findViewById(R.id.appointLocation);

        displayAlarmTime = (TextView) findViewById(R.id.displayAlarmTime);
        displayAlarmTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(true);
            }
        });
        displayAppointmentTime = (TextView) findViewById(R.id.displayAppointTime);
        displayAppointmentTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(false);
            }
        });

        // Ustvarimo color picker za izbiro barve ozadja opomnika
        colorPicker = (Spinner)findViewById(R.id.colorPicker);
        colorPicker.setAdapter(new ColorPickerAdapter(this));

        checkRemove = (CheckBox) findViewById(R.id.removeOld);

        comments = (EditText) findViewById(R.id.commentsInput);
    }

    private void showDatePicker(final boolean alarm){
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.date_and_time_picker, null);
        final TimePicker timePicker = (TimePicker) promptView.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        TextView title = (TextView)promptView.findViewById(R.id.title);
        title.setText(alarm ? getResources().getString(R.string.timePickerTitle1) : getResources().getString(R.string.timePickerTitle2));
        final DatePicker datePicker = (DatePicker)promptView.findViewById(R.id.datePicker);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton(R.string.set,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Calendar cal = Calendar.getInstance();
                                Calendar currentTime = Calendar.getInstance();
                                if(alarm) {
                                    cal.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getCurrentHour(), timePicker.getCurrentMinute());
                                    // preverimo ali je čas alarma pred časom pregleda in če ni slučajno v preteklosti
                                    if(cal.getTimeInMillis()-currentTime.getTimeInMillis() > 0 && (appointmentTime == -1 || cal.getTimeInMillis() < appointmentTime)){
                                        alarmTime = cal.getTimeInMillis();
                                        displayAlarmTime.setText(sdf.format(cal.getTime()));
                                    }else{
                                        Toast.makeText(AppointmentOverview.this, R.string.appointmentError, Toast.LENGTH_LONG).show();
                                    }
                                }else{
                                    cal.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getCurrentHour(), timePicker.getCurrentMinute());
                                    // preverimo ali je čas alarma pred časom pregleda in če ni slučajno čas v preteklosti
                                    if(cal.getTimeInMillis()-currentTime.getTimeInMillis() > 0 && (alarmTime == -1 || alarmTime < cal.getTimeInMillis())){
                                        appointmentTime = cal.getTimeInMillis();
                                        displayAppointmentTime.setText(sdf.format(cal.getTime()));
                                    }else{
                                        Toast.makeText(AppointmentOverview.this, R.string.appointmentError, Toast.LENGTH_LONG).show();
                                    }
                                }
                                dialog.cancel();
                            }
                        })
                .setNeutralButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if(alarm) {
                            alarmTime = -1;
                            displayAlarmTime.setText(getResources().getString(R.string.setTimeText));
                        }else {
                            appointmentTime = -1;
                            displayAppointmentTime.setText(getResources().getString(R.string.setTimeText));
                        }
                        dialog.cancel();
                    }
                })
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    // Izbrise izbrani opomnik
    private void delete(){
        if(selectedNotification != null){
            String chars = getResources().getString(R.string.notificationRemovalTitle);
            SpannableString str = new SpannableString(chars);
            str.setSpan(new ForegroundColorSpan(Color.BLACK), 0, chars.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            new AlertDialog.Builder(this)
                    .setTitle(str)
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
        if(allInputIsValid()) {
            boolean b = saveThisNotification();
            Toast.makeText(this, b ? R.string.notificationSaved : R.string.notificationNotSaved, Toast.LENGTH_LONG).show();
            if (b) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AppointmentOverview.this.finish();
                    }
                }, 500);
            }
        }
        else {
            Toast.makeText(this, R.string.validationError, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean allInputIsValid() {
        boolean isValid = true;
        if(inputDoctor.getText().toString().trim().equals("")) {
            isValid = false;
            inputDoctor.setTextColor(Color.RED);
            inputDoctor.setError(getString(R.string.validationErrorDoctor));
        }
        else
            inputDoctor.setTextColor(Color.BLACK);
        if(inputInstitution.getText().toString().trim().equals("")) {
            isValid = false;
            inputInstitution.setTextColor(Color.RED);
            inputInstitution.setError(getString(R.string.validationErrorInstitution));
        }
        else
            inputInstitution.setTextColor(Color.BLACK);
        if(inputLocation.getText().toString().trim().equals("")) {
            isValid = false;
            inputLocation.setTextColor(Color.RED);
            inputLocation.setError(getString(R.string.validationErrorLocation));
        }
        else
            inputLocation.setTextColor(Color.BLACK);
        return isValid;
    }

    private boolean saveThisNotification() {
        String doctorName = inputDoctor.getText().toString();
        String inst = inputInstitution.getText().toString();
        String location = inputLocation.getText().toString();
        int newColorId = colorPicker.getSelectedItemPosition();
        String comm = comments.getText().toString();
        boolean removeOld = checkRemove.isChecked();

        AppointmentNotification copy = null;
        if(selectedNotification != null){
            copy = new AppointmentNotification(selectedNotification.idOfColor, selectedNotification.institution,
                    selectedNotification.location, selectedNotification.doctor, selectedNotification.timeOfNotification,
                    selectedNotification.timeOfAppointment, selectedNotification.comments, selectedNotification.idOfNoti, selectedNotification.removeOld);
        }


        // Ce je izbrani Notification == null, pomeni da ustvarjamo novega, sicer spreminajmo ze
        // obstojecega
        if(selectedNotification != null){
            selectedNotification.doctor = doctorName;
            selectedNotification.location = location;
            selectedNotification.timeOfNotification = alarmTime;
            selectedNotification.timeOfAppointment = appointmentTime;
            selectedNotification.idOfColor = newColorId;
            selectedNotification.comments = comm;
            selectedNotification.removeOld = removeOld;
            selectedNotification.idOfNoti = (int)System.currentTimeMillis();
        }else{
            selectedNotification = new AppointmentNotification(newColorId, inst, location, doctorName, alarmTime, appointmentTime, comm,  (int)System.currentTimeMillis(), removeOld);
            appointmentNotifications.add(selectedNotification);
        }

        // Shranimo trenutne opomnike, ce shranjevanje uspe vrnemo true, sicer ponastavimo vrednosti
        // trenutnega opomnika na stare in vrnemo false
        boolean b = writeObjectToFile(appointmentNotifications, MainActivity.fileNameWithAppointments);
        if(b){
            if(copy != null && copy.timeOfNotification != -1){
                // Zbrišemo star opomnik ce obstaja
                Intent intent = new Intent(this, AppointmentAlarmReceiver.class);
                Log.i("REMOVE ID", copy.idOfNoti+"");
                alarm.cancelAlarm(this, PendingIntent.getBroadcast(this, copy.idOfNoti, intent, PendingIntent.FLAG_UPDATE_CURRENT));
            }
            if(alarmTime != -1){
                // Nastavimo cas alarma
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(alarmTime);

                Intent intent = new Intent(this, AppointmentAlarmReceiver.class);
                intent.putExtra("time", sdf.format(new Date(selectedNotification.timeOfAppointment)));
                intent.putExtra("notificationID", selectedNotification.idOfNoti);
                Log.i("ADD ID", selectedNotification.idOfNoti+"");
                alarm.setAlarm(this, PendingIntent.getBroadcast(this, selectedNotification.idOfNoti, intent, PendingIntent.FLAG_UPDATE_CURRENT), cal);
            }
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

        // Zbrišemo star opomnik ce obstaja
        if(selectedNotification.timeOfNotification != -1){
            Intent intent = new Intent(this, AppointmentAlarmReceiver.class);
            alarm.cancelAlarm(this, PendingIntent.getBroadcast(this, selectedNotification.idOfNoti, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        }

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
