package com.terabyte.clock001;

import android.content.Context;

import java.util.Arrays;

public class AlarmRepeatUITextBuilder {
    public static String getStringByDaysArray(Context context, boolean[] days) {
        String result = "";
        boolean[] arrayWithTrueValuesToCheckIfCondition = new boolean[7];
        Arrays.fill(arrayWithTrueValuesToCheckIfCondition, true);
        if(Arrays.equals(days, arrayWithTrueValuesToCheckIfCondition)) {
            result = context.getResources().getString(R.string.every_day);
        }
        else {
            int[] dayNamesIds = {R.string.monday_short, R.string.tuesday_short, R.string.wednesday_short, R.string.thursday_short, R.string.friday_short, R.string.saturday_short, R.string.sunday_short};
            for(int i = 0; i<7;i++) {
                if(days[i] == true) {
                    result+=context.getResources().getString(dayNamesIds[i])+" ";
                }
            }
        }
        return result;
    }
    public static String getStringByTime(Context context, int hour, int minute) {
        if(DateManager.isDateTomorrow(hour, minute)) {
            return context.getResources().getString(R.string.tomorrow);
        }
        else {
            return context.getResources().getString(R.string.today);
        }
    }
}
