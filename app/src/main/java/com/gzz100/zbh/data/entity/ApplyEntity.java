package com.gzz100.zbh.data.entity;

/**
 * Created by Lam on 2018/4/10.
 */

public class ApplyEntity {

    /**
     * applyId : 2015
     * companyId : 1
     * createTime : 2018-04-10 10:41:09
     * companyName : 中佰公司
     * userId : 2001
     * applyStatus : 1
     */

    private String applyId;
    private String companyId;
    private String createTime;
    private String companyName;
    private String userId;
    private int applyStatus;

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(int applyStatus) {
        this.applyStatus = applyStatus;
    }
}
