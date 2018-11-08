package com.gzz100.zbh.home.mine;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseBackFragment;
import com.gzz100.zbh.data.eventEnity.UpdateMsg;
import com.gzz100.zbh.utils.GlideCacheUtil;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.xiaomi.mipush.sdk.MiPushClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Lam on 2018/4/28.
 */

public class SettingFragment extends BaseBackFragment {

    @BindView(R.id.topbar)
    QMUITopBar mTopbar;

    Unbinder unbinder;
    @BindView(R.id.tv_cache_size)
    TextView mTvCacheSize;
    @BindView(R.id.sw_msg_push)
    Switch mSwMsgPush;
    private SharedPreferences mConfigSp;

    @Override
    protected View onCreateView(LayoutInflater inflater) {

        View view = inflater.inflate(R.layout.fragment_setting, null);
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, view);
        return attachToSwipeBack(view);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTopbar();
        initView();

    }

    private void initView() {
        String cacheSize = GlideCacheUtil.getInstance().getCacheSize(getContext());
        mTvCacheSize.setText(cacheSize);
        mConfigSp = getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        boolean enablePush = mConfigSp.getBoolean("enablePush", true);
        switchPush(enablePush);
        mSwMsgPush.setChecked(enablePush);
        mSwMsgPush.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchPush(isChecked);
                mConfigSp.edit().putBoolean("enablePush",isChecked).apply();
            }
        });
    }

    private void initTopbar() {
        mTopbar.setTitle("设置");
        mTopbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
    }

    private void switchPush(boolean enable){
        if (enable) {
            MiPushClient.enablePush(getContext().getApplicationContext());
        }else {
            MiPushClient.disablePush(getContext().getApplicationContext());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveClear(UpdateMsg msg){
        if(msg.getAction().equals(UpdateMsg.Action.updateCache)){
            String cacheSize = GlideCacheUtil.getInstance().getCacheSize(getContext());
            mTvCacheSize.setText(cacheSize);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    @OnClick({R.id.tv_account_safe, R.id.rl_clear_cache, R.id.rl_msg_push})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_account_safe:
                startFragment(new AccountSafeFragment());
                break;
            case R.id.rl_clear_cache:
                showClearDialog();
                break;
            case R.id.rl_msg_push:
                mSwMsgPush.setChecked(!mSwMsgPush.isChecked());
                break;
        }
    }

    private void showClearDialog() {
        new QMUIDialog.MessageDialogBuilder(getContext())
                .setTitle("清除缓存")
                .setMessage("确定要清除所有缓存吗?")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(0, "清除", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        GlideCacheUtil.getInstance().clearImageDiskCache(getContext());

                    }
                })
                .show();
    }

}
