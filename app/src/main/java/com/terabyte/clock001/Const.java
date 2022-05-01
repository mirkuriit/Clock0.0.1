package com.terabyte.clock001;

import java.util.List;

/**
 * I use this class like Constant storage
 */

public class Const {
    public static final String TAG = "myDebug";

    //in Room database we have to add name of db. But It doesn't care how to name it
    public static final String ALARM_DB_NAME = "alarmDatabase";
    public static final String DATA_KEY_MILLS = "dataKeyMills";

    public static final String INTENT_KEY_ALARM_ID = "intentKeyAlarmId";
    public static final String INTENT_KEY_HARDCORE_LEVEL = "intentKeyHardcoreLevel";

    public static final String ALARM_NOTIFICATION_CHANNEL_ID = "alarmNotificationChannelId";
    public static final int ALARM_NOTIFICATION_ID = 1;


}
