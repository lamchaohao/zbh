package com.gzz100.zbh.mimc;

/**
 * Created by Lam on 2018/5/7.
 */

public class BaseMCMsg {
    protected int version;    // 建议保留version字段，方便后续协议升级兼容
    private String msgId;   // APP业务层面自维护消息ID
    private int msgType;    // PING|PONG|TEXT|PIC_FILE|AUDIO_FILE|BIN_FILE|...
    private long timestamp;   //时间戳
    private String msgBody;     //消息体

    public int getVersion() {
        return version;
    }

    public String getMsgId() {
        return msgId;
    }

    public int getMsgType() {
        return msgType;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }
}
