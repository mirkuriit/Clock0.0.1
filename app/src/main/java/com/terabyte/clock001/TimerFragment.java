package com.terabyte.clock001;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;


public class TimerFragment extends Fragment {
    private static final long START_TIME_IN_MILLIS = 1200000;

    private TextView mTextVievCountdown;
    private Button mButtonStartPause;
    private Button mButtonReset;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private long mEndTime;

    Intent intentTimerService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timer, container, false);


        mTextVievCountdown = view.findViewById(R.id.text_view_countdown);

        mButtonStartPause = view.findViewById(R.id.button_start_pause);
        mButtonReset = view.findViewById(R.id.button_reset);


//        intentTimerService = new Intent(TimerFragment.this,
//                TimerService.class);


        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTimerRunning){
                    pauseTimer();
                } else{
                    startTimer();
                }
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }
        });

        updateCountDownText();

        return view;
    }

    private void startTimer(){
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //Осталось до конца
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                updateButtons();

            }
        }.start();

        mTimerRunning = true;
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            getContext().startForegroundService(intentTimerService);
//            Toast.makeText(getContext(),
//                    "Работа начата", Toast.LENGTH_SHORT).show();
//        }
//        Log.d("about", "Сервис не заработал");

        updateButtons();
    }

    private void pauseTimer(){
        mCountDownTimer.cancel();
        mTimerRunning = false;

//        Toast.makeText(getContext(),
//                "Работа закончена", Toast.LENGTH_SHORT).show();
//        stopService(intentTimerService);

        updateButtons();
    }

    private void resetTimer(){
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
        updateButtons();

    }

    private void updateCountDownText(){
        int minutes = (int) mTimeLeftInMillis /1000 / 60;
        int seconds = (int) mTimeLeftInMillis /1000 % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);

        mTextVievCountdown.setText(timeLeftFormatted);
    }

    private void updateButtons(){
        if (mTimerRunning){
            mButtonReset.setVisibility(View.INVISIBLE);
            mButtonStartPause.setText("Pause");
        }else{
            mButtonStartPause.setText("Start");
            if (mTimeLeftInMillis < 1000){
                mButtonReset.setVisibility(View.INVISIBLE);
            }else{
                mButtonReset.setVisibility(View.VISIBLE);
            }

            if (mTimeLeftInMillis < START_TIME_IN_MILLIS){
                mButtonReset.setVisibility(View.VISIBLE);
            }else{
                mButtonReset.setVisibility(View.INVISIBLE);
            }

        }
    }

    //При перевороте телефона сохраняем значение переменных, чтоьы приложение продолжило корректно работать
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("millisLeft", mTimeLeftInMillis);
        outState.putLong("endTime",  mEndTime);
        outState.putBoolean("timerRunning", mTimerRunning);
    }

//    @Override
//    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        mTimeLeftInMillis = savedInstanceState.getLong("millisLeft");
//        mTimerRunning = savedInstanceState.getBoolean("timerRunning");
//        updateCountDownText();
//        updateButtons();
//
//        if (mTimerRunning){
//            mEndTime = savedInstanceState.getLong("endTime");
//            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();
//            startTimer();
//        }
//
//    }
}