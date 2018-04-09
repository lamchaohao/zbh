package com.gzz100.zbh.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.gzz100.zbh.utils.TimeFormatUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lam on 2018/3/9.
 */

public class MeetingRoomEntity implements Parcelable {

    private String meetingPlaceId;
    private String companyId;
    private String meetingPlaceName;
    private int meetingPlaceStatus;
    private int meetingPlaceCapacity;
    private String meetingPlaceTab;
    private String meetingPlacePic;
    private int needApply;
    private boolean enable = true;//是否可预约

    private String otherTab;
    private List<MeetingListBean> meetingList;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getMeetingPlaceId() {
        return meetingPlaceId;
    }

    public void setMeetingPlaceId(String meetingPlaceId) {
        this.meetingPlaceId = meetingPlaceId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getMeetingPlaceName() {
        return meetingPlaceName;
    }

    public void setMeetingPlaceName(String meetingPlaceName) {
        this.meetingPlaceName = meetingPlaceName;
    }

    public int getMeetingPlaceStatus() {
        return meetingPlaceStatus;
    }

    public void setMeetingPlaceStatus(int meetingPlaceStatus) {
        this.meetingPlaceStatus = meetingPlaceStatus;
    }

    public int getMeetingPlaceCapacity() {
        return meetingPlaceCapacity;
    }

    public void setMeetingPlaceCapacity(int meetingPlaceCapacity) {
        this.meetingPlaceCapacity = meetingPlaceCapacity;
    }

    public String getMeetingPlaceTab() {
        return meetingPlaceTab;
    }

    public void setMeetingPlaceTab(String meetingPlaceTab) {
        this.meetingPlaceTab = meetingPlaceTab;
    }

    public String getMeetingPlacePic() {
        return meetingPlacePic;
    }

    public void setMeetingPlacePic(String meetingPlacePic) {
        this.meetingPlacePic = meetingPlacePic;
    }

    public int getNeedApply() {
        return needApply;
    }

    public void setNeedApply(int needApply) {
        this.needApply = needApply;
    }

    public String getOtherTab() {
        return otherTab;
    }

    public void setOtherTab(String otherTab) {
        this.otherTab = otherTab;
    }

    public List<MeetingListBean> getMeetingList() {
        return meetingList;
    }

    public void setMeetingList(List<MeetingListBean> meetingList) {
        this.meetingList = meetingList;
    }

    public static class MeetingListBean {

        private String meetingId;
        private String applicant;
        private String departmentName;
        private String startTime;
        private String endTime;

        public String getMeetingId() {
            return meetingId;
        }

        public void setMeetingId(String meetingId) {
            this.meetingId = meetingId;
        }

        public String getApplicant() {
            return applicant;
        }

        public void setApplicant(String applicant) {
            this.applicant = applicant;
        }

        public String getDepartmentName() {
            return departmentName;
        }

        public void setDepartmentName(String departmentName) {
            this.departmentName = departmentName;
        }

        public long getStartTime() {
            return TimeFormatUtil.formatTimeMillis(startTime);
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public long getEndTime() {
            return TimeFormatUtil.formatTimeMillis(endTime);
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.meetingPlaceId);
        dest.writeString(this.companyId);
        dest.writeString(this.meetingPlaceName);
        dest.writeInt(this.meetingPlaceStatus);
        dest.writeInt(this.meetingPlaceCapacity);
        dest.writeString(this.meetingPlaceTab);
        dest.writeString(this.meetingPlacePic);
        dest.writeInt(this.needApply);
        dest.writeByte(this.enable ? (byte) 1 : (byte) 0);
        dest.writeString(this.otherTab);
        dest.writeList(this.meetingList);
    }

    public MeetingRoomEntity() {
    }

    protected MeetingRoomEntity(Parcel in) {
        this.meetingPlaceId = in.readString();
        this.companyId = in.readString();
        this.meetingPlaceName = in.readString();
        this.meetingPlaceStatus = in.readInt();
        this.meetingPlaceCapacity = in.readInt();
        this.meetingPlaceTab = in.readString();
        this.meetingPlacePic = in.readString();
        this.needApply = in.readInt();
        this.enable = in.readByte() != 0;
        this.otherTab = in.readString();
        this.meetingList = new ArrayList<MeetingListBean>();
        in.readList(this.meetingList, MeetingListBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<MeetingRoomEntity> CREATOR = new Parcelable.Creator<MeetingRoomEntity>() {
        @Override
        public MeetingRoomEntity createFromParcel(Parcel source) {
            return new MeetingRoomEntity(source);
        }

        @Override
        public MeetingRoomEntity[] newArray(int size) {
            return new MeetingRoomEntity[size];
        }
    };
}
