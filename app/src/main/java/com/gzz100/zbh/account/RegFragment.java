package com.gzz100.zbh.account;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseFragment;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.UserLoginRequest;
import com.gzz100.zbh.utils.FragmentBackHandler;
import com.gzz100.zbh.utils.MD5Utils;
import com.qmuiteam.qmui.widget.QMUITopBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.gzz100.zbh.res.Common.NumRegEx;
import static com.gzz100.zbh.res.Common.PasswordRegEx;

public class RegFragment extends BaseFragment implements FragmentBackHandler {


    @BindView(R.id.topbar)
    QMUITopBar mTopbar;
    @BindView(R.id.et_reg_phone)
    EditText mEtRegPhone;
    @BindView(R.id.et_reg_code)
    EditText mEtRegCode;
    @BindView(R.id.et_reg_psw)
    EditText mEtRegPsw;
    @BindView(R.id.et_reg_userName)
    EditText mEtUserName;
    @BindView(R.id.btn_next)
    Button mBtnNext;
    Unbinder unbinder;
    private int stepCount;

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_reg, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        mEtRegCode.setVisibility(View.GONE);
        mEtRegPsw.setVisibility(View.GONE);
        mEtUserName.setVisibility(View.GONE);
        mTopbar.setTitle("注册");
        mTopbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!onPrevious()){
                   startWithPop(new LoginFragment());
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private boolean onPrevious(){
        if (stepCount <= (-1)){
            stepCount=0;
        }
        stepCount--;
        switch (stepCount){
            case 0:
                mTopbar.setTitle("注册-账号");
                mEtRegPhone.setVisibility(View.VISIBLE);
                mEtRegCode.setVisibility(View.GONE);
                break;
            case 1:
                mTopbar.setTitle("注册-验证");
                mEtRegCode.setVisibility(View.VISIBLE);
                mEtRegPsw.setVisibility(View.GONE);
                break;
            case 2:
                mTopbar.setTitle("注册-密码");
                mEtUserName.setVisibility(View.GONE);
                mEtRegPsw.setVisibility(View.VISIBLE);
                break;
        }
        return stepCount!=-1;
    }

    @OnClick(R.id.btn_next)
    public void onViewClicked() {
        if (stepCount == (-1)){
            stepCount=0;
        }

        switch (stepCount) {
            case 0:
                mTopbar.setTitle("注册-账号");
                if (!checkInput(true)){
                    Toasty.error(getContext(),"手机号码不正确").show();
                    return;
                }
                stepCount++;
                //进入验证码
                mTopbar.setTitle("注册-验证");
                mEtRegCode.setVisibility(View.VISIBLE);
                mEtRegPhone.setVisibility(View.GONE);
                break;
            case 1:
                //进入设置密码
                mTopbar.setTitle("注册-密码");
                stepCount++;
                mEtRegCode.setVisibility(View.GONE);
                mEtRegPsw.setVisibility(View.VISIBLE);
                break;
            case 2:
                //检查密码
                if (!checkInput(false)){
                    Toasty.error(getContext(),"密码格式不正确").show();

                }else {
                    mTopbar.setTitle("注册-用户名");
                    stepCount++;
                    mEtRegPsw.setVisibility(View.GONE);
                    mEtUserName.setVisibility(View.VISIBLE);
                }
                break;
            case 3:
                reg();
                break;
        }

    }

    private void reg() {
        Observer<HttpResult<User>> observer=new Observer<HttpResult<User>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HttpResult<User> result) {
                if (result.getCode()==1) {
                    Toasty.success(getContext(),"注册成功").show();
                    startWithPop(new LoginFragment());
                }
            }

            @Override
            public void onError(Throwable e) {
                Toasty.error(getContext(),e.getMessage()).show();
            }

            @Override
            public void onComplete() {

            }
        };
        String psw = mEtRegPsw.getText().toString();
        String encodedPsw = MD5Utils.getEncodedStr(psw);
        UserLoginRequest.getInstance()
                .regUser(observer,mEtRegPhone.getText().toString(),encodedPsw,
                        mEtUserName.getText().toString(),mEtRegCode.getText().toString());

    }

    private boolean checkInput(boolean isCheckPhone) {
        if (isCheckPhone) {
            String phone = mEtRegPhone.getText().toString();
            return phone.matches(NumRegEx);
        }else {
            String psw = mEtRegPsw.getText().toString();
            return psw.matches(PasswordRegEx);
        }
    }


    @Override
    public boolean onBackPressed() {
        return onPrevious();
    }
}
