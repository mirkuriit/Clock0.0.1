package com.terabyte.clock001;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateManager {
    public static boolean isDateTomorrow(int hour, int minute) {
        boolean result = false;

        Date currentDate = new Date();
        int currentHour = currentDate.getHours();
        int currentMinute = currentDate.getMinutes();

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
}
