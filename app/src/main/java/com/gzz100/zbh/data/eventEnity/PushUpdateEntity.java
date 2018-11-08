package com.gzz100.zbh.data.eventEnity;

/**
 * Created by Lam on 2018/7/17.
 */

public class PushUpdateEntity {

    public enum PassthrougMsgType{
        joinCompany,
        updateUnreadMsg,
        updateUnreadMeeting,
        updateMeetingReceive,
        meetingDecrement, //自减
        msgDecrement
    }

    private PassthrougMsgType msgType;

    public PushUpdateEntity(PassthrougMsgType msgType) {
        this.msgType = msgType;
    }

    public PassthrougMsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(PassthrougMsgType msgType) {
        this.msgType = msgType;
    }

    private String meetingNum;
    private String messageNum;

    public String getMeetingNum() {
        return meetingNum;
    }

    public void setMeetingNum(String meetingNum) {
        this.meetingNum = meetingNum;
    }

    public String getMessageNum() {
        return messageNum;
    }

    public void setMessageNum(String messageNum) {
        this.messageNum = messageNum;
    }
}
