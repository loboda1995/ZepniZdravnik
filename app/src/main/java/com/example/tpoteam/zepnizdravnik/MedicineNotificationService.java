package com.example.tpoteam.zepnizdravnik;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Luka Loboda on 20-Nov-16.
 */

public class MedicineNotificationService extends IntentService {

    public MedicineNotificationService() {
        super("SchedulingService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int notificationID = intent.getIntExtra("notificationID", 0);

        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent activity = new Intent(this, MainActivity.class);
        activity.putExtra("notificationID", notificationID);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, activity, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        int alarmID = intent.getIntExtra("alarmID", 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.doctor_icon)
                        .setContentTitle("Opomnik za zdravilo")
                        .setSound(soundUri)
                        .setStyle(new NotificationCompat.BigTextStyle())
                        .setAutoCancel(true)
                        .setShowWhen(true)
                        .setContentText("Čas je da vzamete " + intent.getStringExtra("medicineName"));

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(alarmID, mBuilder.build());

        MedicineAlarmReceiver.completeWakefulIntent(intent);
    }

}
