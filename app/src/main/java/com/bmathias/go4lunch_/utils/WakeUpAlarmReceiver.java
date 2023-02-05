package com.bmathias.go4lunch_.utils;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.Calendar;

public class WakeUpAlarmReceiver extends BroadcastReceiver {

    @SuppressLint("UnspecifiedImmutableFlag")
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED") ||
                intent.getAction().equals("android.intent.action.QUICKBOOT_POWERON")) {

            // Restart AlarmManager for notification
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 17);
            calendar.set(Calendar.MINUTE, 40);
            calendar.set(Calendar.SECOND, 0);

            Calendar cur = Calendar.getInstance();

            if (cur.after(calendar)) {
                calendar.add(Calendar.DATE, 1);
            }

            Intent myIntent = new Intent(context, NotificationReceiver.class);
            PendingIntent pendingIntent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                pendingIntent = PendingIntent.getActivity(context,
                        0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            } else {
                pendingIntent = PendingIntent.getActivity(context,
                        0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            }

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            Log.d("Receiver", "Alarm set !");
        }
    }
}