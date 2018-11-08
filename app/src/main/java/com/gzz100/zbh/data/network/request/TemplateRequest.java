package com.gzz100.zbh.data.network.request;

import com.gzz100.zbh.account.User;
import com.gzz100.zbh.data.entity.TemplateEntity;
import com.gzz100.zbh.data.entity.UpdateMeetingEntity;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.client.HttpClient;
import com.gzz100.zbh.data.network.service.TemplateService;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Lam on 2018/7/27.
 */

public class TemplateRequest {

    private final TemplateService mTemplateService;

    public TemplateRequest() {
        mTemplateService = HttpClient.getInstance()
                .getRetrofit()
                .create(TemplateService.class);
    }

    public void getTemplateList(Observer<HttpResult<List<TemplateEntity>>> observer, int offset, int limit){

        User user = User.getUserFromCache();

        mTemplateService.getTemplateList(user.getUserId(),user.getToken(),user.getCompanyId(),offset,limit)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }


    public void addMeetingToTemplate(Observer<HttpResult> observer,String meetingId){
        User user = User.getUserFromCache();
        mTemplateService.addMeetingToTemplate(user.getUserId(),user.getToken(),meetingId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getTemplateById(Observer<HttpResult<UpdateMeetingEntity>> observer, String templateId){

        User user = User.getUserFromCache();
        mTemplateService.getTemplateById(user.getUserId(),user.getToken(),templateId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }


    public void deleteTemplateById(Observer<HttpResult> observer,String templateId){
        User user = User.getUserFromCache();
        mTemplateService.deleteTemplate(user.getUserId(),user.getToken(),user.getCompanyId(),templateId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void addTemplate(Observer<HttpResult> observer,String templateName, String startTime,
                            String endTime, String hostId,
                            String summaryId, String copyIdList, String agendaList, String delegateIdList,String notifyTime){
        User user = User.getUserFromCache();
        mTemplateService.addTemplate(user.getUserId(),user.getToken(),templateName,
                                        user.getCompanyId(),startTime,endTime,hostId,summaryId,
                                        copyIdList,agendaList,delegateIdList,notifyTime)
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(observer);
    }

    public void updateTemplate(Observer<HttpResult> observer,String templateId,String templateName, String startTime,
                            String endTime, String hostId,
                            String summaryId, String copyIdList, String agendaList, String delegateIdList,String notifyTime){
        User user = User.getUserFromCache();
        mTemplateService.updateTemplateById(user.getUserId(),user.getToken(),templateId,templateName,
                                            user.getCompanyId(),startTime,endTime,hostId,summaryId,
                                            copyIdList,agendaList,delegateIdList,notifyTime)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void useTemplateAddMeeting(Observer<HttpResult> observer,String templateId,String meetingPlaceId){
        User user = User.getUserFromCache();
        mTemplateService.useTemplateAddMeeting(user.getUserId(),user.getToken(),templateId,meetingPlaceId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


}
