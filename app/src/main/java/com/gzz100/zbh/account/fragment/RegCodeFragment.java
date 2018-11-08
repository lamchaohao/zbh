package com.gzz100.zbh.account.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.account.eventEntity.RegStepEvent;
import com.gzz100.zbh.base.BaseBackFragment;
import com.gzz100.zbh.data.ObserverImpl;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.AccountRequest;
import com.gzz100.zbh.data.network.service.AccountService;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;

/**
 * Created by Lam on 2018/8/4.
 */

public class RegCodeFragment extends BaseBackFragment {
    @BindView(R.id.tv_sent_phone)
    TextView mTvSentPhone;
    @BindView(R.id.tv_countdown)
    TextView mTvCountdown;
    @BindView(R.id.et_vaild_code)
    EditText mEtVaildCode;
    @BindView(R.id.iv_code_next)
    ImageView mIvCodeNext;
    private Unbinder unbinder;
    private String mPhone;

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_reg_code, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    public void setPhoneFromParent(String phone){
        mPhone = phone;
        if (mTvCountdown.isEnabled()) {
            mTvCountdown.setEnabled(false);
            mTvSentPhone.setText("验证码已发送至"+phone);
            updateCountDown();
        }

    }


    private void getVaildCodeFromServer(final String phone){
        ObserverImpl<HttpResult> observer=new ObserverImpl<HttpResult>() {
            @Override
            protected void onResponse(HttpResult result) {
                mTvCountdown.setEnabled(false);
                mTvSentPhone.setText("验证码已发送至"+phone);
                updateCountDown();
            }

            @Override
            protected void onFailure(Throwable e) {
                Toasty.error(_mActivity,e.getMessage()).show();
            }
        };

        AccountRequest request=new AccountRequest();
        request.getCheckCode(observer,phone, AccountService.Method.Register_User);

    }


    private void updateCountDown() {
        //毫秒。倒计时总时间+间隔
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                if (mTvCountdown!=null){
                    //存在 还在倒计时, 但已经完成修改,退出了此页面的情况
                    mTvCountdown.setEnabled(false);
                    mTvCountdown.setTextColor(getContext().getResources().getColor(R.color.colorTextNormal2));
                    mTvCountdown.setText(millisUntilFinished / 1000+"秒后可重新获取");
                }

            }

            public void onFinish() {
                if (mEtVaildCode!=null){
                    mEtVaildCode.setEnabled(true);
                }
                if (mTvCountdown!=null){
                    mTvCountdown.setEnabled(true);
                    mTvCountdown.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getVaildCodeFromServer(mPhone);
                        }
                    });
                    mTvCountdown.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
                    mTvCountdown.setText("获取验证码");
                }

            }
        }.start();// 调用CountDownTimer对象的start()方法开始倒计时，也不涉及到线程处理
    }
    
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.iv_code_next)
    public void onViewClicked() {
        String vaildCode = mEtVaildCode.getText().toString();

        if (vaildCode.length()>=4){
            EventBus.getDefault().post(new RegStepEvent(1,vaildCode));
        }else {
            mEtVaildCode.setError("请输入正确的验证码");
        }



    }
}
