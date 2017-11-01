package com.funcheap.funmapsf.commons.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by anushree on 10/24/2017.
 */

public class DateRange {


    private static final String TAG = "DateRange";


    public static String getDateRange(String day) {
        StringBuilder dateRange= new StringBuilder();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        if(day.equalsIgnoreCase("Today")){
            dateRange.append(df.format(calendar.getTime()));
            Log.i(TAG,"Today : "+dateRange);
        }

        else if(day.equalsIgnoreCase("Tomorrow")){
            calendar.add(Calendar.DATE,1);
            dateRange.append(df.format(calendar.getTime()));
            Log.i(TAG,"Tomorrow : "+dateRange);
        }

        else if(day.equalsIgnoreCase("This Week")){
            //last day of the week
            dateRange.append(df.format(calendar.getTime()));
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            calendar.roll(Calendar.DATE,1);
            dateRange.append("to"+df.format(calendar.getTime()));
            Log.i(TAG," This Week : "+dateRange);
        }
        else if(day.equalsIgnoreCase("This Weekend")){
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            dateRange.append(df.format(calendar.getTime()));
            calendar.roll(Calendar.DATE,1);
            // Add +1 to sunday
            dateRange.append("to"+df.format(calendar.getTime()));
            Log.i(TAG,"This weekend : "+dateRange);
        }
        else if(day.equalsIgnoreCase("Next Week")){

            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            calendar.roll(Calendar.DATE,2);
            dateRange.append(df.format(calendar.getTime()));
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            calendar.roll(Calendar.DATE,1);
            dateRange.append("to"+df.format(calendar.getTime()));
            Log.i(TAG,"Next week "+dateRange);
        }
        else if(day.equalsIgnoreCase("This Month")){
            dateRange.append(df.format(calendar.getTime()));
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.DATE, -1);
            dateRange.append("to"+df.format(calendar.getTime()));
            Log.i(TAG,"This month "+dateRange);
        }
        else if(day.equalsIgnoreCase("Next Month")){

            calendar.add(Calendar.MONTH,1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            dateRange.append(df.format(calendar.getTime()));
            calendar.add(Calendar.MONTH,1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.DATE, -1);
            dateRange.append("to"+df.format(calendar.getTime()));
            Log.i(TAG,"Next month : "+dateRange);
        }
        else{
            Log.i(TAG,"Unknown day format");
        }
        return dateRange.toString();
    }


    public static String[] getStartAndEndDate(String DateRange){
        return DateRange.split("to");
    }

    public static String getonlyDatefromStartDate(String startDate){

        return startDate.substring(0,10);

    }
}
