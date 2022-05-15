package com.terabyte.clock001;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        String[] VIBRATION_PATTERN_NAMES = {getString(R.string.alarm_vibration_pattern_name1), getString(R.string.alarm_vibration_pattern_name2), getString(R.string.alarm_vibration_pattern_name3)};

        Spinner spinnerAlarmVibrationPattern = view.findViewById(R.id.spinnerAlarmVibrationPattern);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, VIBRATION_PATTERN_NAMES);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAlarmVibrationPattern.setAdapter(adapter);

        SharedPreferences preferences = getContext().getSharedPreferences(Const.SH_PREFERENCES_SETTINGS_NAME, Context.MODE_PRIVATE);
        if(preferences.contains(Const.SH_PREFERENCES_SETTINGS_KEY_ALARM_VIBRATION_PATTERN)) {
            spinnerAlarmVibrationPattern.setSelection(preferences.getInt(Const.SH_PREFERENCES_SETTINGS_KEY_ALARM_VIBRATION_PATTERN, 0));
        }
        else {
            spinnerAlarmVibrationPattern.setSelection(0);
        }


        spinnerAlarmVibrationPattern.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedString = (String) adapterView.getItemAtPosition(position);
                SharedPreferences.Editor editor = getContext().getSharedPreferences(Const.SH_PREFERENCES_SETTINGS_NAME, Context.MODE_PRIVATE).edit();
                for(int i = 0; i<VIBRATION_PATTERN_NAMES.length;i++) {
                    if(VIBRATION_PATTERN_NAMES[i].equals(selectedString)) {
                        editor.putInt(Const.SH_PREFERENCES_SETTINGS_KEY_ALARM_VIBRATION_PATTERN, i);
                        break;
                    }
                }
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                SharedPreferences.Editor editor = getContext().getSharedPreferences(Const.SH_PREFERENCES_SETTINGS_NAME, Context.MODE_PRIVATE).edit();
                editor.putInt(Const.SH_PREFERENCES_SETTINGS_KEY_ALARM_VIBRATION_PATTERN, 0);
                editor.commit();
            }
        });

        return view;
    }
}