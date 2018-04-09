package com.gzz100.zbh.data.entity;

import java.util.List;

/**
 * Created by Lam on 2018/3/23.
 */

public class MeetingInfoEntity {

    /**
     * meetingName : 会议2
     * meetingStatus : 2
     * meetingStartTime : 2017-10-12 10:30
     * delegateList : [{"signInTime":"","delegateRole":1,"delegateId":"14","delegateName":"zsan张三"},{"signInTime":"2017-10-10 10:10","delegateRole":1,"delegateId":"2","delegateName":"xiaoliu2"},{"signInTime":"2017-10-10 10:10","delegateRole":2,"delegateId":"10","delegateName":"xiaoming"},{"signInTime":"2017-10-11 10:10","delegateRole":2,"delegateId":"3","delegateName":"xiaoliu3"},{"signInTime":"2017-10-12 10:10","delegateRole":2,"delegateId":"4","delegateName":"xiaoliu4"},{"signInTime":"2017-10-10 10:10","delegateRole":2,"delegateId":"5","delegateName":"lisi"}]
     * meetingId : 2
     * agendaList : [{"duration":50,"agendaName":"议程1","spearker":"zsan张三","agendaId":"1"},{"duration":40,"agendaName":"议程2","spearker":"lisi","agendaId":"2"},{"duration":30,"agendaName":"议程3","spearker":"zsan张三","agendaId":"3"},{"duration":70,"agendaName":"议程4","spearker":"zsan张三","agendaId":"4"},{"duration":60,"agendaName":"议程5","spearker":"zsan张三","agendaId":"5"},{"duration":60,"agendaName":"（*默认议程）","spearker":"zsan张三","agendaId":"7"}]
     * meetingPlaceName : 会议室2
     * voteList : [{"voteId":"1","meetingId":"2","voteName":"投票1","voteSelectableNum":1,"voteType":1,"voteDescription":"投票1描述","voteStatus":1},{"voteId":"2","meetingId":"2","voteName":"投票2","voteSelectableNum":2,"voteType":1,"voteDescription":"投票2描述","voteStatus":3},{"voteId":"3","meetingId":"2","voteName":"投票3","voteSelectableNum":1,"voteType":1,"voteDescription":"投票3描述","voteStatus":1}]
     * currentUserSignInTime :
     * delegateStatus : 2
     * meetingEndTime : 2017-10-12 11:00
     */

    private String meetingName;
    private int meetingStatus;
    private String meetingStartTime;
    private String meetingId;
    private String meetingPlaceName;
    private String currentUserSignInTime;
    private int delegateStatus;
    private String meetingEndTime;
    private String creatorId;
    private List<DelegateListBean> delegateList;
    private List<AgendaListBean> agendaList;
    private List<VoteListBean> voteList;

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

    public String getCurrentUserSignInTime() {
        return currentUserSignInTime;
    }

    public void setCurrentUserSignInTime(String currentUserSignInTime) {
        this.currentUserSignInTime = currentUserSignInTime;
    }

    public int getDelegateStatus() {
        return delegateStatus;
    }

    public void setDelegateStatus(int delegateStatus) {
        this.delegateStatus = delegateStatus;
    }

    public String getMeetingEndTime() {
        return meetingEndTime;
    }

    public void setMeetingEndTime(String meetingEndTime) {
        this.meetingEndTime = meetingEndTime;
    }

    public List<DelegateListBean> getDelegateList() {
        return delegateList;
    }

    public void setDelegateList(List<DelegateListBean> delegateList) {
        this.delegateList = delegateList;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public List<AgendaListBean> getAgendaList() {
        return agendaList;
    }

    public void setAgendaList(List<AgendaListBean> agendaList) {
        this.agendaList = agendaList;
    }

    public List<VoteListBean> getVoteList() {
        return voteList;
    }

    public void setVoteList(List<VoteListBean> voteList) {
        this.voteList = voteList;
    }

    public static class DelegateListBean {
        /**
         * signInTime :
         * delegateRole : 1
         * delegateId : 14
         * delegateName : zsan张三
         */

        private String signInTime;
        private int delegateRole;
        private String delegateId;
        private String delegateName;

        public String getSignInTime() {
            return signInTime;
        }

        public void setSignInTime(String signInTime) {
            this.signInTime = signInTime;
        }

        public int getDelegateRole() {
            return delegateRole;
        }

        public void setDelegateRole(int delegateRole) {
            this.delegateRole = delegateRole;
        }

        public String getDelegateId() {
            return delegateId;
        }

        public void setDelegateId(String delegateId) {
            this.delegateId = delegateId;
        }

        public String getDelegateName() {
            return delegateName;
        }

        public void setDelegateName(String delegateName) {
            this.delegateName = delegateName;
        }
    }

    public static class AgendaListBean {
        /**
         * duration : 50
         * agendaName : 议程1
         * spearker : zsan张三
         * agendaId : 1
         */

        private int duration;
        private String agendaName;
        private String spearker;
        private String agendaId;

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public String getAgendaName() {
            return agendaName;
        }

        public void setAgendaName(String agendaName) {
            this.agendaName = agendaName;
        }

        public String getSpearker() {
            return spearker;
        }

        public void setSpearker(String spearker) {
            this.spearker = spearker;
        }

        public String getAgendaId() {
            return agendaId;
        }

        public void setAgendaId(String agendaId) {
            this.agendaId = agendaId;
        }
    }

    public static class VoteListBean {
        /**
         * voteId : 1
         * meetingId : 2
         * voteName : 投票1
         * voteSelectableNum : 1
         * voteType : 1
         * voteDescription : 投票1描述
         * voteStatus : 1
         */

        private String voteId;
        private String meetingId;
        private String voteName;
        private int voteSelectableNum;
        private int voteType;
        private String voteDescription;
        private int voteStatus;

        public String getVoteId() {
            return voteId;
        }

        public void setVoteId(String voteId) {
            this.voteId = voteId;
        }

        public String getMeetingId() {
            return meetingId;
        }

        public void setMeetingId(String meetingId) {
            this.meetingId = meetingId;
        }

        public String getVoteName() {
            return voteName;
        }

        public void setVoteName(String voteName) {
            this.voteName = voteName;
        }

        public int getVoteSelectableNum() {
            return voteSelectableNum;
        }

        public void setVoteSelectableNum(int voteSelectableNum) {
            this.voteSelectableNum = voteSelectableNum;
        }

        public int getVoteType() {
            return voteType;
        }

        public void setVoteType(int voteType) {
            this.voteType = voteType;
        }

        public String getVoteDescription() {
            return voteDescription;
        }

        public void setVoteDescription(String voteDescription) {
            this.voteDescription = voteDescription;
        }

        public int getVoteStatus() {
            return voteStatus;
        }

        public void setVoteStatus(int voteStatus) {
            this.voteStatus = voteStatus;
        }
    }

    @Override
    public String toString() {
        return "MeetingInfoEntity{" +
                "meetingName='" + meetingName + '\'' +
                ", meetingStatus=" + meetingStatus +
                ", meetingStartTime='" + meetingStartTime + '\'' +
                ", meetingId='" + meetingId + '\'' +
                ", meetingPlaceName='" + meetingPlaceName + '\'' +
                ", currentUserSignInTime='" + currentUserSignInTime + '\'' +
                ", delegateStatus=" + delegateStatus +
                ", meetingEndTime='" + meetingEndTime + '\'' +
                ", delegateList=" + delegateList +
                ", agendaList=" + agendaList +
                ", voteList=" + voteList +
                '}';
    }
}
