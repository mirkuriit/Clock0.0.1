package com.terabyte.clock001;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SolvePuzzleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solve_puzzle);

        int hardcoreLevel = getIntent().getExtras().getInt(Const.INTENT_KEY_HARDCORE_LEVEL);

        Button buttonSolvePuzzleSendAnswer = findViewById(R.id.buttonSolvePuzzleSendAnswer);
        buttonSolvePuzzleSendAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}