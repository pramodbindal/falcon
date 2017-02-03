package com.pramodbindal.localshop;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;

import com.pramodbindal.localshop.util.CommonConstants;

public class MyAlarmReceiver extends BroadcastReceiver {
    public static final String INTENT_ACTION = "com.pramodbindal.localshop.PERIODIC_TASK_HEART_BEAT";
    public static final String INTENT_ACTION_BATTERY_LOW = "android.intent.action.BATTERY_LOW";
    public static final String INTENT_ACTION_BATTERY_OKAY = "android.intent.action.BATTERY_OKAY";

    public static void restartPeriodicTaskHeartBeat(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CommonConstants.APP_NAME, Context.MODE_PRIVATE);
        boolean isBatteryOk = sharedPreferences.getBoolean(CommonConstants.BACKGROUND_SERVICE_BATTERY_CONTROL, true);
        Intent alarmIntent = new Intent(context, MyAlarmReceiver.class);
        boolean isAlarmUp = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_NO_CREATE) != null;

        if (isBatteryOk && !isAlarmUp) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmIntent.setAction(INTENT_ACTION);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 10000, pendingIntent);
        }
    }

    // Triggered by the Alarm periodically (starts the service to run task)
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CommonConstants.APP_NAME, Context.MODE_PRIVATE);
        if (intent.getAction() == null) {
            return;
        }
        if (intent.getAction().equals(INTENT_ACTION_BATTERY_LOW)) {
            sharedPreferences.edit().putBoolean(CommonConstants.BACKGROUND_SERVICE_BATTERY_CONTROL, false).apply();
            stopPeriodicTaskHeartBeat(context);
        } else if (intent.getAction().equals(INTENT_ACTION_BATTERY_OKAY)) {
            sharedPreferences.edit().putBoolean(CommonConstants.BACKGROUND_SERVICE_BATTERY_CONTROL, true).apply();
            restartPeriodicTaskHeartBeat(context);
        } else if (INTENT_ACTION.equals(intent.getAction())) {
            context.startService(new Intent(context, DataSyncService.class));
        }
    }

    public void stopPeriodicTaskHeartBeat(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, getClass());
        alarmIntent.setAction(INTENT_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
        alarmManager.cancel(pendingIntent);
    }


}