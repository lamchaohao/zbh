package com.gzz100.zbh.data.network.service;

import com.gzz100.zbh.data.network.HttpResult;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Lam on 2018/3/22.
 */

public interface FileUploadService {

    @POST("files")
    Observable<HttpResult> uploadFileList(@Body RequestBody body);

    @POST("vote")
    Observable<HttpResult> uploadVote(@Body RequestBody body);

    @FormUrlEncoded
    @POST("deleteFileById")
    Observable<HttpResult> deleteFile(@Field("userId")String userId,
                                      @Field("token")String token,
                                      @Field("meetingId")String meetingId,
                                      @Field("documentId")String documentId);


}
