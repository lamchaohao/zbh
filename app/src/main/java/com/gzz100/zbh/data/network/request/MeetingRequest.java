package com.gzz100.zbh.data.network.request;

import com.gzz100.zbh.account.User;
import com.gzz100.zbh.data.entity.DelegateEntity;
import com.gzz100.zbh.data.entity.MeetingEntity;
import com.gzz100.zbh.data.entity.MeetingInfoEntity;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.client.HttpClient;
import com.gzz100.zbh.data.network.service.MeetingService;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Lam on 2018/3/19.
 */

public class MeetingRequest {

    private MeetingService mMeetingService;

    public MeetingRequest() {
        mMeetingService = HttpClient.getInstance()
                .getRetrofit()
                .create(MeetingService.class);

    }

    public void getMeetingList(Observer<HttpResult<List<MeetingEntity>>> observer, int offset,int limit){
        User user = User.getUserFromCache();
        mMeetingService.getMeetings(user.getUserId(),user.getToken(),offset,limit)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getMyCreatedMeeting(Observer<HttpResult<List<MeetingEntity>>> observer, int offset,int limit){
        User user = User.getUserFromCache();
        mMeetingService.getMeetingsByCreator(user.getUserId(),user.getToken(),offset,limit)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getSingleMeetingInfo(Observer<HttpResult<MeetingInfoEntity>> observer, String meetingId){
        User user = User.getUserFromCache();
        mMeetingService.getSingleMeetingInfo(user.getUserId(),user.getToken(),meetingId,user.getCompanyId())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    public void getDelegates(Observer<HttpResult<List<DelegateEntity>>> observer, String meetingId){
        User user = User.getUserFromCache();
        mMeetingService.getDelegates(user.getUserId(),user.getToken(),meetingId,user.getCompanyId())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    public void addDelegate(Observer<HttpResult> observer, String meetingId,String userIdList){
        User user = User.getUserFromCache();
        mMeetingService.addDelegate(user.getUserId(),user.getToken(),meetingId,userIdList)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void cancleMeeting(Observer<HttpResult> observer, String meetingId){
        User user = User.getUserFromCache();
        mMeetingService.cancleMeeting(user.getUserId(),user.getToken(),meetingId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }


}
