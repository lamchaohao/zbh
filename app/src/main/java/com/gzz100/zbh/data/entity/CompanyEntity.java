package com.gzz100.zbh.data.entity;

/**
 * Created by Lam on 2018/3/12.
 */

public class CompanyEntity {

    /**
     * companyId : 1
     * companyPic : companyPic
     * companyName : 中佰公司
     */

    private String companyId;
    private String companyPic;
    private String companyName;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyPic() {
        return companyPic;
    }

    public void setCompanyPic(String companyPic) {
        this.companyPic = companyPic;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        return "CompanyEntity{" +
                "companyId='" + companyId + '\'' +
                ", companyPic='" + companyPic + '\'' +
                ", companyName='" + companyName + '\'' +
                '}';
    }
}
