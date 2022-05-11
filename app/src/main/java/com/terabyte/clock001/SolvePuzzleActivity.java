package com.terabyte.clock001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class SolvePuzzleActivity extends AppCompatActivity {
    private static final String INTENT_KEY_ANSWER = "intentKeyAnswer";
    private static final String INTENT_KEY_PUZZLE = "intentKeyPuzzle";

    private int answer;
    private String puzzle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solve_puzzle);

        getSupportActionBar().hide();

        TextView textSolvePuzzlePuzzle = findViewById(R.id.textSolvePuzzlePuzzle);
        TextView textSolvePuzzleInstruction = findViewById(R.id.textSolvePuzzleInstruction);
        EditText editSolvePuzzleAnswer = findViewById(R.id.editSolvePuzzleAnswer);
        Button buttonSolvePuzzleSendAnswer = findViewById(R.id.buttonSolvePuzzleSendAnswer);

        long alarmId = getIntent().getExtras().getLong(Const.INTENT_KEY_ALARM_ID);
        int hardcoreLevel = getIntent().getExtras().getInt(Const.INTENT_KEY_HARDCORE_LEVEL);

        if(hardcoreLevel== 0 | hardcoreLevel==1) {
            editSolvePuzzleAnswer.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
        }
        else {
            editSolvePuzzleAnswer.setInputType(InputType.TYPE_CLASS_TEXT);
        }

        if(savedInstanceState==null) {
            Pair<String, Integer> pair = AlarmPuzzleGenerator.createRandomPuzzle(hardcoreLevel);
            puzzle = pair.first;
            answer = pair.second;
        }
        else {
            puzzle = savedInstanceState.getString(INTENT_KEY_PUZZLE);
            answer = savedInstanceState.getInt(INTENT_KEY_ANSWER);
        }


        textSolvePuzzlePuzzle.setText(puzzle);

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


        buttonSolvePuzzleSendAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int userAnswer = Integer.parseInt(editSolvePuzzleAnswer.getText().toString());
                    if(userAnswer == answer) {
                        //here we turn off mediaPlayer and vibration
                        stopMediaPlayer(alarmId);
                        stopVibration(alarmId);
                        finish();
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

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(INTENT_KEY_PUZZLE, puzzle);
        outState.putInt(INTENT_KEY_ANSWER, answer);
        super.onSaveInstanceState(outState);
    }

    private void showSnackIncorrectAnswer(View view) {
        Snackbar.make(view, getResources().getString(R.string.incorrect_answer), Snackbar.LENGTH_LONG).show();
    }

    private void stopMediaPlayer(long alarmId) {
        if(MediaPlayerManager.mediaPlayer!=null & MediaPlayerManager.alarmIdForMediaPlayer==alarmId) {
            MediaPlayerManager.mediaPlayer.stop();
            MediaPlayerManager.mediaPlayer = null;
            MediaPlayerManager.alarmIdForMediaPlayer = -1;
        }
    }

    private void stopVibration(long alarmId) {
        if(MediaPlayerManager.vibrator!=null & MediaPlayerManager.alarmIdForVibrator==alarmId) {
            MediaPlayerManager.vibrator.cancel();
            MediaPlayerManager.vibrator = null;
            MediaPlayerManager.alarmIdForVibrator = -1;
        }
    }
}