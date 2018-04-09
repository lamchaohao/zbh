package com.gzz100.zbh.res;

/**
 * Created by Lam on 2018/3/9.
 */

public class Common {
    public static final String BASE_URL="http://192.168.1.168/zbh-app/";
    public static final String BASE_DOCUMENT_URL="http://192.168.1.168:8777/zbh-document/";
    public static final String VERSION="1.0";
    public static final String PasswordRegEx="^[0-9a-zA-Z_.,]{6,16}$";//密码格式
    public static final String NameRegEx="^([\\u4e00-\\u9fa5]{1,20}|[a-zA-Z\\.\\s]{1,20})$";
    public static final String NumRegEx="^1((3[0-9]|4[57]|5[0-35-9]|7[0678]|8[0-9])\\d{8}$)";//手机号码正则表达式
    public static String USER_PATH;
    public static String PUSH_TOKEN;

    public static void setUserPath(String userPath) {
        USER_PATH = userPath;
    }
}
