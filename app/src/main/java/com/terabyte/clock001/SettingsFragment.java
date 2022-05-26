package com.terabyte.clock001;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.VideoView;

public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        SharedPreferences preferences = getContext().getSharedPreferences(Const.SH_PREFERENCES_SETTINGS_NAME, Context.MODE_PRIVATE);

        //alarm settings
        String[] VIBRATION_PATTERN_NAMES = {getString(R.string.alarm_vibration_pattern_name1), getString(R.string.alarm_vibration_pattern_name2), getString(R.string.alarm_vibration_pattern_name3)};

        Spinner spinnerAlarmVibrationPattern = view.findViewById(R.id.spinnerAlarmVibrationPattern);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, VIBRATION_PATTERN_NAMES);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAlarmVibrationPattern.setAdapter(adapter);

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

        //timer settings
        Switch switchTimerVibration = view.findViewById(R.id.switchTimerSettingsVibration);
        switchTimerVibration.setChecked(preferences.getBoolean(Const.SH_PREFERENCES_SETTINGS_KEY_TIMER_VIBRATION, false));
        switchTimerVibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                preferences.edit().putBoolean(Const.SH_PREFERENCES_SETTINGS_KEY_TIMER_VIBRATION, b).commit();
            }
        });

        Button buttonChooseTimerSound = view.findViewById(R.id.buttonTimerSettingsChooseSound);
        buttonChooseTimerSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseTimerSoundDialog dialog = new ChooseTimerSoundDialog();
                dialog.show(getActivity().getSupportFragmentManager(), "chooseTimerSoundDialog");
            }
        });

        //about application
        VideoView videoAboutApp = view.findViewById(R.id.videoAboutApp);
        videoAboutApp.setVideoURI(Uri.parse("android.resource://" + getContext().getPackageName() + "/" + R.raw.test));
        MediaController mediaController = new MediaController(getContext());
        videoAboutApp.setMediaController(mediaController);
        mediaController.setMediaPlayer(videoAboutApp);
//        ImageButton buttonPlayPauseVideo = view.findViewById(R.id.buttonPlayPauseVideo);
//        buttonPlayPauseVideo.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                if(videoAboutApp.isPlaying()) {
//                    //there is pause mode
//                    videoAboutApp.pause();
//                    buttonPlayPauseVideo.setImageResource(R.drawable.ic_video_play);
//                }
//                else {
//                    videoAboutApp.resume();
//                    buttonPlayPauseVideo.setImageResource(R.drawable.ic_video_pause);
//                }
//            }
//        });
//
//        ImageButton buttonStopVideo = view.findViewById(R.id.buttonStopVideo);
//        buttonStopVideo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                videoAboutApp.stopPlayback();
//            }
//        });

        return view;
    }
}