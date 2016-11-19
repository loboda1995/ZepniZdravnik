package com.example.tpoteam.zepnizdravnik;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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
    private EditText medicineQuantityInput;

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
            medicineQuantityInput.setText(Integer.toString(selectedNotification.medicineQuantity));
            if(selectedNotification.dailyInterval){
                radioDaily.setChecked(true);
                for(int i = 0; i < dailyCheckboxes.length; i++){
                    dailyCheckboxes[i].setChecked(selectedNotification.times[i]);
                }
            }else{
                radioWeekly.setChecked(true);
                for(int i = 0; i < weeklyCheckboxes.length; i++){
                    weeklyCheckboxes[i].setChecked(selectedNotification.times[i]);
                }
            }
            comments.setText(selectedNotification.comments);
        }
    }

    // Pridobimo in shranimo vsa vnosna polja
    private void getAllInputs(){
        medicineNameInput = (EditText) findViewById(R.id.medicineName);
        medicineQuantityInput = (EditText) findViewById(R.id.medicineQuantity);

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
            new AlertDialog.Builder(this)
                    .setTitle(R.string.notificationRemovalTitle)
                    .setMessage(R.string.notificationRemoveMessage)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            deleteThisNotification();
                            Toast.makeText(NotificationOverview.this, R.string.notifcationRemoved, Toast.LENGTH_LONG).show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    NotificationOverview.this.finish();
                                }
                            }, 500);
                        }})
                    .setNegativeButton(R.string.no, null).show();
        }
    }

    // Shrani novoustvarjen opomnik ali pa shrani spremenjen opomnik
    private void save(){
        if(allInputIsValid()){
            saveThisNotification();
            Toast.makeText(this, R.string.notificationSaved, Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    NotificationOverview.this.finish();
                }
            }, 500);
        }
        else
            Toast.makeText(this, R.string.validationError, Toast.LENGTH_LONG).show();
    }

    // Preveri ali so vrednosti vseh vnosnih polj podane in regularne
    private boolean allInputIsValid(){
        boolean isValid = true;
        String newMedicineName = medicineNameInput.getText().toString();
        if(newMedicineName.trim().equals("")) {
            isValid = false;
            medicineQuantityInput.setTextColor(Color.RED);
        }
        else
            medicineQuantityInput.setTextColor(Color.BLACK);
        try {
            int newMedicineQuantity = Integer.parseInt(medicineQuantityInput.getText().toString());
            medicineQuantityInput.setTextColor(Color.BLACK);
        }
        catch (Exception e) {
            isValid = false;
            medicineQuantityInput.setTextColor(Color.RED);
        }
        return isValid;
    }

    private void saveThisNotification() {
        String newMedicineName = medicineNameInput.getText().toString();
        int newMedicineQuantity = Integer.parseInt(medicineQuantityInput.getText().toString());
        boolean isDaily = radioDaily.isChecked();
        boolean[] times = new boolean[24];
        String comm = comments.getText().toString();
        if(isDaily){
            for(int i = 0; i < dailyCheckboxes.length; i++){
                times[i] = dailyCheckboxes[i].isChecked();
            }
        }else{
            for(int i = 0; i < weeklyCheckboxes.length; i++){
                times[i] = weeklyCheckboxes[i].isChecked();
            }
        }

        // Odstranimo element, ki sluzi dodajanju novih opomnikov
        medicineNotifications.remove(medicineNotifications.size()-1);

        // Ce je izbrani Notification == null, pomeni da ustvarjamo novega, sicer spreminajmo ze
        // obstojecega
        if(selectedNotification != null){
            selectedNotification.medicineName = newMedicineName;
            selectedNotification.medicineQuantity = newMedicineQuantity;
            selectedNotification.dailyInterval = isDaily;
            selectedNotification.times = times;
            selectedNotification.comments = comm;
        }else{
            selectedNotification = new MedicineNotification(newMedicineName, newMedicineQuantity, isDaily, times, comm);
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
