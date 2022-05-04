package com.terabyte.clock001;

import android.media.MediaPlayer;
import android.os.Vibrator;
import android.os.VibratorManager;

public class MediaPlayerManager {
    public static MediaPlayer mediaPlayer;
    public static long alarmIdForMediaPlayer = -1;

    public static Vibrator vibrator;
    public static long alarmIdForVibrator = -1;
}
