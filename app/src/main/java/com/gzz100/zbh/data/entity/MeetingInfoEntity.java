package com.gzz100.zbh.data.entity;

import java.util.List;

/**
 *
 * Created by Lam on 2018/3/23.
 */

public class MeetingInfoEntity {


    /**
     * meetingName : 文件会议
     * meetingStatus : 2
     * meetingStartTime : 2018-04-02 09:30
     * delegateList : [{"signInTime":null,"delegateRole":1,"delegateId":"1279","delegateName":"令狐冲","delegateStatus":1,"userId":"23"},{"signInTime":null,"delegateRole":2,"delegateId":"1280","delegateName":"刘德华","delegateStatus":1,"userId":"8"},{"signInTime":null,"delegateRole":2,"delegateId":"1281","delegateName":"令狐冲","delegateStatus":1,"userId":"23"},{"signInTime":null,"delegateRole":2,"delegateId":"1282","delegateName":"闻一多","delegateStatus":1,"userId":"4"},{"signInTime":null,"delegateRole":2,"delegateId":"1283","delegateName":"周济","delegateStatus":1,"userId":"25"},{"signInTime":null,"delegateRole":2,"delegateId":"1284","delegateName":"渣渣辉","delegateStatus":1,"userId":"6"},{"signInTime":null,"delegateRole":2,"delegateId":"1285","delegateName":"古天乐","delegateStatus":1,"userId":"5"},{"signInTime":null,"delegateRole":2,"delegateId":"1286","delegateName":"二狗子","delegateStatus":1,"userId":"1002"},{"signInTime":null,"delegateRole":2,"delegateId":"1287","delegateName":"黄药师","delegateStatus":1,"userId":"11"},{"signInTime":null,"delegateRole":2,"delegateId":"1288","delegateName":"黎明","delegateStatus":1,"userId":"9"},{"signInTime":null,"delegateRole":2,"delegateId":"1289","delegateName":"周伯通","delegateStatus":1,"userId":"1004"},{"signInTime":null,"delegateRole":2,"delegateId":"1290","delegateName":"王重阳","delegateStatus":1,"userId":"2"},{"signInTime":null,"delegateRole":2,"delegateId":"1291","delegateName":"洪七公","delegateStatus":1,"userId":"12"},{"signInTime":null,"delegateRole":2,"delegateId":"1292","delegateName":"牛顿","delegateStatus":1,"userId":"10"},{"signInTime":null,"delegateRole":2,"delegateId":"1293","delegateName":"谢逊","delegateStatus":1,"userId":"22"},{"signInTime":null,"delegateRole":2,"delegateId":"1294","delegateName":"晓红","delegateStatus":1,"userId":"1012"},{"signInTime":null,"delegateRole":2,"delegateId":"1295","delegateName":"阿基米德","delegateStatus":1,"userId":"1"},{"signInTime":null,"delegateRole":2,"delegateId":"1296","delegateName":"欧阳锋","delegateStatus":1,"userId":"21"}]
     * creatorId : 6
     * meetingId : 1038
     * agendaList : [{"duration":null,"agendaName":"议程一","spearker":"刘德华","agendaId":"1020"}]
     * meetingPlaceName : 会议室8
     * voteList : [{"voteId":"2006","meetingId":"1038","voteName":"选出你喜欢的表情包","voteSelectableNum":2,"voteType":null,"voteDescription":"","voteStatus":2,"voteModel":2,"voteAnonymous":2},{"voteId":"2005","meetingId":"1038","voteName":"投票一","voteSelectableNum":1,"voteType":null,"voteDescription":"","voteStatus":3,"voteModel":2,"voteAnonymous":2}]
     * meetingEndTime : 2018-04-02 10:00
     */

    private String meetingName;
    private int meetingStatus;
    private String meetingStartTime;
    private String creatorId;
    private String meetingId;
    private String meetingPlaceName;
    private String meetingEndTime;
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

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
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

    public List<DelegateListBean> getDelegateList() {
        return delegateList;
    }

    public void setDelegateList(List<DelegateListBean> delegateList) {
        this.delegateList = delegateList;
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
         * signInTime : null
         * delegateRole : 1
         * delegateId : 1279
         * delegateName : 令狐冲
         * delegateStatus : 1
         * userId : 23
         */

        private Object signInTime;
        private int delegateRole;
        private String delegateId;
        private String delegateName;
        private int delegateStatus;
        private String userId;

        public Object getSignInTime() {
            return signInTime;
        }

        public void setSignInTime(Object signInTime) {
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

        public int getDelegateStatus() {
            return delegateStatus;
        }

        public void setDelegateStatus(int delegateStatus) {
            this.delegateStatus = delegateStatus;
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
         * duration : null
         * agendaName : 议程一
         * spearker : 刘德华
         * agendaId : 1020
         */

        private Object duration;
        private String agendaName;
        private String spearker;
        private String agendaId;

        public Object getDuration() {
            return duration;
        }

        public void setDuration(Object duration) {
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
         * voteId : 2006
         * meetingId : 1038
         * voteName : 选出你喜欢的表情包
         * voteSelectableNum : 2
         * voteType : null
         * voteDescription :
         * voteStatus : 2
         * voteModel : 2
         * voteAnonymous : 2
         */

        private String voteId;
        private String meetingId;
        private String voteName;
        private int voteSelectableNum;
        private Object voteType;
        private String voteDescription;
        private int voteStatus;
        private int voteModel;
        private int voteAnonymous;

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

        public Object getVoteType() {
            return voteType;
        }

        public void setVoteType(Object voteType) {
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

        public int getVoteModel() {
            return voteModel;
        }

        public void setVoteModel(int voteModel) {
            this.voteModel = voteModel;
        }

        public int getVoteAnonymous() {
            return voteAnonymous;
        }

        public void setVoteAnonymous(int voteAnonymous) {
            this.voteAnonymous = voteAnonymous;
        }
    }
}
