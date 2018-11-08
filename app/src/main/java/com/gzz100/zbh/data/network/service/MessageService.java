package com.gzz100.zbh.data.network.service;

import com.gzz100.zbh.data.entity.MessageEntity;
import com.gzz100.zbh.data.entity.UnReadEntity;
import com.gzz100.zbh.data.network.HttpResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Lam on 2018/3/30.
 */

public interface MessageService {

    @FormUrlEncoded
    @POST("getMessages")
    Observable<HttpResult<List<MessageEntity>>> getMessages(@Field("userId")String userId, @Field("token")String token,
                                                          @Field("companyId")String companyId, @Field("offset")int offset, @Field("limit")int limit);



    @FormUrlEncoded
    @POST("readMessage")
    Observable<HttpResult> readMessage(@Field("userId")String userId, @Field("token")String token,
                                                          @Field("messageId")String messageId, @Field("receiveId")String receiveId );


    @FormUrlEncoded
    @POST("getMeetingUnreadCount")
    Observable<HttpResult<UnReadEntity>> getUnreadCount(@Field("userId")String userId, @Field("token")String token, @Field("companyId")String companyId);

}
