package com.gzz100.zbh.data.network.client;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Lam on 2017/9/18.
 */

public class RspCheckInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        try {
            ResponseBody rspBody = response.body();
            JSONObject jsonObject = new JSONObject(InterceptorUtils.getRspData(rspBody));
            int status = jsonObject.getInt("code");
            if (status ==2){
                throw new IOException(jsonObject.getString("msg"));
            }else if (status==105){
                throw new IOException(jsonObject.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
            throw new IOException("parase data error");
        }catch (Exception e){
            if (e instanceof IOException){
                throw (IOException)e;
            }
        }

        return response;
    }
}
