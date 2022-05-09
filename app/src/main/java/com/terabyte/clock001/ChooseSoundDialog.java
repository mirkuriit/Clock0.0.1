package com.terabyte.clock001;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.io.IOException;

public class ChooseSoundDialog extends DialogFragment {
    private long alarmId;
    private MediaPlayer mediaPlayer;
    private int chosenId;

    public ChooseSoundDialog(long alarmId) {
        this.alarmId = alarmId;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Объявить и инициализировать экземпляр класса AlertDialog.Builder с передачей контекста конструктору
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        // Установить заголовок диалогового окна
        alertDialogBuilder.setMessage(getString(R.string.choose_alarm_sound));
        // Эта функция делает диалоговое окно не закрываемым по нажатию на кнопку назад или вне диаологового окна
        setCancelable(false);
        // Получить экземпляр класса LayoutInflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Присвоить полученный макет локальной переменной экземпляру класса View
        final View v = inflater.inflate(R.layout.dialog_choose_sound, null, false);
        // Установить полученный макет в качестве макета диалогового окна
        alertDialogBuilder.setView(v);

        RadioGroup radioGroupAlarmSounds = v.findViewById(R.id.radioGroupAlarmSounds);

        Alarm alarm = AlarmDatabaseManager.getAlarmFromListById(alarmId);
        String defaultUriBeginning = "android.resource://com.terabyte.clock001/";
        if(alarm.soundURIString.equals(defaultUriBeginning+R.raw.egor_track)) {
            radioGroupAlarmSounds.check(R.id.radioAlarmSoundEgorTrack);
        }
        if(alarm.soundURIString.equals(defaultUriBeginning+R.raw.beep_beep)) {
            radioGroupAlarmSounds.check(R.id.radioAlarmSoundBeepBeep);
        }
        if(alarm.soundURIString.equals(defaultUriBeginning+R.raw.happy_bells)) {
            radioGroupAlarmSounds.check(R.id.radioAlarmSoundHappyBells);
        }
        if(alarm.soundURIString.equals(defaultUriBeginning+R.raw.oxygen)) {
            radioGroupAlarmSounds.check(R.id.radioAlarmSoundOxygen);
        }
        if(alarm.soundURIString.equals(defaultUriBeginning+R.raw.platinum)) {
            radioGroupAlarmSounds.check(R.id.radioAlarmSoundPlatinum);
        }
        if(alarm.soundURIString.equals(defaultUriBeginning+R.raw.walk_in_forest)) {
            radioGroupAlarmSounds.check(R.id.radioAlarmSoundWalkInForest);
        }


        int[] radioIds = {R.id.radioAlarmSoundEgorTrack, R.id.radioAlarmSoundBeepBeep, R.id.radioAlarmSoundHappyBells, R.id.radioAlarmSoundOxygen, R.id.radioAlarmSoundPlatinum,
        R.id.radioAlarmSoundSilverSmoke, R.id.radioAlarmSoundWalkInForest};
        int[] rawIds = {R.raw.egor_track, R.raw.beep_beep, R.raw.happy_bells, R.raw.oxygen, R.raw.platinum, R.raw.silver_smoke, R.raw.walk_in_forest};

        radioGroupAlarmSounds.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                //we set alarm.soundURIString field in depending of id of selected radioButton
                alarm.soundURIString = defaultUriBeginning;
                for(int i = 0; i<radioIds.length;i++) {
                    if(radioIds[i] == id) {
                        alarm.soundURIString+=rawIds[i];
                    }
                }

                if(chosenId!=0) {
                    RadioButton radioForForeach;
                    for(int radioButtonId : radioIds) {
                        if(radioButtonId!=id) {
                            radioForForeach = v.findViewById(radioButtonId);
                            radioForForeach.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                        }
                    }
                }

                RadioButton radioButton = v.findViewById(id);
                radioButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(chosenId==view.getId()) {
                            mediaPlayer.stop();
                            radioButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            chosenId = 0;
                        }
                        else {
                            playSound(alarm.soundURIString);
                            radioButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_alarm_sound_play, 0);
                            chosenId = view.getId();
                        }
                    }
                });
            }
        });


        // Устанавливает кнопку подтверждения и обработчик события клик для кнопки
        alertDialogBuilder.setPositiveButton(getString(R.string.apply), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(mediaPlayer!=null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
                dismiss();
            }
        });
        // Вернуть экземпляр диалогового окна для показа
        return alertDialogBuilder.create();
    }

    private void playSound(String uriString) {
        if(mediaPlayer!=null) {
            mediaPlayer.stop();
            mediaPlayer.release();

        }
        mediaPlayer = MediaPlayer.create(getContext(), Uri.parse(uriString));
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

}
