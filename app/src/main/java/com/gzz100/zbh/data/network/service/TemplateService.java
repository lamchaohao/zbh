package com.gzz100.zbh.data.network.service;

import com.gzz100.zbh.data.entity.TemplateEntity;
import com.gzz100.zbh.data.entity.UpdateMeetingEntity;
import com.gzz100.zbh.data.network.HttpResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Lam on 2018/7/27.
 */

public interface TemplateService {

    @FormUrlEncoded
    @POST("getMeetingTemplateList")
    Observable<HttpResult<List<TemplateEntity>>> getTemplateList(@Field("userId")String userId, @Field("token")String token,
                                                                 @Field("companyId")String companyId, @Field("offset")int offset, @Field("limit")int limit);

    @FormUrlEncoded
    @POST("addMeetingToMeetingTemplate")
    Observable<HttpResult> addMeetingToTemplate(@Field("userId")String userId, @Field("token")String token,
                                                                      @Field("meetingId")String meetingId);


    @FormUrlEncoded
    @POST("getMeetingTemplate")
    Observable<HttpResult<UpdateMeetingEntity>> getTemplateById(@Field("userId")String userId, @Field("token")String token,
                                                                @Field("meetingId")String templateId);


    @FormUrlEncoded
    @POST("deleteMeetingTemplate")
    Observable<HttpResult> deleteTemplate(@Field("userId")String userId, @Field("token")String token,
                                          @Field("companyId")String companyId,@Field("meetingId")String templateId);


    @FormUrlEncoded
    @POST("updateMeetingTemplate")
    Observable<HttpResult> updateTemplateById(@Field("userId") String userId,
                                           @Field("token") String token,
                                           @Field("meetingId") String meetingId,
                                           @Field("meetingName") String meetingName,
                                           @Field("companyId") String companyId,
                                           @Field("meetingStartTime") String startTime,
                                           @Field("meetingEndTime") String endTime,
                                           @Field("hostId") String hostId,
                                           @Field("summaryId") String summaryId,
                                           @Field("copyIdList") String copyIdList,
                                           @Field("agendaList") String agendaList,
                                           @Field("delegateIdList") String delegateIdList,
                                           @Field("notifyTime")String notifyTime);
    @FormUrlEncoded
    @POST("addMeetingTemplate")
    Observable<HttpResult> addTemplate(@Field("userId") String userId,
                                                @Field("token") String token,
                                                @Field("meetingName") String meetingName,
                                                @Field("companyId") String companyId,
                                                @Field("meetingStartTime") String startTime,
                                                @Field("meetingEndTime") String endTime,
                                                @Field("hostId") String hostId,
                                                @Field("summaryId") String summaryId,
                                                @Field("copyIdList") String copyIdList,
                                                @Field("agendaList") String agendaList,
                                                @Field("delegateIdList") String delegateIdList,
                                                @Field("notifyTime")String notifyTime);

    @FormUrlEncoded
    @POST("addMeetingTemplateToMeeting")
    Observable<HttpResult> useTemplateAddMeeting(@Field("userId") String userId,
                                                 @Field("token") String token,
                                                 @Field("meetingId")String templateId,@Field("meetingPlaceId")String meetingPlaceId);

}
