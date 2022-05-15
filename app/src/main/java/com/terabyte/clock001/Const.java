package com.terabyte.clock001;

import java.util.List;

/**
 * I use this class like Constant storage
 */

public class Const {
    public static final int MODE_SLEEP = 1;
    public static final int MODE_RUN = 2;

    public static final String TAG = "myDebug";

    //in Room database we have to add name of db. But It doesn't care how to name it
    public static final String ALARM_DB_NAME = "alarmDatabase";
    public static final String DATA_KEY_MILLS = "dataKeyMills";

    public static final String INTENT_KEY_ALARM_ID = "intentKeyAlarmId";
    public static final String INTENT_KEY_HARDCORE_LEVEL = "intentKeyHardcoreLevel";
    public static final String INTENT_KEY_IS_DELAYED_ALARM = "intentKeyIsDelayedAlarm";
    public static final String INTENT_KEY_TIMER_LEFT_TIME_MILLS = "intentKeyTimerLeftTimeMills";
    public static final String INTENT_KEY_BACKGROUND_IMAGE_ID = "intentKeyBackgroundImageId";


    public static final String ALARM_NOTIFICATION_CHANNEL_ID = "alarmNotificationChannelId";
    public static final int ALARM_NOTIFICATION_ID = 1;

    public static final String TIMER_NOTIFICATION_CHANNEL_ID ="timerNotificationChannelId";
    public static final int TIMER_NOTIFICATION_ID = 2;

    public static final String SH_PREFERENCES_SETTINGS_NAME = "sharedPreferencesSettings";
    public static final String SH_PREFERENCES_SETTINGS_KEY_ALARM_VIBRATION_PATTERN = "shPreferencesKeyAlarmVibrationPattern";

    public static final long[][] ALARM_VIBRATION_PATTERNS = {{0,1500, 1000, 1500}, {0, 1000}, {0, 500, 500, 500, 500, 500, 1000, 1000}};

    public static final int[] ALARM_DEFAULT_BACKGROUNDS = {R.drawable.alarm_ring_default_background1, R.drawable.alarm_ring_default_background2, R.drawable.alarm_ring_default_background3,
            R.drawable.alarm_ring_default_background4, R.drawable.alarm_ring_default_background5, R.drawable.alarm_ring_default_background6};

}
