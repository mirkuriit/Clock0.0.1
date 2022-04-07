package com.terabyte.clock001;

import android.content.Context;

import androidx.room.Room;

/**
 * I wrote this class to save resources of app
 * Google recommends to create RoomDatabase instance very rarely
 * so, this class is needed to create db instance only once
 */

public class AlarmDatabaseClient {
    private Context context;
    private static AlarmDatabaseClient clientInstance;

    private AlarmDatabase appDatabase;

    private AlarmDatabaseClient(Context context) {
        this.context = context;
        appDatabase = Room.databaseBuilder(context, AlarmDatabase.class, Const.ALARM_DB_NAME).build();
    }

    public static synchronized AlarmDatabaseClient getInstance(Context context) {
        if(clientInstance == null) {
            clientInstance = new AlarmDatabaseClient(context);
        }
        return clientInstance;
    }

    public AlarmDatabase getAppDatabase() {
        return appDatabase;
    }
}
