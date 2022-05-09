package com.terabyte.clock001;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;
import java.util.Calendar;
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

    private int mExpandedPosition = -1;
    private int previousExpandedPosition = -1;

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
        Alarm alarm = alarms.get(position);


        //here we make expanding and scrolling items properly
        final boolean isExpanded = holder.getAdapterPosition()==mExpandedPosition;
        holder.constraintAlarmListExtended.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.itemView.setActivated(isExpanded);
        if (isExpanded) {
            previousExpandedPosition = holder.getAdapterPosition();
        }

        holder.switchAlarmList.setChecked(alarm.isEnabled);
        holder.switchAlarmList.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                alarm.isEnabled = b;

                if (b) {
                    if(alarm.isRepeat) {
                        AlarmRepeating alarmRepeating = AlarmDatabaseManager.getAlarmRepeatingFromListByParentAlarmId(alarm.id);
                        AlarmManagerLauncher.startTask(context, alarm.id, alarm.hour, alarm.minute, alarmRepeating.getArrayOfBooleanDays());
                    }
                    else {
                        AlarmManagerLauncher.startTask(context, alarm.id, alarm.hour, alarm.minute);
                    }

                } else {
                    AlarmManagerLauncher.stopTask(context, alarm.id);
                }
            }
        });

        holder.textAlarmListTime.setText(String.format("%02d:%02d", alarm.hour, alarm.minute));

        holder.textAlarmListDescription.setText(alarm.description);

        //here we are setting value for textAlarmListDates
        if (alarm.isRepeat) {
            boolean[] days = AlarmDatabaseManager.getAlarmRepeatingFromListByParentAlarmId(alarm.id).getArrayOfBooleanDays();
            holder.textAlarmListDates.setText(AlarmRepeatUITextBuilder.getStringByDaysArray(context, days));
        } else {
            holder.textAlarmListDates.setText(AlarmRepeatUITextBuilder.getStringByTime(context, alarm.hour, alarm.minute));
        }

        holder.buttonAlarmListExtendParams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View buttonAlarmListExtendParams) {
                mExpandedPosition = isExpanded ? -1:holder.getAdapterPosition();
                notifyItemChanged(previousExpandedPosition);
                notifyItemChanged(holder.getAdapterPosition());
            }
        });

        //actually this views might be invisible in onBindViewHolder, but we can set data into this views before
        if (alarm.isPuzzle) {
            holder.buttonAlarmListChoosePuzzleType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ChoosePuzzleDialog dialog = new ChoosePuzzleDialog(alarm.id);
                    dialog.show(fragment.getActivity().getSupportFragmentManager(), "choosePuzzleDialog");
                }
            });
        } else {
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
                                AlarmManagerLauncher.stopTask(context, alarm.id);
                                AlarmManagerLauncher.startTask(context, alarm.id, alarm.hour, alarm.minute);

                            }
                        } else {
                            holder.textAlarmListDates.setText(AlarmRepeatUITextBuilder.getStringByDaysArray(context, alarmRepeating.getArrayOfBooleanDays()));

                            if (alarm.isEnabled) {
                                AlarmManagerLauncher.stopTask(context, alarm.id);
                                AlarmManagerLauncher.startTask(context, alarm.id, alarm.hour, alarm.minute, alarmRepeating.getArrayOfBooleanDays());
                            }
                        }


                    }
                });
            }
        } else {
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

                    AlarmRepeating repeating = new AlarmRepeating(alarm.id, days);

                    AlarmDatabaseManager.createAlarmRepeating(context, repeating, new PostExecuteCode() {
                        @Override
                        public void doInPostExecuteWhenWeGotIdOfCreatedAlarmRepeating(long createdAlarmRepeatingId) {
                            repeating.id = createdAlarmRepeatingId;
                            AlarmDatabaseManager.updateAlarmRepeatingList(repeating);

                            for (int i = 0; i < 7; i++) {
                                Chip chip = (Chip) holder.linearLayoutChips.getChildAt(i);
                                chip.setChecked(days[i]);
                                chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                        switch (chip.getId()) {
                                            case R.id.chipAlarmListMonday:
                                                repeating.mo = b;
                                                break;
                                            case R.id.chipAlarmListTuesday:
                                                repeating.tu = b;
                                                break;
                                            case R.id.chipAlarmListWednesday:
                                                repeating.we = b;
                                                break;
                                            case R.id.chipAlarmListThursday:
                                                repeating.th = b;
                                                break;
                                            case R.id.chipAlarmListFriday:
                                                repeating.fr = b;
                                                break;
                                            case R.id.chipAlarmListSaturday:
                                                repeating.sa = b;
                                                break;
                                            case R.id.chipAlarmListSunday:
                                                repeating.su = b;
                                                break;
                                        }

                                        boolean[] falseArrayForCheckingCondition = new boolean[7];
                                        Arrays.fill(falseArrayForCheckingCondition, false);
                                        if (Arrays.equals(repeating.getArrayOfBooleanDays(), falseArrayForCheckingCondition)) {
                                            //here all chips are false and we can hide chipGroup, turn off checkBoxRepeat and delete alarmRepeating from database
                                            //but to do this we just can set checked false in checkBoxAlarmListRepeat. In checkBox will be run onCheckedChangeListener if we do it
                                            holder.scrollViewChips.setVisibility(View.GONE);

                                            holder.checkBoxAlarmListRepeat.setChecked(false);

                                            AlarmDatabaseManager.getAlarmRepeatingList().remove(repeating);

                                            holder.textAlarmListDates.setText(AlarmRepeatUITextBuilder.getStringByTime(context, alarm.hour, alarm.minute));

                                            if (alarm.isEnabled) {
                                                AlarmManagerLauncher.stopTask(context, alarm.id);
                                                AlarmManagerLauncher.startTask(context, alarm.id, alarm.hour, alarm.minute);

                                            }
                                        } else {
                                            holder.textAlarmListDates.setText(AlarmRepeatUITextBuilder.getStringByDaysArray(context, repeating.getArrayOfBooleanDays()));

                                            if (alarm.isEnabled) {
                                                AlarmManagerLauncher.stopTask(context, alarm.id);
                                                AlarmManagerLauncher.startTask(context, alarm.id, alarm.hour, alarm.minute, repeating.getArrayOfBooleanDays());
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    });


                    holder.textAlarmListDates.setText(AlarmRepeatUITextBuilder.getStringByDaysArray(context, days));
                } else {
                    holder.scrollViewChips.setVisibility(View.GONE);
                    //we delete alarmRepeating from db
                    //but first we should get alarmRepeatingObject
                    AlarmRepeating alarmRepeating = AlarmDatabaseManager.getAlarmRepeatingFromListByParentAlarmId(alarm.id);
                    AlarmDatabaseManager.getAlarmRepeatingList().remove(alarmRepeating);

                    holder.textAlarmListDates.setText(AlarmRepeatUITextBuilder.getStringByTime(context, alarm.hour, alarm.minute));

                    if (alarm.isEnabled) {
                        AlarmManagerLauncher.stopTask(context, alarm.id);
                        AlarmManagerLauncher.startTask(context, alarm.id, alarm.hour, alarm.minute);
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

                    AlarmPuzzle alarmPuzzle = new AlarmPuzzle(alarm.id, 0);
                    AlarmDatabaseManager.createAlarmPuzzle(context, alarmPuzzle, new PostExecuteCode() {
                        @Override
                        public void doInPostExecuteWhenWeGotIdOfCreatedAlarmPuzzle(long createdAlarmPuzzleId) {
                            alarmPuzzle.id = createdAlarmPuzzleId;
                            AlarmDatabaseManager.updateAlarmPuzzleList(alarmPuzzle);
                            holder.buttonAlarmListChoosePuzzleType.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ChoosePuzzleDialog dialog = new ChoosePuzzleDialog(alarm.id);
                                    dialog.show(fragment.getActivity().getSupportFragmentManager(), "choosePuzzleDialog");
                                }
                            });
                        }
                    });
                } else {
                    holder.buttonAlarmListChoosePuzzleType.setVisibility(View.GONE);
                    AlarmPuzzle alarmPuzzle = AlarmDatabaseManager.getAlarmPuzzleFromListByParentAlarmId(alarm.id);
                    AlarmDatabaseManager.getAlarmPuzzleList().remove(alarmPuzzle);
                }
            }
        });

        holder.buttonAlarmListSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseSoundDialog dialog = new ChooseSoundDialog(alarm.id);
                dialog.show(fragment.getActivity().getSupportFragmentManager(), "chooseSoundDialog");
            }
        });

        holder.buttonAlarmListDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View buttonAlarmListDelete) {
                if (alarm.isRepeat) {
                    AlarmDatabaseManager.getAlarmRepeatingList().remove(AlarmDatabaseManager.getAlarmRepeatingFromListByParentAlarmId(alarm.id));
                }
                if (alarm.isPuzzle) {
                    AlarmDatabaseManager.getAlarmPuzzleList().remove(AlarmDatabaseManager.getAlarmPuzzleFromListByParentAlarmId(alarm.id));
                }

                AlarmManagerLauncher.stopTask(context, alarm.id);

                AlarmDatabaseManager.getAlarmList().remove(alarm);

                if(AlarmDatabaseManager.getAlarmList().size()==0) {
                    //here we have already deleted all alarms and we need to restart fragment to
                    //show "no alarms" text
                    FragmentManager fragmentManager = fragment.getActivity().getSupportFragmentManager();
                    AlarmFragment fragment = new AlarmFragment();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.frameLayoutForFragments, fragment).commit();
                }
                else {
                    AlarmAdapter.super.notifyDataSetChanged();
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
