package com.gzz100.zbh.home.appointment.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lam on 2018/3/7.
 */

public class VoteWrap implements Parcelable {
    public static final int CODE_DELETE = -1;
    public static final int CODE_ADD = 200;
    public static final int CODE_UPDATE = 2;
    private int code;//-1 delete, 200 a new instance, 2 update;
    private String voteName;
    private String voteDespc;
    private List<VoteOption> mOptionList;
    private boolean isSingle;
    private boolean isHideName;
    private int maxCount = 1;
    private boolean isAutoStart;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getVoteName() {
        return voteName;
    }

    public void setVoteName(String voteName) {
        this.voteName = voteName;
    }

    public String getVoteDespc() {
        return voteDespc;
    }

    public void setVoteDespc(String voteDespc) {
        this.voteDespc = voteDespc;
    }

    public List<VoteOption> getOptionList() {
        return mOptionList;
    }

    public void setOptionList(List<VoteOption> optionList) {
        mOptionList = optionList;
    }

    public boolean isSingle() {
        return isSingle;
    }

    public void setSingle(boolean single) {
        isSingle = single;
    }

    public boolean isHideName() {
        return isHideName;
    }

    public void setHideName(boolean hideName) {
        isHideName = hideName;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public boolean isAutoStart() {
        return isAutoStart;
    }

    public void setAutoStart(boolean autoStart) {
        isAutoStart = autoStart;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeString(this.voteName);
        dest.writeString(this.voteDespc);
        dest.writeList(this.mOptionList);
        dest.writeByte(this.isSingle ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isHideName ? (byte) 1 : (byte) 0);
        dest.writeInt(this.maxCount);
        dest.writeByte(this.isAutoStart ? (byte) 1 : (byte) 0);
    }

    public VoteWrap() {
    }

    protected VoteWrap(Parcel in) {
        this.code = in.readInt();
        this.voteName = in.readString();
        this.voteDespc = in.readString();
        this.mOptionList = new ArrayList<VoteOption>();
        in.readList(this.mOptionList, VoteOption.class.getClassLoader());
        this.isSingle = in.readByte() != 0;
        this.isHideName = in.readByte() != 0;
        this.maxCount = in.readInt();
        this.isAutoStart = in.readByte() != 0;
    }

    public static final Parcelable.Creator<VoteWrap> CREATOR = new Parcelable.Creator<VoteWrap>() {
        @Override
        public VoteWrap createFromParcel(Parcel source) {
            return new VoteWrap(source);
        }

        @Override
        public VoteWrap[] newArray(int size) {
            return new VoteWrap[size];
        }
    };
}
