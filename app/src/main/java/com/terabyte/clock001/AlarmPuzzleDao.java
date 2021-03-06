package com.terabyte.clock001;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * this Dao interface is used by Room database. And this Dao interface works with AlarmPuzzle class.
 */

@Dao
public interface AlarmPuzzleDao {
    @Insert
    long insert(AlarmPuzzle alarmPuzzle);

    @Update
    void update(AlarmPuzzle alarmPuzzle);

    @Delete
    void delete(AlarmPuzzle alarmPuzzle);

    @Query("select * from alarmpuzzle where parentAlarmId = :parentAlarmId")
    AlarmPuzzle getByParentAlarmId(long parentAlarmId);

    @Query("select * from alarmpuzzle")
    List<AlarmPuzzle> getAll();
}
