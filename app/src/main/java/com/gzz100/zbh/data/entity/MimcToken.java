package com.gzz100.zbh.data.entity;

/**
 * Created by Lam on 2018/5/8.
 */

public class MimcToken {


    /**
     * code : 1
     * msg : 成功
     * result : {"appPackage":"com.gzz100.zbh","miUserSecurityKey":"qNUMaJRU53SxlD30C3PIyw==","appId":"2882303761517707027","appAccount":"1","miChid":"9","miUserId":"10685454810873856","mimcToken":"bJRLeg7AgtSh0T13YjL/IH6EanQSrNZVxUbS6bnSP"}
     */

    private int code;
    private String message;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return message;
    }

    public DataBean getData() {
        return data;
    }

    public static class DataBean {
        /**
         * appId : $appId
         * appPackage : $appPackage
         * appAccount : $appAccount
         * miChid : $chid
         * miUserId : $uuid
         * miUserSecurityKey : $appSecret
         * token : $token
         */

        private String appId;
        private String appPackage;
        private String appAccount;
        private String miChid;
        private String miUserId;
        private String miUserSecurityKey;
        private String token;

        public String getAppId() {
            return appId;
        }


        public String getAppPackage() {
            return appPackage;
        }


        public String getAppAccount() {
            return appAccount;
        }


        public String getMiChid() {
            return miChid;
        }


        public String getMiUserId() {
            return miUserId;
        }

        public String getMiUserSecurityKey() {
            return miUserSecurityKey;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
        /**
         * appPackage : com.gzz100.zbh
         * miUserSecurityKey : qNUMaJRU53SxlD30C3PIyw==
         * appId : 2882303761517707027
         * appAccount : 1
         * miChid : 9
         * miUserId : 10685454810873856
         * mimcToken : bJRLeg7AgtSh0T13YjL/IH6EanQSrNZVxUbS6bnSP
         */




    }
}
