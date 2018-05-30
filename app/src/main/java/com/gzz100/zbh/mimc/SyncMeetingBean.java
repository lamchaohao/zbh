package com.gzz100.zbh.mimc;

/**
 * Created by Lam on 2018/5/8.
 */

public class SyncMeetingBean extends BaseSync{

    public static final int ACTION_CLEAR=101;
    public static final int ACTION_SWITCH_SPEAK=102;
    public static final int ACTION_SWITCH_STATUS=103;

    private String speakerId;
    private int meetingStau;
    private int actionType;

    public SyncMeetingBean(int actionType) {
        this.actionType = actionType;
    }

    public String getSpeakerId() {
        return speakerId;
    }

    public int getMeetingStau() {
        return meetingStau;
    }

    public int getActionType() {
        return actionType;
    }

    public void setSpeakerId(String speakerId) {
        this.speakerId = speakerId;
    }

    public void setMeetingStau(int meetingStau) {
        this.meetingStau = meetingStau;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }
}
