package com.gzz100.zbh.home.meetingadmin.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseBackFragment;
import com.gzz100.zbh.data.entity.VoteEntity;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.UploadRequest;
import com.gzz100.zbh.data.network.request.VoteRequest;
import com.gzz100.zbh.home.appointment.entity.VoteWrap;
import com.gzz100.zbh.home.appointment.fragment.SelectVoteModeFragment;
import com.gzz100.zbh.home.meetingadmin.adapter.VoteListAdapter;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.widget.QMUIEmptyView;
import com.qmuiteam.qmui.widget.QMUITopBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Lam on 2018/4/3.
 */

public class VoteListFragment extends BaseBackFragment {
    @BindView(R.id.topbar)
    QMUITopBar mTopbar;
    @BindView(R.id.rcv_vote_list)
    RecyclerView mRcvVoteList;
    @BindView(R.id.emptyView)
    QMUIEmptyView mEmptyView;
    Unbinder unbinder;
    private String mMeetingId;
    private String mHostId;

    public static VoteListFragment newInstance(String meetingId,String hostId){
        VoteListFragment fragment = new VoteListFragment();
        Bundle bundle=new Bundle();
        bundle.putString("meetingId",meetingId);
        bundle.putString("hostId",hostId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_vote_list, null);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return attachToSwipeBack(view);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments()!=null) {
            mMeetingId = getArguments().getString("meetingId");
            mHostId = getArguments().getString("hostId");
        }
        mTopbar.setTitle("投票");
        Button addVote = mTopbar.addRightTextButton("新增投票", R.id.buttonSave);
        addVote.setTextColor(Color.WHITE);
        addVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFragment(new SelectVoteModeFragment());
            }
        });
        loadData();
    }

    private void loadData() {

        Observer<HttpResult<List<VoteEntity>>> observer = new Observer<HttpResult<List<VoteEntity>>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HttpResult<List<VoteEntity>> result) {
                initView(result.getResult());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        VoteRequest request=new VoteRequest();
        request.getVoteList(observer,mMeetingId);

    }

    private void initView(List<VoteEntity> entityList) {
        if (entityList==null){
            mEmptyView.show();
        }else {
            if (entityList.size()==0) {
                mEmptyView.show();
            }else {
                mEmptyView.hide();
            }
        }
        mRcvVoteList.setLayoutManager(new LinearLayoutManager(getContext()));
        VoteListAdapter adapter=new VoteListAdapter(getContext(),entityList);
        mRcvVoteList.setAdapter(adapter);

        adapter.setOnItemClickListener(new VoteListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(VoteEntity vote, int position) {
                startFragment(VoteFragment.newInstance(mMeetingId,vote.getVoteId(),mHostId));
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveVote(VoteWrap voteWrap){
        switch (voteWrap.getCode()) {
            case VoteWrap.CODE_DELETE://delete
                break;
            case VoteWrap.CODE_ADD://add
                Logger.i(voteWrap.getVoteName());
                uploadVote(voteWrap);
                break;
            case VoteWrap.CODE_UPDATE://update
                break;
        }
    }

    private void uploadVote(VoteWrap voteWrap) {

        UploadRequest request = new UploadRequest();
        Observer<HttpResult> observer = new Observer<HttpResult>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HttpResult result) {
                Logger.i("onNext--");
            }

            @Override
            public void onError(Throwable e) {
                Logger.i("onError--"+e.getMessage());
            }

            @Override
            public void onComplete() {

                Logger.i("complete==");

            }
        };
        request.uploadVoteList(observer,voteWrap,mMeetingId);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }
}
