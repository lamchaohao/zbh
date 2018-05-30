package com.gzz100.zbh.data.network.service;

import com.gzz100.zbh.data.entity.MimcToken;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Lam on 2018/5/8.
 */

public interface TokenService {

    @FormUrlEncoded
    @POST("getMimcToken")
    Call<MimcToken> getMimcToken(@Field("userId")String userId, @Field("token")String token);
}
