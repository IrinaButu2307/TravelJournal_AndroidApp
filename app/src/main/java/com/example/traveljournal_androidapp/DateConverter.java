package com.example.traveljournal_androidapp;

import java.util.Calendar;

public class DateConverter {

    @TypeConverter
    public static Calendar toDate(Long timestamp){
        Calendar calendar = Calendar.getInstance();
        if(timestamp == null){
            calendar = null;
        }
        else{
            calendar.setTimeInMillis(timestamp);
        }
        return calendar;
    }

    @TypeConverter
    public static Long toTimestamp(Calendar calendar){
        return calendar == null ? null : calendar.getTimeInMillis();
    }
}
