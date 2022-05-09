package com.terabyte.clock001;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ChoosePuzzleDialog extends DialogFragment {
    private long alarmId;
    public ChoosePuzzleDialog(long alarmId) {
        this.alarmId = alarmId;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        // Установить заголовок диалогового окна
        alertDialogBuilder.setMessage(getString(R.string.choose_puzzle_type));
        // Эта функция делает диалоговое окно не закрываемым по нажатию на кнопку назад или вне диаологового окна
        setCancelable(false);
        // Получить экземпляр класса LayoutInflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Присвоить полученный макет локальной переменной экземпляру класса View
        final View view = inflater.inflate(R.layout.dialog_choose_puzzle, null, false);
        // Установить полученный макет в качестве макета диалогового окна
        alertDialogBuilder.setView(view);

        setUIBehaviourOfTextDescriptions(view);

        AlarmPuzzle alarmPuzzle = AlarmDatabaseManager.getAlarmPuzzleFromListByParentAlarmId(alarmId);

        RadioGroup radioGroupAlarmPuzzles = view.findViewById(R.id.radioGroupAlarmPuzzles);

        int[] radioIds = {R.id.radioFirstAlarmPuzzleDifficulty, R.id.radioSecondAlarmPuzzleDifficulty, R.id.radioThirdAlarmPuzzleDifficulty, R.id.radioFourthAlarmPuzzleDifficulty};
        int[] textIds = {R.id.textFirstAlarmPuzzleDifficulty, R.id.textSecondAlarmPuzzleDifficulty, R.id.textThirdAlarmPuzzleDifficulty, R.id.textFourthAlarmPuzzleDifficulty};

        radioGroupAlarmPuzzles.check(radioIds[alarmPuzzle.hardcoreLevel]);
        view.findViewById(textIds[alarmPuzzle.hardcoreLevel]).setVisibility(View.VISIBLE);

        radioGroupAlarmPuzzles.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                int hardcoreLevel = indexOf(id, radioIds);

                for(int textId : textIds) {
                    if(textId == textIds[hardcoreLevel]) {
                        view.findViewById(textId).setVisibility(View.VISIBLE);
                    }
                    else {
                        view.findViewById(textId).setVisibility(View.GONE);
                    }
                }

                alarmPuzzle.hardcoreLevel = hardcoreLevel;
            }
        });

        alertDialogBuilder.setPositiveButton(getString(R.string.apply), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        // Вернуть экземпляр диалогового окна для показа
        return alertDialogBuilder.create();

    }

    private void setUIBehaviourOfTextDescriptions(View inflatedView) {
        TextView textFirstAlarmPuzzleDifficulty = inflatedView.findViewById(R.id.textFirstAlarmPuzzleDifficulty);
        TextView textSecondAlarmPuzzleDifficulty = inflatedView.findViewById(R.id.textSecondAlarmPuzzleDifficulty);
        TextView textThirdAlarmPuzzleDifficulty = inflatedView.findViewById(R.id.textThirdAlarmPuzzleDifficulty);
        TextView textFourthAlarmPuzzleDifficulty = inflatedView.findViewById(R.id.textFourthAlarmPuzzleDifficulty);
        textFirstAlarmPuzzleDifficulty.setVisibility(View.GONE);
        textSecondAlarmPuzzleDifficulty.setVisibility(View.GONE);
        textThirdAlarmPuzzleDifficulty.setVisibility(View.GONE);
        textFourthAlarmPuzzleDifficulty.setVisibility(View.GONE);
    }

    private int indexOf(int element, int[] array) {
        for(int i = 0; i<array.length; i++) {
            if(array[i]==element) {
                return i;
            }
        }
        return -1;
    }
}
