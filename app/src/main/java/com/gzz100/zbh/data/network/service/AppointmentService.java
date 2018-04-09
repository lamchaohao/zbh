package com.gzz100.zbh.data.network.service;

import com.gzz100.zbh.data.entity.MeetingEntity;
import com.gzz100.zbh.data.entity.Staff;
import com.gzz100.zbh.data.network.HttpResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Lam on 2018/3/14.
 */

public interface AppointmentService {

    @FormUrlEncoded
    @POST("addMeeting")
    Observable<HttpResult<MeetingEntity>> addMeeting(@Field("userId") String userId,
                                                     @Field("token") String token,
                                                     @Field("meetingName") String meetingName,
                                                     @Field("meetingPlaceId") String meetingPlaceId,
                                                     @Field("companyId") String companyId,
                                                     @Field("startTime") String startTime,
                                                     @Field("endTime") String endTime,
                                                     @Field("hostId") String hostId,
                                                     @Field("summaryId") String summaryId,
                                                     @Field("copyIdList") String copyIdList,
                                                     @Field("agendaList") String agendaList,
                                                     @Field("delegateIdList") String delegateIdList);

    @FormUrlEncoded
    @POST("addDelegate")//添加参会人员
    Observable<HttpResult> addDelegate(@Field("userId") String userId,
                                                           @Field("token") String token,
                                                           @Field("meetingId") String meetingId,
                                                           @Field("delegateIdList") String delegateIdList);

    @FormUrlEncoded
    @POST("getStaffs")//获取员工列表
    Observable<HttpResult<List<Staff>>> getStaffs(@Field("userId") String userId,
                                                  @Field("token") String token,
                                                  @Field("companyId") String companyId);

}
