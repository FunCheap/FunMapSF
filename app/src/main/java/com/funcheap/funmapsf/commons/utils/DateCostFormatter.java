package com.funcheap.funmapsf.commons.utils;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static android.content.ContentValues.TAG;

/**
 * Created by gkudva on 26/10/17.
 */

public class DateCostFormatter {

    private static String TAG = "DateCostFormatter";

    public static String formatDate(String date)  {
        String retDate = date;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
            sdf.setTimeZone(TimeZone.getTimeZone("PDT"));
            Date mDate =   sdf.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(mDate);
            Calendar today = Calendar.getInstance();
            Calendar yesterday = Calendar.getInstance();
            Calendar tomorrow = Calendar.getInstance();
            yesterday.add(Calendar.DATE, -1);
            tomorrow.add(Calendar.DATE, +1);
            DateFormat timeFormatter = new SimpleDateFormat("hh:mma");
            timeFormatter.setTimeZone(TimeZone.getTimeZone("PDT"));

            if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
                retDate =  "Today, "+ getMonth(today.get(Calendar.MONTH)+1)+" "+ today.get(Calendar.DATE)+" - " + timeFormatter.format(mDate);
            } else if (calendar.get(Calendar.YEAR) == tomorrow.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == tomorrow.get(Calendar.DAY_OF_YEAR)) {
                retDate = "Tomorrow, " + getMonth(tomorrow.get(Calendar.MONTH)+1)+" "+ tomorrow.get(Calendar.DATE)+" - " + timeFormatter.format(mDate);
            }
            else {
                retDate = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US) +", " +getMonth(calendar.get(Calendar.MONTH)+1)+" "+ calendar.get(Calendar.DATE) +" - " + timeFormatter.format(mDate);
            }
        }
        catch (Exception ex)
        {

        }

        return retDate;
    }

    public static String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }

    public static String formatCost(String cost)
    {
        String retCost = "";

        if (cost.equalsIgnoreCase("0"))
        {
            retCost = "FREE";
        }
        else if (cost.matches(".*\\d.*"))
        {
            retCost = "$" + cost;
        }

        return retCost;
    }

    public static String formatContent(String content)
    {
        String retContent = "";

        try
        {
            Document doc = Jsoup.parse(content);
            Elements paragraphs = doc.select("p");
            for(Element p : paragraphs)
                retContent += "<br>" + p.text() + "</br>";
    /*
            Document doc = Jsoup.parseBodyFragment(content);
            Element body = doc.body();
            retContent = "<p>" + body.text() +"</p>";
         */
        }
        catch (Exception ex)
        {
            Log.d(TAG, ex.getMessage());
        }

        return retContent;
    }
}
