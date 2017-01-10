package com.example.tpoteam.zepnizdravnik;

import android.app.AlarmManager;
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
 * Created by Luka Loboda on 16-Nov-16.
 */

public class NotificationOverview extends AppCompatActivity {

    private ArrayList<MedicineNotification> medicineNotifications =  new ArrayList<>();
    private int IDselected;
    private MedicineNotification selectedNotification;
    private MedicineAlarmReceiver alarm = new MedicineAlarmReceiver();

    private EditText medicineNameInput;
    private EditText medicineQuantityInput;
    private Spinner colorPicker;

    private RadioButton radioWeekly;
    private RadioButton radioDaily;

    private LinearLayout weeklyOptions;
    private LinearLayout dailyOptions;
    private CheckBox[] weeklyCheckboxes = new CheckBox[7];
    private CheckBox[] dailyCheckboxes = new CheckBox[24];

    private EditText comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_overview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.BLACK);

        // Pridobimo vnosna polja
        getAllInputs();

        // Ob spremembi vrednosti izbranega tipa intervala jemanja spremenimo prikazane moznosti terminov
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.intervalTypeGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == radioDaily.getId()){
                    dailyOptions.setVisibility(View.VISIBLE);
                    weeklyOptions.setVisibility(View.GONE);
                }else{
                    dailyOptions.setVisibility(View.GONE);
                    weeklyOptions.setVisibility(View.VISIBLE);
                }
            }
        });

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
        medicineNotifications = (ArrayList<MedicineNotification>) this.getIntent().getSerializableExtra(MainActivity.nameOfExtra1);
        IDselected = this.getIntent().getIntExtra(MainActivity.nameOfExtra2, 0);

        // Ce je IDselected kaze na zadnji element pomeni, da ustvarjamo nov opomnik, sicer
        // spreminajmo ze obstojecega
        if(IDselected < medicineNotifications.size()-1){
            selectedNotification = medicineNotifications.get(IDselected);

            medicineNameInput.setText(selectedNotification.medicineName);
            medicineQuantityInput.setText(selectedNotification.medicineQuantity);

            colorPicker.setSelection(selectedNotification.idOfColor);

            if(selectedNotification.dailyInterval){
                radioDaily.setChecked(true);
                for(int i = 0; i < dailyCheckboxes.length; i++){
                    dailyCheckboxes[i].setChecked(selectedNotification.times[i] != 0);
                }
            }else{
                radioWeekly.setChecked(true);
                for(int i = 0; i < weeklyCheckboxes.length; i++){
                    weeklyCheckboxes[i].setChecked(selectedNotification.times[i] != 0);
                }
            }
            comments.setText(selectedNotification.comments);
        }
    }

    // Pridobimo in shranimo vsa vnosna polja
    private void getAllInputs(){
        medicineNameInput = (EditText) findViewById(R.id.medicineName);
        medicineQuantityInput = (EditText) findViewById(R.id.medicineQuantity);

        // Ustvarimo color picker za izbiro barve ozadja opomnika
        colorPicker = (Spinner)findViewById(R.id.colorPicker);
        colorPicker.setAdapter(new ColorPickerAdapter(this));

        radioDaily = (RadioButton) findViewById(R.id.radioDaily);
        radioWeekly = (RadioButton) findViewById(R.id.radioWeekly);

        weeklyOptions = (LinearLayout) findViewById(R.id.weeklyOptions);
        dailyOptions = (LinearLayout) findViewById(R.id.dailyOptions);
        int id = 0;
        for (int i = 0; i < weeklyOptions.getChildCount(); i++) {
            View v = weeklyOptions.getChildAt(i);
            if (v instanceof CheckBox) {
                weeklyCheckboxes[id++] = (CheckBox)v;
            }
        }
        id = 0;
        for (int i = 0; i < dailyOptions.getChildCount(); i++) {
            View v = dailyOptions.getChildAt(i);
            if (v instanceof LinearLayout) {
                for(int j = 0; j < ((LinearLayout)v).getChildCount(); j++){
                    View u = ((LinearLayout) v).getChildAt(j);
                    if (u instanceof CheckBox) {
                        dailyCheckboxes[id+j*12] = (CheckBox)u;
                    }
                }
                id++;
            }
        }

        comments = (EditText) findViewById(R.id.commentsInput);
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
                            Toast.makeText(NotificationOverview.this, b ? R.string.notifcationRemoved : R.string.notifcationNotRemoved, Toast.LENGTH_LONG).show();
                            if(b){
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        NotificationOverview.this.finish();
                                    }
                                }, 500);
                            }
                        }})
                    .setNegativeButton(R.string.no, null).show();
        }
    }

    // Shrani novoustvarjen opomnik ali pa shrani spremenjen opomnik
    private void save(){
        if(allInputIsValid()){
            boolean b = saveThisNotification();
            Toast.makeText(this, b ? R.string.notificationSaved : R.string.notificationNotSaved, Toast.LENGTH_LONG).show();
            if(b){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        NotificationOverview.this.finish();
                    }
                }, 500);
            }
        }
        else {
            Toast.makeText(this, R.string.validationError, Toast.LENGTH_SHORT).show();
        }
    }

    // Preveri ali so vrednosti vseh vnosnih polj podane in regularne
    private boolean allInputIsValid(){
        boolean isValid = true;
        String newMedicineName = medicineNameInput.getText().toString();
        if(newMedicineName.trim().equals("")) {
            isValid = false;
            medicineNameInput.setTextColor(Color.RED);
            medicineNameInput.setError(getString(R.string.validationErrorMedicine));
        }
        else
            medicineNameInput.setTextColor(Color.BLACK);
        if(medicineQuantityInput.getText().toString().trim().equals("")) {
            isValid = false;
            medicineQuantityInput.setError(getString(R.string.validationErrorQuantityEmpty));
            medicineQuantityInput.setTextColor(Color.RED);
        }
        else
            medicineQuantityInput.setTextColor(Color.BLACK);
        return isValid;
    }

    private boolean saveThisNotification() {
        String newMedicineName = medicineNameInput.getText().toString();
        String newMedicineQuantity = medicineQuantityInput.getText().toString();
        int newColorId = colorPicker.getSelectedItemPosition();
        boolean isDaily = radioDaily.isChecked();
        int[] times = new int[24];
        String comm = comments.getText().toString();
        if(isDaily){
            for(int i = 0; i < dailyCheckboxes.length; i++){
                if(dailyCheckboxes[i].isChecked()){
                    times[i] = ((int) System.currentTimeMillis())+i;
                }
            }
        }else{
            for(int i = 0; i < weeklyCheckboxes.length; i++){
                if(weeklyCheckboxes[i].isChecked()){
                    times[i] = ((int) System.currentTimeMillis())+i;
                }
            }
        }
        MedicineNotification copy = null;
        if(selectedNotification != null){
             copy = new MedicineNotification(selectedNotification.medicineName,
                    selectedNotification.medicineQuantity, selectedNotification.idOfColor,
                    selectedNotification.dailyInterval, selectedNotification.times, selectedNotification.comments, selectedNotification.idOfNoti);
        }

        // Odstranimo element, ki sluzi dodajanju novih opomnikov
        medicineNotifications.remove(medicineNotifications.size()-1);

        // Ce je izbrani Notification == null, pomeni da ustvarjamo novega, sicer spreminajmo ze
        // obstojecega
        if(selectedNotification != null){
            selectedNotification.medicineName = newMedicineName;
            selectedNotification.medicineQuantity = newMedicineQuantity;
            selectedNotification.idOfColor = newColorId;
            selectedNotification.dailyInterval = isDaily;
            selectedNotification.times = times;
            selectedNotification.comments = comm;
            selectedNotification.idOfNoti = (int)System.currentTimeMillis();
        }else{
            selectedNotification = new MedicineNotification(newMedicineName, newMedicineQuantity, newColorId, isDaily, times, comm, (int)System.currentTimeMillis());
            medicineNotifications.add(selectedNotification);
        }

        // Shranimo trenutne opomnike, ce shranjevanje uspe vrnemo true, sicer ponastavimo vrednosti
        // trenutnega opomnika na stare in vrnemo false
        boolean b = writeObjectToFile(medicineNotifications, MainActivity.fileNameWithNotifications);
        if(b){
            if(copy != null){
                // Pocistimo stare alarme
                for(int i = 0; i < copy.times.length; i++){
                    if(copy.times[i] != 0){
                        Intent intent = new Intent(this, MedicineAlarmReceiver.class);
                        alarm.cancelAlarm(this, PendingIntent.getBroadcast(this, copy.times[i], intent, PendingIntent.FLAG_UPDATE_CURRENT));
                    }
                }
            }
            for(int i = 0; i < times.length; i++){
                if(times[i] != 0){
                    // Nastavimo cas alarma, ce je v preteklosti, pristejemo dan ali teden
                    Calendar cal = Calendar.getInstance();
                    if(isDaily){
                        cal.set(Calendar.HOUR_OF_DAY, i);
                        cal.set(Calendar.MINUTE, 0);
                        cal.set(Calendar.SECOND, 0);
                        if(cal.getTimeInMillis() < System.currentTimeMillis()){
                            cal.setTimeInMillis(cal.getTimeInMillis() + AlarmManager.INTERVAL_DAY);
                        }
                    }else{
                        if(i < 6){
                            cal.set(Calendar.DAY_OF_WEEK, i+2);
                        }else{
                            cal.set(Calendar.DAY_OF_WEEK, 1);
                        }
                        cal.set(Calendar.HOUR_OF_DAY, 10);
                        cal.set(Calendar.MINUTE, 0);
                        cal.set(Calendar.SECOND, 0);
                        if(cal.getTimeInMillis() < System.currentTimeMillis()){
                            cal.setTimeInMillis(cal.getTimeInMillis() + AlarmManager.INTERVAL_DAY*7);
                        }
                    }

                    Intent intent = new Intent(this, MedicineAlarmReceiver.class);
                    intent.putExtra("medicineName", selectedNotification.medicineName);
                    intent.putExtra("notificationID", selectedNotification.idOfNoti);
                    intent.putExtra("alarmID", times[i]);
                    alarm.setAlarm(this, PendingIntent.getBroadcast(this, times[i], intent, PendingIntent.FLAG_UPDATE_CURRENT), cal);
                }
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
        for(int i = 0; i < selectedNotification.times.length; i++){
            if(selectedNotification.times[i] != 0){
                Intent intent = new Intent(this, MedicineAlarmReceiver.class);
                alarm.cancelAlarm(this, PendingIntent.getBroadcast(this, selectedNotification.times[i], intent, PendingIntent.FLAG_UPDATE_CURRENT));
            }
        }
        medicineNotifications.remove(medicineNotifications.size()-1);
        medicineNotifications.remove(IDselected);

        // Shranimo trenutne opomnike
        return writeObjectToFile(medicineNotifications, MainActivity.fileNameWithNotifications);
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
