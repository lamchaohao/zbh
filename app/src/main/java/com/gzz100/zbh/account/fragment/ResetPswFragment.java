package com.gzz100.zbh.account.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseBackFragment;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.AccountRequest;
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

/**
 * Created by Lam on 2018/6/21.
 */

public class ResetPswFragment extends BaseBackFragment {


    @BindView(R.id.topbar)
    QMUITopBar mTopbar;
    @BindView(R.id.et_newPsw)
    EditText mEtNewPsw;
    @BindView(R.id.et_repeatPsw)
    EditText mEtRepeatPsw;
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;
    Unbinder unbinder;

    String mCheckCode;
    String mPhone;

    public static ResetPswFragment newInstance(String checkCode,String phone){
        ResetPswFragment fragment =new ResetPswFragment();
        Bundle bundle=new Bundle();
        bundle.putString("checkCode",checkCode);
        bundle.putString("phone",phone);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.reset_psw_fragment, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVar();
        initTopbar();
        initView();
    }

    private void initVar() {
        if (getArguments()!=null) {
            mCheckCode = getArguments().getString("checkCode");
            mPhone = getArguments().getString("phone");
        }
    }

    private void initView() {


    }

    @OnClick(R.id.btn_confirm)
    void onConfirmClick(View view){
        if (mEtNewPsw.getText().toString().matches(Common.PasswordRegEx)) {
            if (mEtNewPsw.getText().toString().equals(mEtRepeatPsw.getText().toString())){
                requestToResetPsw();
            }else {
                mEtRepeatPsw.setError("两次密码不匹配");
            }
        }else {
            mEtNewPsw.setError("密码格式不正确");
        }

    }

    private void initTopbar() {
        mTopbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });

        mTopbar.setTitle("忘记密码");

    }



    private void requestToResetPsw() {
        String psw = mEtNewPsw.getText().toString();
        String encodePsw = MD5Utils.getEncodedStr(psw);
        AccountRequest request=new AccountRequest();
        request.resetPassword(new Observer<HttpResult>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HttpResult result) {
                Toasty.success(_mActivity,"修改密码成功").show();
                pop();
            }

            @Override
            public void onError(Throwable e) {
                Toasty.error(_mActivity,e.getMessage()).show();
            }

            @Override
            public void onComplete() {

            }
        },mPhone,encodePsw,mCheckCode);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
