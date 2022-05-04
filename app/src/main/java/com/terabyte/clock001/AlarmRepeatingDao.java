package com.terabyte.clock001;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface AlarmRepeatingDao {
    @Insert
    long insert(AlarmRepeating alarmRepeating);

    @Update
    void update(AlarmRepeating alarmRepeating);

    @Delete
    void delete(AlarmRepeating alarmRepeating);

    @Query("select * from alarmrepeating where parentAlarmId = :parentAlarmId")
    AlarmRepeating getByParentAlarmId(long parentAlarmId);

    @Query("select * from alarmrepeating")
    List<AlarmRepeating> getAll();
}
