package com.example.tpoteam.zepnizdravnik;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.util.Calendar;

/**
 * Created by Luka Loboda on 03-Jan-17.
 */

public class AppointmentAlarmReceiver extends WakefulBroadcastReceiver{

    private AlarmManager alarmMgr;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, AppointmentNotificationService.class);
        service.putExtra("time", intent.getStringExtra("time"));
        service.putExtra("location", intent.getStringExtra("location"));
        service.putExtra("notificationID", intent.getIntExtra("notificationID", 0));
        startWakefulService(context, service);
    }

    public void setAlarm(Context context, PendingIntent alarmIntent, Calendar calendar){
        if(alarmMgr == null){
            alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        }
        alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
    }

    public void cancelAlarm(Context context, PendingIntent alarmIntent){
        if(alarmMgr == null){
            alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        }
        alarmMgr.cancel(alarmIntent);
    }

}
