package com.gzz100.zbh.utils;

import android.content.Context;

import com.orhanobut.logger.Logger;
import com.tencent.smtt.sdk.QbSdk;

import java.io.File;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;

/**
 * Created by Lam on 2018/10/16.
 */

public class QbSdkFileUtil {

    public static void openFile(Context context, File file){
        HashMap<String, String> configValue = new HashMap<>();
        //“true”表示是进入打开方式选择界面，如果不设置或设置为“false” ，则进入 miniqb 浏览器模式。
        configValue.put("style", "1");
        configValue.put("local", "false");
        configValue.put("topBarBgColor", "#263238");
        try {
            QbSdk.openFileReader(context, file.getAbsolutePath(), configValue, null);
        }catch (Exception e){
            Toasty.error(context,e.getMessage()).show();
            Logger.i(e.getMessage());
        }

    }
}
