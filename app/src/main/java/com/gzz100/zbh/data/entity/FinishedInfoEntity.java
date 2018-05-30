package com.gzz100.zbh.data.entity;

/**
 * Created by Lam on 2018/5/3.
 */

public class FinishedInfoEntity {


    /**
     * meetingName : lll
     * fileNum : 2
     * meetingStartTime : 2018-04-04 10:15
     * voteNum : 1
     * host : zsan张三
     * hostId : 1
     * meetingPlaceName : 会议室1
     * meetingSummary : null
     * meetingEndTime : 2018-04-04 11:00
     * delegateNum : 2
     */

    private String meetingName;
    private String fileNum;
    private String meetingStartTime;
    private String voteNum;
    private String host;
    private String hostId;
    private String meetingPlaceName;
    private boolean meetingSummary;
    private String meetingEndTime;
    private String delegateNum;

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    public String getFileNum() {
        return fileNum;
    }

    public void setFileNum(String fileNum) {
        this.fileNum = fileNum;
    }

    public String getMeetingStartTime() {
        return meetingStartTime;
    }

    public void setMeetingStartTime(String meetingStartTime) {
        this.meetingStartTime = meetingStartTime;
    }

    public String getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(String voteNum) {
        this.voteNum = voteNum;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public String getMeetingPlaceName() {
        return meetingPlaceName;
    }

    public void setMeetingPlaceName(String meetingPlaceName) {
        this.meetingPlaceName = meetingPlaceName;
    }

    public boolean getMeetingSummary() {
        return meetingSummary;
    }

    public void setMeetingSummary(boolean meetingSummary) {
        this.meetingSummary = meetingSummary;
    }

    public String getMeetingEndTime() {
        return meetingEndTime;
    }

    public void setMeetingEndTime(String meetingEndTime) {
        this.meetingEndTime = meetingEndTime;
    }

    public String getDelegateNum() {
        return delegateNum;
    }

    public void setDelegateNum(String delegateNum) {
        this.delegateNum = delegateNum;
    }
}
