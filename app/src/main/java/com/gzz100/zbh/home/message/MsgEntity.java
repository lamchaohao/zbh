package com.gzz100.zbh.home.message;

/**
 * Created by Lam on 2018/1/30.
 */

public class MsgEntity {
    private String msgBody;
    private String msgTitle;
    private String keyWord;
    private boolean isRead;
    private String updateTime;

    public MsgEntity(String msgBody) {
        this.msgBody = msgBody;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }

    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
