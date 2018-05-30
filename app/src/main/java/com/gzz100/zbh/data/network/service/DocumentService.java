package com.gzz100.zbh.data.network.service;

import com.gzz100.zbh.data.entity.DocumentEntity;
import com.gzz100.zbh.data.network.HttpResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Lam on 2018/3/26.
 */

public interface DocumentService {

    @FormUrlEncoded
    @POST("getFileList")
    Observable<HttpResult<List<DocumentEntity>>> getDocumentList(@Field("userId")String userId, @Field("token")String token,
                                                                 @Field("meetingId")String meetingId);


    @FormUrlEncoded
    @POST("deleteFileById")
    Observable<HttpResult> deleteFile(@Field("userId")String userId,@Field("token")String token,
                                      @Field("meetingId")String meetingId,
                                      @Field("documentId")String documentId);


}
