package com.gzz100.zbh.data.network.client;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.gzz100.zbh.res.Common.BASE_URL;

/**
 * Created by Lam on 2018/3/9.
 */

public class HttpClient {
    private static HttpClient INSTANCE;
    private Retrofit retrofit;

    private HttpClient(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor(new RspCheckInterceptor());//添加拦截器
        builder.connectTimeout(120, TimeUnit.SECONDS);
        builder.readTimeout(120, TimeUnit.SECONDS);
        builder.writeTimeout(120, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);
        OkHttpClient client = builder.build();

        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .client(client)
                .build();
    }

    public static HttpClient getInstance(){
        if (INSTANCE!=null) {
            return INSTANCE;
        }else {
            INSTANCE = new HttpClient();
            return INSTANCE;
        }
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
