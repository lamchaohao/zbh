package com.gzz100.zbh.mimc;

import com.google.gson.Gson;
import com.gzz100.zbh.account.User;
import com.gzz100.zbh.data.entity.MimcToken;
import com.gzz100.zbh.data.network.client.HttpClient;
import com.gzz100.zbh.data.network.service.TokenService;
import com.xiaomi.mimc.MIMCTokenFetcher;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;


/**
 * Created by Lam on 2018/5/8.
 */

public class TokenFetcher implements MIMCTokenFetcher {
    @Override
    public String fetchToken() {

        User user = User.getUserFromCache();
        Gson gson=new Gson();
        TokenService tokenService = HttpClient.getInstance().getRetrofit().create(TokenService.class);
        Call<MimcToken> tokenCall = tokenService.getMimcToken(user.getUserId(), user.getToken());
        MimcToken mimcToken = null;
        try {
            Response<MimcToken> execute = tokenCall.execute();
            mimcToken = execute.body();

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (mimcToken!=null){
            String tokenData = gson.toJson(mimcToken.getData());
            return tokenData;
        }else {
            return null;
        }
    }
}
