package com.gzz100.zbh.data.network.service;

import com.gzz100.zbh.data.entity.VoteDetailEntity;
import com.gzz100.zbh.data.entity.VoteEntity;
import com.gzz100.zbh.data.network.HttpResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

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

}
