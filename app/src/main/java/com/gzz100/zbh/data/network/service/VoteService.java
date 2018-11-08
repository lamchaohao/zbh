package com.gzz100.zbh.data.network.service;

import com.gzz100.zbh.data.entity.VoteDetailEntity;
import com.gzz100.zbh.data.entity.VoteEntity;
import com.gzz100.zbh.data.network.HttpResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Lam on 2018/4/4.
 */

public interface VoteService {

    @FormUrlEncoded
    @POST("getVoteList")
    Observable<HttpResult<List<VoteEntity>>> getVoteList(@Field("userId")String userId, @Field("token")String token,
                                                         @Field("meetingId")String meetingId);

    @FormUrlEncoded
    @POST("getVote")
    Observable<HttpResult<VoteDetailEntity>> getVoteInfo(@Field("userId")String userId, @Field("token")String token,
                                                         @Field("meetingId")String meetingId, @Field("voteId")String voteId);

    @FormUrlEncoded
    @POST("uploadVoteResult")
    Observable<HttpResult> uploadVoteResult(@Field("userId")String userId, @Field("token")String token,
                                                         @Field("voteOptionIdList")String voteOptionIdList, @Field("voteId")String voteId);

    @FormUrlEncoded
    @POST("startVote")
    Observable<HttpResult> startVote(@Field("userId")String userId, @Field("token")String token,
                                             @Field("voteId")String voteId);
    @FormUrlEncoded
    @POST("endVote")
    Observable<HttpResult> endVote(@Field("userId")String userId, @Field("token")String token,
                                     @Field("voteId")String voteId);

    @DELETE("vote/{voteId}")
    Observable<HttpResult> deleteVote(@Path("voteId") String voteId,
                                      @Query("userId") String userId, @Query("token") String token
    );

    @FormUrlEncoded
    @POST("updateVote")
    Observable<HttpResult> updateVote();
}
