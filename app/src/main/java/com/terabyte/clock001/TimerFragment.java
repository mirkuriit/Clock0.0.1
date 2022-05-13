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
    private static boolean isPaused;
    private static long pauseMills;

    private Button buttonTimerStart, buttonTimerPause, buttonTimerResume, buttonTimerCancel;
    private TextView textTimerRun, textTimerPause;

    NumberPicker numberPickerHours;
    NumberPicker numberPickerMinutes;
    NumberPicker numberPickerSeconds;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_timer_sleep, container, false);

        if(TimerService.isRunning) {
            //timer is in run state
            view = inflater.inflate(R.layout.fragment_timer_run, container, false);
            initializationModeRun(view);

            TimerService.setTextTimer(textTimerRun);

            int[] hoursMinutesSecondsArray = getHoursMinutesSecondsAsArrayFromMills(TimerService.getTimeLeft());
            textTimerRun.setText(String.format("%02d:%02d:%02d", hoursMinutesSecondsArray[0], hoursMinutesSecondsArray[1], hoursMinutesSecondsArray[2]));

            buttonTimerPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pauseMills = TimerService.getTimeLeft();
                    isPaused = true;
                    getContext().stopService(new Intent(getContext(), TimerService.class));
                    recreateFragment();
                }
            });

            buttonTimerCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getContext().stopService(new Intent(getContext(), TimerService.class));
                    TimerService.isRunning = false;
                    recreateFragment();
                }
            });
        }

        if(isPaused) {
            view = inflater.inflate(R.layout.fragment_timer_pause, container, false);
            initializationModePause(view);
            //timer is in pause state
            int[] hoursMinutesSecondsArray = getHoursMinutesSecondsAsArrayFromMills(pauseMills);
            textTimerPause.setText(String.format("%02d:%02d:%02d", hoursMinutesSecondsArray[0], hoursMinutesSecondsArray[1], hoursMinutesSecondsArray[2]));
            TimerService.setTextTimer(null);

            buttonTimerResume.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), TimerService.class);
                    intent.putExtra(Const.INTENT_KEY_TIMER_LEFT_TIME_MILLS, pauseMills);
                    getContext().startService(intent);
                    TimerService.isRunning = true;
                    isPaused = false;
                    recreateFragment();
                }
            });

            buttonTimerCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isPaused = false;
                    getContext().stopService(new Intent(getContext(), TimerService.class));
                    recreateFragment();
                }
            });
        }

        if(!TimerService.isRunning & !isPaused) {
            //timer in sleep state
            view = inflater.inflate(R.layout.fragment_timer_sleep, container, false);
            initializationModeSleep(view);
            buttonTimerStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    long startTime = getTimeLeftMillisFromNumberPicker(numberPickerHours.getValue(), numberPickerMinutes.getValue(), numberPickerSeconds.getValue());
                    Intent intent = new Intent(getContext(), TimerService.class);
                    intent.putExtra(Const.INTENT_KEY_TIMER_LEFT_TIME_MILLS, startTime);
                    getContext().startService(intent);
                    TimerService.isRunning = true;
                    recreateFragment();
                }
            });
            TimerService.setTextTimer(null);
        }

        TimerService.setFragmentExists(true);
        return view;
    }



    @Override
    public void onStop() {
        TimerService.setFragmentExists(false);
        TimerService.setTextTimer(null);
        super.onStop();
    }

    private int getTimeLeftMillisFromNumberPicker(int hours, int minutes, int seconds){
        return hours* 3_600_000 + minutes*60_000 + seconds*1000;
    }

    private void initializationModeSleep(View view){
        buttonTimerStart = view.findViewById(R.id.buttonTimerStart);

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

    private void initializationModeRun(View view){
        buttonTimerPause = view.findViewById(R.id.buttonTimerPause);
        textTimerRun = view.findViewById(R.id.textTimerRun);
        buttonTimerCancel = view.findViewById(R.id.buttonTimerCancel);
    }

    private void initializationModePause(View view) {
        buttonTimerResume = view.findViewById(R.id.buttonTimerResume);
        buttonTimerCancel = view.findViewById(R.id.buttonTimerCancel);
        textTimerPause = view.findViewById(R.id.textTimerPause);
    }

    public static int[] getHoursMinutesSecondsAsArrayFromMills(long mills) {
        int[] result = new int[3];

        int inputSeconds = (int) mills/1000;
        result[0] = inputSeconds/3600;
        result[1] = (inputSeconds - result[0]*3600)/60;
        result[2] = (inputSeconds - result[0]*3600)%60;
        return result;
    }

    public static boolean getPaused() {
        return isPaused;
    }

    public static long getPauseMills() {
        return pauseMills;
    }

    private void recreateFragment() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        TimerFragment timerRunFragment = new TimerFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutForFragments, timerRunFragment).commit();
    }
}