package com.gzz100.zbh.data.network.request;

import com.gzz100.zbh.account.User;
import com.gzz100.zbh.data.entity.DocumentEntity;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.client.RspCheckInterceptor;
import com.gzz100.zbh.data.network.service.DocumentService;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.gzz100.zbh.res.Common.BASE_DOCUMENT_URL;

/**
 * Created by Lam on 2018/3/27.
 */

public class DocumentRequest {


    private DocumentService mDocumentService;
    public DocumentRequest() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor(new RspCheckInterceptor());//添加拦截器
        builder.connectTimeout(120, TimeUnit.SECONDS);
        builder.readTimeout(60, TimeUnit.SECONDS);
        builder.writeTimeout(60, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);
        OkHttpClient client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_DOCUMENT_URL)
                .client(client)
                .build();
        mDocumentService = retrofit.create(DocumentService.class);

    }


    public void getDocumentList(Observer<HttpResult<List<DocumentEntity>>> observer, String meetingId){
        User user = User.getUserFromCache();
        mDocumentService.getDocumentList(user.getUserId(),user.getToken(),meetingId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

}
