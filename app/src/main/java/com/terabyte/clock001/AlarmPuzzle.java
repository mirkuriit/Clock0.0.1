package com.terabyte.clock001;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * There is also Entity class for Room database
 * In our app we can add a puzzle for alarm. If you want to dismiss alarm you need to solve a puzzle
 * Obviously, I could just create [hardcoreLevel] field in Alarm Entity class, but in that case if variable
 * [isPuzzle] = false this field would be empty, so it would be strange architecture solution
 * Like in case of AlarmRepeating I've created this entity in Room database
 */

@Entity
public class AlarmPuzzle {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public long parentAlarmId;
    public int hardcoreLevel;

    public AlarmPuzzle(long parentAlarmId, int hardcoreLevel) {
        this.parentAlarmId = parentAlarmId;
        this.hardcoreLevel = hardcoreLevel;
    }
}
