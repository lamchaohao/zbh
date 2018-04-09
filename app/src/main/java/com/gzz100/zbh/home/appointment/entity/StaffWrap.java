package com.gzz100.zbh.home.appointment.entity;

import com.gzz100.zbh.data.entity.Staff;

import java.util.List;
/**
 * Created by Lam on 2018/2/6.
 */

public class StaffWrap {
    private int requestCode;
    private List<Staff> mStaffList;

    public StaffWrap(int requestCode, List<Staff> staffList) {
        this.requestCode = requestCode;
        mStaffList = staffList;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public List<Staff> getStaffList() {
        return mStaffList;
    }

    public void setStaffList(List<Staff> staffList) {
        mStaffList = staffList;
    }
}
