package com.gzz100.zbh.data.entity;

import java.util.List;

/**
 * Created by Lam on 2018/4/20.
 */

public class UpdateMeetingEntity {

    /**
     * hostName : 我的
     * meetingStatus : 2
     * summaryList : [{"userName":"黎明","userId":"9"}]
     * notifyTime : 15
     * copyList : [{"userName":"我的","userId":"1"},{"userName":"牛顿","userId":"10"},{"userName":"二狗子","userId":"1002"},{"userName":"ZB-黄宗泽","userId":"1004"},{"userName":"晓红","userId":"1012"},{"userName":"黄药师","userId":"11"},{"userName":"洪七公","userId":"12"},{"userName":"王重阳","userId":"2"},{"userName":"1522821816871","userId":"2001"},{"userName":"欧阳锋","userId":"21"},{"userName":"谢逊","userId":"22"},{"userName":"令狐冲","userId":"23"},{"userName":"周济","userId":"25"},{"userName":"周树人","userId":"3"},{"userName":"闻一多","userId":"4"},{"userName":"古天乐","userId":"5"},{"userName":"渣渣辉","userId":"6"},{"userName":"张学友","userId":"7"},{"userName":"黎明","userId":"9"}]
     * hostId : 1
     * meetingId : 2029
     * agendaList : [{"agendaName":"议程1","speaker":"刘德华","agendaId":"2004"}]
     * meetingEndTime : 2018-04-16 18:15
     * meetingName : 会议0416
     * meetingStartTime : 2018-04-16 17:15
     * delegateList : [{"userName":"我的","userId":"1"},{"userName":"刘德华","userId":"8"},{"userName":"牛顿","userId":"10"},{"userName":"二狗子","userId":"1002"},{"userName":"ZB-黄宗泽","userId":"1004"},{"userName":"晓红","userId":"1012"},{"userName":"黄药师","userId":"11"},{"userName":"洪七公","userId":"12"},{"userName":"王重阳","userId":"2"},{"userName":"1522821816871","userId":"2001"},{"userName":"欧阳锋","userId":"21"},{"userName":"谢逊","userId":"22"},{"userName":"令狐冲","userId":"23"},{"userName":"周济","userId":"25"},{"userName":"周树人","userId":"3"},{"userName":"闻一多","userId":"4"},{"userName":"古天乐","userId":"5"},{"userName":"渣渣辉","userId":"6"},{"userName":"张学友","userId":"7"},{"userName":"黎明","userId":"9"}]
     * meetingApplyStatus : 4
     * meetingPlaceId : 3
     */

    private String hostName;
    private int meetingStatus;
    private String notifyTime;
    private String hostId;
    private String meetingId;
    private String meetingEndTime;
    private String meetingName;
    private String meetingStartTime;
    private int meetingApplyStatus;
    private String meetingPlaceId;
    private StaffBean summaryPerson;
    private List<StaffBean> copyList;
    private List<AgendaListBean> agendaList;
    private List<StaffBean> delegateList;

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getMeetingStatus() {
        return meetingStatus;
    }

    public void setMeetingStatus(int meetingStatus) {
        this.meetingStatus = meetingStatus;
    }

    public String getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(String notifyTime) {
        this.notifyTime = notifyTime;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getMeetingEndTime() {
        return meetingEndTime;
    }

    public void setMeetingEndTime(String meetingEndTime) {
        this.meetingEndTime = meetingEndTime;
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    public String getMeetingStartTime() {
        return meetingStartTime;
    }

    public void setMeetingStartTime(String meetingStartTime) {
        this.meetingStartTime = meetingStartTime;
    }

    public int getMeetingApplyStatus() {
        return meetingApplyStatus;
    }

    public void setMeetingApplyStatus(int meetingApplyStatus) {
        this.meetingApplyStatus = meetingApplyStatus;
    }

    public String getMeetingPlaceId() {
        return meetingPlaceId;
    }

    public void setMeetingPlaceId(String meetingPlaceId) {
        this.meetingPlaceId = meetingPlaceId;
    }


    public List<StaffBean> getCopyList() {
        return copyList;
    }

    public void setCopyList(List<StaffBean> copyList) {
        this.copyList = copyList;
    }

    public List<AgendaListBean> getAgendaList() {
        return agendaList;
    }

    public void setAgendaList(List<AgendaListBean> agendaList) {
        this.agendaList = agendaList;
    }

    public List<StaffBean> getDelegateList() {
        return delegateList;
    }

    public void setDelegateList(List<StaffBean> delegateList) {
        this.delegateList = delegateList;
    }

    public StaffBean getSummaryPerson() {
        return summaryPerson;
    }

    public void setSummaryPerson(StaffBean summaryPerson) {
        this.summaryPerson = summaryPerson;
    }

    public static class StaffBean {
        /**
         * userName : 我的
         * userId : 1
         */

        private String userName;
        private String userId;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }

    public static class AgendaListBean {
        /**
         * agendaName : 议程1
         * speaker : 刘德华
         * agendaId : 2004
         */

        private String agendaName;
        private String speaker;
        private String speakerId;
        private String agendaId;

        public String getAgendaName() {
            return agendaName;
        }

        public void setAgendaName(String agendaName) {
            this.agendaName = agendaName;
        }

        public String getSpeaker() {
            return speaker;
        }

        public void setSpeaker(String speaker) {
            this.speaker = speaker;
        }

        public String getAgendaId() {
            return agendaId;
        }

        public void setAgendaId(String agendaId) {
            this.agendaId = agendaId;
        }

        public String getSpeakerId() {
            return speakerId;
        }

        public void setSpeakerId(String speakerId) {
            this.speakerId = speakerId;
        }
    }

    @Override
    public String toString() {
        return "UpdateMeetingEntity{" +
                "hostName='" + hostName + '\'' +
                ", meetingStatus=" + meetingStatus +
                ", notifyTime='" + notifyTime + '\'' +
                ", hostId='" + hostId + '\'' +
                ", meetingId='" + meetingId + '\'' +
                ", meetingEndTime='" + meetingEndTime + '\'' +
                ", meetingName='" + meetingName + '\'' +
                ", meetingStartTime='" + meetingStartTime + '\'' +
                ", meetingApplyStatus=" + meetingApplyStatus +
                ", meetingPlaceId='" + meetingPlaceId + '\'' +
                ", summaryPerson=" + summaryPerson +
                ", copyList=" + copyList +
                ", agendaList=" + agendaList +
                ", delegateList=" + delegateList +
                '}';
    }
}
