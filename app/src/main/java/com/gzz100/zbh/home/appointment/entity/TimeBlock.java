package com.gzz100.zbh.home.appointment.entity;

import com.gzz100.zbh.data.entity.MeetingRoomEntity;

import java.util.Calendar;

/**
 * Created by Lam on 2018/1/31.
 */

public class TimeBlock{


    private boolean isBooked;
    private boolean isGone;
    private boolean isSelect;
    private Calendar time;
    private MeetingRoomEntity.MeetingListBean meetingBean;
    public boolean isGone() {
        return isGone;
    }

    public void setGone(boolean gone) {
        isGone = gone;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public MeetingRoomEntity.MeetingListBean getMeetingBean() {
        return meetingBean;
    }

    public void setMeetingBean(MeetingRoomEntity.MeetingListBean meetingBean) {
        this.meetingBean = meetingBean;
    }
}
