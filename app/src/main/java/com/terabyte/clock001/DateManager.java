package com.terabyte.clock001;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

// FIXME: 08.04.2022 
public class DateManager {
    public static boolean isDateTomorrow(int hour, int minute) {
        boolean result = false;

        Date currentDate = new Date();
        int currentHour = Calendar.getInstance().get(Calendar.HOUR);
        int currentMinute = Calendar.getInstance().get(Calendar.MINUTE);

        if(hour>currentHour) {
            result = false;
        }
        if(hour==currentHour) {
            if(minute>currentMinute) {
                result = false;
            }
            if(minute<=currentMinute) {
                result = true;
            }
        }
        if(hour<currentHour) {
            result = true;
        }
        return result;
    }

    public static long getMillsForAlarm(int hour, int minute) {
        long result = 0;

        int currentHour = Calendar.getInstance().get(Calendar.HOUR);
        int currentMinute = Calendar.getInstance().get(Calendar.MINUTE);

        int amPm = Calendar.getInstance().get(Calendar.AM_PM);
        if(amPm == Calendar.PM) {
            currentHour+=12;
        }
        if(hour>currentHour) {
            if(minute>currentMinute) {
                result = (hour-currentHour)* 3_600_000 +minute*60000;
            }
            if(minute==currentMinute) {
                result = (hour-currentHour)*3600000;
            }
            if(minute<currentMinute) {
                result = (hour-currentHour)*3600000-(currentMinute-minute)*60000;
            }
        }
        if(hour==currentHour) {
            if(minute>currentMinute) {
                result = (minute-currentMinute)* 60_000;
            }
            else {
                result = 86400_000-(currentMinute-minute)* 60_000;
            }
        }
        if(hour<currentHour) {
            if(minute>currentMinute) {
                result = 86400000 - (currentHour-hour)*3600000-(minute-currentMinute)*60000;
            }
            if(minute==currentMinute) {
                result = 86400000 - (currentHour-hour)*3600000;
            }
            if(minute<currentMinute) {
                result = 86400000 - (currentHour-hour)*3600000+(currentMinute-minute)*60000;
            }
        }
        return result;
    }
}
