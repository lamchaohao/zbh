package com.gzz100.zbh.data.network.request;

import com.gzz100.zbh.account.User;
import com.gzz100.zbh.data.entity.MeetingEntity;
import com.gzz100.zbh.data.entity.Staff;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.client.HttpClient;
import com.gzz100.zbh.data.network.service.AppointmentService;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Lam on 2018/3/14.
 */

public class AppointmentRequest {

    AppointmentService mAppointmentService;
    public AppointmentRequest() {
        mAppointmentService = HttpClient.getInstance()
                .getRetrofit()
                .create(AppointmentService.class);

    }

    public void getAllStaffs(Observer<HttpResult<List<Staff>>> observer){
        User user = User.getUserFromCache();
        mAppointmentService.getStaffs(user.getUserId(),user.getToken(),user.getCompanyId())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void addDelegates(Observer<HttpResult> observer,String meetingId,String delegateIds){
        User user = User.getUserFromCache();
        mAppointmentService.addDelegate(user.getUserId(),user.getToken(),meetingId,delegateIds)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void addMeeting(Observer<HttpResult<MeetingEntity>> observer, String meetingname,
                           String meetingPlaceId, String startTime,
                           String endTime, String hostId,
                           String summaryId, String copyIdList, String agendaList, String delegateIdList,String notifyTime){
        User user = User.getUserFromCache();
        mAppointmentService.addMeeting(user.getUserId(),user.getToken(),
                meetingname,meetingPlaceId,user.getCompanyId(),startTime,endTime,hostId,summaryId,
                copyIdList,agendaList,delegateIdList,notifyTime)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

}
