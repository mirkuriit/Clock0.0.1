package com.terabyte.clock001;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class AlarmRepeating {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public long parentAlarmId;
    public boolean mo, tu, we, th, fr, sa, su;

    //we never use this constructor in code, but Room Database want to have it. Without it we have
    //error from Room:
    //"Entities and POJOs must have a usable public constructor. You can have an empty constructor or a constructor whose parameters match the fields (by name and type)."
    public AlarmRepeating() {

    }

    public AlarmRepeating(long parentAlarmId, boolean[] days) {
        this.parentAlarmId = parentAlarmId;
        mo = days[0];
        tu = days[1];
        we = days[2];
        th = days[3];
        fr = days[4];
        sa = days[5];
        su = days[6];
    }

    public boolean[] getArrayOfBooleanDays() {
        return new boolean[]{mo, tu, we, th, fr, sa, su};
    }
}
