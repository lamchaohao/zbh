package com.gzz100.zbh.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Lam on 2018/10/17.
 */

public class ImageUtil {

    //保存图片到手机指定目录
    public void savaBitmap(String imgName, byte[] bytes) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String filePath = null;
            FileOutputStream fos = null;
            try {
                filePath = Environment.getExternalStorageDirectory().getCanonicalPath() + "/MyImg";
                File imgDir = new File(filePath);
                if (!imgDir.exists()) {
                    imgDir.mkdirs();
                }
                imgName = filePath + "/" + imgName;
                fos = new FileOutputStream(imgName);
                fos.write(bytes);
//                Toast.makeText(context, "图片已保存到" + filePath, Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
//            Toast.makeText(context, "请检查SD卡是否可用", Toast.LENGTH_SHORT).show();
        }
    }


}
