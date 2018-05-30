package com.gzz100.zbh.home.meetingadmin.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseBackFragment;
import com.gzz100.zbh.data.entity.FinishedInfoEntity;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.MeetingRequest;
import com.gzz100.zbh.res.Common;
import com.qmuiteam.qmui.widget.QMUITopBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class FinishedMeetingFragment extends BaseBackFragment {


    @BindView(R.id.topbar)
    QMUITopBar mTopbar;
    @BindView(R.id.tv_meetingName)
    TextView mTvMeetingName;
    @BindView(R.id.tv_meetingTime)
    TextView mTvMeetingTime;
    @BindView(R.id.tv_meetingHost)
    TextView mTvMeetingHost;
    @BindView(R.id.tvSummaryStaus)
    TextView mTvSummaryStaus;
    @BindView(R.id.tvFileCount)
    TextView mTvFileCount;
    @BindView(R.id.tvVoteCount)
    TextView mTvVoteCount;
    @BindView(R.id.tvDelegateCount)
    TextView mTvDelegateCount;
    Unbinder unbinder;
    private String mMeetingId;
    private String mMeetingName;
    private String mHostId;
    private FinishedInfoEntity mFinishedInfoEntity;

    public static FinishedMeetingFragment getNewInstance(String meetingId,String meetingName){
        Bundle bundle=new Bundle();
        FinishedMeetingFragment fragment=new FinishedMeetingFragment();
        bundle.putString("meetingId",meetingId);
        bundle.putString("meetingName",meetingName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_finished_meeting, null);
        unbinder = ButterKnife.bind(this, view);
        return attachToSwipeBack(view);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVar();
        initTopbar();
        loadData();
    }

    private void initTopbar() {
        mTopbar.setTitle(mMeetingName);
        mTopbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
    }


    private void loadData() {
        MeetingRequest mRequest = new MeetingRequest();

        Observer<HttpResult<FinishedInfoEntity>> observer=new Observer<HttpResult<FinishedInfoEntity>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HttpResult<FinishedInfoEntity> result) {
                mFinishedInfoEntity = result.getResult();

                updateView();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        mRequest.getFinishedMeetingInfo(observer,mMeetingId);
    }

    private void updateView() {
        mHostId = mFinishedInfoEntity.getHostId();
        String meetingEndTime = mFinishedInfoEntity.getMeetingEndTime();
        mTvMeetingName.setText(mFinishedInfoEntity.getMeetingName());
        mTvMeetingTime.setText(mFinishedInfoEntity.getMeetingStartTime()+"-"+meetingEndTime);
        mTvMeetingHost.setText(mFinishedInfoEntity.getHost());
        if (mFinishedInfoEntity.getMeetingSummary()) {
            mTvSummaryStaus.setText("已上传");
        }else {
            mTvSummaryStaus.setText("未上传");
        }
        mTvFileCount.setText(mFinishedInfoEntity.getFileNum());
        mTvVoteCount.setText(mFinishedInfoEntity.getVoteNum());
        mTvDelegateCount.setText(mFinishedInfoEntity.getDelegateNum());

    }

    private void initVar() {
        if (getArguments()!=null) {
            mMeetingId = getArguments().getString("meetingId");
            mMeetingName = getArguments().getString("meetingName");
        }

    }


    @OnClick({R.id.rlSummary, R.id.rlFile, R.id.rlVote, R.id.rlDelegate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlSummary:
                startFragment(SummaryFragment.newInstance(mMeetingId));
                break;
            case R.id.rlFile:
                startFragment(DownloadFileFragment.newInstance(mMeetingId));
                break;
            case R.id.rlVote:
                startFragment(VoteListFragment.newInstance(mMeetingId,mHostId, Common.STATUS_END));
                break;
            case R.id.rlDelegate:
                startFragment(DelegateSummaryFragment.newInstance(mMeetingId));
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
