package com.terabyte.clock001;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class StopwatchFragment extends Fragment {
    private static boolean isPaused;
    private static long pauseElapsedTime;

    private Button buttonStart, buttonStop, buttonInterval, buttonResume, buttonCancel;
    private TextView textTime, textTimeInterval;
    private RecyclerView recyclerIntervals;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stopwatch_sleep, container, false);

        if(StopwatchService.isRunning) {
            view = inflater.inflate(R.layout.fragment_stopwatch_run, container, false);
            initRunLayout(view);
            StopwatchService.setTextTime(textTime);
            textTime.setText(StopwatchService.getTextTimeForUI(StopwatchService.getElapsedTime()));

            buttonInterval.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            buttonStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isPaused = true;
                    pauseElapsedTime = StopwatchService.getElapsedTime();
                    getContext().stopService(new Intent(getContext(), StopwatchService.class));
                    recreateFragment();
                }
            });

        }
        if(isPaused) {
            view = inflater.inflate(R.layout.fragment_stopwatch_pause, container, false);
            initPauseLayout(view);
            textTime.setText(StopwatchService.getTextTimeForUI(pauseElapsedTime));
            StopwatchService.setTextTime(null);

            buttonResume.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), StopwatchService.class);
                    intent.putExtra(Const.INTENT_KEY_STOPWATCH_ELAPSED_TIME_MILLS, pauseElapsedTime);
                    getContext().startService(intent);
                    StopwatchService.isRunning = true;
                    isPaused = false;
                    recreateFragment();
                }
            });

            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isPaused = false;
                    getContext().stopService(new Intent(getContext(), StopwatchService.class));
                    recreateFragment();
                }
            });
        }
        if(!StopwatchService.isRunning & !isPaused) {
            view = inflater.inflate(R.layout.fragment_stopwatch_sleep, container, false);
            initSleepLayout(view);
            StopwatchService.setTextTime(null);

            buttonStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), StopwatchService.class);
                    intent.putExtra(Const.INTENT_KEY_STOPWATCH_ELAPSED_TIME_MILLS, 0);
                    getContext().startService(intent);
                    StopwatchService.isRunning = true;
                    recreateFragment();
                }
            });
        }
        StopwatchService.setFragmentExists(true);
        return view;
    }

    @Override
    public void onStop() {
        StopwatchService.setFragmentExists(false);
        StopwatchService.setTextTime(null);
        super.onStop();
    }

    private void initSleepLayout(View view) {
        buttonStart = view.findViewById(R.id.buttonStopwatchStart);
    }

    private void initRunLayout(View view) {
        buttonStop = view.findViewById(R.id.buttonStopwatchStop);
        buttonInterval = view.findViewById(R.id.buttonStopwatchInterval);
        textTime = view.findViewById(R.id.textStopwatchTimeRun);
        textTimeInterval = view.findViewById(R.id.textStopwatchTimeInterval);
        recyclerIntervals = view.findViewById(R.id.recyclerStopwatchIntervals);
    }

    private void initPauseLayout(View view) {
        buttonResume = view.findViewById(R.id.buttonStopwatchResume);
        buttonCancel = view.findViewById(R.id.buttonStopwatchCancel);
        textTime = view.findViewById(R.id.textStopwatchTimePause);
        textTimeInterval = view.findViewById(R.id.textStopwatchTimeInterval);
        recyclerIntervals = view.findViewById(R.id.recyclerStopwatchIntervals);
    }

    private void recreateFragment() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        StopwatchFragment stopwatchFragment = new StopwatchFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutForFragments, stopwatchFragment).commit();
    }
}