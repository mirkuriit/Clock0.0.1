package com.terabyte.clock001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Random;

public class AlarmRingActivity extends AppCompatActivity {
    TimeMonitorTask timeMonitorTask;
    TextView textAlarmRingTime, textAlarmRingDescription;
    Button buttonAlarmSolvePuzzle, buttonAlarmDelay, buttonAlarmDismiss;
    int backgroundImageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ring);

        getSupportActionBar().hide();

        if(savedInstanceState==null) {
            backgroundImageId = setRandomBackgroundImage(findViewById(R.id.imageAlarmRingBackground));
        }
        else {
            backgroundImageId = savedInstanceState.getInt(Const.INTENT_KEY_BACKGROUND_IMAGE_ID);
            ImageView imageAlarmRingBackground = findViewById(R.id.imageAlarmRingBackground);
            imageAlarmRingBackground.setImageDrawable(getDrawable(backgroundImageId));
        }


        long alarmId = getIntent().getExtras().getLong(Const.INTENT_KEY_ALARM_ID);

        initViewFields();

        if(AlarmDatabaseManager.getAlarmList()!=null) {
            Alarm alarm = AlarmDatabaseManager.getAlarmFromListById(alarmId);
            initTimeMonitorTask(textAlarmRingTime);
            initMediaPlayer(alarm);
            initVibration(alarm);

            //here we turn off or turn on again alarm (it depends on alarmRepeating)
            if(savedInstanceState==null) {
                if(alarm.isRepeat) {
                    AlarmRepeating alarmRepeating = AlarmDatabaseManager.getAlarmRepeatingFromListByParentAlarmId(alarm.id);
                    AlarmManagerLauncher.startTask(getApplicationContext(), alarm.id, alarm.hour, alarm.minute, alarmRepeating.getArrayOfBooleanDays());
                }
                else {
                    alarm.isEnabled = false;
                    AlarmDatabaseManager.updateAlarm(getApplicationContext(), alarm);
                }
            }

            textAlarmRingDescription.setText(alarm.description);

            setButtonsVisibilityByIsPuzzle(alarm.isPuzzle);
            if(alarm.isPuzzle) {
                AlarmPuzzle alarmPuzzle = AlarmDatabaseManager.getAlarmPuzzleFromListByParentAlarmId(alarm.id);
                buttonAlarmSolvePuzzle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), SolvePuzzleActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        intent.putExtra(Const.INTENT_KEY_HARDCORE_LEVEL, alarmPuzzle.hardcoreLevel);
                        intent.putExtra(Const.INTENT_KEY_ALARM_ID, alarm.id);
                        intent.putExtra(Const.INTENT_KEY_BACKGROUND_IMAGE_ID, backgroundImageId);
                        startActivity(intent);
                    }
                });
            }
            else {
                buttonAlarmDismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        stopMediaPlayer(alarm);
                        stopVibration(alarm);
                        finish();
                    }
                });
                buttonAlarmDelay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        stopMediaPlayer(alarm);
                        stopVibration(alarm);
                        //here we launch alarmWorker to five minutes +
                        AlarmManagerLauncher.startTask(getApplicationContext(), alarm.id);
                        finish();
                    }
                });
            }
        }
        else {
            AlarmDatabaseManager.getAlarmById(getApplicationContext(), alarmId, new PostExecuteCode() {
                @Override
                public void doInPostExecuteWhenWeGotAlarm(Alarm alarm) {
                    initTimeMonitorTask(textAlarmRingTime);
                    initMediaPlayer(alarm);
                    initVibration(alarm);

                    if(savedInstanceState==null) {
                        if(alarm.isRepeat) {
                            AlarmDatabaseManager.getAlarmRepeatingByParentAlarmId(getApplicationContext(), alarm.id, new PostExecuteCode() {
                                @Override
                                public void doInPostExecuteWhenWeGotAlarmRepeating(AlarmRepeating alarmRepeating) {
                                    AlarmManagerLauncher.startTask(getApplicationContext(), alarm.id, alarm.hour, alarm.minute, alarmRepeating.getArrayOfBooleanDays());
                                }
                            });

                        }
                        else {
                            alarm.isEnabled = false;
                            AlarmDatabaseManager.updateAlarm(getApplicationContext(), alarm);
                        }
                    }

                    textAlarmRingDescription.setText(alarm.description);

                    setButtonsVisibilityByIsPuzzle(alarm.isPuzzle);
                    if(alarm.isPuzzle) {
                        AlarmDatabaseManager.getAlarmPuzzleByParentAlarmId(getApplicationContext(), alarm.id, new PostExecuteCode() {
                            @Override
                            public void doInPostExecuteWhenWeGotAlarmPuzzle(AlarmPuzzle alarmPuzzle) {
                                buttonAlarmSolvePuzzle.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(getApplicationContext(), SolvePuzzleActivity.class);
                                        intent.putExtra(Const.INTENT_KEY_HARDCORE_LEVEL, alarmPuzzle.hardcoreLevel);
                                        intent.putExtra(Const.INTENT_KEY_ALARM_ID, alarm.id);
                                        intent.putExtra(Const.INTENT_KEY_BACKGROUND_IMAGE_ID, backgroundImageId);
                                        startActivity(intent);
                                    }
                                });
                            }
                        });

                    }
                    else {
                        buttonAlarmDismiss.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                stopMediaPlayer(alarm);
                                stopVibration(alarm);
                                finish();
                            }
                        });
                        buttonAlarmDelay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                stopMediaPlayer(alarm);
                                stopVibration(alarm);
                                //here we launch alarmWorker to five minutes +
                                AlarmManagerLauncher.startTask(getApplicationContext(), alarm.id);
                                finish();
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(Const.INTENT_KEY_BACKGROUND_IMAGE_ID, backgroundImageId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        //actually the probability of null value in timeMonitorTask field is very small. But I
        //want to check everything!
        if(timeMonitorTask!=null) {
            timeMonitorTask.finishThread();
        }
        super.onDestroy();
    }

    private void initViewFields() {
        textAlarmRingTime = findViewById(R.id.textAlarmRingTime);
        textAlarmRingDescription = findViewById(R.id.textAlarmRingDescription);
        buttonAlarmSolvePuzzle = findViewById(R.id.buttonAlarmSolvePuzzle);
        buttonAlarmDelay = findViewById(R.id.buttonAlarmDelay);
        buttonAlarmDismiss = findViewById(R.id.buttonAlarmDismiss);
    }

    private void initTimeMonitorTask(TextView textAlarmRingTime) {
        Calendar calendar = Calendar.getInstance();
        if(DateFormat.is24HourFormat(getApplicationContext())) {
            textAlarmRingTime.setText(String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
        }
        else {
            textAlarmRingTime.setText(String.format("%02d:%02d", calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE)));
        }
        timeMonitorTask = new TimeMonitorTask(textAlarmRingTime);
        timeMonitorTask.execute();
    }

    private void initMediaPlayer(Alarm alarm) {
        if(MediaPlayerManager.mediaPlayer==null) {
            MediaPlayerManager.mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(alarm.soundURIString));
            MediaPlayerManager.alarmIdForMediaPlayer = alarm.id;
            MediaPlayerManager.mediaPlayer.start();
            MediaPlayerManager.mediaPlayer.setLooping(true);
        }
    }

    private void initVibration(Alarm alarm) {
        if(MediaPlayerManager.vibrator==null & alarm.isVibration) {
            MediaPlayerManager.vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            MediaPlayerManager.alarmIdForVibrator = alarm.id;
            if(MediaPlayerManager.vibrator.hasVibrator()) {
                long[] vibratePattern;

                SharedPreferences preferences = getSharedPreferences(Const.SH_PREFERENCES_SETTINGS_NAME, MODE_PRIVATE);
                if(preferences.contains(Const.SH_PREFERENCES_SETTINGS_KEY_ALARM_VIBRATION_PATTERN)) {
                    vibratePattern = Const.ALARM_VIBRATION_PATTERNS[preferences.getInt(Const.SH_PREFERENCES_SETTINGS_KEY_ALARM_VIBRATION_PATTERN, 0)];
                }
                else {
                    vibratePattern = Const.ALARM_VIBRATION_PATTERNS[0];
                }

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    VibrationEffect effect = VibrationEffect.createWaveform(vibratePattern, 0);
                    MediaPlayerManager.vibrator.vibrate(effect);
                }
                else {
                    MediaPlayerManager.vibrator.vibrate(vibratePattern, 0);
                }

            }
        }
    }

    private void stopMediaPlayer(Alarm alarm) {
        if(MediaPlayerManager.mediaPlayer!=null & MediaPlayerManager.alarmIdForMediaPlayer==alarm.id) {
            MediaPlayerManager.mediaPlayer.stop();
            MediaPlayerManager.mediaPlayer = null;
            MediaPlayerManager.alarmIdForMediaPlayer = -1;
        }
    }

    private void stopVibration(Alarm alarm) {
        if(MediaPlayerManager.vibrator!=null & MediaPlayerManager.alarmIdForVibrator==alarm.id) {
            MediaPlayerManager.vibrator.cancel();
            MediaPlayerManager.vibrator = null;
            MediaPlayerManager.alarmIdForVibrator = -1;
        }
    }

    private void setButtonsVisibilityByIsPuzzle(boolean isPuzzle) {
        if(isPuzzle) {
            buttonAlarmSolvePuzzle.setVisibility(View.VISIBLE);
            buttonAlarmDismiss.setVisibility(View.GONE);
            buttonAlarmDelay.setVisibility(View.GONE);
        }
        else {
            buttonAlarmSolvePuzzle.setVisibility(View.GONE);
            buttonAlarmDismiss.setVisibility(View.VISIBLE);
            buttonAlarmDelay.setVisibility(View.VISIBLE);
        }
    }

    private int setRandomBackgroundImage(ImageView imageView) {
        Random random = new Random();
        int randomImageIndex = random.nextInt(Const.ALARM_DEFAULT_BACKGROUNDS.length);
        imageView.setImageDrawable(getDrawable(Const.ALARM_DEFAULT_BACKGROUNDS[randomImageIndex]));
        return Const.ALARM_DEFAULT_BACKGROUNDS[randomImageIndex];
    }

    class TimeMonitorTask extends AsyncTask<Void, Calendar, Void> {
        private boolean running = true;
        private TextView textAlarmRingTime;

        public TimeMonitorTask(TextView textAlarmRingTime) {
            this.textAlarmRingTime = textAlarmRingTime;
        }

        @Override
        protected Void doInBackground(Void ...voids) {
            while(running) {
                try {
                    Thread.sleep(1000*60);
                } catch (InterruptedException e) {

                }
                publishProgress(Calendar.getInstance());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Calendar... values) {
            Calendar calendar = values[0];
            if(DateFormat.is24HourFormat(getApplicationContext())) {
                textAlarmRingTime.setText(String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
            }
            else {
                textAlarmRingTime.setText(String.format("%02d:%02d", calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE)));
            }
        }

        public void finishThread() {
            running = false;
        }
    }


}