package com.gzz100.zbh.account;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseFragment;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.UserLoginRequest;
import com.gzz100.zbh.home.root.HomeFragment;
import com.gzz100.zbh.utils.MD5Utils;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.widget.QMUITopBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.gzz100.zbh.res.Common.NumRegEx;
import static com.gzz100.zbh.res.Common.PasswordRegEx;

/**
 * Created by Lam on 2018/1/15.
 */

public class LoginFragment extends BaseFragment {
    @BindView(R.id.topbar)
    QMUITopBar mTopbar;
    @BindView(R.id.btn_login_login)
    Button mBtnLoginLogin;
    @BindView(R.id.btn_login_reg)
    TextView mBtnLoginReg;
    @BindView(R.id.tv_forgetPsw)
    TextView tvForgetPsw;
    @BindView(R.id.et_login_phone)
    EditText mEtPhone;
    @BindView(R.id.et_login_psw)
    EditText metPsw;

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View root = inflater.inflate(R.layout.fragment_login, null);
        ButterKnife.bind(this, root);
        return root;
    }


    @OnClick({R.id.btn_login_login, R.id.btn_login_reg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login_login:
                checkInfo();
                break;
            case R.id.btn_login_reg:
                startWithPop(new RegFragment());
                break;
        }
    }

    private void checkInfo() {
        String phone = mEtPhone.getText().toString();
        String psw = metPsw.getText().toString();

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
        Observer observer = new Observer<HttpResult<User>> (){
            @Override
            public void onSubscribe(Disposable d) {
                Logger.i(d.toString());
            }

            @Override
            public void onNext(HttpResult<User> userResult) {
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
            public void onError(Throwable e) {
                Toasty.error(getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        };
        String encodedStr = MD5Utils.getEncodedStr(psw);
        UserLoginRequest.getInstance().login(observer,phone,encodedStr);
    }
}
