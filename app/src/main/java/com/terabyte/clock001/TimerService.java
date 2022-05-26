package com.terabyte.clock001;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class TimerService extends Service {
    public static boolean isRunning;
    private static long timeLeft;
    private static boolean isFragmentExists;
    private static TimerFragment timerFragment;
    private static TextView textTimer;
    public static boolean isRinging;
    public static MediaPlayer mediaPlayer;
    public static Vibrator vibrator;
    CountDownTimer mCountDownTimer;

    public TimerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent==null) {
            //here service thinks that it was crush and service try to restart. But we don't need it
        }
        else {
            isRunning = true;
            isRinging = false;

            timeLeft = intent.getExtras().getLong(Const.INTENT_KEY_TIMER_LEFT_TIME_MILLS);

            mCountDownTimer = new CountDownTimer(timeLeft, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if(isFragmentExists & textTimer!=null) {
                        timeLeft = millisUntilFinished;
                        int[] hoursMinutesSecondsArray = TimerFragment.getHoursMinutesSecondsAsArrayFromMills(timeLeft);
                        textTimer.setText(String.format("%02d:%02d:%02d", hoursMinutesSecondsArray[0], hoursMinutesSecondsArray[1], hoursMinutesSecondsArray[2]));
                    }
                    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
                    notificationManagerCompat.notify(Const.TIMER_NOTIFICATION_ID, createNotification(millisUntilFinished));
                }

                @Override
                public void onFinish() {
                    isRinging = true;
                    isRunning = false;

                    SharedPreferences preferences = getSharedPreferences(Const.SH_PREFERENCES_SETTINGS_NAME, MODE_PRIVATE);
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), preferences.getInt(Const.SH_PREFERENCES_SETTINGS_KEY_TIMER_SOUND_RAW_RESOURCE, R.raw.beep_beep));
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();

                    if(preferences.getBoolean(Const.SH_PREFERENCES_SETTINGS_KEY_TIMER_VIBRATION, false)) {
                        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                        if(vibrator.hasVibrator()) {
                            long[] vibratePattern = {0,1500, 1000, 1500};
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                VibrationEffect effect = VibrationEffect.createWaveform(vibratePattern, 0);
                                vibrator.vibrate(effect);
                            }
                            else {
                                vibrator.vibrate(vibratePattern, 0);
                            }
                        }
                    }

                    if(isFragmentExists) {
                        timerFragment.recreateFragment();
                    }
                }
            }.start();

            startForeground(Const.TIMER_NOTIFICATION_ID, createNotification(timeLeft));
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        mCountDownTimer.cancel();
        if(mediaPlayer!=null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if(vibrator!=null) {
            vibrator.cancel();
            vibrator = null;
        }
        super.onDestroy();
    }

    private Notification createNotification(long startTimeLeftInMills) {
        createTimerNotificationChannel(getApplicationContext());

        int hours = (int) startTimeLeftInMills / 1000 / 3600;
        int minutes = (int) startTimeLeftInMills / 1000 / 60 % 60;
        int seconds = (int) startTimeLeftInMills / 1000 % 60;

        Notification notification = new NotificationCompat.Builder(getApplicationContext(), Const.TIMER_NOTIFICATION_CHANNEL_ID)
                .setContentTitle(getString(R.string.timer_notification_content_title))
                .setSmallIcon(R.drawable.ic_timer)
                .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), MainActivity.class), 0))
                .setContentText(String.format("%02d:%02d:%02d", hours, minutes, seconds))
                .setSilent(true)
                .build();

        return notification;
    }

    private void createTimerNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.timer_notification_channel_name);
            String description = context.getString(R.string.timer_notification_channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(Const.TIMER_NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static long getTimeLeft() {
        return timeLeft;
    }

    public static void setFragmentExists(boolean isFragmentExists) {
        TimerService.isFragmentExists = isFragmentExists;
    }

    public static void setTimerFragment(TimerFragment timerFragment) {
        TimerService.timerFragment = timerFragment;
    }

    public static void setTextTimer(TextView textTimer) {
        TimerService.textTimer = textTimer;
    }
}