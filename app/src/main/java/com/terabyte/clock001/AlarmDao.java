package com.terabyte.clock001;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * it's interface for Room database. It helps us communicate with database.
 * we must write insert, update and delete methods. Also we can add a plenty of @Query methods with sql requests
 * this Dao interface works with Alarm class
 */

@Dao
public interface AlarmDao {
    @Insert
    long insert(Alarm alarm);

    @Update
    void update(Alarm alarm);

    @Delete
    void delete(Alarm alarm);

    @Query("select * from alarm")
    List<Alarm> getAll();

    @Query("select * from alarm where id = :alarmId")
    Alarm getAlarmById(long alarmId);

    @Query("select * from alarm where isEnabled = :isEnabled")
    List<Alarm> getAllAlarmsByEnabled(boolean isEnabled);
}

