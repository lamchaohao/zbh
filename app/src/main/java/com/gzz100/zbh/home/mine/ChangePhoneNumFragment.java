package com.gzz100.zbh.home.mine;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.account.User;
import com.gzz100.zbh.base.BaseBackFragment;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.AccountRequest;
import com.gzz100.zbh.data.network.service.AccountService;
import com.gzz100.zbh.data.eventEnity.UpdateMsg;
import com.gzz100.zbh.res.Common;
import com.qmuiteam.qmui.widget.QMUITopBar;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ChangePhoneNumFragment extends BaseBackFragment {

    @BindView(R.id.topbar)
    QMUITopBar mTopbar;
    @BindView(R.id.tv_tips)
    TextView tvTips;
    @BindView(R.id.et_newPhone)
    EditText mEtNewPhone;
    @BindView(R.id.btn_getCheckCode)
    TextView mBtnGetCheckCode;
    @BindView(R.id.et_checkcode)
    EditText mEtCheckCode;
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;
    Unbinder unbinder;
    boolean isGetCheckCode;
    private String mCheckedPhone;


    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_change_phone_num, null);
        unbinder = ButterKnife.bind(this, view);
        return attachToSwipeBack(view);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTopBar();
        initView();
    }

    private void initTopBar() {

        mTopbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });

        mTopbar.setTitle("绑定新手机");
    }

    private void initView() {

        mEtCheckCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()>=4) {
                    if (isGetCheckCode) {
                        mBtnConfirm.setEnabled(true);
                    }else {
                        mBtnConfirm.setEnabled(false);
                    }
                }else {
                    mBtnConfirm.setEnabled(false);
                }
            }
        });

        mEtNewPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().matches(Common.NumRegEx)) {
                    mBtnGetCheckCode.setEnabled(true);
                }else {
                    mBtnGetCheckCode.setEnabled(false);
                }
            }
        });

    }

    @OnClick({R.id.btn_getCheckCode, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_getCheckCode:
                checkPhoneNum();
                break;
            case R.id.btn_confirm:
                requestToChange();
                break;
        }
    }

    private void requestToChange() {
        AccountRequest request=new AccountRequest();
        request.updatePhone(new Observer<HttpResult>() {
            @Override
            public void onSubscribe(Disposable d) {
                mBtnConfirm.setEnabled(false);
            }

            @Override
            public void onNext(HttpResult result) {
                User user = User.getUserFromCache();
                user.setPhone(mCheckedPhone);
                User.save(user);
                Toasty.success(_mActivity,"绑定成功").show();
                EventBus.getDefault().post(new UpdateMsg(UpdateMsg.Action.updatePhoneNum));
                pop();
            }

            @Override
            public void onError(Throwable e) {
                Toasty.error(getContext(),e.getMessage()).show();
            }

            @Override
            public void onComplete() {

            }
        },mCheckedPhone,mEtCheckCode.getText().toString());

    }

    private void checkPhoneNum() {
        if (mEtNewPhone.getText()!=null) {
            if (mEtNewPhone.getText().toString().matches(Common.NumRegEx)) {
                requestCheckCode();
                mEtNewPhone.setEnabled(false);
                isGetCheckCode=true;
            }else {
                Toasty.error(getContext(),"手机号码格式不正确").show();
            }
        }
    }

    private void requestCheckCode() {
        mCheckedPhone = mEtNewPhone.getText().toString();
        AccountRequest request=new AccountRequest();
        request.getCheckCode(new Observer<HttpResult>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HttpResult result) {
                tvTips.setText("短信验证码已发送到"+mEtNewPhone.getText().toString());
                updateCountDown();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        },mCheckedPhone, AccountService.Method.Modify_Phone);
    }

    private void updateCountDown() {
        //毫秒。倒计时总时间+间隔
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                if (mBtnGetCheckCode!=null){
                    //存在 还在倒计时, 但已经完成修改,退出了此页面的情况
                    mBtnGetCheckCode.setEnabled(false);
                    mBtnGetCheckCode.setText(millisUntilFinished / 1000+"s重新获取");
                }

            }

            public void onFinish() {
                if (mEtNewPhone!=null){
                    mEtNewPhone.setEnabled(true);
                }
                if (mBtnGetCheckCode!=null){
                    mBtnGetCheckCode.setEnabled(true);
                    mBtnGetCheckCode.setText("获取验证码");
                }

            }
        }.start();// 调用CountDownTimer对象的start()方法开始倒计时，也不涉及到线程处理
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
