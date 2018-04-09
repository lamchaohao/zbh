package com.gzz100.zbh.data.network.request;

import com.gzz100.zbh.account.User;
import com.gzz100.zbh.data.entity.MeetingRoomEntity;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.client.HttpClient;
import com.gzz100.zbh.data.network.service.MeetingRoomService;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Lam on 2018/3/9.
 */

public class MeetingRoomRequest {
    MeetingRoomService meetingRoomService;
    public MeetingRoomRequest() {
       meetingRoomService = HttpClient.getInstance()
               .getRetrofit()
               .create(MeetingRoomService.class);

    }


    public void getMeetingRooms(Observer<HttpResult<List<MeetingRoomEntity>>> observer){
        User user = User.getUserFromCache();
        meetingRoomService.getMeetingRooms(user.getUserId(),user.getToken(),Integer.parseInt(user.getCompanyId()))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getAvailableMeetingRooms(Observer<HttpResult<List<MeetingRoomEntity>>> observer,String date){
        User user = User.getUserFromCache();
        meetingRoomService.getAvailableRooms(user.getUserId(),user.getToken(),Integer.parseInt(user.getCompanyId()),date)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getMeetingListByRoomId(Observer<HttpResult<List<MeetingRoomEntity>>> observer,String roomId,String startTime){
        User user = User.getUserFromCache();
        meetingRoomService.getMeetingListByRoomId(user.getUserId(),user.getToken(),Integer.parseInt(user.getCompanyId()),roomId,startTime)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }



}
