package com.gzz100.zbh.home.meetingadmin.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseBackFragment;
import com.gzz100.zbh.data.entity.DelegateSummaryEntity;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.MeetingRequest;
import com.gzz100.zbh.home.meetingadmin.adapter.DelegateSummaryAdapter;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Lam on 2018/5/23.
 */

public class DelegateSummaryFragment extends BaseBackFragment {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;
    @BindView(R.id.tv_delegatesum)
    TextView mTvDelegatesum;
    @BindView(R.id.tv_delegateArrived)
    TextView mTvDelegateArrived;
    @BindView(R.id.tv_normals)
    TextView mTvNormals;
    @BindView(R.id.tv_absents)
    TextView mTvAbsents;
    @BindView(R.id.tv_personCount)
    TextView mTvPerson;
    @BindView(R.id.rcv_delegates)
    RecyclerView mRcvDelegates;
    Unbinder unbinder;
    private String mMeetingId;
    private List<DelegateSummaryEntity.DelegateBean> mDelegateList;
    private DelegateSummaryAdapter mAdapter;
    private DelegateSummaryEntity mEntity;

    public static DelegateSummaryFragment newInstance(String meetingId) {
        DelegateSummaryFragment fragment = new DelegateSummaryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("meetingId", meetingId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_delegate_summary, null);
        unbinder = ButterKnife.bind(this, view);
        return attachToSwipeBack(view);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVar();
        initTopbar();
        initView();
        loadData();
    }

    private void loadData() {
        Observer<HttpResult<DelegateSummaryEntity>> observer=new Observer<HttpResult<DelegateSummaryEntity>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HttpResult<DelegateSummaryEntity> result) {
                mEntity = result.getResult();
                setToView();

            }

            @Override
            public void onError(Throwable e) {
                Toasty.error(getContext().getApplicationContext(),e.getMessage()).show();
            }

            @Override
            public void onComplete() {

            }
        };
        MeetingRequest request=new MeetingRequest();
        request.getDelegateSummary(observer,mMeetingId);
    }

    private void setToView() {
        mTvDelegateArrived.setText("到场率:"+mEntity.getAttendance());
        mTvDelegatesum.setText("总参会人数:"+mEntity.getDelegateNum());
        mDelegateList.clear();
        if (mEntity.getNormal()!=null) {
            mDelegateList.addAll(mEntity.getNormal());
        }
        mAdapter.notifyDataSetChanged();
        mTvPerson.setText(mDelegateList.size()+"人");
    }

    private void initView() {
        mDelegateList = new ArrayList<>();
        mAdapter = new DelegateSummaryAdapter(mDelegateList,getContext());
        mRcvDelegates.setLayoutManager(new LinearLayoutManager(getContext()));
        mRcvDelegates.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        mRcvDelegates.setAdapter(mAdapter);

        mTvNormals.setTextColor(Color.WHITE);
        mTvNormals.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

    }

    private void initTopbar() {
        mTopBar.setTitle("人员统计");
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
    }

    private void initVar() {
        if (getArguments()!=null) {
            mMeetingId = getArguments().getString("meetingId");
        }
    }

    @OnClick({R.id.tv_normals,R.id.tv_absents})
    void onClick(View v){
        switch (v.getId()) {
            case R.id.tv_absents:
                mTvNormals.setTextColor(Color.BLACK);
                mTvNormals.setBackgroundColor(0);
                mTvAbsents.setTextColor(Color.WHITE);
                mTvAbsents.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                mDelegateList.clear();
                if (mEntity.getAbsent()!=null) {
                    mDelegateList.addAll(mEntity.getAbsent());
                }
                mAdapter.notifyDataSetChanged();
                mTvPerson.setText(mDelegateList.size()+"人");
                break;
            case R.id.tv_normals:
                mTvAbsents.setTextColor(Color.BLACK);
                mTvAbsents.setBackgroundColor(0);
                mTvNormals.setTextColor(Color.WHITE);
                mTvNormals.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                mDelegateList.clear();
                if (mEntity.getNormal()!=null) {
                    mDelegateList.addAll(mEntity.getNormal());
                }
                mAdapter.notifyDataSetChanged();
                mTvPerson.setText(mDelegateList.size()+"人");
                break;
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
