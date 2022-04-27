package com.terabyte.clock001;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Arrays;

public class ChoosePuzzleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_puzzle);

        getSupportActionBar().setTitle(R.string.choose_puzzle_type);

        setUIBehaviourOfTextDescriptions();

        Button buttonApplyAlarmPuzzle = findViewById(R.id.buttonApplyAlarmPuzzle);
        buttonApplyAlarmPuzzle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });


        long alarmId = getIntent().getExtras().getLong(Const.INTENT_KEY_ALARM_ID);
        AlarmDatabaseManager.getAlarmPuzzleByParentAlarmId(AlarmDatabaseClient.getInstance(getApplicationContext()).getAppDatabase(), alarmId, new PostExecuteCode() {
            @Override
            public void doInPostExecuteWhenWeGotAlarmPuzzle(AlarmPuzzle alarmPuzzle) {
                RadioGroup radioGroupAlarmPuzzles = findViewById(R.id.radioGroupAlarmPuzzles);

                int[] radioIds = {R.id.radioFirstAlarmPuzzleDifficulty, R.id.radioSecondAlarmPuzzleDifficulty, R.id.radioThirdAlarmPuzzleDifficulty, R.id.radioFourthAlarmPuzzleDifficulty};
                int[] textIds = {R.id.textFirstAlarmPuzzleDifficulty, R.id.textSecondAlarmPuzzleDifficulty, R.id.textThirdAlarmPuzzleDifficulty, R.id.textFourthAlarmPuzzleDifficulty};

                radioGroupAlarmPuzzles.check(radioIds[alarmPuzzle.hardcoreLevel]);
                findViewById(textIds[alarmPuzzle.hardcoreLevel]).setVisibility(View.VISIBLE);

                radioGroupAlarmPuzzles.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int id) {
                        int hardcoreLevel = indexOf(id, radioIds);

                        for(int textId : textIds) {
                            if(textId == textIds[hardcoreLevel]) {
                                findViewById(textId).setVisibility(View.VISIBLE);
                            }
                            else {
                                findViewById(textId).setVisibility(View.GONE);
                            }
                        }

                        alarmPuzzle.hardcoreLevel = hardcoreLevel;
                        AlarmDatabaseManager.updateAlarmPuzzle(AlarmDatabaseClient.getInstance(getApplicationContext()).getAppDatabase(), alarmPuzzle);
                    }
                });
            }
        });












    }


    private void setUIBehaviourOfTextDescriptions() {
        TextView textFirstAlarmPuzzleDifficulty = findViewById(R.id.textFirstAlarmPuzzleDifficulty);
        TextView textSecondAlarmPuzzleDifficulty = findViewById(R.id.textSecondAlarmPuzzleDifficulty);
        TextView textThirdAlarmPuzzleDifficulty = findViewById(R.id.textThirdAlarmPuzzleDifficulty);
        TextView textFourthAlarmPuzzleDifficulty = findViewById(R.id.textFourthAlarmPuzzleDifficulty);
        textFirstAlarmPuzzleDifficulty.setVisibility(View.GONE);
        textSecondAlarmPuzzleDifficulty.setVisibility(View.GONE);
        textThirdAlarmPuzzleDifficulty.setVisibility(View.GONE);
        textFourthAlarmPuzzleDifficulty.setVisibility(View.GONE);
    }

    private int indexOf(int element, int[] array) {
        for(int i = 0; i<array.length; i++) {
            if(array[i]==element) {
                return i;
            }
        }
        return -1;
    }
}