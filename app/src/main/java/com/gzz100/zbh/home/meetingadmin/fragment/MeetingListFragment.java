package com.gzz100.zbh.home.meetingadmin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseFragment;
import com.gzz100.zbh.data.entity.MeetingEntity;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.MeetingRequest;
import com.gzz100.zbh.home.meetingadmin.adapter.MeetingListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * create by lamchaohao 2018/1/15
 */
public class MeetingListFragment extends BaseFragment {

    @BindView(R.id.rcv_meetinglist)
    RecyclerView mRcvMeetinglist;
    View mRootView;
    private List<MeetingEntity> mMeetings;
    private MeetingListAdapter mAdapter;
    private boolean isCreator;

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
    }


    private void loadData(){

        Observer<HttpResult<List<MeetingEntity>>> observer = new Observer<HttpResult<List<MeetingEntity>>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HttpResult<List<MeetingEntity>> result) {
                List<MeetingEntity> meetingList = result.getResult();
                mMeetings.addAll(meetingList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        MeetingRequest request=new MeetingRequest();
        if (isCreator){
            request.getMyCreatedMeeting(observer,0,20);
        }else {
            request.getMeetingList(observer,0,20);
        }
    }

    private void initView() {
        mRcvMeetinglist.setLayoutManager(new LinearLayoutManager(getContext()));
//        for (int i =0;i<15;i++){
//            metingBeans.add(new MetingBean("每周例会","科研会议室","14:30","16:00"));
//        }
        mMeetings = new ArrayList();
        mAdapter = new MeetingListAdapter(getContext(), mMeetings);
        mRcvMeetinglist.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new MeetingListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                ((BaseFragment) getParentFragment().getParentFragment())
                        .startFragment(MeetingParentFragment.getNewInstance(mMeetings.get(pos).getMeetingId(),mMeetings.get(pos).getMeetingName()));

            }
        });
    }

}
