package com.terabyte.clock001;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;

public class AlarmRingActivity extends AppCompatActivity {
    TimeMonitorTask timeMonitorTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ring);

        getSupportActionBar().hide();

        long alarmId = getIntent().getExtras().getLong(Const.INTENT_KEY_ALARM_ID);

        TextView textAlarmRingTime = findViewById(R.id.textAlarmRingTime);
        TextView textAlarmRingDescription = findViewById(R.id.textAlarmRingDescription);

        AlarmDatabaseManager.getAlarmById(getApplicationContext(), alarmId, new PostExecuteCode() {
            @Override
            public void doInPostExecuteWhenWeGotAlarm(Alarm alarm) {

                Calendar calendar = Calendar.getInstance();
                if(DateFormat.is24HourFormat(getApplicationContext())) {
                    textAlarmRingTime.setText(String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
                }
                else {
                    textAlarmRingTime.setText(String.format("%02d:%02d", calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE)));
                }
                timeMonitorTask = new TimeMonitorTask(textAlarmRingTime);
                timeMonitorTask.execute();

                textAlarmRingDescription.setText(alarm.description);

                if(MediaPlayerManager.mediaPlayer==null) {
                    MediaPlayerManager.mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.test_sound);
                    MediaPlayerManager.alarmIdForMediaPlayer = alarm.id;
                    MediaPlayerManager.mediaPlayer.start();
                    MediaPlayerManager.mediaPlayer.setLooping(true);
                }

                if(MediaPlayerManager.vibrator==null & alarm.isVibration) {
                    MediaPlayerManager.vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    MediaPlayerManager.alarmIdForVibrator = alarm.id;
                    if(MediaPlayerManager.vibrator.hasVibrator()) {
                        long[] vibratePattern = {0,1500, 1000, 1500};
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            VibrationEffect effect = VibrationEffect.createWaveform(vibratePattern, 0);
                            MediaPlayerManager.vibrator.vibrate(effect);
                        }
                        else {
                            MediaPlayerManager.vibrator.vibrate(vibratePattern, 0);
                        }

                    }
                }

                Button buttonAlarmSolvePuzzle = findViewById(R.id.buttonAlarmSolvePuzzle);
                Button buttonAlarmDelay = findViewById(R.id.buttonAlarmDelay);
                Button buttonAlarmDismiss = findViewById(R.id.buttonAlarmDismiss);

                if(alarm.isPuzzle) {
                    buttonAlarmSolvePuzzle.setVisibility(View.VISIBLE);
                    buttonAlarmDismiss.setVisibility(View.GONE);
                    buttonAlarmDelay.setVisibility(View.GONE);

                    AlarmDatabaseManager.getAlarmPuzzleByParentAlarmId(getApplicationContext(), alarm.id, new PostExecuteCode() {
                        @Override
                        public void doInPostExecuteWhenWeGotAlarmPuzzle(AlarmPuzzle alarmPuzzle) {
                            buttonAlarmSolvePuzzle.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getApplicationContext(), SolvePuzzleActivity.class);
                                    intent.putExtra(Const.INTENT_KEY_HARDCORE_LEVEL, alarmPuzzle.hardcoreLevel);
                                    intent.putExtra(Const.INTENT_KEY_ALARM_ID, alarm.id);
                                    startActivity(intent);
                                }
                            });
                        }
                    });

                }
                else {
                    buttonAlarmSolvePuzzle.setVisibility(View.GONE);
                    buttonAlarmDismiss.setVisibility(View.VISIBLE);
                    buttonAlarmDelay.setVisibility(View.VISIBLE);

                    buttonAlarmDismiss.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(MediaPlayerManager.mediaPlayer!=null & MediaPlayerManager.alarmIdForMediaPlayer==alarm.id) {
                                MediaPlayerManager.mediaPlayer.stop();
                                MediaPlayerManager.mediaPlayer = null;
                                MediaPlayerManager.alarmIdForMediaPlayer = -1;
                            }

                            if(MediaPlayerManager.vibrator!=null & MediaPlayerManager.alarmIdForVibrator==alarm.id) {
                                MediaPlayerManager.vibrator.cancel();
                                MediaPlayerManager.vibrator = null;
                                MediaPlayerManager.alarmIdForVibrator = -1;
                            }

                            if(alarm.isRepeat) {
                                // TODO: 26.04.2022 here we select alarmRepeating and turn on alarmWorker again
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
                            
                            finish();
                        }
                    });
                    buttonAlarmDelay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(MediaPlayerManager.mediaPlayer!=null & MediaPlayerManager.alarmIdForMediaPlayer==alarm.id) {
                                MediaPlayerManager.mediaPlayer.stop();
                                MediaPlayerManager.mediaPlayer = null;
                                MediaPlayerManager.alarmIdForMediaPlayer = -1;
                            }

                            if(MediaPlayerManager.vibrator!=null & MediaPlayerManager.alarmIdForVibrator==alarm.id) {
                                MediaPlayerManager.vibrator.cancel();
                                MediaPlayerManager.vibrator = null;
                                MediaPlayerManager.alarmIdForVibrator = -1;
                            }

                            // TODO: 27.04.2022 here we turn off alarm but we launch alarmWorker to five minutes +
                            AlarmManagerLauncher.startTask(getApplicationContext(), alarm.id);
                            alarm.isEnabled = false;


                            AlarmDatabaseManager.updateAlarm(getApplicationContext(), alarm);
                            finish();
                        }
                    });
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

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