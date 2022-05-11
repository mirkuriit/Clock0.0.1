package com.terabyte.clock001;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

public class AlarmManagerLauncher {

    public static void startTask(Context context, long alarmId, int hour, int minute) {
        Intent preparingIntent = new Intent(context, AlarmRingActivity.class);
        preparingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        preparingIntent.putExtra(Const.INTENT_KEY_ALARM_ID, alarmId);


        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) alarmId, preparingIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long mills = DateManager.getMillsForAlarm(hour, minute);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+mills, pendingIntent);

    }

    public static void startTask(Context context, long alarmId, int hour, int minute, boolean[] days) {
        Intent preparingIntent = new Intent(context, AlarmRingActivity.class);
        preparingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        preparingIntent.putExtra(Const.INTENT_KEY_ALARM_ID, alarmId);


        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) alarmId, preparingIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long mills = DateManager.getMillsForAlarm(hour, minute, days);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+mills, pendingIntent);

    }

    public static void startTask(Context context, long alarmId) {
        Intent preparingIntent = new Intent(context, AlarmRingActivity.class);
        preparingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        preparingIntent.putExtra(Const.INTENT_KEY_ALARM_ID, alarmId);


        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) alarmId, preparingIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long mills = 60000*5;
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+mills, pendingIntent);
    }

    public static void stopTask(Context context, long alarmId) {
        Intent preparingIntent = new Intent(context, AlarmRingActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) alarmId, preparingIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}
