package com.terabyte.clock001;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class StopwatchService extends Service {
    private static long elapsedTime;
    public static boolean isRunning;
    private static boolean isFragmentExists;
    private static TextView textTime;

    public static final int TIME_INTERVAL = 100;

    private Handler.Callback handlerCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            if(isRunning) {
                elapsedTime+= TIME_INTERVAL;
                if(elapsedTime%1000==0) {
                    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
                    notificationManagerCompat.notify(Const.STOPWATCH_NOTIFICATION_ID, createNotification(elapsedTime));
                }
                if(isFragmentExists & textTime!=null) {
                    textTime.setText(getTextTimeForUI(elapsedTime));
                }
                handler.sendMessageDelayed(handler.obtainMessage(0), TIME_INTERVAL);
            }
            return false;
        }
    };
    private Handler handler;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isRunning = true;
        elapsedTime = intent.getExtras().getLong(Const.INTENT_KEY_STOPWATCH_ELAPSED_TIME_MILLS);

        handler = new Handler(handlerCallback);
        handler.sendMessageDelayed(handler.obtainMessage(0), TIME_INTERVAL);

        startForeground(Const.STOPWATCH_NOTIFICATION_ID, createNotification(elapsedTime));

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.cancel(Const.STOPWATCH_NOTIFICATION_ID);
        super.onDestroy();
    }

    private void createStopwatchNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.stopwatch_notification_channel_name);
            String description = context.getString(R.string.stopwatch_notification_channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(Const.STOPWATCH_NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private Notification createNotification(long mills) {
        createStopwatchNotificationChannel(getApplicationContext());

        Notification notification = new NotificationCompat.Builder(getApplicationContext(), Const.STOPWATCH_NOTIFICATION_CHANNEL_ID)
                .setContentTitle(getString(R.string.stopwatch_notification_content_title))
                .setSmallIcon(R.drawable.ic_stopwatch)
                .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), MainActivity.class), 0))
                .setContentText(getTextTimeForUI(mills))
                .setSilent(true)
                .build();

        return notification;
    }

    public static String getTextTimeForUI(long mills) {
        int minutes = (int) mills/60000;
        int seconds = (int) (mills-minutes*60000)/1000;
        int deciSeconds = (int) (mills-minutes*60000-seconds*1000)/10;
        return String.format("%02d:%02d.%02d", minutes, seconds, deciSeconds);
    }

    public static void setTextTime(TextView textTime) {
        StopwatchService.textTime = textTime;
    }

    public static void setFragmentExists(boolean isFragmentExists) {
        StopwatchService.isFragmentExists = isFragmentExists;
    }

    public static long getElapsedTime() {
        return elapsedTime;
    }
}