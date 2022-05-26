package com.terabyte.clock001;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import androidx.fragment.app.DialogFragment;

public class ChooseTimerSoundDialog extends DialogFragment {
    private static MediaPlayer mediaPlayer;
    private static int chosenId;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Объявить и инициализировать экземпляр класса AlertDialog.Builder с передачей контекста конструктору
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        // Установить заголовок диалогового окна
        alertDialogBuilder.setMessage(getString(R.string.choose_timer_ringtone));
        // Эта функция делает диалоговое окно не закрываемым по нажатию на кнопку назад или вне диаологового окна
        setCancelable(false);
        // Получить экземпляр класса LayoutInflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Присвоить полученный макет локальной переменной экземпляру класса View
        final View v = inflater.inflate(R.layout.dialog_choose_timer_sound, null, false);
        // Установить полученный макет в качестве макета диалогового окна
        alertDialogBuilder.setView(v);

        RadioGroup radioGroupTimerSounds = v.findViewById(R.id.radioGroupTimerSounds);

        SharedPreferences shPreferences = getActivity().getSharedPreferences(Const.SH_PREFERENCES_SETTINGS_NAME, Context.MODE_PRIVATE);

        if(!shPreferences.contains(Const.SH_PREFERENCES_SETTINGS_KEY_TIMER_SOUND_RAW_RESOURCE)) {
            shPreferences.edit().putInt(Const.SH_PREFERENCES_SETTINGS_KEY_TIMER_SOUND_RAW_RESOURCE, R.raw.beep_beep);
        }
        int selectedRaw = shPreferences.getInt(Const.SH_PREFERENCES_SETTINGS_KEY_TIMER_SOUND_RAW_RESOURCE, R.raw.beep_beep);


        int[] radioIds = {R.id.radioTimerSoundBeepBeep, R.id.radioTimerSoundTimeUp, R.id.radioTimerSoundMavrodiCongratulation, R.id.radioTimerSoundFloodingBalls, R.id.radioTimerSoundGlassAndWater};
        int[] rawIds = {R.raw.beep_beep, R.raw.time_up, R.raw.sergey_mavrodi_congratulation, R.raw.flooding_balls, R.raw.glass_and_water};

        radioGroupTimerSounds.check(radioIds[getIndexOfElementInArray(rawIds, selectedRaw)]);

        //here if we changed orientation of screen, we need to create icon of playing sound for selected id
        if (chosenId!=0) {
            RadioButton radioPlayingSound = v.findViewById(radioGroupTimerSounds.getCheckedRadioButtonId());
            radioPlayingSound.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_alarm_sound_play, 0);
            radioPlayingSound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(chosenId==view.getId()) {
                        mediaPlayer.stop();
                        radioPlayingSound.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        chosenId = 0;
                    }
                    else {
                        playSound(shPreferences.getInt(Const.SH_PREFERENCES_SETTINGS_KEY_TIMER_SOUND_RAW_RESOURCE, R.raw.beep_beep));
                        radioPlayingSound.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_alarm_sound_play, 0);
                        chosenId = view.getId();
                    }
                }
            });
        }


        radioGroupTimerSounds.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                shPreferences.edit().putInt(Const.SH_PREFERENCES_SETTINGS_KEY_TIMER_SOUND_RAW_RESOURCE, rawIds[getIndexOfElementInArray(radioIds, id)]).commit();

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
                            playSound(shPreferences.getInt(Const.SH_PREFERENCES_SETTINGS_KEY_TIMER_SOUND_RAW_RESOURCE, R.raw.beep_beep));
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
                mediaPlayer = null;
                chosenId = 0;
                dismiss();
            }
        });
        // Вернуть экземпляр диалогового окна для показа
        return alertDialogBuilder.create();
    }

    private void playSound(@RawRes int musicResource) {
        if(mediaPlayer!=null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(getContext(), musicResource);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    private int getIndexOfElementInArray(int[] array, int element) {
        for(int i = 0; i<array.length;i++) {
            if(array[i] == element) {
                return i;
            }
        }
        return -1;
    }
}
