package com.gzz100.zbh.data.entity;

/**
 * Created by Lam on 2018/4/12.
 */

public class DelegateEntity {

    /**
     * departmentName : 技术部1
     * positionName : 职位1
     * signInTime : 2017-10-10 10:10
     * delegateRole : 2
     * delegateId : 5
     * positionId : 1
     * delegateName : lisi
     * departmentId : 1
     * summaryRole : 0
     * delegateStatus : 2
     * copyRole : 0
     */

    private String departmentName;
    private String positionName;
    private String signInTime;
    private int delegateRole;
    private String delegateId;
    private String positionId;
    private String delegateName;
    private String departmentId;
    private String userId;
    private int summaryRole;
    private int delegateStatus;
    private int copyRole;

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

    public String getSignInTime() {
        return signInTime;
    }

    public void setSignInTime(String signInTime) {
        this.signInTime = signInTime;
    }

    public int getDelegateRole() {
        return delegateRole;
    }

    public void setDelegateRole(int delegateRole) {
        this.delegateRole = delegateRole;
    }

    public String getDelegateId() {
        return delegateId;
    }

    public void setDelegateId(String delegateId) {
        this.delegateId = delegateId;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getDelegateName() {
        return delegateName;
    }

    public void setDelegateName(String delegateName) {
        this.delegateName = delegateName;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public int getSummaryRole() {
        return summaryRole;
    }

    public void setSummaryRole(int summaryRole) {
        this.summaryRole = summaryRole;
    }

    public int getDelegateStatus() {
        return delegateStatus;
    }

    public void setDelegateStatus(int delegateStatus) {
        this.delegateStatus = delegateStatus;
    }

    public int getCopyRole() {
        return copyRole;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCopyRole(int copyRole) {
        this.copyRole = copyRole;
    }
}
