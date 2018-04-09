package com.gzz100.zbh.home.meetingadmin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseBackFragment;
import com.gzz100.zbh.data.entity.VoteEntity;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.VoteRequest;
import com.gzz100.zbh.home.meetingadmin.adapter.VoteListAdapter;
import com.qmuiteam.qmui.widget.QMUITopBar;

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
    Unbinder unbinder;
    private String mMeetingId;


    public static VoteListFragment newInstance(String meetingId){
        VoteListFragment fragment = new VoteListFragment();
        Bundle bundle=new Bundle();
        bundle.putString("meetingId",meetingId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_vote_list, null);
        unbinder = ButterKnife.bind(this, view);
        return attachToSwipeBack(view);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments()!=null) {
            mMeetingId = getArguments().getString("meetingId");
        }
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
        request.getMessages(observer,mMeetingId);

    }

    private void initView(List<VoteEntity> entityList) {
        mRcvVoteList.setLayoutManager(new LinearLayoutManager(getContext()));
        VoteListAdapter adapter=new VoteListAdapter(getContext(),entityList);
        mRcvVoteList.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
