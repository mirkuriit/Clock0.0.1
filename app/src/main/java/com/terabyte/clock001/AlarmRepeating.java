package com.terabyte.clock001;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class AlarmRepeating {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public long parentAlarmId;
    public boolean mo, tu, we, th, fr, sa, su;

    public AlarmRepeating(long parentAlarmId) {
        this.parentAlarmId = parentAlarmId;
    }

    public boolean[] getArrayOfBooleanDays() {
        return new boolean[]{mo, tu, we, th, fr, sa, su};
    }
}
