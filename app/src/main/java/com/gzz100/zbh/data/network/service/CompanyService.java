package com.gzz100.zbh.data.network.service;

import com.gzz100.zbh.data.entity.CompanyEntity;
import com.gzz100.zbh.data.network.HttpResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Lam on 2018/3/12.
 */

public interface CompanyService {

    @FormUrlEncoded
    @POST("searchCompany")
    Observable<HttpResult<List<CompanyEntity>>> search(@Field("userId") String userId, @Field("token") String token,
                                                                  @Field("keyword") String keyword);
    @FormUrlEncoded
    @POST("applyIntoCompany")
    Observable<HttpResult> applyIntoCompany(@Field("userId") String userId, @Field("token") String token,
                                                       @Field("companyId") String companyId);
}
