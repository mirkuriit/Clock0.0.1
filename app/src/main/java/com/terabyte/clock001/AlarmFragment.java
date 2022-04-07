package com.terabyte.clock001;

import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        fillRecyclerView(recyclerAlarms);











        FloatingActionButton buttonAddAlarm = view.findViewById(R.id.buttonAddAlarm);
        buttonAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {









                TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        class AlarmTask extends AsyncTask<AlarmDatabase, Void, Void> {
                            @Override
                            protected Void doInBackground(AlarmDatabase... alarmDatabases) {
                                AlarmDatabase db = alarmDatabases[0];
                                Alarm alarm;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    alarm = new Alarm(timePicker.getHour(), timePicker.getMinute(), "", true, false, true);
                                }
                                else {
                                    alarm = new Alarm(timePicker.getCurrentHour(), timePicker.getCurrentMinute(), "", true, false, true);
                                }
                                db.alarmDao().insert(alarm);
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void unused) {
                                super.onPostExecute(unused);
                                fillRecyclerView(recyclerAlarms);
                            }
                        }
                        AlarmTask alarmTask = new AlarmTask();
                        alarmTask.execute(AlarmDatabaseClient.getInstance(getContext()).getAppDatabase());
                    }
                };








                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), listener, Calendar.getInstance().getTime().getHours(), Calendar.getInstance().getTime().getMinutes(), true);
                timePickerDialog.show();
            }
        });



















        return view;
    }

    private void fillRecyclerView(RecyclerView recyclerAlarms) {
        class DatabaseTask extends AsyncTask<AlarmDatabase, Void, List<Alarm>> {
            @Override
            protected List<Alarm> doInBackground(AlarmDatabase... alarmDatabases) {
                AlarmDatabase db = alarmDatabases[0];
                return db.alarmDao().getAll();
            }

            @Override
            protected void onPostExecute(List<Alarm> alarms) {
                super.onPostExecute(alarms);
                if(alarms.size()>0) {
                    recyclerAlarms.setAdapter(new AlarmAdapter(getContext(), alarms));
                    recyclerAlarms.setLayoutManager(new LinearLayoutManager(getContext()));
                }
                else {
                    // TODO: 30.03.2022 show text "no alarms here yet" 
                }
            }
        }
        DatabaseTask task = new DatabaseTask();
        task.execute(AlarmDatabaseClient.getInstance(getContext()).getAppDatabase());
    }
}