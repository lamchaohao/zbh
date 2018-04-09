package com.gzz100.zbh.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Lam on 2017/10/26.
 * md5加密
 */

public class MD5Utils {

    public static String getEncodedStr(String text){
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(text.getBytes());
            byte[] digest = md5.digest();
            String encode = byteToHexString(digest);
            return encode;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


        return "";
    }
    private static String byteToHexString(byte[] digest) {
        BigInteger bigInteger = new BigInteger(1, digest);
        return bigInteger.toString(16);
    }


    public static String getFileMD5(File file) {
        String value = null;
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;

    }

}
