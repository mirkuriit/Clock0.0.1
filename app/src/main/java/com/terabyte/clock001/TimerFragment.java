package com.terabyte.clock001;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class TimerFragment extends Fragment {
    private static final long START_TIME_IN_MILLIS = 1200000;

    private int MODE;


    private TextView mTextVievCountdown;
    private Button mButtonStartPause;
    private Button mButtonStartWorkingTimer;
    private Button mButtonReset;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private static long mTimeLeftInMillis;
    private long mEndTime;

    NumberPicker numberPickerHours;
    NumberPicker numberPickerMinutes;
    NumberPicker numberPickerSeconds;

    public TimerFragment(int MODE){
        this.MODE = MODE;
    }

    public TimerFragment(int MODE, long mTimeLeftInMillis){
        this.MODE = MODE;
        this.mTimeLeftInMillis = mTimeLeftInMillis;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_timer_sleep, container, false);
        switch (MODE){
            case Const.MODE_SLEEP:
                view = inflater.inflate(R.layout.fragment_timer_sleep, container, false);
                initializationModeSleep(view);

                mButtonStartWorkingTimer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTimeLeftInMillis = getTimeLeftMillisFromNumberPicker(numberPickerHours.getValue(),
                                numberPickerMinutes.getValue(), numberPickerSeconds.getValue());
                        //todo переход на этот же фрагмент с передачей mTimeLeftInMillis и MODE_RUN
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        TimerFragment timerRunFragment = new TimerFragment(Const.MODE_RUN, mTimeLeftInMillis);
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frameLayoutForFragments, timerRunFragment).commit();

                    }
                });
                break;
            case Const.MODE_RUN:
                view = inflater.inflate(R.layout.fragment_timer_run, container, false);
                initializationModeRun(view);
                mButtonStartPause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mTimerRunning){
                            pauseTimer();
                        }else{
                            startTimer();
                        }
                    }
                });


                mButtonReset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //todo персоздаем фрагмент с MODE_SLEEP
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        TimerFragment timerRunFragment = new TimerFragment(Const.MODE_SLEEP);
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frameLayoutForFragments, timerRunFragment).commit();
                    }
                });
                startTimer();
                break;
        }
        return view;
    }

    private void initializationModeSleep(View view){
        mButtonStartWorkingTimer = view.findViewById(R.id.button_start);

        numberPickerHours = view.findViewById(R.id.numberPickerHours);
        numberPickerMinutes = view.findViewById(R.id.numberPickerMinutes);
        numberPickerSeconds = view.findViewById(R.id.numberPickerSeconds);

        numberPickerHours.setMinValue(0);
        numberPickerMinutes.setMinValue(0);
        numberPickerSeconds.setMinValue(0);
        numberPickerHours.setMaxValue(23);
        numberPickerMinutes.setMaxValue(59);
        numberPickerSeconds.setMaxValue(59);
    }

    private int getTimeLeftMillisFromNumberPicker(int hours, int minutes, int seconds){
        return hours* 3_600_000 + minutes*60_000 + seconds*1000;
    }

    private void initializationModeRun(View view){
        mButtonStartPause = view.findViewById(R.id.button_pause);
        mTextVievCountdown = view.findViewById(R.id.textRunningTimer);
        mButtonReset = view.findViewById(R.id.button_reset);
    }

    private void startTimer(){
        mButtonStartPause.setText("Pause");
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1) {
            @Override
            public void onTick(long millisUntilFinished) {
                //Осталось до конца
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
            }
        }.start();
        mTimerRunning = true;
    }

    private void pauseTimer(){
        mButtonStartPause.setText("Start");
        mCountDownTimer.cancel();
        mTimerRunning = false;
    }

    private void updateCountDownText(){
        int hours = (int) mTimeLeftInMillis / 1000 / 3600;
        int minutes = (int) mTimeLeftInMillis / 1000 / 60 % 60;
        int seconds = (int) mTimeLeftInMillis / 1000 % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d:%02d", hours, minutes, seconds);

        mTextVievCountdown.setText(timeLeftFormatted);
    }


}