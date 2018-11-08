package com.gzz100.zbh.home.appointment.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseFragment;
import com.gzz100.zbh.data.ObserverImpl;
import com.gzz100.zbh.data.entity.TemplateEntity;
import com.gzz100.zbh.data.eventEnity.MeetingRoomEventEntity;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.TemplateRequest;
import com.gzz100.zbh.home.appointment.adapter.TemplateAdapter;
import com.gzz100.zbh.home.meetingadmin.fragment.SelectMeetingRoomFragment;
import com.gzz100.zbh.widget.LineDecoration;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.FalsifyHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;

/**
 * Created by Lam on 2018/1/30.
 */

public class TemplateFragment extends BaseFragment {
    @BindView(R.id.rcv_template)
    RecyclerView mRcvTemplate;
    @BindView(R.id.iv_empty_view)
    ImageView mEmptyView;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout mRefreshLayout;
    Unbinder unbinder;
    private List<TemplateEntity> mTempList;
    private TemplateAdapter mAdapter;
    private int mOffset;
    private TemplateRequest mRequest;
    private String mTemplateId;

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_template, null);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initVar();
        initView();
        loadTemplateData();
    }

    private void initVar() {
        mTempList = new ArrayList<>();
    }

    private void initView() {
        mRefreshLayout.setRefreshHeader(new FalsifyHeader(getContext()));
        mRefreshLayout.setEnableLoadMore(true);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                loadTemplateData();
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                loadMore();
            }
        });
        mRcvTemplate.setLayoutManager(new LinearLayoutManager(getContext()));
        mRcvTemplate.addItemDecoration(new LineDecoration(getContext()));
        mAdapter = new TemplateAdapter(mTempList, getContext());
        mRcvTemplate.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new TemplateAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                ((BaseFragment)getParentFragment().getParentFragment()).startFragment(TemplateDetailFragment.newInstance(mTempList.get(pos).getMeetingId()));
            }

            @Override
            public void onItemLongPress(int pos) {
                mTemplateId = mTempList.get(pos).getMeetingId();
                ((BaseFragment)getParentFragment().getParentFragment()).startFragment(new SelectMeetingRoomFragment());
            }
        });
    }

    private void loadTemplateData() {

        mRequest = new TemplateRequest();
        mRequest.getTemplateList(new ObserverImpl<HttpResult<List<TemplateEntity>>>() {
            @Override
            protected void onResponse(HttpResult<List<TemplateEntity>> result) {
                List<TemplateEntity> entities = result.getResult();
                mTempList.clear();
                mTempList.addAll(entities);
                mOffset=mTempList.size();
                mAdapter.notifyDataSetChanged();
                if (mTempList.size()==0){
                    mEmptyView.setVisibility(View.VISIBLE);
                }else {
                    mEmptyView.setVisibility(View.GONE);
                }
                mRefreshLayout.finishRefresh();
            }

            @Override
            protected void onFailure(Throwable e) {
                Toasty.error(getContext(),e.getMessage()).show();
            }
        },0,10);
    }

    private void loadMore( ){
        mRequest.getTemplateList(new ObserverImpl<HttpResult<List<TemplateEntity>>>() {
            @Override
            protected void onResponse(HttpResult<List<TemplateEntity>> result) {
                List<TemplateEntity> entities = result.getResult();
                mTempList.addAll(entities);
                mOffset=mTempList.size();
                mAdapter.notifyDataSetChanged();
                if (mTempList.size()==0){
                    mEmptyView.setVisibility(View.VISIBLE);
                }else {
                    mEmptyView.setVisibility(View.GONE);
                }
                mRefreshLayout.finishLoadMore();
            }

            @Override
            protected void onFailure(Throwable e) {
                Toasty.error(getContext(),e.getMessage()).show();
            }
        },mOffset,mOffset+10);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSelectedRoom(MeetingRoomEventEntity entity){

        final QMUITipDialog dialog = new QMUITipDialog.Builder(getContext())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在添加会议到"+entity.meetingRoomName)
                .create();

        mRequest.useTemplateAddMeeting(new ObserverImpl<HttpResult>() {
            @Override
            protected void onResponse(HttpResult result) {
                dialog.dismiss();
                Toasty.success(_mActivity,"会议添加成功").show();

            }

            @Override
            protected void onFailure(Throwable e) {
                dialog.dismiss();
                Toasty.error(_mActivity,e.getMessage()).show();
            }
        },mTemplateId,entity.meetingRoomId);


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    @OnClick(R.id.ll_add_template)
    public void onViewClicked() {
        ((BaseFragment)getParentFragment().getParentFragment()).startFragment(TemplateDetailFragment.newInstance("0"));

    }

}
