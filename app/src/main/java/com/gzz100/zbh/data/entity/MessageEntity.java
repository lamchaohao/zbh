package com.gzz100.zbh.data.entity;

/**
 * Created by Lam on 2018/3/30.
 */

public class MessageEntity {


    /**
     * messageId : 982564566
     * companyId : 0
     * messageDescription : 系统公告
     * messageType : 11
     * messageStatus : 1
     * createTime : 1519887049000
     * sendTime : 1519887049000
     * createMan : null
     * messageTitle : 系统公告
     * receviceId : null
     */

    private String messageId;
    private String companyId;
    private String messageDescription;
    private int messageType;
    private int messageStatus;
    private long createTime;
    private long sendTime;
    private String createMan;
    private String messageTitle;
    private String receviceId;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getMessageDescription() {
        return messageDescription;
    }

    public void setMessageDescription(String messageDescription) {
        this.messageDescription = messageDescription;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public int getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(int messageStatus) {
        this.messageStatus = messageStatus;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public Object getCreateMan() {
        return createMan;
    }

    public void setCreateMan(String createMan) {
        this.createMan = createMan;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public Object getReceviceId() {
        return receviceId;
    }

    public void setReceviceId(String receviceId) {
        this.receviceId = receviceId;
    }
}
