package com.terabyte.clock001;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.WorkInfo;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.List;


public class AlarmFragment extends Fragment {

    public AlarmFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        if(AlarmDatabaseManager.getAlarmList().size()==0) {
            view = inflater.inflate(R.layout.fragment_alarm_no_alarms, container, false);
        }
        else {
            view = inflater.inflate(R.layout.fragment_alarm, container, false);

            RecyclerView recyclerAlarms = view.findViewById(R.id.recyclerAlarms);
            fillRecyclerAlarms(recyclerAlarms);
        }





        FloatingActionButton buttonAddAlarm = view.findViewById(R.id.buttonAddAlarm);
        buttonAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View buttonAddAlarm) {

                TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        //ternary operator
                        Alarm alarm = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ?
                                new Alarm(timePicker.getHour(), timePicker.getMinute(), "", true, false, true, false) :
                                new Alarm(timePicker.getCurrentHour(), timePicker.getCurrentMinute(), "", true, false, true, false);

                        boolean willWeRestartFragmentToUpdateLayout = AlarmDatabaseManager.getAlarmList().size()==0;

                        AlarmDatabaseManager.createAlarm(getContext(), alarm, new PostExecuteCode() {
                            @Override
                            public void doInPostExecuteWhenWeGotIdOfCreatedAlarm(Long createdAlarmId) {
                                //here we start work manager and fill recyclerView again
                                alarm.id = createdAlarmId;
                                AlarmDatabaseManager.updateAlarmList(alarm);

                                AlarmManagerLauncher.startTask(getContext(), alarm.id, alarm.hour, alarm.minute);

                                if(willWeRestartFragmentToUpdateLayout) {
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    AlarmFragment fragment = new AlarmFragment();
                                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                                    transaction.replace(R.id.frameLayoutForFragments, fragment).commit();
                                }
                                else {
                                    RecyclerView recyclerAlarms = view.findViewById(R.id.recyclerAlarms);
                                    fillRecyclerAlarms(recyclerAlarms);
                                }
                            }
                        });
                    }
                };

                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), listener, Calendar.getInstance().get(Calendar.HOUR), Calendar.getInstance().get(Calendar.MINUTE), DateFormat.is24HourFormat(getContext()));
                timePickerDialog.show();
            }
        });

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void fillRecyclerAlarms(RecyclerView recyclerAlarms) {
        AlarmAdapter adapter = new AlarmAdapter(getContext(), this, AlarmDatabaseManager.getAlarmList());
        recyclerAlarms.setAdapter(adapter);
        recyclerAlarms.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}