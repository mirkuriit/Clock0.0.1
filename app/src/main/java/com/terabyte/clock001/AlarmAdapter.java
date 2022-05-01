package com.terabyte.clock001;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.Chip;
import java.util.Arrays;
import java.util.List;

/**
 * In AlarmFragment we have RecyclerView and this is adapter for this recycler
 * In onBindViewHolder we set our layout of one element by certain Alarm object
 * In AlarmHolder class we initialize interface fields to work with them later in onBindViewHolder method
 * We can get Context here with method inflater.getContext(); but I'm not sure that it's actually properly
 */

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmHolder> {
    private LayoutInflater inflater;
    private List<Alarm> alarms;
    private Fragment fragment;
    private Context context;
    public AlarmAdapter(Context context, Fragment fragment, List<Alarm> alarms) {
        this.alarms = alarms;
        this.fragment = fragment;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public AlarmAdapter.AlarmHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_alarms, parent, false);
        return new AlarmHolder(view);
    }

    @Override
    public void onBindViewHolder(AlarmHolder holder, int position) {
//        Alarm alarm = alarms.get(position);
//
//        holder.switchAlarmList.setChecked(alarm.isEnabled);
//        holder.switchAlarmList.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                alarm.isEnabled = b;
//                AlarmDatabaseManager.updateAlarm(AlarmDatabaseClient.getInstance(inflater.getContext()).getAppDatabase(), alarm);
//
//                if (b) {
//                    AlarmWorkLauncher.startAlarmWorker(inflater.getContext(), fragment, alarm.id, alarm.hour, alarm.minute, AlarmWorkLauncher.getAlarmWorkerObserver(inflater.getContext(), alarm.id));
//                } else {
//                    // FIXME: 26.04.2022 here we suddenly don't stop alarm worker! I don't know why!
//                    AlarmWorkLauncher.stopAlarmWorker(inflater.getContext(), String.valueOf(alarm.id));
//                }
//            }
//        });
//
//        holder.textAlarmListTime.setText(String.format("%02d:%02d", alarm.hour, alarm.minute));
//
//        holder.textAlarmListDescription.setText(alarm.description);
//
//        holder.checkBoxAlarmListRepeat.setChecked(alarm.isRepeat);
//        holder.checkBoxAlarmListRepeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) {
//                    boolean[] days = new boolean[7];
//                    Arrays.fill(days, true);
//                    AlarmDatabaseManager.createAlarmRepeatingIfNotExists(AlarmDatabaseClient.getInstance(inflater.getContext()).getAppDatabase(), alarm.id, days);
//                    for (int i = 0; i < 7; i++) {
//                        Chip chip = (Chip) holder.linearLayoutChips.getChildAt(i);
//                        chip.setChecked(days[i]);
//                    }
//
//                    holder.textAlarmListDates.setText(AlarmRepeatUITextBuilder.getStringByDaysArray(inflater.getContext(), days));
//                    holder.scrollViewChips.setVisibility(View.VISIBLE);
//                } else {
//                    //we delete alarmRepeating from db
//                    //but first we should get alarmRepeatingObject
//                    AlarmDatabaseManager.getAlarmRepeatingByParentAlarmId(AlarmDatabaseClient.getInstance(inflater.getContext()).getAppDatabase(), alarm.id, new PostExecuteCode() {
//                        @Override
//                        public void doInPostExecuteWhenWeGotAlarmRepeating(AlarmRepeating alarmRepeating) {
//                            AlarmDatabaseManager.deleteAlarmRepeating(AlarmDatabaseClient.getInstance(inflater.getContext()).getAppDatabase(), alarmRepeating);
//                        }
//                    });
//                    holder.textAlarmListDates.setText(AlarmRepeatUITextBuilder.getStringByTime(inflater.getContext(), alarm.hour, alarm.minute));
//                    holder.scrollViewChips.setVisibility(View.GONE);
//
//                    if (alarm.isEnabled) {
//                        AlarmWorkLauncher.stopAlarmWorker(inflater.getContext(), String.valueOf(alarm.id));
//                        AlarmWorkLauncher.startAlarmWorker(inflater.getContext(), fragment, alarm.id, alarm.hour, alarm.minute, AlarmWorkLauncher.getAlarmWorkerObserver(inflater.getContext(), alarm.id));
//                    }
//                }
//                alarm.isRepeat = b;
//                AlarmDatabaseManager.updateAlarm(AlarmDatabaseClient.getInstance(inflater.getContext()).getAppDatabase(), alarm);
//            }
//        });
//
//        holder.checkBoxAlarmListVibration.setChecked(alarm.isVibration);
//        holder.checkBoxAlarmListVibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                alarm.isVibration = b;
//                AlarmDatabaseManager.updateAlarm(AlarmDatabaseClient.getInstance(inflater.getContext()).getAppDatabase(), alarm);
//            }
//        });
//
//        holder.checkBoxAlarmListPuzzle.setChecked(alarm.isPuzzle);
//        holder.checkBoxAlarmListPuzzle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) {
//                    alarm.isPuzzle = true;
//                    AlarmDatabaseManager.createAlarmPuzzle(AlarmDatabaseClient.getInstance(inflater.getContext()).getAppDatabase(), new AlarmPuzzle(alarm.id, 0));
//                    AlarmDatabaseManager.updateAlarm(AlarmDatabaseClient.getInstance(inflater.getContext()).getAppDatabase(), alarm);
//                    holder.buttonAlarmListChoosePuzzleType.setVisibility(View.VISIBLE);
//                } else {
//                    alarm.isPuzzle = false;
//                    AlarmDatabaseManager.getAlarmPuzzleByParentAlarmId(AlarmDatabaseClient.getInstance(inflater.getContext()).getAppDatabase(), alarm.id, new PostExecuteCode() {
//                        @Override
//                        public void doInPostExecuteWhenWeGotAlarmPuzzle(AlarmPuzzle alarmPuzzle) {
//                            AlarmDatabaseManager.deleteAlarmPuzzle(AlarmDatabaseClient.getInstance(inflater.getContext()).getAppDatabase(), alarmPuzzle);
//                        }
//                    });
//                    AlarmDatabaseManager.updateAlarm(AlarmDatabaseClient.getInstance(inflater.getContext()).getAppDatabase(), alarm);
//
//                    holder.buttonAlarmListChoosePuzzleType.setVisibility(View.GONE);
//                }
//            }
//        });
//        if (alarm.isPuzzle) {
//            holder.buttonAlarmListChoosePuzzleType.setVisibility(View.VISIBLE);
//        } else {
//            holder.buttonAlarmListChoosePuzzleType.setVisibility(View.GONE);
//        }
//
//        holder.editAlarmListDescription.setText(alarm.description);
//        holder.editAlarmListDescription.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                holder.textAlarmListDescription.setText(editable.toString());
//                alarm.description = editable.toString();
//                AlarmDatabaseManager.updateAlarm(AlarmDatabaseClient.getInstance(inflater.getContext()).getAppDatabase(), alarm);
//            }
//        });
//
//
//        holder.buttonAlarmListExtendParams.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (holder.constraintAlarmListExtended.getVisibility() == View.VISIBLE) {
//                    holder.constraintAlarmListExtended.setVisibility(View.GONE);
//                } else {
//                    holder.constraintAlarmListExtended.setVisibility(View.VISIBLE);
//                    if (holder.checkBoxAlarmListRepeat.isChecked()) {
//                        holder.scrollViewChips.setVisibility(View.VISIBLE);
//                    } else {
//                        holder.scrollViewChips.setVisibility(View.GONE);
//                    }
//                }
//            }
//        });
//
//        holder.buttonAlarmListDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //delete alarm
//                AlarmDatabaseManager.deleteAlarm(AlarmDatabaseClient.getInstance(inflater.getContext()).getAppDatabase(), alarm, new PostExecuteCode() {
//                    @Override
//                    public void doInPostExecute() {
//                        if (alarm.isRepeat) {
//                            AlarmDatabaseManager.getAlarmRepeatingByParentAlarmId(AlarmDatabaseClient.getInstance(inflater.getContext()).getAppDatabase(), alarm.id, new PostExecuteCode() {
//                                @Override
//                                public void doInPostExecuteWhenWeGotAlarmRepeating(AlarmRepeating alarmRepeating) {
//                                    AlarmDatabaseManager.deleteAlarmRepeating(AlarmDatabaseClient.getInstance(inflater.getContext()).getAppDatabase(), alarmRepeating);
//                                }
//                            });
//                        }
//
//                        alarms.remove(alarm);
//                        AlarmAdapter.super.notifyDataSetChanged();
//                        // TODO: 08.04.2022 here we stop work manager
//                        AlarmWorkLauncher.stopAlarmWorker(inflater.getContext(), String.valueOf(alarm.id));
//                    }
//                });
//            }
//        });
//
//        holder.constraintAlarmListExtended.setVisibility(View.GONE);
//
//        holder.buttonAlarmListChoosePuzzleType.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(inflater.getContext(), ChoosePuzzleActivity.class);
//                intent.putExtra(Const.INTENT_KEY_ALARM_ID, alarm.id);
//                inflater.getContext().startActivity(intent);
//            }
//        });
//
//        holder.textAlarmListDates.setText(AlarmRepeatUITextBuilder.getStringByTime(inflater.getContext(), alarm.hour, alarm.minute));
//        if (alarm.isRepeat) {
//            AlarmDatabaseManager.getAlarmRepeatingByParentAlarmId(AlarmDatabaseClient.getInstance(inflater.getContext()).getAppDatabase(), alarm.id, new PostExecuteCode() {
//                @Override
//                public void doInPostExecuteWhenWeGotAlarmRepeating(AlarmRepeating alarmRepeating) {
//                    boolean[] days = alarmRepeating.getArrayOfBooleanDays();
//                    for (int i = 0; i < 7; i++) {
//                        Chip chip = (Chip) holder.linearLayoutChips.getChildAt(i);
//                        chip.setChecked(days[i]);
//                    }
//                    holder.textAlarmListDates.setText(AlarmRepeatUITextBuilder.getStringByDaysArray(inflater.getContext(), days));
//                }
//            });
//
//
//        }
//        else {
//            holder.textAlarmListDates.setText(AlarmRepeatUITextBuilder.getStringByTime(inflater.getContext(), alarm.hour, alarm.minute));
//        }
//
//        for (int i = 0; i < 7; i++) {
//            Chip chip = (Chip) holder.linearLayoutChips.getChildAt(i);
//            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                    AlarmDatabaseManager.getAlarmRepeatingByParentAlarmId(AlarmDatabaseClient.getInstance(inflater.getContext()).getAppDatabase(), alarm.id, new PostExecuteCode() {
//                        @Override
//                        public void doInPostExecuteWhenWeGotAlarmRepeating(AlarmRepeating alarmRepeating) {
//                            switch (chip.getId()) {
//                                case R.id.chipAlarmListMonday:
//                                    alarmRepeating.mo = b;
//                                    break;
//                                case R.id.chipAlarmListTuesday:
//                                    alarmRepeating.tu = b;
//                                    break;
//                                case R.id.chipAlarmListWednesday:
//                                    alarmRepeating.we = b;
//                                    break;
//                                case R.id.chipAlarmListThursday:
//                                    alarmRepeating.th = b;
//                                    break;
//                                case R.id.chipAlarmListFriday:
//                                    alarmRepeating.fr = b;
//                                    break;
//                                case R.id.chipAlarmListSaturday:
//                                    alarmRepeating.sa = b;
//                                    break;
//                                case R.id.chipAlarmListSunday:
//                                    alarmRepeating.su = b;
//                                    break;
//                            }
//                            boolean[] falseArrayForCheckingCondition = new boolean[7];
//                            Arrays.fill(falseArrayForCheckingCondition, false);
//                            if (Arrays.equals(alarmRepeating.getArrayOfBooleanDays(), falseArrayForCheckingCondition)) {
//                                //here all chips are false and we can hide chipGroup, turn off checkBoxRepeat and delete alarmRepeating from database
//                                //but to do this we just can set checked false in checkBoxAlarmListRepeat. In checkBox will be run onCheckedChangeListener if we do it
//                                holder.checkBoxAlarmListRepeat.setChecked(false);
//                                holder.textAlarmListDates.setText(AlarmRepeatUITextBuilder.getStringByTime(inflater.getContext(), alarm.hour, alarm.minute));
//
//                                if (alarm.isEnabled) {
//                                    AlarmWorkLauncher.stopAlarmWorker(inflater.getContext(), String.valueOf(alarm.id));
//                                    AlarmWorkLauncher.startAlarmWorker(inflater.getContext(), fragment, alarm.id, alarm.hour, alarm.minute, AlarmWorkLauncher.getAlarmWorkerObserver(inflater.getContext(), alarm.id));
//                                }
//                            } else {
//                                AlarmDatabaseManager.updateAlarmRepeating(AlarmDatabaseClient.getInstance(inflater.getContext()).getAppDatabase(), alarmRepeating);
//                                holder.textAlarmListDates.setText(AlarmRepeatUITextBuilder.getStringByDaysArray(inflater.getContext(), alarmRepeating.getArrayOfBooleanDays()));
//
//                                if (alarm.isEnabled) {
//                                    AlarmWorkLauncher.stopAlarmWorker(inflater.getContext(), String.valueOf(alarm.id));
//                                    AlarmWorkLauncher.startAlarmWorker(inflater.getContext(), fragment, alarm.id, alarm.hour, alarm.minute, alarmRepeating.getArrayOfBooleanDays(), AlarmWorkLauncher.getAlarmWorkerObserver(inflater.getContext(), alarm.id));
//                                }
//                            }
//
//                        }
//                    });
//                }
//            });
//        }
//
//



        ////////////////////////////////
        Alarm alarm = alarms.get(position);

        holder.constraintAlarmListExtended.setVisibility(View.GONE);

        holder.switchAlarmList.setChecked(alarm.isEnabled);
        holder.switchAlarmList.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                alarm.isEnabled = b;

                if (b) {
                    AlarmWorkLauncher.startAlarmWorker(context, fragment, alarm.id, alarm.hour, alarm.minute, AlarmWorkLauncher.getAlarmWorkerObserver(context, alarm.id));
                }
                else {
                    // FIXME: 26.04.2022 here we suddenly don't stop alarm worker! I don't know why!
                    AlarmWorkLauncher.stopAlarmWorker(context, String.valueOf(alarm.id));
                }
            }
        });

        holder.textAlarmListTime.setText(String.format("%02d:%02d", alarm.hour, alarm.minute));

        holder.textAlarmListDescription.setText(alarm.description);

        //here we are setting value for textAlarmListDates
        if (alarm.isRepeat) {
            boolean[] days = AlarmDatabaseManager.getAlarmRepeatingFromListByParentAlarmId(alarm.id).getArrayOfBooleanDays();
            holder.textAlarmListDates.setText(AlarmRepeatUITextBuilder.getStringByDaysArray(context, days));
        }
        else {
            holder.textAlarmListDates.setText(AlarmRepeatUITextBuilder.getStringByTime(context, alarm.hour, alarm.minute));
        }

        holder.buttonAlarmListExtendParams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.constraintAlarmListExtended.getVisibility() == View.VISIBLE) {
                    holder.constraintAlarmListExtended.setVisibility(View.GONE);
                }
                else {
                    holder.constraintAlarmListExtended.setVisibility(View.VISIBLE);
                    if (!alarm.isPuzzle) {
                        holder.buttonAlarmListChoosePuzzleType.setVisibility(View.GONE);
                    }

                    if (alarm.isRepeat) {
                        AlarmRepeating alarmRepeating = AlarmDatabaseManager.getAlarmRepeatingFromListByParentAlarmId(alarm.id);
                        boolean[] days = alarmRepeating.getArrayOfBooleanDays();
                        for (int i = 0; i < 7; i++) {
                            Chip chip = (Chip) holder.linearLayoutChips.getChildAt(i);
                            chip.setChecked(days[i]);
                            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                    switch (chip.getId()) {
                                        case R.id.chipAlarmListMonday:
                                            alarmRepeating.mo = b;
                                            break;
                                        case R.id.chipAlarmListTuesday:
                                            alarmRepeating.tu = b;
                                            break;
                                        case R.id.chipAlarmListWednesday:
                                            alarmRepeating.we = b;
                                            break;
                                        case R.id.chipAlarmListThursday:
                                            alarmRepeating.th = b;
                                            break;
                                        case R.id.chipAlarmListFriday:
                                            alarmRepeating.fr = b;
                                            break;
                                        case R.id.chipAlarmListSaturday:
                                            alarmRepeating.sa = b;
                                            break;
                                        case R.id.chipAlarmListSunday:
                                            alarmRepeating.su = b;
                                            break;
                                    }

                                    boolean[] falseArrayForCheckingCondition = new boolean[7];
                                    Arrays.fill(falseArrayForCheckingCondition, false);
                                    if (Arrays.equals(alarmRepeating.getArrayOfBooleanDays(), falseArrayForCheckingCondition)) {
                                        //here all chips are false and we can hide chipGroup, turn off checkBoxRepeat and delete alarmRepeating from database
                                        //but to do this we just can set checked false in checkBoxAlarmListRepeat. In checkBox will be run onCheckedChangeListener if we do it
                                        holder.scrollViewChips.setVisibility(View.GONE);

                                        holder.checkBoxAlarmListRepeat.setChecked(false);

                                        AlarmDatabaseManager.getAlarmRepeatingList().remove(alarmRepeating);

                                        holder.textAlarmListDates.setText(AlarmRepeatUITextBuilder.getStringByTime(context, alarm.hour, alarm.minute));

                                        if (alarm.isEnabled) {
                                            AlarmWorkLauncher.stopAlarmWorker(context, String.valueOf(alarm.id));
                                            AlarmWorkLauncher.startAlarmWorker(context, fragment, alarm.id, alarm.hour, alarm.minute, AlarmWorkLauncher.getAlarmWorkerObserver(context, alarm.id));
                                        }
                                    }
                                    else {
                                        holder.textAlarmListDates.setText(AlarmRepeatUITextBuilder.getStringByDaysArray(context, alarmRepeating.getArrayOfBooleanDays()));

                                        if (alarm.isEnabled) {
                                            AlarmWorkLauncher.stopAlarmWorker(context, String.valueOf(alarm.id));
                                            AlarmWorkLauncher.startAlarmWorker(context, fragment, alarm.id, alarm.hour, alarm.minute, alarmRepeating.getArrayOfBooleanDays(), AlarmWorkLauncher.getAlarmWorkerObserver(context, alarm.id));
                                        }
                                    }
                                }
                            });
                        }
                    }
                    else {
                        holder.scrollViewChips.setVisibility(View.GONE);
                    }

                    holder.checkBoxAlarmListRepeat.setChecked(alarm.isRepeat);
                    holder.checkBoxAlarmListRepeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if (b) {
                                holder.scrollViewChips.setVisibility(View.VISIBLE);

                                boolean[] days = new boolean[7];
                                Arrays.fill(days, true);
                                AlarmDatabaseManager.createAlarmRepeating(AlarmDatabaseClient.getInstance(context).getAppDatabase(), alarm.id, days, new PostExecuteCode() {
                                    @Override
                                    public void doInPostExecute() {
                                        AlarmDatabaseManager.updateAlarmRepeatingList(AlarmDatabaseClient.getInstance(context).getAppDatabase(), new PostExecuteCode() {
                                            @Override
                                            public void doInPostExecute() {
                                                AlarmRepeating alarmRepeating = AlarmDatabaseManager.getAlarmRepeatingFromListByParentAlarmId(alarm.id);
                                                for (int i = 0; i < 7; i++) {
                                                    Chip chip = (Chip) holder.linearLayoutChips.getChildAt(i);
                                                    chip.setChecked(days[i]);
                                                    chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                        @Override
                                                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                                            switch (chip.getId()) {
                                                                case R.id.chipAlarmListMonday:
                                                                    alarmRepeating.mo = b;
                                                                    break;
                                                                case R.id.chipAlarmListTuesday:
                                                                    alarmRepeating.tu = b;
                                                                    break;
                                                                case R.id.chipAlarmListWednesday:
                                                                    alarmRepeating.we = b;
                                                                    break;
                                                                case R.id.chipAlarmListThursday:
                                                                    alarmRepeating.th = b;
                                                                    break;
                                                                case R.id.chipAlarmListFriday:
                                                                    alarmRepeating.fr = b;
                                                                    break;
                                                                case R.id.chipAlarmListSaturday:
                                                                    alarmRepeating.sa = b;
                                                                    break;
                                                                case R.id.chipAlarmListSunday:
                                                                    alarmRepeating.su = b;
                                                                    break;
                                                            }

                                                            boolean[] falseArrayForCheckingCondition = new boolean[7];
                                                            Arrays.fill(falseArrayForCheckingCondition, false);
                                                            if (Arrays.equals(alarmRepeating.getArrayOfBooleanDays(), falseArrayForCheckingCondition)) {
                                                                //here all chips are false and we can hide chipGroup, turn off checkBoxRepeat and delete alarmRepeating from database
                                                                //but to do this we just can set checked false in checkBoxAlarmListRepeat. In checkBox will be run onCheckedChangeListener if we do it
                                                                holder.scrollViewChips.setVisibility(View.GONE);

                                                                holder.checkBoxAlarmListRepeat.setChecked(false);

                                                                AlarmDatabaseManager.getAlarmRepeatingList().remove(alarmRepeating);

                                                                holder.textAlarmListDates.setText(AlarmRepeatUITextBuilder.getStringByTime(context, alarm.hour, alarm.minute));

                                                                if (alarm.isEnabled) {
                                                                    AlarmWorkLauncher.stopAlarmWorker(context, String.valueOf(alarm.id));
                                                                    AlarmWorkLauncher.startAlarmWorker(context, fragment, alarm.id, alarm.hour, alarm.minute, AlarmWorkLauncher.getAlarmWorkerObserver(context, alarm.id));
                                                                }
                                                            }
                                                            else {
                                                                holder.textAlarmListDates.setText(AlarmRepeatUITextBuilder.getStringByDaysArray(context, alarmRepeating.getArrayOfBooleanDays()));

                                                                if (alarm.isEnabled) {
                                                                    AlarmWorkLauncher.stopAlarmWorker(context, String.valueOf(alarm.id));
                                                                    AlarmWorkLauncher.startAlarmWorker(context, fragment, alarm.id, alarm.hour, alarm.minute, alarmRepeating.getArrayOfBooleanDays(), AlarmWorkLauncher.getAlarmWorkerObserver(context, alarm.id));
                                                                }
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    }
                                });



                                holder.textAlarmListDates.setText(AlarmRepeatUITextBuilder.getStringByDaysArray(context, days));
                            }
                            else {
                                holder.scrollViewChips.setVisibility(View.GONE);
                                //we delete alarmRepeating from db
                                //but first we should get alarmRepeatingObject
                                AlarmRepeating alarmRepeating = AlarmDatabaseManager.getAlarmRepeatingFromListByParentAlarmId(alarm.id);
                                AlarmDatabaseManager.getAlarmRepeatingList().remove(alarmRepeating);

                                holder.textAlarmListDates.setText(AlarmRepeatUITextBuilder.getStringByTime(context, alarm.hour, alarm.minute));

                                if (alarm.isEnabled) {
                                    AlarmWorkLauncher.stopAlarmWorker(context, String.valueOf(alarm.id));
                                    AlarmWorkLauncher.startAlarmWorker(context, fragment, alarm.id, alarm.hour, alarm.minute, AlarmWorkLauncher.getAlarmWorkerObserver(context, alarm.id));
                                }
                            }

                            alarm.isRepeat = b;
                        }
                    });

                    holder.checkBoxAlarmListVibration.setChecked(alarm.isVibration);
                    holder.checkBoxAlarmListVibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            alarm.isVibration = b;
                        }
                    });

                    holder.editAlarmListDescription.setText(alarm.description);
                    holder.editAlarmListDescription.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            holder.textAlarmListDescription.setText(editable.toString());
                            alarm.description = editable.toString();
                        }
                    });

                    holder.checkBoxAlarmListPuzzle.setChecked(alarm.isPuzzle);
                    holder.checkBoxAlarmListPuzzle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            alarm.isPuzzle = b;

                            if (b) {
                                holder.buttonAlarmListChoosePuzzleType.setVisibility(View.VISIBLE);
                                AlarmDatabaseManager.createAlarmPuzzle(AlarmDatabaseClient.getInstance(context).getAppDatabase(), new AlarmPuzzle(alarm.id, 0), new PostExecuteCode() {
                                    @Override
                                    public void doInPostExecute() {
                                        AlarmDatabaseManager.updateAlarmPuzzleList(AlarmDatabaseClient.getInstance(context).getAppDatabase(), new PostExecuteCode() {
                                            @Override
                                            public void doInPostExecute() {
                                                holder.buttonAlarmListChoosePuzzleType.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        Intent intent = new Intent(context, ChoosePuzzleActivity.class);
                                                        intent.putExtra(Const.INTENT_KEY_ALARM_ID, alarm.id);
                                                        context.startActivity(intent);
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                            else {
                                holder.buttonAlarmListChoosePuzzleType.setVisibility(View.GONE);
                                AlarmPuzzle alarmPuzzle = AlarmDatabaseManager.getAlarmPuzzleFromListByParentAlarmId(alarm.id);
                                AlarmDatabaseManager.getAlarmPuzzleList().remove(alarmPuzzle);
                            }
                        }
                    });

                    holder.buttonAlarmListDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(alarm.isRepeat) {
                                AlarmDatabaseManager.getAlarmRepeatingList().remove(AlarmDatabaseManager.getAlarmRepeatingFromListByParentAlarmId(alarm.id));
                            }
                            if(alarm.isPuzzle) {
                                AlarmDatabaseManager.getAlarmPuzzleList().remove(AlarmDatabaseManager.getAlarmPuzzleFromListByParentAlarmId(alarm.id));
                            }

                            AlarmWorkLauncher.stopAlarmWorker(context, String.valueOf(alarm.id));

                            AlarmDatabaseManager.getAlarmList().remove(alarm);

                            AlarmAdapter.super.notifyDataSetChanged();
                        }
                    });
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }

    static class AlarmHolder extends RecyclerView.ViewHolder {
        public TextView textAlarmListTime, textAlarmListDates, textAlarmListDescription;
        public Switch switchAlarmList;
        public ImageButton buttonAlarmListExtendParams;
        public ConstraintLayout constraintAlarmListExtended;
        CheckBox checkBoxAlarmListRepeat, checkBoxAlarmListVibration, checkBoxAlarmListPuzzle;
        HorizontalScrollView scrollViewChips;
        LinearLayout linearLayoutChips;
        Button buttonAlarmListSound, buttonAlarmListDelete, buttonAlarmListChoosePuzzleType;
        EditText editAlarmListDescription;

        public AlarmHolder(@NonNull View itemView) {
            super(itemView);
            textAlarmListTime = itemView.findViewById(R.id.textAlarmListTime);
            textAlarmListDates = itemView.findViewById(R.id.textAlarmListDates);
            textAlarmListDescription = itemView.findViewById(R.id.textAlarmListDescription);
            switchAlarmList = itemView.findViewById(R.id.switchAlarmList);
            buttonAlarmListExtendParams = itemView.findViewById(R.id.buttonAlarmListExtendParams);
            checkBoxAlarmListRepeat = itemView.findViewById(R.id.checkBoxAlarmListRepeat);
            checkBoxAlarmListVibration = itemView.findViewById(R.id.checkBoxAlarmListVibration);
            checkBoxAlarmListPuzzle = itemView.findViewById(R.id.checkBoxAlarmListPuzzle);
            scrollViewChips = itemView.findViewById(R.id.scrollViewChips);
            linearLayoutChips = itemView.findViewById(R.id.linearLayoutChips);
            buttonAlarmListSound = itemView.findViewById(R.id.buttonAlarmListSound);
            buttonAlarmListDelete = itemView.findViewById(R.id.buttonAlarmListDelete);
            buttonAlarmListChoosePuzzleType = itemView.findViewById(R.id.buttonAlarmListChoosePuzzleType);
            editAlarmListDescription = itemView.findViewById(R.id.editAlarmListDescription);
            constraintAlarmListExtended = itemView.findViewById(R.id.constraintAlarmListExtended);
        }
    }
}
