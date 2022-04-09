package com.terabyte.clock001;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * abstract database class is needed by Room Database;
 * we have to write entities and schema in params of annotation
 * our class has to extend RoomDatabase and have abstract methods that returns %EntityName%Dao
 */

@Database(entities = {Alarm.class, AlarmRepeating.class, AlarmPuzzle.class}, version = 4)
public abstract class AlarmDatabase extends RoomDatabase {
    public abstract AlarmDao alarmDao();

    public abstract AlarmRepeatingDao alarmRepeatingDao();

    public abstract AlarmPuzzleDao alarmPuzzleDao();
}
