package com.gzz100.zbh.home.mine;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.account.User;
import com.gzz100.zbh.base.BaseBackFragment;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.AccountRequest;
import com.gzz100.zbh.home.root.UpdateMsg;
import com.gzz100.zbh.utils.FragmentBackHandler;
import com.gzz100.zbh.utils.MD5Utils;
import com.qmuiteam.qmui.widget.QMUITopBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.gzz100.zbh.res.Common.PasswordRegEx;

public class SetPhoneFragment extends BaseBackFragment implements FragmentBackHandler {

    @BindView(R.id.topbar)
    QMUITopBar mTopbar;
    @BindView(R.id.rl_currentphone)
    RelativeLayout rlCurrentPhone;
    @BindView(R.id.tv_currentPhone)
    TextView mTvCurrentPhone;
    @BindView(R.id.rl_setphone)
    RelativeLayout mRlSetphone;
    @BindView(R.id.et_currentPsw)
    EditText mEtCurrentPswd;
    @BindView(R.id.btn_next)
    Button mBtnNext;
    Unbinder unbinder;
    private boolean isConfirmPsw;

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_set_phone, null);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return attachToSwipeBack(view);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        setEncodePhone();
        mTopbar.setTitle("手机");
        mTopbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConfirmPsw) {
                    setBackward();
                }else {
                    pop();
                }
            }
        });
    }

    @OnClick({R.id.rl_setphone, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_setphone:
                setEditPhone();
                break;
            case R.id.btn_next:
                checkPasswd();
                break;
        }
    }

    private void checkPasswd() {
        String psw = mEtCurrentPswd.getText().toString();
        if (psw.matches(PasswordRegEx)) {
            checkToServer();
        }else {
            Toasty.error(getContext(),"密码格式不正确").show();
        }
    }

    private void checkToServer() {
        String pswd = mEtCurrentPswd.getText().toString();
        String encodedStr = MD5Utils.getEncodedStr(pswd);
        AccountRequest request=new AccountRequest();
        request.checkPswd(new Observer<HttpResult>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HttpResult result) {
                setBackward();
                startFragment(new ChangePhoneNumFragment());
            }

            @Override
            public void onError(Throwable e) {
                Toasty.error(getContext(),"密码错误").show();
            }

            @Override
            public void onComplete() {

            }
        },encodedStr);

    }

    private void setEditPhone(){
        mEtCurrentPswd.setVisibility(View.VISIBLE);
        mBtnNext.setVisibility(View.VISIBLE);
        mRlSetphone.setVisibility(View.GONE);
        rlCurrentPhone.setVisibility(View.GONE);
        isConfirmPsw = true;
    }

    private void setBackward(){
        mEtCurrentPswd.setText("");
        mEtCurrentPswd.setVisibility(View.GONE);
        mBtnNext.setVisibility(View.GONE);
        mRlSetphone.setVisibility(View.VISIBLE);
        rlCurrentPhone.setVisibility(View.VISIBLE);
        isConfirmPsw = false;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveLoad(UpdateMsg msg){
        if (msg.getAction().equals(UpdateMsg.Action.updatePhoneNum)){
            setEncodePhone();
        }

    }


    private void setEncodePhone(){
        String phone = User.getUserFromCache().getPhone();
        String encodePhone = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        mTvCurrentPhone.setText(encodePhone);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    @Override
    public boolean onBackPressed() {
        if (isConfirmPsw) {
            setBackward();
            return true;
        }else {
            return false;
        }

    }
}
