package com.gzz100.zbh.data.network.service;

import com.gzz100.zbh.data.entity.MeetingEntity;
import com.gzz100.zbh.data.entity.MeetingInfoEntity;
import com.gzz100.zbh.data.network.HttpResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Lam on 2018/3/19.
 */

public interface MeetingService {

    @FormUrlEncoded
    @POST("meetings")
    Observable<HttpResult<List<MeetingEntity>>> getMeetings(@Field("userId")String userId, @Field("token")String token,
                                                            @Field("offset")int offset, @Field("limit")int limit);

    @FormUrlEncoded
    @POST("getMeetingByCreator")
    Observable<HttpResult<List<MeetingEntity>>> getMeetingsByCreator(@Field("userId")String userId, @Field("token")String token,
                                                            @Field("offset")int offset, @Field("limit")int limit);

    @FormUrlEncoded
    @POST("getMeeting")
    Observable<HttpResult<MeetingInfoEntity>> getSingleMeetingInfo(@Field("userId")String userId, @Field("token")String token,
                                                               @Field("meetingId")String meetingid,@Field("companyId")String companyId);

}
