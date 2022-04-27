package com.terabyte.clock001;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AlarmRingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ring);
        long alarmId = getIntent().getExtras().getLong(Const.INTENT_KEY_ALARM_ID);

        TextView textAlarmRingTime = findViewById(R.id.textAlarmRingTime);
        TextView textAlarmRingDescription = findViewById(R.id.textAlarmRingDescription);

        AlarmDatabaseManager.getAlarmById(AlarmDatabaseClient.getInstance(getApplicationContext()).getAppDatabase(), alarmId, new PostExecuteCode() {
            @Override
            public void doInPostExecuteWhenWeGotAlarm(Alarm alarm) {
                textAlarmRingTime.setText(String.format("%02d:%02d", alarm.hour, alarm.minute));
                textAlarmRingDescription.setText(alarm.description);

                if(MediaPlayerManager.mediaPlayer==null) {
                    MediaPlayerManager.mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.test_sound);
                    MediaPlayerManager.alarmIdForMediaPlayer = alarm.id;
                    MediaPlayerManager.mediaPlayer.start();
                    MediaPlayerManager.mediaPlayer.setLooping(true);
                }

                Button buttonAlarmSolvePuzzle = findViewById(R.id.buttonAlarmSolvePuzzle);
                Button buttonAlarmDelay = findViewById(R.id.buttonAlarmDelay);
                Button buttonAlarmDismiss = findViewById(R.id.buttonAlarmDismiss);

                if(alarm.isPuzzle) {
                    buttonAlarmSolvePuzzle.setVisibility(View.VISIBLE);
                    buttonAlarmDismiss.setVisibility(View.GONE);
                    buttonAlarmDelay.setVisibility(View.GONE);

                    AlarmDatabaseManager.getAlarmPuzzleByParentAlarmId(AlarmDatabaseClient.getInstance(getApplicationContext()).getAppDatabase(), alarm.id, new PostExecuteCode() {
                        @Override
                        public void doInPostExecuteWhenWeGotAlarmPuzzle(AlarmPuzzle alarmPuzzle) {
                            buttonAlarmSolvePuzzle.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getApplicationContext(), SolvePuzzleActivity.class);
                                    intent.putExtra(Const.INTENT_KEY_HARDCORE_LEVEL, alarmPuzzle.hardcoreLevel);
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
                            
                            if(alarm.isRepeat) {
                                // TODO: 26.04.2022 here we select alarmRepeating and turn on alarmWorker again
                                AlarmDatabaseManager.getAlarmRepeatingByParentAlarmId(AlarmDatabaseClient.getInstance(getApplicationContext()).getAppDatabase(), alarm.id, new PostExecuteCode() {
                                    @Override
                                    public void doInPostExecuteWhenWeGotAlarmRepeating(AlarmRepeating alarmRepeating) {
                                        AlarmWorkLauncher.startAlarmWorker(getApplicationContext(), AlarmRingActivity.this, alarm.id, alarm.hour, alarm.minute, alarmRepeating.getArrayOfBooleanDays(), AlarmWorkLauncher.getAlarmWorkerObserver(getApplicationContext(), alarm.id));
                                    }
                                });

                            }
                            else {
                                alarm.isEnabled = false;
                                AlarmDatabaseManager.updateAlarm(AlarmDatabaseClient.getInstance(getApplicationContext()).getAppDatabase(), alarm);
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
                            // TODO: 27.04.2022 here we turn off alarm but we launch alarmWorker to five minutes + 
                        }
                    });
                }

            }
        });
    }
}