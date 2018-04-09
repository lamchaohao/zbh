package com.gzz100.zbh.account;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gzz100.zbh.R;
import com.gzz100.zbh.account.adapter.CompanyAdapter;
import com.gzz100.zbh.base.BaseBackFragment;
import com.gzz100.zbh.data.entity.CompanyEntity;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.CompanyRequest;
import com.gzz100.zbh.data.network.request.UserLoginRequest;
import com.gzz100.zbh.home.root.HomeFragment;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class SearchCompFragment extends BaseBackFragment {
    @BindView(R.id.topbar)
    QMUITopBar mTopBar;
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.tv_search)
    TextView mTvSearch;
    @BindView(R.id.rcv_company_list)
    RecyclerView mRcvCompanyList;
    Unbinder unbinder;
    private CompanyRequest mRequest;
    private List<CompanyEntity> mCompanyList;
    private CompanyAdapter mAdapter;

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_search_comp, null);
        unbinder = ButterKnife.bind(this, view);
        return attachToSwipeBack(view);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        mTopBar.setTitle("搜索公司");
        mCompanyList = new ArrayList<>();
        mAdapter = new CompanyAdapter(mCompanyList,getContext());
        mRcvCompanyList.setLayoutManager(new LinearLayoutManager(getContext()));
        mRcvCompanyList.setAdapter(mAdapter);
        mRcvCompanyList.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        mRequest = new CompanyRequest();

        final Observer observer= new Observer<HttpResult<List<CompanyEntity>>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HttpResult<List<CompanyEntity>> result) {
                mCompanyList.clear();
                mCompanyList.addAll(result.getResult());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Logger.i("onComplete");
            }
        };

        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    mRequest.searchComp(observer,s.toString());
                }
            }
        });


        final Observer applyObserver = new Observer<HttpResult>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HttpResult result) {
                if (result.getCode()==1) {
                    updateUserData();
                }
            }

            @Override
            public void onError(Throwable e) {
                Toasty.error(getContext(),e.getMessage()).show();
            }

            @Override
            public void onComplete() {

            }
        };

        mAdapter.setOnApplyClickListener(new CompanyAdapter.OnApplyClickListener() {
            @Override
            public void onApplyClick(CompanyEntity companyEntity, int position) {
                mRequest.applyCompany(applyObserver,companyEntity.getCompanyId());
            }
        });

    }

    private void updateUserData(){

        Observer observer = new Observer<HttpResult<User>> (){
            @Override
            public void onSubscribe(Disposable d) {
                Logger.i(d.toString());
            }

            @Override
            public void onNext(HttpResult<User> userResult) {
                userResult.getResult().setLogin(true);
                User.save(userResult.getResult());
                Logger.i(userResult.getResult().toString());
            }

            @Override
            public void onError(Throwable e) {
                Toasty.error(getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
                startWithPop(HomeFragment.newInstance(true));
            }
        };


        UserLoginRequest.getInstance().updateToken(observer);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
