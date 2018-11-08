package com.gzz100.zbh.utils;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

/**
 * Created by Lam on 2018/3/15.
 */

public class TextHeadPicUtil {



    public static TextDrawable getHeadPic(String userName) {
        if (userName==null) {
            userName="缺省";
        }
        TextDrawable drawable = TextDrawable.builder()
            .beginConfig()
            .fontSize(36)
            .endConfig()
            .buildRound(getHeadPicName(userName), getColor(userName));

        return drawable;
    }

    public static TextDrawable getHeadPic(String userName, int fontSize,int width){
        if (userName==null) {
            userName="缺省";
        }
        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .fontSize(fontSize)
                .width(width)
                .height(width)
                .endConfig()
                .buildRound(getHeadPicName(userName), getColor(userName));

        return drawable;
    }

    public static TextDrawable getHeadPic(String userName, int fontSize){
        if (userName==null) {
            userName="缺省";
        }
        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .fontSize(fontSize)
                .endConfig()
                .buildRound(getHeadPicName(userName), getColor(userName));

        return drawable;
    }

    private static String getHeadPicName(String userName){
        String picName=null;
        if (userName==null){

            userName="缺省";
        }
        if (userName.length()>2){
            picName = userName.substring(userName.length()-2, userName.length());
        }else {
            picName=userName;
        }
        return picName;
    }

    public static int getColor(String userName){
        ColorGenerator generator = ColorGenerator.MATERIAL;
        return generator.getColor(userName);
    }
}
