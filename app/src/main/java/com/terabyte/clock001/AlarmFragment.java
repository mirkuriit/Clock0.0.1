package com.terabyte.clock001;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.WorkInfo;

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
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);

        RecyclerView recyclerAlarms = view.findViewById(R.id.recyclerAlarms);
        fillRecyclerAlarms(recyclerAlarms);


        FloatingActionButton buttonAddAlarm = view.findViewById(R.id.buttonAddAlarm);
        buttonAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        //ternary operator
                        Alarm alarm = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ?
                                new Alarm(timePicker.getHour(), timePicker.getMinute(), "", true, false, true, false) :
                                new Alarm(timePicker.getCurrentHour(), timePicker.getCurrentMinute(), "", true, false, true, false);

                        AlarmDatabaseManager.createAlarm(AlarmDatabaseClient.getInstance(getContext()).getAppDatabase(), alarm, new PostExecuteCode() {
                            @Override
                            public void doInPostExecuteWhenWeGotIdOfCreatedAlarm(Long createdAlarmId) {
                                //here we start work manager and fill recyclerView again
                                AlarmDatabaseManager.updateAlarmList(AlarmDatabaseClient.getInstance(getContext()).getAppDatabase(), new PostExecuteCode() {
                                    @Override
                                    public void doInPostExecute() {
                                        fillRecyclerAlarms(recyclerAlarms);
                                    }
                                });

                                AlarmWorkLauncher.startAlarmWorker(getContext(), AlarmFragment.this, createdAlarmId, alarm.hour, alarm.minute, AlarmWorkLauncher.getAlarmWorkerObserver(getContext(), createdAlarmId));
                            }
                        });
                    }
                };

                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), listener, Calendar.getInstance().get(Calendar.HOUR), Calendar.getInstance().get(Calendar.MINUTE), true);
                timePickerDialog.show();
            }
        });

        return view;
    }


    private void fillRecyclerAlarms(RecyclerView recyclerAlarms) {
        AlarmAdapter adapter = new AlarmAdapter(getContext(), this, AlarmDatabaseManager.getAlarmList());
        recyclerAlarms.setAdapter(adapter);
        recyclerAlarms.setLayoutManager(new LinearLayoutManager(getContext()));
    }
//    //parameter "noChangesAmongAlarms" is for optimization. If we're sure that there's no changes
//    //among alarms in Room database, we set true.
//    private void fillRecyclerView (RecyclerView recyclerAlarms, boolean noChangesAmongAlarms){
//        if(noChangesAmongAlarms) {
//            if(Const.allAlarms==null) {
//                AlarmDatabaseManager.getAllAlarms(AlarmDatabaseClient.getInstance(getContext()).getAppDatabase(), new PostExecuteCode() {
//                    @Override
//                    public void doInPostExecuteWhenWeGotAllAlarms(List<Alarm> alarms) {
//                        if (alarms.size() > 0) {
//                            recyclerAlarms.setAdapter(new AlarmAdapter(getContext(), AlarmFragment.this, alarms));
//                            recyclerAlarms.setLayoutManager(new LinearLayoutManager(getContext()));
//                        }
//                        else {
//                            // TODO: 30.03.2022 show text "no alarms here yet"
//                        }
//                        Const.allAlarms = alarms;
//                    }
//                });
//            }
//            else {
//                if (Const.allAlarms.size() > 0) {
//                    recyclerAlarms.setAdapter(new AlarmAdapter(getContext(), AlarmFragment.this, Const.allAlarms));
//                    recyclerAlarms.setLayoutManager(new LinearLayoutManager(getContext()));
//                }
//                else {
//                    // TODO: 30.03.2022 show text "no alarms here yet"
//                }
//            }
//        }
//        else {
//            AlarmDatabaseManager.getAllAlarms(AlarmDatabaseClient.getInstance(getContext()).getAppDatabase(), new PostExecuteCode() {
//                @Override
//                public void doInPostExecuteWhenWeGotAllAlarms(List<Alarm> alarms) {
//                    if (alarms.size() > 0) {
//                        recyclerAlarms.setAdapter(new AlarmAdapter(getContext(), AlarmFragment.this, alarms));
//                        recyclerAlarms.setLayoutManager(new LinearLayoutManager(getContext()));
//                    }
//                    else {
//                        // TODO: 30.03.2022 show text "no alarms here yet"
//                    }
//                }
//            });
//        }
//
//    }

}