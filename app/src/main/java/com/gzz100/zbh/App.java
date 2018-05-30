package com.gzz100.zbh;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Process;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.gzz100.zbh.mimc.MCUserManager;
import com.gzz100.zbh.res.Common;
import com.huawei.android.hms.agent.HMSAgent;
import com.huawei.android.hms.agent.push.handler.GetTokenHandler;
import com.huawei.hms.support.api.push.TokenResult;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.smtt.sdk.QbSdk;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mimc.MIMCClient;
import com.xiaomi.mimc.MIMCConstant;
import com.xiaomi.mimc.MIMCException;
import com.xiaomi.mimc.MIMCLogger;
import com.xiaomi.mimc.MIMCLoggerInterface;
import com.xiaomi.mimc.MIMCUser;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

/**
 *
 * Created by Lam on 2018/1/15.
 */

public class App extends Application {
    private String TAG = "mimc";
    public static final String WECHAT_APP_ID="wxff40b682ebb7b701";
    public static final String WECHAT_APP_SCRET="2c5c60511a9009b130b31f0d5e983bd7";
    public static final String APP_ID = "2882303761517707027";
    public static final String APP_KEY = "5221770780027";
    public static final String APP_SCRET ="dq4wN2spI9ye0ieNSbmJVA==";
    private int mCount = 0;
    private IWXAPI mWxapi;

    @Override
    public void onCreate() {
        super.onCreate();
        initCommon();
        initPushModel();
        //tencent QQbrowser initial
        QbSdk.initX5Environment(this,null);
        //logger initial
        Logger.addLogAdapter(new AndroidLogAdapter());
        //小米即时消息云
        initMimc();
        initWechatShare();

    }

    private void initWechatShare() {
        mWxapi = WXAPIFactory.createWXAPI(this, WECHAT_APP_ID, true);
        mWxapi.registerApp(WECHAT_APP_ID);

    }


    private void initMimc() {

        MIMCLoggerInterface logger=new MIMCLoggerInterface() {
            @Override
            public void setTag(String s) {

            }

            @Override
            public void log(String s) {
                Log.d(TAG,s);
            }

            @Override
            public void log(String s, Throwable throwable) {
                Logger.e(s);
            }
        };
        MIMCLogger.setLogger(logger);
        MIMCLogger.setLogLevel(MIMCLogger.INFO);
        MIMCClient.initialize(this);
        MIMCLogger.enableMIMCLog(getApplicationContext(), true);
        registerLifeCycler();
    }

    private void registerLifeCycler() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
                mCount++;
//                 切换到前台
                if (mCount == 1) {
                    MIMCUser user = MCUserManager.getInstance().getUser();
                    if (user != null) {
                        try {
                            user.login();
                            // 建议，拉一下数据
                            if (MCUserManager.getInstance().getStatus() == MIMCConstant.STATUS_LOGIN_SUCCESS) {
                                user.pull();
                            }
                        } catch (MIMCException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
                mCount--;
                // 切换到后台
                if (mCount == 0) {
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
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

    public IWXAPI getWxapi() {
        return mWxapi;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this) ;
    }
}
