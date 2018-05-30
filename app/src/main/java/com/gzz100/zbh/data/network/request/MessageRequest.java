package com.gzz100.zbh.data.network.request;

import com.gzz100.zbh.account.User;
import com.gzz100.zbh.data.entity.MessageEntity;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.client.HttpClient;
import com.gzz100.zbh.data.network.service.MessageService;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Lam on 2018/3/30.
 */

public class MessageRequest {

    private MessageService mMessageService;
    public MessageRequest() {
        mMessageService = HttpClient.getInstance()
                .getRetrofit()
                .create(MessageService.class);

    }

    public void getMessages(Observer<HttpResult<List<MessageEntity>>> observer, int offset, int limit){
        User user = User.getUserFromCache();
        mMessageService.getMessages(user.getUserId(),user.getToken(),user.getCompanyId(),offset,limit)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
