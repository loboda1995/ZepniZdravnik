package com.example.tpoteam.zepnizdravnik;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.util.Calendar;

/**
 * Created by Luka Loboda on 20-Nov-16.
 */

public class MedicineAlarmReceiver extends WakefulBroadcastReceiver{

    private AlarmManager alarmMgr;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, MedicineNotificationService.class);
        service.putExtra("medicineName", intent.getStringExtra("medicineName"));
        service.putExtra("notificationID", intent.getIntExtra("notificationID", 0));
        startWakefulService(context, service);
    }

    public void setAlarm(Context context, PendingIntent alarmIntent, Calendar calendar){
        if(alarmMgr == null){
            alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        }
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
    }

    public void cancelAlarm(Context context, PendingIntent alarmIntent){
        if(alarmMgr == null){
            alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        }
        alarmMgr.cancel(alarmIntent);
    }
}
