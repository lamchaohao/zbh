package com.gzz100.zbh.data.network.service;

import com.gzz100.zbh.account.User;
import com.gzz100.zbh.data.network.HttpResult;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Lam on 2018/3/9.
 */

public interface LoginService {

    @FormUrlEncoded
    @POST("login")
    Observable<HttpResult<User>> login(@Field("phone") String phone, @Field("password") String password,
                                       @Field("phoneBrand") String phoneBrand, @Field("phoneModel") String phoneModel,
                                       @Field("version") String version, @Field("os") String os,
                                        @Field("pushToken")String pushToken);
    @FormUrlEncoded
    @POST("register")
    Observable<HttpResult<User>> regUser(@Field("phone") String phone, @Field("password") String password,
                                         @Field("userName") String userName, @Field("checkCode") String checkCode);

    @FormUrlEncoded
    @POST("updateToken")
    Observable<HttpResult<User>> updateToken(@Field("userId") String userId, @Field("token") String token);


    @FormUrlEncoded
    @POST("updateUserName")
    Observable<HttpResult> updateUserName(@Field("userId") String userId, @Field("token") String token,@Field("userName")String userName);

    @FormUrlEncoded
    @POST("logout")
    Observable<HttpResult> logout(@Field("userId") String userId, @Field("token") String token);

}
