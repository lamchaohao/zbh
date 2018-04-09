package com.gzz100.zbh.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Lam on 2018/3/14.
 */

public class TimeFormatUtil {

    public static long formatTimeMillis(String dateTime){

//        2017-10-10 10:10
        if (dateTime.length()==16&&dateTime.contains("-")) {
            int year = Integer.parseInt(dateTime.substring(0, 4));
            int month =  Integer.parseInt(dateTime.substring(5, 7));
            month--;//月份从0开始
            int day = Integer.parseInt(dateTime.substring(8, 10));
            int hour = Integer.parseInt(dateTime.substring(11, 13));
            int minute = Integer.parseInt(dateTime.substring(14));

            Calendar calendar=Calendar.getInstance();
            calendar.set(year,month,day,hour,minute);
            calendar.clear(Calendar.SECOND);
            calendar.clear(Calendar.MILLISECOND);
            return calendar.getTimeInMillis();
        }else {
            return 0;
        }

    }

    public static long formatDateToMillis(String date){

//        2017-10-10 10:10
        if (date.length()==10&&date.contains("-")) {
            int year = Integer.parseInt(date.substring(0, 4));
            int month =  Integer.parseInt(date.substring(5, 7));
            month--;//月份从0开始
            int day = Integer.parseInt(date.substring(8, 10));
            Calendar calendar=Calendar.getInstance();
            calendar.set(year,month,day);
            calendar.clear(Calendar.MILLISECOND);
            calendar.clear(Calendar.HOUR);
            calendar.clear(Calendar.HOUR_OF_DAY);
            calendar.clear(Calendar.MINUTE);
            calendar.clear(Calendar.SECOND);
            return calendar.getTimeInMillis();
        }else {
            return 0;
        }

    }

    public static String formatDate(Date date){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
       return  sdf.format(date);
    }

    public static String formatDate(long date){
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(calendar.getTime());
    }

    public static String formatDateAndTime(long time){
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(calendar.getTime());

    }

}
