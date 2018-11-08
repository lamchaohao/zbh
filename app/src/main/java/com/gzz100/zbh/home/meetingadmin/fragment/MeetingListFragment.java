package com.gzz100.zbh.home.meetingadmin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseFragment;
import com.gzz100.zbh.data.ObserverImpl;
import com.gzz100.zbh.data.entity.MeetingEntity;
import com.gzz100.zbh.data.eventEnity.PushUpdateEntity;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.MeetingRequest;
import com.gzz100.zbh.data.network.request.TemplateRequest;
import com.gzz100.zbh.home.meetingadmin.adapter.MeetingListAdapter;
import com.gzz100.zbh.home.root.HomeFragment;
import com.gzz100.zbh.res.Common;
import com.qmuiteam.qmui.widget.QMUIEmptyView;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
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
import es.dmoral.toasty.Toasty;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * create by lamchaohao 2018/1/15
 */
public class MeetingListFragment extends BaseFragment {

    @BindView(R.id.prl_refresh)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.rcv_meetinglist)
    RecyclerView mRcvMeetinglist;
    @BindView(R.id.emptyView)
    QMUIEmptyView mEmptyView;
    View mRootView;
    private List<MeetingEntity> mMeetings;
    private MeetingListAdapter mAdapter;
    private boolean isCreator;
    private MeetingRequest mRequest;

    private int offset=0;

    public static MeetingListFragment getNewInstance(boolean isCreator){
        Bundle bundle = new Bundle();
        bundle.putBoolean("isCreator",isCreator);
        MeetingListFragment fragment = new MeetingListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.fragment_meeting_list, null);
        ButterKnife.bind(this, mRootView);
        EventBus.getDefault().register(this);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initVar();
        initView();
        loadData();
    }



    private void initVar() {
        if (getArguments()!=null) {
            isCreator = getArguments().getBoolean("isCreator");
        }
        mRequest = new MeetingRequest();
    }



    private void loadData(){

        Observer<HttpResult<List<MeetingEntity>>> observer = new Observer<HttpResult<List<MeetingEntity>>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HttpResult<List<MeetingEntity>> result) {
                List<MeetingEntity> meetingList = result.getResult();
                offset=meetingList.size();
                if (meetingList!=null){
                    mMeetings.clear();
                    mMeetings.addAll(meetingList);
                    mAdapter.notifyDataSetChanged();
                    if (mMeetings.size()==0){
                        mEmptyView.show();
                    }else {
                        mEmptyView.hide();
                    }
                }else {
                    mEmptyView.show();
                }
            }

            @Override
            public void onError(Throwable e) {
                mRefreshLayout.finishRefresh();
            }

            @Override
            public void onComplete() {
                mRefreshLayout.finishRefresh();
            }
        };

        if (isCreator){
            mRequest.getMyCreatedMeeting(observer,0,20);
        }else {
            mRequest.getMeetingList(observer,0,20);
        }
    }

    private void initView() {
        initRefreshLayout();
        mRcvMeetinglist.setLayoutManager(new LinearLayoutManager(getContext()));
        mMeetings = new ArrayList();
        mAdapter = new MeetingListAdapter(getContext(), mMeetings);
        mRcvMeetinglist.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new MeetingListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
               dealOnMeetingItemClick(pos);
            }

            @Override
            public void onItemLongClick(int pos) {
                showTemplateDialog(pos);
            }
        });
    }

    private void showTemplateDialog(final int pos) {

        new QMUIDialog.MessageDialogBuilder(_mActivity)
                .setMessage("添加到会议模板")
                .addAction("添加", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        addToTemplate(pos);
                    }
                })
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .show();

    }

    private void addToTemplate(int pos) {
        String meetingId = mMeetings.get(pos).getMeetingId();
        TemplateRequest request = new TemplateRequest();
        request.addMeetingToTemplate(new ObserverImpl<HttpResult>() {
            @Override
            protected void onResponse(HttpResult result) {
                Toasty.success(_mActivity,"添加成功").show();
            }

            @Override
            protected void onFailure(Throwable e) {
                Toasty.error(_mActivity,e.getMessage()).show();
            }
        },meetingId);

    }

    private void dealOnMeetingItemClick(int pos) {
        MeetingEntity meetingEntity = mMeetings.get(pos);
        meetingEntity.setUnread("");
        mAdapter.notifyItemChanged(pos);
        PushUpdateEntity pushUpdateEntity = new PushUpdateEntity(PushUpdateEntity.PassthrougMsgType.meetingDecrement);
        EventBus.getDefault().post(pushUpdateEntity);
        if(meetingEntity.getMeetingStatus()== Common.STATUS_END){
            if (getParentFragment()!=null) {
                ((BaseFragment) getParentFragment())
                        .startParentFragment(
                                FinishedMeetingFragment.getNewInstance
                                        (meetingEntity.getMeetingId(),meetingEntity.getMeetingName()));
            }
//
        }else {
            if (getParentFragment()!=null) {
                long groupId=0;
                if (!TextUtils.isEmpty(meetingEntity.getMimcTopicId())) {
                    groupId = Long.parseLong(meetingEntity.getMimcTopicId());
                }
                ((BaseFragment) getParentFragment())
                        .startParentFragment(
                                MeetingParentFragment.getNewInstance
                                        (meetingEntity.getMeetingId(),meetingEntity.getMeetingName(), groupId));
            }
        }
    }

    private void initRefreshLayout() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                loadData();
            }
        });

        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                loadMore();
            }
        });

        mRefreshLayout.setRefreshHeader(new FalsifyHeader(getContext()));
//        mRefreshLayout.setRefreshFooter(new FalsifyFooter(getContext()));
    }

    private void loadMore() {
        Observer<HttpResult<List<MeetingEntity>>> observer = new Observer<HttpResult<List<MeetingEntity>>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HttpResult<List<MeetingEntity>> result) {
                List<MeetingEntity> meetingList = result.getResult();
                offset+=meetingList.size();
                if (meetingList!=null){
                    mMeetings.addAll(meetingList);
                    mAdapter.notifyDataSetChanged();
                }else {
                    mEmptyView.show();
                }
            }

            @Override
            public void onError(Throwable e) {
                mRefreshLayout.finishLoadMore();
            }

            @Override
            public void onComplete() {
                mRefreshLayout.finishLoadMore();
            }
        };

        if (isCreator){
            mRequest.getMyCreatedMeeting(observer,offset,20);
        }else {
            mRequest.getMeetingList(observer,offset,20);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPage(HomeFragment.HomePage page){
        switch (page) {
            case meeting:
                loadData();
                break;

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
