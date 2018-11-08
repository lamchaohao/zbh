package com.gzz100.zbh.account.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.account.eventEntity.RegStepEvent;
import com.gzz100.zbh.base.BaseFragment;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.gzz100.zbh.res.Common.PasswordRegEx;

/**
 * Created by Lam on 2018/8/4.
 */

public class RegPswFragment extends BaseFragment {
    @BindView(R.id.et_reg_psw)
    EditText mEtRegPsw;
    @BindView(R.id.iv_psw_next)
    ImageView mIvPswNext;
    Unbinder unbinder;

    @Override
    protected View onCreateView(LayoutInflater inflater) {

        View view = inflater.inflate(R.layout.fragment_reg_psw, null);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.iv_psw_next)
    public void onViewClicked() {
        String pswStr = mEtRegPsw.getText().toString();
        if (pswStr.matches(PasswordRegEx)) {
            Logger.i(pswStr);
            EventBus.getDefault().post(new RegStepEvent(2,pswStr));
        }else {
            mEtRegPsw.setError("密码格式不正确");
        }

    }
}
