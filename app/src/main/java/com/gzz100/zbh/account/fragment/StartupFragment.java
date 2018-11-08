package com.gzz100.zbh.account.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.gzz100.zbh.R;
import com.gzz100.zbh.account.User;
import com.gzz100.zbh.base.BaseFragment;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.UserLoginRequest;
import com.gzz100.zbh.home.root.HomeFragment;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class StartupFragment extends BaseFragment {

    @BindView(R.id.iv_pic_startup)
    ImageView mImageView;

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View inflate = inflater.inflate(R.layout.fragment_startup, null);
        ButterKnife.bind(this,inflate);
        return inflate;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        User userFromCache = User.getUserFromCache();
        if (userFromCache==null) {
            toLogin();
        }else {
            updateUserData();
        }

    }


    private void updateUserData(){
        Observer observer = new Observer<HttpResult<User>> (){
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(HttpResult<User> userResult) {
                userResult.getResult().setLogin(true);
                User.save(userResult.getResult());
                Logger.i("updateUserData: "+userResult.getResult().getToken());
            }

            @Override
            public void onError(Throwable e) {
                Toasty.error(getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
                toLogin();
            }

            @Override
            public void onComplete() {
              checkPageToStart();
            }
        };

        UserLoginRequest.getInstance().updateToken(observer);
    }

    private void checkPageToStart() {
        User userFromCache = User.getUserFromCache();
        if (userFromCache==null) {
            toLogin();
        }else {
            if (userFromCache.isLogin()){
                if ( userFromCache.getCompanyId().equals("0")||
                        userFromCache.getCompanyId()==null) {
                    toHomePage(false);
                }else {
                    toHomePage(true);
                }

            }else {
                toLogin();
            }
        }
    }

    private void toHomePage(final boolean isJoined){

        mImageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                startWithPop(HomeFragment.newInstance(isJoined));
            }
        },2000);

    }

    private void toLogin(){
        mImageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                startWithPop(new LoginFragment());
            }
        },2000);
    }

}
