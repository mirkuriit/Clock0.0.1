package com.terabyte.clock001;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NotificationLauncher {
    private static void createAlarmNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.alarm_notification_channel_name);
            String description = context.getString(R.string.alarm_notification_channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(Const.ALARM_NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void createAlarmNotification(Context context) {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        createAlarmNotificationChannel(context);

        Notification notification = new NotificationCompat.Builder(context, Const.ALARM_NOTIFICATION_CHANNEL_ID)
                .setContentTitle(context.getString(R.string.alarm_notification_content_title))
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0))
                .build();
        notificationManagerCompat.notify(Const.ALARM_NOTIFICATION_ID, notification);
    }

    public static void cancelAlarmNotification(Context context) {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.cancel(Const.ALARM_NOTIFICATION_ID);
    }
}
