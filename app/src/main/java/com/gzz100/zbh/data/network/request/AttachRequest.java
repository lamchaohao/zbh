package com.gzz100.zbh.data.network.request;

import com.gzz100.zbh.account.User;
import com.gzz100.zbh.data.entity.StarFileEntity;
import com.gzz100.zbh.data.entity.UpdateInfo;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.client.HttpClient;
import com.gzz100.zbh.data.network.service.AttachService;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Lam on 2018/5/16.
 */

public class AttachRequest {

    AttachService mAttachService;
    public AttachRequest() {
        mAttachService = HttpClient.getInstance()
                .getRetrofit()
                .create(AttachService.class);

    }

    public void checkUpdate(Observer<HttpResult<UpdateInfo>> observer, String versionCode, String versionName){
        User user = User.getUserFromCache();
        mAttachService.checkUpdate(user.getUserId(),user.getToken(),versionCode,versionName)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void submitFeedback(Observer<HttpResult> observer,String versionCode,String feedbackContent){
        User user = User.getUserFromCache();
        mAttachService.submitFeedback(user.getUserId(),user.getToken(),versionCode,feedbackContent)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


    public void getStarFile(Observer<HttpResult<List<StarFileEntity>>> observer,int offset,int limit){

        User user = User.getUserFromCache();
        mAttachService.getStarFile(user.getUserId(),user.getToken(),offset,limit)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }


    public void addDocumentToStar(Observer<HttpResult> observer,String docIdList){
        User user = User.getUserFromCache();
        mAttachService.addToStar(user.getUserId(),user.getToken(),docIdList)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
    public void deleteDocumentFromStar(Observer<HttpResult> observer,String docIdList){
        User user = User.getUserFromCache();
        mAttachService.deleteFromStar(user.getUserId(),user.getToken(),docIdList)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

}
