package com.neo.gas_ec.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by root on 26/02/16.
 */
public class DateUtil {

    private static final String TAG = "DateUtils";

    public static Date stringToDate (String strDate, String formatDate){

        DateFormat format = new SimpleDateFormat(formatDate, Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG,e.getMessage());
        }
        return date;
    }

    public static String dateToString(Date date, String format){
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        return df.format(format, date).toString();
    }
}
