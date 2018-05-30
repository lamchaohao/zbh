package com.gzz100.zbh.data.network.request;

import com.gzz100.zbh.account.User;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.client.HttpClient;
import com.gzz100.zbh.data.network.service.AccountService;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Lam on 2018/4/28.
 */

public class AccountRequest {

    private final AccountService mAccountService;

    public AccountRequest() {
        mAccountService = HttpClient.getInstance()
                .getRetrofit()
                .create(AccountService.class);
    }


    public void checkPswd(Observer<HttpResult> observer,String password){
        User user = User.getUserFromCache();
        mAccountService.checkPsw(user.getUserId(),user.getToken(),password)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


    public void getCheckCode(Observer<HttpResult> observer,String phone,String type){
        long time = System.currentTimeMillis();
        String hashStr=phone+time;
        int i = hashStr.hashCode();
        mAccountService.getCheckCode(phone,time,i+"",type)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    public void updatePhone(Observer<HttpResult> observer,String newPhone,String checkCode){
        User user = User.getUserFromCache();
        mAccountService.updatePhone(user.getUserId(),user.getToken(),newPhone,checkCode)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    public void updatePsw(Observer<HttpResult> observer,String psw,String checkCode){
        User user = User.getUserFromCache();
        mAccountService.updatePassword(user.getPhone(),psw,checkCode)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

}
