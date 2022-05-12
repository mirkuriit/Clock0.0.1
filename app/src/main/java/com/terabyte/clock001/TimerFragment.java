package com.terabyte.clock001;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import java.util.Timer;


public class TimerFragment extends Fragment {
    private int MODE;

    private TextView mTextVievCountdown;
    private Button mButtonStartPause;
    private Button mButtonStartWorkingTimer;
    private Button mButtonReset;

    NumberPicker numberPickerHours;
    NumberPicker numberPickerMinutes;
    NumberPicker numberPickerSeconds;

    public TimerFragment(int MODE){
        this.MODE = MODE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TimerService.setFragmentExists(true);

        View view;
        view = inflater.inflate(R.layout.fragment_timer_sleep, container, false);

        switch(MODE) {
            case Const.MODE_SLEEP:
                view = inflater.inflate(R.layout.fragment_timer_sleep, container, false);
                initializationModeSleep(view);
                mButtonStartWorkingTimer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        long startTime = getTimeLeftMillisFromNumberPicker(numberPickerHours.getValue(), numberPickerMinutes.getValue(), numberPickerSeconds.getValue());
                        //todo переход на этот же фрагмент с передачей mTimeLeftInMillis и MODE_RUN
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        TimerFragment timerRunFragment = new TimerFragment(Const.MODE_RUN);
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frameLayoutForFragments, timerRunFragment).commit();

                        Intent intent = new Intent(getContext(), TimerService.class);
                        intent.putExtra(Const.INTENT_KEY_TIMER_LEFT_TIME_MILLS, startTime);
                        getContext().startService(intent);
                    }
                });
                TimerService.setTextTimer(null);
                break;
            case Const.MODE_RUN:
                view = inflater.inflate(R.layout.fragment_timer_run, container, false);
                initializationModeRun(view);
                TimerService.setTextTimer(mTextVievCountdown);

                if(TimerService.getRunning()) {
                    mButtonStartPause.setText("pause");
                }
                else {
                    mButtonStartPause.setText("resume");
                }

                int[] hoursMinutesSecondsArray = getHoursMinutesSecondsAsArrayFromMills(TimerService.getTimeLeft());
                mTextVievCountdown.setText(String.format("%02d:%02d:%02d", hoursMinutesSecondsArray[0], hoursMinutesSecondsArray[1], hoursMinutesSecondsArray[2]));

                mButtonStartPause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TimerService.getRunning()){
                            getContext().stopService(new Intent(getContext(), TimerService.class));
                            mButtonStartPause.setText("resume");

                        }
                        else{
                            Intent intent = new Intent(getContext(), TimerService.class);
                            intent.putExtra(Const.INTENT_KEY_TIMER_LEFT_TIME_MILLS, TimerService.getTimeLeft());
                            getContext().startService(intent);
                            mButtonStartPause.setText("pause");
                        }
                    }
                });

                mButtonReset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getContext().stopService(new Intent(getContext(), TimerService.class));

                        //todo персоздаем фрагмент с MODE_SLEEP
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        TimerFragment timerRunFragment = new TimerFragment(Const.MODE_SLEEP);
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frameLayoutForFragments, timerRunFragment).commit();
                    }
                });
                break;
        }
        return view;
    }

    @Override
    public void onStop() {
        TimerService.setFragmentExists(false);
        TimerService.setTextTimer(null);
        super.onStop();
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

    public static int[] getHoursMinutesSecondsAsArrayFromMills(long mills) {
        int[] result = new int[3];

        int inputSeconds = (int) mills/1000;
        result[0] = inputSeconds/3600;
        result[1] = (inputSeconds - result[0]*3600)/60;
        result[2] = (inputSeconds - result[0]*3600)%60;
        return result;
    }






}