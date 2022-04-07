package com.terabyte.clock001;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * It's simple entity class for Room Database.
 * every field of this class is supposed to be public or it's supposed to have getters and setters.
 * Room (like SQL) can't keep hard types: we can't put here arrays, reference objects of classes (besides String objects)
 */

@Entity
public class Alarm {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public int hour, minute;
    public String description;
    public boolean isEnabled, isRepeat, isVibration;

    public Alarm(int hour, int minute, String description, boolean isEnabled, boolean isRepeat, boolean isVibration) {
        this.hour = hour;
        this.minute = minute;
        this.description = description;
        this.isEnabled = isEnabled;
        this.isRepeat = isRepeat;
        this.isVibration = isVibration;
    }
}
