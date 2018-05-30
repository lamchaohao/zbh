package com.gzz100.zbh.home.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.account.User;
import com.gzz100.zbh.base.BaseBackFragment;
import com.qmuiteam.qmui.widget.QMUITopBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Lam on 2018/5/10.
 */

public class AccountSafeFragment extends BaseBackFragment {

    @BindView(R.id.topbar)
    QMUITopBar mTopbar;
    @BindView(R.id.tv_phone)
    TextView mTvPhone;
    Unbinder unbinder;

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_account_safe, null);
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
        String phone = User.getUserFromCache().getPhone();
        String encodePhone = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        mTvPhone.setText(encodePhone);
    }

    private void initTopbar() {
        mTopbar.setTitle("账号与安全");
        mTopbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
    }

    @OnClick({R.id.rl_setphone, R.id.tv_setpsw})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_setphone:
                startFragment(new SetPhoneFragment());
                break;
            case R.id.tv_setpsw:
                startFragment(new UpdatePswFragment());
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
