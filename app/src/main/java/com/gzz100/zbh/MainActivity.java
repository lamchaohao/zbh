package com.gzz100.zbh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.githang.statusbar.StatusBarCompat;
import com.gzz100.zbh.account.fragment.StartupFragment;
import com.gzz100.zbh.base.BaseFragmentActivity;
import com.gzz100.zbh.home.root.HomeFragment;
import com.gzz100.zbh.data.eventEnity.PushUpdateEntity;
import com.gzz100.zbh.push.HMSpushReciver;
import com.gzz100.zbh.utils.BackHandlerHelper;
import com.huawei.android.hms.agent.HMSAgent;
import com.huawei.android.hms.agent.common.handler.ConnectHandler;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import es.dmoral.toasty.Toasty;


public class MainActivity extends BaseFragmentActivity {
    BroadcastReceiver broadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
//        StatusBarCompat.setStatusBarColor(this,getResources().getColor(R.color.colorTopbar));
//        StatusBarCompat.translucentStatusBar(this, true);
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorTopbar), true);
        if (findFragment(HomeFragment.class) == null) {
            loadRootFragment(R.id.containersId, new StartupFragment());
        }
        registerBroadcast();
        if (Build.BRAND.toUpperCase().contains("HUAWEI")){
            initHMS();
        }
    }


    private void initHMS() {
        HMSAgent.connect(this, new ConnectHandler() {
            @Override
            public void onConnect(int rst) {
                Logger.e("HMS connect end: erroCode" + rst);
            }
        });
        HMSAgent.Push.enableReceiveNotifyMsg(true);
        HMSAgent.Push.enableReceiveNormalMsg(true);
    }

    private void registerBroadcast() {

        IntentFilter filter = new IntentFilter();
        filter.addAction(HMSpushReciver.ACTION_UPDATEUI);
        broadcastReceiver = new UpdateUIBroadcastReceiver();
        registerReceiver(broadcastReceiver, filter);
    }

    /**
     * 定义广播接收器（内部类）
     */
    private class UpdateUIBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String log = intent.getExtras().getString("log");
            Logger.i(log);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMeetingChange(PushUpdateEntity pushUpdateEntity){
        Logger.i("onReceiveMeetingChange");
        switch (pushUpdateEntity.getMsgType()) {
            case updateMeetingReceive:
                Toasty.success(this,"收到新消息").show();

                new QMUIDialog.MessageDialogBuilder(this)
                        .setMessage("你有一条新的会议通知,请查收")
                        .addAction("查看", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        }).addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                        .show();

                break;

        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        Logger.i("call back onActivityResult requestCode"+requestCode+",resultCode="+resultCode);
        FragmentManager fragmentManager=getSupportFragmentManager();
        for(int indext=0;indext<fragmentManager.getFragments().size();indext++) {
            Fragment fragment=fragmentManager.getFragments().get(indext); //找到第一层Fragment
            if(fragment==null)
                Logger.i("Activity result no fragment exists for index: 0x"
                        + Integer.toHexString(requestCode));
            else
                handleResult(fragment,requestCode,resultCode,data);
        }
    }

    /**
     * 递归调用 传值onActivityResult
     */
    private void handleResult(Fragment fragment,int requestCode,int resultCode,Intent data) {
        fragment.onActivityResult(requestCode, resultCode, data);//调用每个Fragment的onActivityResult
        List<Fragment> childFragment = fragment.getChildFragmentManager().getFragments(); //找到第二层Fragment
        if(childFragment!=null)
            for(Fragment f:childFragment) {
                if (f != null) {
                    handleResult(f, requestCode, resultCode, data);
                }
            }
    }


    @Override
    public void onBackPressedSupport() {
        if (!BackHandlerHelper.handleBackPress(this)) {
            super.onBackPressedSupport();
        }
    }


}
