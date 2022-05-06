package com.terabyte.clock001;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class SolvePuzzleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solve_puzzle);

        getSupportActionBar().hide();

        TextView textSolvePuzzlePuzzle = findViewById(R.id.textSolvePuzzlePuzzle);
        TextView textSolvePuzzleInstruction = findViewById(R.id.textSolvePuzzleInstruction);
        EditText editSolvePuzzleAnswer = findViewById(R.id.editSolvePuzzleAnswer);

        long alarmId = getIntent().getExtras().getLong(Const.INTENT_KEY_ALARM_ID);
        int hardcoreLevel = getIntent().getExtras().getInt(Const.INTENT_KEY_HARDCORE_LEVEL);


        Pair<String, Integer> pair = AlarmPuzzleGenerator.createRandomPuzzle(hardcoreLevel);

        textSolvePuzzlePuzzle.setText(pair.first);

        switch(hardcoreLevel) {
            case 0:
            case 1:
                textSolvePuzzleInstruction.setText(getResources().getString(R.string.difficulty_instruction1));
                break;
            case 2:
            case 3:
                textSolvePuzzleInstruction.setText(getResources().getString(R.string.difficulty_instruction2));
                break;
        }

        Button buttonSolvePuzzleSendAnswer = findViewById(R.id.buttonSolvePuzzleSendAnswer);
        buttonSolvePuzzleSendAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int answer = Integer.parseInt(editSolvePuzzleAnswer.getText().toString());
                    if(answer == pair.second) {
                        // TODO: 30.04.2022 here we turn off alarm and look if there's any repeating
                        if(MediaPlayerManager.mediaPlayer!=null & MediaPlayerManager.alarmIdForMediaPlayer==alarmId) {
                            MediaPlayerManager.mediaPlayer.stop();
                            MediaPlayerManager.mediaPlayer = null;
                            MediaPlayerManager.alarmIdForMediaPlayer = -1;
                        }

                        if(MediaPlayerManager.vibrator!=null & MediaPlayerManager.alarmIdForVibrator==alarmId) {
                            MediaPlayerManager.vibrator.cancel();
                            MediaPlayerManager.vibrator = null;
                            MediaPlayerManager.alarmIdForVibrator = -1;
                        }

                        AlarmDatabaseManager.getAlarmById(getApplicationContext(), alarmId, new PostExecuteCode() {
                            @Override
                            public void doInPostExecuteWhenWeGotAlarm(Alarm alarm) {
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

                    }
                    else {
                        showSnackIncorrectAnswer(view);
                    }
                }
                catch (NumberFormatException e) {
                    showSnackIncorrectAnswer(view);
                }

            }
        });
    }

    private void showSnackIncorrectAnswer(View view) {
        Snackbar.make(view, getResources().getString(R.string.incorrect_answer), Snackbar.LENGTH_LONG).show();
    }
}