package com.gzz100.zbh;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Process;
import android.support.multidex.MultiDex;

import com.gzz100.zbh.res.Common;
import com.huawei.android.hms.agent.HMSAgent;
import com.huawei.android.hms.agent.push.handler.GetTokenHandler;
import com.huawei.hms.support.api.push.TokenResult;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.smtt.sdk.QbSdk;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

/**
 * Created by Lam on 2018/1/15.
 */

public class App extends Application {

    public static final String APP_ID = "2882303761517707027";
    public static final String APP_KEY = "5221770780027";
    public static final String APP_SCRET ="dq4wN2spI9ye0ieNSbmJVA==";

    @Override
    public void onCreate() {
        super.onCreate();
        initCommon();
        initPushModel();
        //tencent QQbrowser initial
        QbSdk.initX5Environment(this,null);
        //logger initial
        Logger.addLogAdapter(new AndroidLogAdapter());

    }

    private void initPushModel() {
        //xiaomiPush
        initXiaomiPush();
        //huawei push
//        initHMSPush();

    }

    private void initXiaomiPush() {
        if (shouldInit()){
            MiPushClient.registerPush(this, APP_ID, APP_KEY);
        }
        initPushLogger();
    }

    private void initHMSPush() {
        if (HMSAgent.init(this)) {
            getHMSToken();

        }
    }

    private void initCommon() {
        Common.setUserPath(this.getFilesDir().getAbsolutePath()+"user");
    }

    private void initPushLogger() {
        LoggerInterface newLogger = new LoggerInterface() {
            @Override
            public void setTag(String tag) {
                // ignore
            }
            @Override
            public void log(String content, Throwable t) {
                Logger.i(content, t);
            }
            @Override
            public void log(String content) {
                Logger.i(content);
            }
        };
        com.xiaomi.mipush.sdk.Logger.setLogger(this, newLogger);
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    private void getHMSToken() {
        Logger.i("get token: begin");
        HMSAgent.Push.getToken(new GetTokenHandler() {
            @Override
            public void onResult(int rtnCode, TokenResult tokenResult) {
                Logger.e("get token: end erroCode=" + rtnCode+"");
//                Logger.e(tokenResult.getTokenRes().getToken());
            }
        });
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this) ;
    }
}
