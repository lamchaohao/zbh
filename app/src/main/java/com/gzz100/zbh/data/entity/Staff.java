package com.gzz100.zbh.data.entity;

/**
 * Created by Lam on 2018/3/14.
 */

public class Staff {

    /**
     * departmentName : 销售部3
     * positionName : 职位4
     * positionId : 4
     * phone : 13536940310
     * departmentId : 3
     * userName : xiaoliu
     * userId : 8
     */

    private String departmentName;
    private String positionName;
    private String positionId;
    private String phone;
    private String departmentId;
    private String userName;
    private String userId;
    private boolean isSelect;


    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    @Override
    public String toString() {
        return "Staff{" +
                "departmentName='" + departmentName + '\'' +
                ", positionName='" + positionName + '\'' +
                ", positionId='" + positionId + '\'' +
                ", phone='" + phone + '\'' +
                ", departmentId='" + departmentId + '\'' +
                ", userName='" + userName + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
