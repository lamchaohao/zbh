package com.gzz100.zbh.home.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseBackFragment;
import com.gzz100.zbh.utils.GlideCacheUtil;
import com.qmuiteam.qmui.widget.QMUITopBar;

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

    @Override
    protected View onCreateView(LayoutInflater inflater) {

        View view = inflater.inflate(R.layout.fragment_setting, null);

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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_account_safe, R.id.rl_clear_cache, R.id.rl_msg_push})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_account_safe:
                startFragment(new AccountSafeFragment());
                break;
            case R.id.rl_clear_cache:
                break;
            case R.id.rl_msg_push:
                mSwMsgPush.setChecked(!mSwMsgPush.isChecked());
                break;
        }
    }
}
