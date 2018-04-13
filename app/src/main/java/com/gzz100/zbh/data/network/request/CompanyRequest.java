package com.gzz100.zbh.data.network.request;

import com.gzz100.zbh.account.User;
import com.gzz100.zbh.data.entity.ApplyEntity;
import com.gzz100.zbh.data.entity.CompanyEntity;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.client.HttpClient;
import com.gzz100.zbh.data.network.service.CompanyService;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Lam on 2018/3/12.
 */

public class CompanyRequest {

    private final CompanyService mCompanyService;

    public CompanyRequest() {
        mCompanyService = HttpClient.getInstance().getRetrofit().create(CompanyService.class);
    }
    //搜索公司
    public void searchComp(Observer<HttpResult<List<CompanyEntity>>> observer, String keyword){
        User user = User.getUserFromCache();
            mCompanyService.search(user.getUserId(),user.getToken(),keyword)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
    }

    //申请加入公司
    public void applyCompany(Observer<HttpResult<ApplyEntity>> observer, String companyId){
        User user = User.getUserFromCache();
        mCompanyService.applyIntoCompany(user.getUserId(),user.getToken(),companyId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    //申请加入公司
    public void quitCompany(Observer<HttpResult> observer){
        User user = User.getUserFromCache();
        mCompanyService.quitCompany(user.getUserId(),user.getToken(),user.getCompanyId())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void cancelApplyCompany(Observer<HttpResult> observer){

        User user = User.getUserFromCache();
        mCompanyService.cancelApply(user.getUserId(),user.getToken(),user.getApply().getApplyId())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


}
