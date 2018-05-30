package com.gzz100.zbh.data.entity;

import java.util.List;

/**
 * Created by Lam on 2018/5/24.
 */

public class DelegateSummaryEntity {

    /**
     * normal : [{"departmentName":"科研3部","positionName":"Android开发工程师","departmentId":"2001","userName":"索亚","userId":"2011","postionId":"2002"}]
     * absent : [{"departmentName":"科研3部","positionName":"java开发工程师","departmentId":"2001","userName":"中佰-黄宗泽","userId":"2010","postionId":"2001"},{"departmentName":"科研3部","positionName":"java开发工程师","departmentId":"2001","userName":"zsan","userId":"2012","postionId":"2001"},{"departmentName":"产品部","positionName":"产品经理","departmentId":"2004","userName":"杜子藤","userId":"2013","postionId":"2006"},{"departmentName":"科研3部","positionName":"Android开发工程师","departmentId":"2001","userName":"二狗子","userId":"2017","postionId":"2002"},{"departmentName":"科研3部","positionName":"java开发工程师","departmentId":"2001","userName":"lisi","userId":"2018","postionId":"2001"},{"departmentName":"科研3部","positionName":"java开发工程师","departmentId":"2001","userName":"188","userId":"2019","postionId":"2001"}]
     * attendance : 14.29%
     * delegateNum : 7
     */

    private String attendance;
    private int delegateNum;
    private List<DelegateBean> normal;
    private List<DelegateBean> absent;

    public String getAttendance() {
        return attendance;
    }

    public int getDelegateNum() {
        return delegateNum;
    }

    public List<DelegateBean> getNormal() {
        return normal;
    }

    public List<DelegateBean> getAbsent() {
        return absent;
    }

    public static class DelegateBean {
        /**
         * departmentName : 科研3部
         * positionName : Android开发工程师
         * departmentId : 2001
         * userName : 索亚
         * userId : 2011
         * postionId : 2002
         */

        private String departmentName;
        private String positionName;
        private String departmentId;
        private String userName;
        private String userId;
        private String postionId;

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

        public String getPostionId() {
            return postionId;
        }

        public void setPostionId(String postionId) {
            this.postionId = postionId;
        }
    }


}
