package com.gzz100.zbh.account.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

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

import static com.gzz100.zbh.res.Common.NumRegEx;

/**
 * Created by Lam on 2018/8/4.
 */

public class RegPhoneFragment extends BaseBackFragment {

    @BindView(R.id.et_reg_phone)
    EditText mEtRegPhone;
    @BindView(R.id.iv_phone_next)
    ImageView mIvPhoneNext;
    private Unbinder unbinder;

    @Override
    protected View onCreateView(LayoutInflater inflater) {

        View view = inflater.inflate(R.layout.fragment_reg_phone, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {


    }

    private void getVaildCodeFromServer(final String phone){
        ObserverImpl<HttpResult> observer=new ObserverImpl<HttpResult>() {
            @Override
            protected void onResponse(HttpResult result) {
                EventBus.getDefault().post(new RegStepEvent(0,phone));
            }

            @Override
            protected void onFailure(Throwable e) {
                Toasty.error(_mActivity,e.getMessage()).show();
            }
        };

        AccountRequest request=new AccountRequest();
        request.getCheckCode(observer,phone, AccountService.Method.Register_User);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.iv_phone_next)
    public void onViewClicked() {

        String phone = mEtRegPhone.getText().toString();
        if (phone.matches(NumRegEx)){
           getVaildCodeFromServer(phone);
        }else {
            mEtRegPhone.setError("手机号码格式不正确");
        }




    }
}
