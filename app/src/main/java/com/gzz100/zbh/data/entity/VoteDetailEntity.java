package com.gzz100.zbh.data.entity;

import java.util.List;

/**
 * Created by Lam on 2018/4/10.
 */

public class VoteDetailEntity {


    private int voteSelectableNum;
    private String voteName;
    private String voteDescription;
    private int voteModel;
    private String meetingId;
    private int voteStatus;
    private String voterNum;
    private String voteId;
    private int voteAnonymous;
    private List<String> voterIdList;
    private List<VoteOptionListBean> voteOptionList;

    public int getVoteSelectableNum() {
        return voteSelectableNum;
    }

    public void setVoteSelectableNum(int voteSelectableNum) {
        this.voteSelectableNum = voteSelectableNum;
    }

    public String getVoteName() {
        return voteName;
    }

    public void setVoteName(String voteName) {
        this.voteName = voteName;
    }

    public String getVoteDescription() {
        return voteDescription;
    }

    public void setVoteDescription(String voteDescription) {
        this.voteDescription = voteDescription;
    }

    public int getVoteModel() {
        return voteModel;
    }

    public void setVoteModel(int voteModel) {
        this.voteModel = voteModel;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public int getVoteStatus() {
        return voteStatus;
    }

    public void setVoteStatus(int voteStatus) {
        this.voteStatus = voteStatus;
    }

    public String getVoterNum() {
        return voterNum;
    }

    public void setVoterNum(String voterNum) {
        this.voterNum = voterNum;
    }

    public String getVoteId() {
        return voteId;
    }

    public void setVoteId(String voteId) {
        this.voteId = voteId;
    }

    public int getVoteAnonymous() {
        return voteAnonymous;
    }

    public void setVoteAnonymous(int voteAnonymous) {
        this.voteAnonymous = voteAnonymous;
    }

    public List<String> getVoterIdList() {
        return voterIdList;
    }

    public void setVoterIdList(List<String> voterIdList) {
        this.voterIdList = voterIdList;
    }

    public List<VoteOptionListBean> getVoteOptionList() {
        return voteOptionList;
    }

    public void setVoteOptionList(List<VoteOptionListBean> voteOptionList) {
        this.voteOptionList = voteOptionList;
    }

    public static class VoteOptionListBean {

        private String voteOptionId;
        private String voteId;
        private String voteOptionName;
        private String voteDocumentPath;
        private List<UserListBean> userList;

        public String getVoteOptionId() {
            return voteOptionId;
        }

        public void setVoteOptionId(String voteOptionId) {
            this.voteOptionId = voteOptionId;
        }

        public String getVoteId() {
            return voteId;
        }

        public void setVoteId(String voteId) {
            this.voteId = voteId;
        }

        public String getVoteOptionName() {
            return voteOptionName;
        }

        public void setVoteOptionName(String voteOptionName) {
            this.voteOptionName = voteOptionName;
        }

        public String getVoteDocumentPath() {
            return voteDocumentPath;
        }

        public void setVoteDocumentPath(String voteDocumentPath) {
            this.voteDocumentPath = voteDocumentPath;
        }

        public List<UserListBean> getUserList() {
            return userList;
        }

        public void setUserList(List<UserListBean> userList) {
            this.userList = userList;
        }

        public static class UserListBean{
            private String userId;
            private String userName;

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }
        }
    }
}
