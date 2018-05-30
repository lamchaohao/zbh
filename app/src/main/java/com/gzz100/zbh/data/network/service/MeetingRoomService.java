package com.gzz100.zbh.data.network.service;

import com.gzz100.zbh.data.entity.MeetingRoomEntity;
import com.gzz100.zbh.data.network.HttpResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Lam on 2018/3/9.
 */

public interface MeetingRoomService {

    @FormUrlEncoded
    @POST("getMeetingRooms")
    Observable<HttpResult<List<MeetingRoomEntity>>> getMeetingRooms(@Field("userId")String userId, @Field("token")String token,
                                                                    @Field("companyId")String companyId);
    @FormUrlEncoded
    @POST("getMeetingRooms")
    Observable<HttpResult<List<MeetingRoomEntity>>> getAvailableRooms(@Field("userId")String userId, @Field("token")String token,//不带时间, 带时间
                                                                    @Field("companyId")String companyId,@Field("startTime")String startTime);
    @FormUrlEncoded
    @POST("getMeetingRooms")
    Observable<HttpResult<List<MeetingRoomEntity>>> getMeetingListByRoomId(@Field("userId")String userId, @Field("token")String token,//不带时间, 带时间
                                                                           @Field("companyId")String companyId,@Field("meetingPlaceId")String roomId,
                                                                           @Field("startTime")String startTime);

}
