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
import com.gzz100.zbh.res.Common;
import com.gzz100.zbh.utils.MD5Utils;
import com.qmuiteam.qmui.widget.QMUITopBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class UpdatePswFragment extends BaseBackFragment {


    @BindView(R.id.topbar)
    QMUITopBar mTopbar;
    @BindView(R.id.tv_tips)
    TextView tvTips;
    @BindView(R.id.et_checkcode)
    EditText mEtCheckcode;
    @BindView(R.id.btn_getCheckCode)
    TextView mBtnGetCheckCode;
    @BindView(R.id.et_newPsw)
    EditText mEtNewPsw;
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;
    Unbinder unbinder;

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_update_psw, null);

        unbinder = ButterKnife.bind(this, view);
        return attachToSwipeBack(view);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTopbar();
    }

    private void initTopbar() {
        mTopbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });

        mTopbar.setTitle("修改密码");

        mEtNewPsw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().matches(Common.PasswordRegEx)
                        && mEtCheckcode.getText().toString().length()>=4) {
                    mBtnConfirm.setEnabled(true);

                }else {
                    mBtnConfirm.setEnabled(false);
                }
            }
        });

        mEtCheckcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()>=4
                        &&mEtNewPsw.getText().toString().matches(Common.PasswordRegEx)) {
                    mBtnConfirm.setEnabled(true);
                }else {
                    mBtnConfirm.setEnabled(false);
                }
            }
        });
    }

    @OnClick({R.id.btn_getCheckCode, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_getCheckCode:
                getCodeAndCountDown();
                break;
            case R.id.btn_confirm:
                requestToUpdatePsw();
                break;
        }
    }

    private void requestToUpdatePsw() {
        String checkCode = mEtCheckcode.getText().toString();
        String psw = mEtNewPsw.getText().toString();
        String encodePsw = MD5Utils.getEncodedStr(psw);
        AccountRequest request=new AccountRequest();
        request.updatePsw(new Observer<HttpResult>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HttpResult result) {
                Toasty.success(_mActivity,"修改成功").show();
                pop();
            }

            @Override
            public void onError(Throwable e) {
                Toasty.error(_mActivity,e.getMessage()).show();
            }

            @Override
            public void onComplete() {

            }
        },encodePsw,checkCode);

    }


    private void getCodeAndCountDown() {
        //1.check
        User userFromCache = User.getUserFromCache();
        String phone = userFromCache.getPhone();
        final String encodePhone = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        //2.getCode;
        AccountRequest request=new AccountRequest();
        request.getCheckCode(new Observer<HttpResult>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HttpResult result) {
                tvTips.setText("短信验证码已发送到"+encodePhone);
                //3.startCountDown;
                startCountDown();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        },phone, AccountService.Method.Modify_Password);


    }

    private void startCountDown() {
        //毫秒。倒计时总时间+间隔
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                if (mBtnGetCheckCode!=null){
                    mBtnGetCheckCode.setEnabled(false);
                    mBtnGetCheckCode.setText(millisUntilFinished / 1000+"s重新获取");
                }

            }

            public void onFinish() {
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
