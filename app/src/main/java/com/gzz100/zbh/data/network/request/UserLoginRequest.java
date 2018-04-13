package com.gzz100.zbh.data.network.request;

import android.os.Build;

import com.gzz100.zbh.account.User;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.client.ApiException;
import com.gzz100.zbh.data.network.client.HttpClient;
import com.gzz100.zbh.data.network.service.LoginService;
import com.gzz100.zbh.res.Common;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.gzz100.zbh.res.Common.VERSION;

/**
 * Created by Lam on 2018/3/9.
 */

public class UserLoginRequest {
    private LoginService mLoginService;

    private UserLoginRequest() {
        mLoginService = HttpClient.getInstance().getRetrofit().create(LoginService.class);
    }

    private static class SingletonHolder{
        private static final UserLoginRequest INSTANCE = new UserLoginRequest();
    }

    //获取单例
    public static UserLoginRequest getInstance(){
        return SingletonHolder.INSTANCE;
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFunc<T> implements Function<HttpResult<T>, T> {


        @Override
        public T apply(HttpResult<T> httpResult) throws Exception {
            if (httpResult.getCode() != 1) {
                try {
                    throw new ApiException(httpResult.getMsg());
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
            return httpResult.getResult();
        }
    }

    public void login(Observer<HttpResult<User>> observer, String phoneNum, String password){
//        String pushToken = "";
//        if (!Build.BRAND.contains("huawei")) {
//            pushToken= Common.PUSH_TOKEN;
//        }
        String pushToken= Common.PUSH_TOKEN;
        mLoginService.login(phoneNum, password,Build.BRAND,Build.MODEL,VERSION,"android",pushToken)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void regUser(Observer<HttpResult<User>> observer, String phoneNum, String password,String userName,String checkCode){
        mLoginService.regUser(phoneNum,password,userName,checkCode)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void updateToken(Observer<HttpResult<User>> observer){
        mLoginService.updateToken(User.getUserFromCache().getUserId(),User.getUserFromCache().getToken())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void updateUserName(Observer<HttpResult> observer,String theUserName){
        User user = User.getUserFromCache();
        mLoginService.updateUserName(user.getUserId(),user.getToken(),theUserName)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

}
