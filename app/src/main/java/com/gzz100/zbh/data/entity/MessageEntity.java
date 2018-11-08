package com.gzz100.zbh.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lam on 2018/3/30.
 */

public class MessageEntity implements Parcelable {


    /**
     * createTime : 1530612759000
     * extra : {"type":"1","meeting":"{\"meetingEndTime\":1530617400000,\"meetingId\":\"2210\",\"meetingName\":\"会议07031900\",\"meetingPlaceName\":\"-\",\"meetingStartTime\":1530615600000}"}
     * messageId : 0153061275891
     * messageTitle : 会议通知
     * messageDescription : 会议（会议07031900）将在 2018-07-03 19:00开始，请准时参加
     */

    private long createTime;
    private String extra;
    private String messageId;
    private String messageTitle;
    private String messageDescription;
    private long sendTime;
    private String unread;


    public long getCreateTime() {
        return createTime;
    }

    public String getExtra() {
        return extra;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public String getMessageDescription() {
        return messageDescription;
    }

    public long getSendTime() {
        return sendTime;
    }

    public String getUnread() {
        return unread;
    }

    public void setUnread(String unread) {
        this.unread = unread;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.createTime);
        dest.writeString(this.extra);
        dest.writeString(this.messageId);
        dest.writeString(this.messageTitle);
        dest.writeString(this.messageDescription);
        dest.writeLong(this.sendTime);
        dest.writeString(this.unread);
    }

    public MessageEntity() {
    }

    protected MessageEntity(Parcel in) {
        this.createTime = in.readLong();
        this.extra = in.readString();
        this.messageId = in.readString();
        this.messageTitle = in.readString();
        this.messageDescription = in.readString();
        this.sendTime = in.readLong();
        this.unread = in.readString();
    }

    public static final Creator<MessageEntity> CREATOR = new Creator<MessageEntity>() {
        @Override
        public MessageEntity createFromParcel(Parcel source) {
            return new MessageEntity(source);
        }

        @Override
        public MessageEntity[] newArray(int size) {
            return new MessageEntity[size];
        }
    };
}
