package com.gzz100.zbh.data.network.request;

import com.gzz100.zbh.account.User;
import com.gzz100.zbh.data.entity.VoteDetailEntity;
import com.gzz100.zbh.data.entity.VoteEntity;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.client.HttpClient;
import com.gzz100.zbh.data.network.service.VoteService;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Lam on 2018/4/4.
 */

public class VoteRequest {

    private VoteService mVoteService;
    public VoteRequest() {
        mVoteService = HttpClient.getInstance()
                .getRetrofit()
                .create(VoteService.class);

    }

    public void getVoteList(Observer<HttpResult<List<VoteEntity>>> observer, String meetingId){
        User user = User.getUserFromCache();
        mVoteService.getVoteList(user.getUserId(),user.getToken(),meetingId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getVoteInfo(Observer<HttpResult<VoteDetailEntity>> observer, String meetingId, String voteId){
        User user = User.getUserFromCache();
        mVoteService.getVoteInfo(user.getUserId(),user.getToken(),meetingId,voteId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void uploadVoteResult(Observer<HttpResult> observer, String voteOptionIdList, String voteId){
        User user = User.getUserFromCache();
        mVoteService.uploadVoteResult(user.getUserId(),user.getToken(),voteOptionIdList,voteId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void startVote(Observer<HttpResult> observer,String voteId){
        User user = User.getUserFromCache();
        mVoteService.startVote(user.getUserId(),user.getToken(),voteId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void endVote(Observer<HttpResult> observer,String voteId){
        User user = User.getUserFromCache();
        mVoteService.endVote(user.getUserId(),user.getToken(),voteId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void deleteVote(Observer<HttpResult> observer, String voteId){
        User user = User.getUserFromCache();
        mVoteService.deleteVote(voteId,user.getUserId(),user.getToken())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


}
