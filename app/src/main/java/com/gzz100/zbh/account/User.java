package com.gzz100.zbh.account;

import com.google.gson.annotations.SerializedName;
import com.gzz100.zbh.res.Common;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Lam on 2018/3/9.
 */

public class User implements Serializable {

    private String departmentName;
    private String positionName;
    private String companyId;
    private String positionId;
    private String phone;
    private String departmentId;
    private String companyName;
    private String userName;
    private String userId;
    private String token;
    private boolean isLogin;
    private static User sUser;
    /**
     * apply : {"applyId":"12","companyId":"1","companyName":"中佰公司"}
     */

    private ApplyBean apply;

    //保存数据
    public static void save(User user){
        ObjectOutputStream fos=null;
        try {
            File file=new File(Common.USER_PATH);
            fos=new ObjectOutputStream(new FileOutputStream(file));

            //这里不能再用普通的write的方法了
            //要使用writeObject
            fos.writeObject(user);
            sUser = user;
            Logger.i("save: "+user.getToken());
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if (fos!=null) {
                    fos.close();
                }
            } catch (IOException e) {
            }

        }

    }

    //读取数据
    public static User getUserFromCache() {
        if (sUser==null){
            ObjectInputStream ois = null;
            try {
                //获取输入流
                ois = new ObjectInputStream(new FileInputStream(Common.USER_PATH));
                //获取文件中的数据
                sUser = (User) ois.readObject();
                Logger.i("getUserFromCache: "+sUser.toString());
                return sUser;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (ois != null) {
                        ois.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else {
            return sUser;
        }

        return sUser;
    }


    public static boolean logout(){
        File file = new File(Common.USER_PATH);
        sUser=null;
        return file.delete();

    }

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

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    @Override
    public String toString() {
        return "User{" +
                "departmentName='" + departmentName + '\'' +
                ", positionName='" + positionName + '\'' +
                ", companyId='" + companyId + '\'' +
                ", positionId='" + positionId + '\'' +
                ", phone='" + phone + '\'' +
                ", departmentId='" + departmentId + '\'' +
                ", companyName='" + companyName + '\'' +
                ", userName='" + userName + '\'' +
                ", userId='" + userId + '\'' +
                ", token='" + token + '\'' +
                ", isLogin=" + isLogin +
                ", apply=" + apply +
                '}';
    }

    public ApplyBean getApply() {
        return apply;
    }

    public void setApply(ApplyBean apply) {
        this.apply = apply;
    }

    public static class ApplyBean implements Serializable{
        /**
         * applyId : 12
         * companyId : 1
         * companyName : 中佰公司
         */

        private String applyId;
        @SerializedName("companyId")
        private String companyIdX;
        @SerializedName("companyName")
        private String companyNameX;

        public String getApplyId() {
            return applyId;
        }

        public void setApplyId(String applyId) {
            this.applyId = applyId;
        }

        public String getCompanyIdX() {
            return companyIdX;
        }

        public void setCompanyIdX(String companyIdX) {
            this.companyIdX = companyIdX;
        }

        public String getCompanyNameX() {
            return companyNameX;
        }

        public void setCompanyNameX(String companyNameX) {
            this.companyNameX = companyNameX;
        }

        @Override
        public String toString() {
            return "ApplyBean{" +
                    "applyId='" + applyId + '\'' +
                    ", companyIdX='" + companyIdX + '\'' +
                    ", companyNameX='" + companyNameX + '\'' +
                    '}';
        }
    }
}
