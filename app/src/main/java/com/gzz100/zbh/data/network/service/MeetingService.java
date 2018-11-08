package com.gzz100.zbh.data.network.service;

import com.gzz100.zbh.data.entity.DelegateEntity;
import com.gzz100.zbh.data.entity.DelegateSummaryEntity;
import com.gzz100.zbh.data.entity.DownloadFileEntity;
import com.gzz100.zbh.data.entity.FinishedInfoEntity;
import com.gzz100.zbh.data.entity.MeetingEntity;
import com.gzz100.zbh.data.entity.MeetingInfoEntity;
import com.gzz100.zbh.data.entity.UpdateMeetingEntity;
import com.gzz100.zbh.data.network.HttpResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Lam on 2018/3/19.
 *
 */

public interface MeetingService {


    @FormUrlEncoded
    @POST("meetings")
    Observable<HttpResult<List<MeetingEntity>>> getMeetings(@Field("userId")String userId, @Field("token")String token,
                                                            @Field("companyId")String companyId,
                                                            @Field("offset")int offset, @Field("limit")int limit);


    /**
     *
     * @param userId
     * @param token
     * @param companyId
     * @param offset
     * @param limit 返回条数
     * @param startTime 20181010或201810101010
     * @return
     */
    @FormUrlEncoded
    @POST("meetings")
    Observable<HttpResult<List<MeetingEntity>>> getMeetingsByTime(@Field("userId")String userId, @Field("token")String token,
                                                            @Field("companyId")String companyId,
                                                            @Field("offset")int offset, @Field("limit")int limit,@Field("startTime")String startTime);

    @FormUrlEncoded
    @POST("getMeetingByCreator")
    Observable<HttpResult<List<MeetingEntity>>> getMeetingsByCreator(@Field("userId")String userId, @Field("token")String token,@Field("companyId")String companyId,
                                                            @Field("offset")int offset, @Field("limit")int limit);

    @FormUrlEncoded
    @POST("getMeeting")
    Observable<HttpResult<MeetingInfoEntity>> getSingleMeetingInfo(@Field("userId")String userId, @Field("token")String token,
                                                               @Field("meetingId")String meetingid,@Field("companyId")String companyId);

    @FormUrlEncoded
    @POST("getDelegates")
    Observable<HttpResult<List<DelegateEntity>>> getDelegates(@Field("userId")String userId, @Field("token")String token,
                                                              @Field("meetingId")String meetingid, @Field("companyId")String companyId);


    @FormUrlEncoded
    @POST("addDelegate")
    Observable<HttpResult> addDelegate(@Field("userId")String userId, @Field("token")String token,
                                                              @Field("meetingId")String meetingid, @Field("userIdList")String userIdList);
    @FormUrlEncoded
    @POST("cancelMeeting")
    Observable<HttpResult> cancleMeeting(@Field("userId")String userId, @Field("token")String token,
                                       @Field("meetingId")String meetingid);

    @FormUrlEncoded
    @POST("signIn")
    Observable<HttpResult> signIn(@Field("userId")String userId, @Field("token")String token,
                                         @Field("meetingId")String meetingid);

    @FormUrlEncoded
    @POST("startMeeting")
    Observable<HttpResult> startMeeting(@Field("userId")String userId, @Field("token")String token,
                                  @Field("meetingId")String meetingid);
    @FormUrlEncoded
    @POST("endMeeting")
    Observable<HttpResult> endMeeting(@Field("userId")String userId, @Field("token")String token,
                                        @Field("meetingId")String meetingid);

    @FormUrlEncoded
    @POST("updateMeeting")
    Observable<HttpResult> updateMeeting(  @Field("userId") String userId,
                                           @Field("token") String token,
                                           @Field("meetingId") String meetingId,
                                           @Field("meetingName") String meetingName,
                                           @Field("meetingPlaceId") String meetingPlaceId,
                                           @Field("companyId") String companyId,
                                           @Field("startTime") String startTime,
                                           @Field("endTime") String endTime,
                                           @Field("hostId") String hostId,
                                           @Field("summaryId") String summaryId,
                                           @Field("copyIdList") String copyIdList,
                                           @Field("agendaList") String agendaList,
                                           @Field("delegateIdList") String delegateIdList,
                                           @Field("notifyTime")String notifyTime);

    @FormUrlEncoded
    @POST("getUpdateMeetingInfo")
    Observable<HttpResult<UpdateMeetingEntity>> getUpdateMeetingInfo(@Field("userId") String userId,
                                                                     @Field("token") String token,
                                                                     @Field("companyId") String companyId,
                                                                     @Field("meetingId") String meetingId);



    @FormUrlEncoded
    @POST("getEndOfMeeting")
    Observable<HttpResult<FinishedInfoEntity>> getFinishedMeeting(@Field("userId") String userId,
                                                                  @Field("token") String token,
                                                                  @Field("companyId") String companyId,
                                                                  @Field("meetingId") String meetingId);


    @FormUrlEncoded
    @POST("getDownloadableFileList")
    Observable<HttpResult<List<DownloadFileEntity>>> getDownloadFile(@Field("userId") String userId,
                                                                        @Field("token") String token,
                                                                        @Field("meetingId") String meetingId);


    @FormUrlEncoded
    @POST("getDelegateInfo")
    Observable<HttpResult<DelegateSummaryEntity>> getDelegateSummary(@Field("userId") String userId,
                                                                     @Field("token") String token,
                                                                     @Field("meetingId") String meetingId,
                                                                     @Field("companyId")String companyId);


}

