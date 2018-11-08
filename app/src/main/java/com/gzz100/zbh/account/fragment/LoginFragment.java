package com.gzz100.zbh.account.fragment;

import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gzz100.zbh.R;
import com.gzz100.zbh.account.eventEntity.RegInfoBus;
import com.gzz100.zbh.account.User;
import com.gzz100.zbh.base.BaseFragment;
import com.gzz100.zbh.data.ObserverImpl;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.UserLoginRequest;
import com.gzz100.zbh.home.root.HomeFragment;
import com.gzz100.zbh.utils.MD5Utils;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

import static com.gzz100.zbh.res.Common.NumRegEx;
import static com.gzz100.zbh.res.Common.PasswordRegEx;

/**
 * Created by Lam on 2018/1/15.
 */

public class LoginFragment extends BaseFragment {
    @BindView(R.id.iv_login)
    ImageView mIvLogin;
    @BindView(R.id.iv_psw_eye)
    CheckBox mCbPswEye;
    @BindView(R.id.btn_login_reg)
    TextView mBtnLoginReg;
    @BindView(R.id.tv_forgetPsw)
    TextView tvForgetPsw;
    @BindView(R.id.et_login_phone)
    EditText mEtPhone;
    @BindView(R.id.et_login_psw)
    EditText mEtPsw;

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View root = inflater.inflate(R.layout.fragment_login, null);
        ButterKnife.bind(this, root);
        EventBus.getDefault().register(this);
        initView();
        return root;
    }

    private void initView() {
        if (User.getUserFromCache()!=null) {
            mEtPhone.setText(User.getUserFromCache().getPhone());
        }

        mCbPswEye.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //选择状态 显示明文--设置为可见的密码
                    mEtPsw.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else {
                    //默认状态显示密码--设置文本 要一起写才能起作用  InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                    mEtPsw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        mEtPsw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()>=6) {
                    mIvLogin.setImageResource(R.drawable.login_arrow_right_bright);
                }else {
                    mIvLogin.setImageResource(R.drawable.login_arrow_right);
                }
            }
        });

    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRegSuccess(RegInfoBus infoBus){
        if (infoBus!=null){
            mEtPhone.setText(infoBus.getPhone());
        }
    }


    @OnClick({R.id.iv_login, R.id.btn_login_reg,R.id.tv_forgetPsw,R.id.iv_psw_eye})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_login:
                checkInfo();
                break;
            case R.id.btn_login_reg:
                startFragment(new RegFragment());
                break;
            case R.id.tv_forgetPsw:
                startFragment(new ForgotPswFragment());
                break;
            case R.id.iv_psw_eye:

                break;
        }
    }

    private void checkInfo() {
        String phone = mEtPhone.getText().toString();
        String psw = mEtPsw.getText().toString();

        if (!TextUtils.isEmpty(phone)) {
            if (!phone.matches(NumRegEx)) {
                Toasty.error(getContext(),"手机号码错误").show();
                return;
            }
        }else {
            Toasty.error(getContext(),"手机号码不能为空").show();
            return;
        }
        if (!TextUtils.isEmpty(psw)) {
            if (!psw.matches(PasswordRegEx)) {
                Toasty.error(getContext(),"密码格式错误").show();
                return;
            }
        }else {
            Toasty.error(getContext(),"密码不能为空").show();
            return;
        }
        login(phone,psw);
    }

    private void login(String phone,String psw) {
        ObserverImpl<HttpResult<User>> observer = new ObserverImpl<HttpResult<User>> (){

            @Override
            protected void onResponse(HttpResult<User> userResult) {
                userResult.getResult().setLogin(true);
                User.save(userResult.getResult());
                Logger.i(userResult.getResult().toString());

                if ( userResult.getResult().getCompanyId().equals("0")||
                        userResult.getResult().getCompanyId()==null) {
                    startWithPop(HomeFragment.newInstance(false));
                }else {
                    startWithPop(HomeFragment.newInstance(true));
                }
            }

            @Override
            protected void onFailure(Throwable e) {
                Toasty.error(getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        String encodedStr = MD5Utils.getEncodedStr(psw);
        UserLoginRequest.getInstance().login(observer,phone,encodedStr);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        hideSoftInput();
    }
}
