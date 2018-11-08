package com.gzz100.zbh.data.entity;

/**
 * Created by Lam on 2018/3/19.
 */

public class MeetingEntity {

    /**
     * meetingName : 会议2
     * meetingStatus : 1 未开始, 2 进行中, 3 已结束
     * meetingStartTime : 2017-10-12 10:30
     * meetingId : 2
     * meetingPlaceName : 会议室2
     * meetingEndTime : 2017-10-12 11:00
     *  1 未审核, 2 通过,3不通过,4 不需要审核
     */

    private String meetingName;
    private int meetingStatus;
    private String meetingStartTime;
    private String meetingId;
    private String meetingPlaceName;
    private String meetingEndTime;
    private String mimcTopicId;
    private int meetingRole;
    private String unread;//unread":"5153913603351"
    /**
     * meetingApplyStatus : 4
     */

    private int meetingApplyStatus;

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    public int getMeetingStatus() {
        return meetingStatus;
    }

    public void setMeetingStatus(int meetingStatus) {
        this.meetingStatus = meetingStatus;
    }

    public String getMeetingStartTime() {
        return meetingStartTime;
    }

    public void setMeetingStartTime(String meetingStartTime) {
        this.meetingStartTime = meetingStartTime;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getMeetingPlaceName() {
        return meetingPlaceName;
    }

    public void setMeetingPlaceName(String meetingPlaceName) {
        this.meetingPlaceName = meetingPlaceName;
    }

    public String getMeetingEndTime() {
        return meetingEndTime;
    }

    public void setMeetingEndTime(String meetingEndTime) {
        this.meetingEndTime = meetingEndTime;
    }

    public int getMeetingApplyStatus() {
        return meetingApplyStatus;
    }

    public void setMeetingApplyStatus(int meetingApplyStatus) {
        this.meetingApplyStatus = meetingApplyStatus;
    }

    public String getUnread() {
        return unread;
    }

    public void setUnread(String unread) {
        this.unread = unread;
    }

    public String getMimcTopicId() {
        return mimcTopicId;
    }

    public void setMimcTopicId(String mimcTopicId) {
        this.mimcTopicId = mimcTopicId;
    }

    public int getMeetingRole() {
        return meetingRole;
    }

    public void setMeetingRole(int meetingRole) {
        this.meetingRole = meetingRole;
    }
}
