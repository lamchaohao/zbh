package com.gzz100.zbh.data.network.service;

import com.gzz100.zbh.data.network.HttpResult;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Lam on 2018/4/28.
 */

public interface AccountService {

    enum Method{
        Register_User,
        User_Login,
        Modify_Password,
        Modify_Phone
    }

    @FormUrlEncoded
    @POST("checkPassword")
    Observable<HttpResult> checkPsw(@Field("userId") String userId, @Field("token") String token, @Field("password")String password);


    @FormUrlEncoded
    @POST("getCheckCode")
    Observable<HttpResult> getCheckCode(@Field("phone") String phone, @Field("timestamp") long timeStamp, @Field("hashcode")String hashcode,@Field("method")Method method);



    @FormUrlEncoded
    @POST("updatePhone")
    Observable<HttpResult> updatePhone(@Field("userId") String userId, @Field("token") String token, @Field("newPhone") String newPhone,@Field("checkCode")String checkCode);

    @FormUrlEncoded
    @POST("changePsw")
    Observable<HttpResult> updatePassword(@Field("phone") String phone, @Field("password") String password, @Field("checkCode") String checkCode);

}
