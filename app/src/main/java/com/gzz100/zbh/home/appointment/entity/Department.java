package com.gzz100.zbh.home.appointment.entity;

import com.gzz100.zbh.data.entity.Staff;

import java.util.List;

/**
 * Created by Lam on 2018/2/2.
 */

public class Department {
    private long departmentId;
    private String departmentName;
    private List<Staff> userList;
    private boolean isSelect;

    public long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public List<Staff> getStaffs() {
        return userList;
    }

    public void setStaffs(List<Staff> staffs) {
        this.userList = staffs;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
