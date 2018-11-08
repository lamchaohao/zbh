package com.gzz100.zbh.account.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.account.User;
import com.gzz100.zbh.account.eventEntity.RegInfoBus;
import com.gzz100.zbh.account.eventEntity.RegStepEvent;
import com.gzz100.zbh.base.BaseFragment;
import com.gzz100.zbh.data.ObserverImpl;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.UserLoginRequest;
import com.gzz100.zbh.utils.MD5Utils;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;

/**
 * Created by Lam on 2018/8/4.
 */

public class RegUserNameFragment extends BaseFragment {
    @BindView(R.id.et_reg_userName)
    EditText mEtRegUserName;
    @BindView(R.id.iv_username_next)
    ImageView mIvUsernameNext;
    Unbinder unbinder;
    private String mPhone;
    private String mVaildCode;
    private String mPassword;

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_reg_user_name, null);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.iv_username_next)
    public void onViewClicked() {
        doRegister();
    }

    private void doRegister() {
        ObserverImpl<HttpResult<User>> observer = new ObserverImpl<HttpResult<User>>() {
            @Override
            protected void onResponse(HttpResult<User> result) {
                if (result.getCode()==1) {
                    Toasty.success(_mActivity,"注册成功").show();
                    EventBus.getDefault().post(new RegInfoBus(result.getResult().getPhone()));
                    EventBus.getDefault().post(new RegStepEvent(3,"success"));
                }
            }

            @Override
            protected void onFailure(Throwable e) {
                Toasty.error(_mActivity,e.getMessage()).show();
            }
        };
        String encodedPsw = MD5Utils.getEncodedStr(mPassword);
        UserLoginRequest.getInstance()
                .regUser(observer,mPhone,encodedPsw,
                        mEtRegUserName.getText().toString(),mVaildCode);
    }

    public void setDataFromParent(String phone,String vaildCode,String psw){
        Logger.i(phone+","+vaildCode+",psw-"+psw);
        mPhone = phone;
        mVaildCode = vaildCode;
        mPassword = psw;

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
