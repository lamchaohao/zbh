package com.gzz100.zbh.data.network.service;

import com.gzz100.zbh.data.entity.StarFileEntity;
import com.gzz100.zbh.data.entity.UpdateInfo;
import com.gzz100.zbh.data.network.HttpResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Lam on 2018/5/16.
 */

public interface AttachService {

    @FormUrlEncoded
    @POST("uploadUserSuggestion")
    Observable<HttpResult> submitFeedback(@Field("userId")String userId,
                                          @Field("token")String token,
                                          @Field("versionCode")String versionCode,
                                          @Field("suggestion")String feedback);


    @FormUrlEncoded
    @POST("checkUpdate")
    Observable<HttpResult<UpdateInfo>> checkUpdate(@Field("userId")String userId, @Field("token")String token,
                                                   @Field("versionCode")String versionCode,
                                                   @Field("versionName")String versionName);

    @FormUrlEncoded
    @POST("getDocumentCollection")
    Observable<HttpResult<List<StarFileEntity>>> getStarFile(@Field("userId")String userId, @Field("token")String token,
                                                             @Field("offset")int offset,@Field("limit")int limit);



    @FormUrlEncoded
    @POST("addDocumentCollection")
    Observable<HttpResult> addToStar(@Field("userId")String userId, @Field("token")String token,
                                                             @Field("documentIdList")String docIdList);


    @FormUrlEncoded
    @POST("deleteDocumentCollection")
    Observable<HttpResult> deleteFromStar(@Field("userId")String userId, @Field("token")String token,
                                     @Field("documentIdList")String docIdList);

}
