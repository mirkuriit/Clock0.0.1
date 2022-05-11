package com.terabyte.clock001;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.net.URI;

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
    public String description, soundURIString;
    public boolean isEnabled, isRepeat, isVibration, isPuzzle;


    public Alarm(int hour, int minute, String description, String soundURIString, boolean isEnabled, boolean isRepeat, boolean isVibration, boolean isPuzzle) {
        this.hour = hour;
        this.minute = minute;
        this.description = description;
        this.soundURIString = soundURIString;
        this.isEnabled = isEnabled;
        this.isRepeat = isRepeat;
        this.isVibration = isVibration;
        this.isPuzzle = isPuzzle;
    }

    public static String getDefaultSoundUriString() {
        return "android.resource://com.terabyte.clock001/"+R.raw.egor_track;
    }


}
