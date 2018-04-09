package com.gzz100.zbh.home.appointment.entity;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Lam on 2018/2/1.
 */

public class BookedTime {

    private Calendar startTime;
    private Calendar endTime;

    public BookedTime(Calendar startTime, Calendar endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public BookedTime(Date startTime, Date endTime) {
        Calendar start = Calendar.getInstance();
        start.setTime(startTime);
        Calendar end = Calendar.getInstance();
        start.setTime(endTime);
        this.startTime = start;
        this.endTime = end;
    }

    public BookedTime(long startTime, long endTime) {
        Calendar start = Calendar.getInstance();
        start.setTimeInMillis(startTime);
        Calendar end = Calendar.getInstance();
        start.setTimeInMillis(endTime);
        this.startTime = start;
        this.endTime = end;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }
}
