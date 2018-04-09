package com.gzz100.zbh.data.entity;

/**
 *
 * Created by Lam on 2018/4/3.
 */

public class VoteEntity {

    /**
     * voteId : 1021
     * meetingId : 1
     * voteName : 投票111
     * voteSelectableNum : 1
     * voteType : 1
     * voteDescription : 投票111描述
     * voteStatus : 1
     * voteModel : 1
     * voteAnonymous : 2
     */

    private String voteId;
    private String meetingId;
    private String voteName;
    private int voteSelectableNum;
    private int voteType;
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
