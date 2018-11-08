package com.gzz100.zbh.home.meetingadmin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseBackFragment;
import com.gzz100.zbh.data.ObserverImpl;
import com.gzz100.zbh.data.entity.DelegateSummaryEntity;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.MeetingRequest;
import com.gzz100.zbh.home.meetingadmin.adapter.DelegateSummaryAdapter;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;

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
    @BindView(R.id.tv_arrived_percent)
    TextView tvArrivedPercent;
    @BindView(R.id.tablayout_delegate_summary)
    TabLayout mTabLayout;
    @BindView(R.id.rcv_delegates)
    RecyclerView mRcvDelegates;
    Unbinder unbinder;
    private String mMeetingId;
    private List<DelegateSummaryEntity.DelegateBean> mDelegateList;
    private DelegateSummaryAdapter mAdapter;
    private DelegateSummaryEntity mEntity;
    private ObserverImpl<HttpResult<DelegateSummaryEntity>> mObserver;

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
        initTablayout();
        loadData();
    }

    private void initTablayout() {
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        mDelegateList.clear();
                        if (mEntity.getNormal()!=null) {
                            mDelegateList.addAll(mEntity.getNormal());
                        }
                        mAdapter.notifyDataSetChanged();
                        break;
                    case 1:
                        mDelegateList.clear();
                        if (mEntity.getAbsent()!=null) {
                            mDelegateList.addAll(mEntity.getAbsent());
                        }
                        mAdapter.notifyDataSetChanged();
                        break;
                    case 2:
                        mDelegateList.clear();
                        if (mEntity.getAbsent()!=null) {
                            mDelegateList.addAll(mEntity.getAbsent());
                        }
                        mAdapter.notifyDataSetChanged();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void loadData() {
        mObserver = new ObserverImpl<HttpResult<DelegateSummaryEntity>>() {

            @Override
            protected void onResponse(HttpResult<DelegateSummaryEntity> result) {
                mEntity = result.getResult();
                setToView();
            }

            @Override
            protected void onFailure(Throwable e) {
                Toasty.error(getContext().getApplicationContext(),e.getMessage()).show();
            }
        };
        MeetingRequest request=new MeetingRequest();
        request.getDelegateSummary(mObserver,mMeetingId);
    }

    private void setToView() {
        mTvDelegateArrived.setText("总参会人数:"+mEntity.getDelegateNum());
        mTvDelegatesum.setText("到场人数: "+mEntity.getNormal().size());
        tvArrivedPercent.setText("到场率："+mEntity.getAttendance());
        mDelegateList.clear();
        if (mEntity.getNormal()!=null) {
            mDelegateList.addAll(mEntity.getNormal());
        }
        mAdapter.notifyDataSetChanged();
    }

    private void initView() {
        mDelegateList = new ArrayList<>();
        mAdapter = new DelegateSummaryAdapter(mDelegateList,getContext());
        mRcvDelegates.setLayoutManager(new LinearLayoutManager(getContext()));
        mRcvDelegates.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        mRcvDelegates.setAdapter(mAdapter);

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



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        mObserver.cancleRequest();
    }
}
