package com.gzz100.zbh.home.meetingadmin.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.account.User;
import com.gzz100.zbh.base.BaseFragment;
import com.gzz100.zbh.data.entity.MeetingInfoEntity;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.MeetingRequest;
import com.gzz100.zbh.home.meetingadmin.adapter.AgendaInfoAdapter;
import com.gzz100.zbh.home.root.UpdateMsg;
import com.gzz100.zbh.utils.DensityUtil;
import com.gzz100.zbh.utils.TextHeadPicUtil;
import com.gzz100.zbh.utils.TimeFormatUtil;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.gzz100.zbh.res.Common.ROLE_HOST;
import static com.gzz100.zbh.res.Common.STATUS_END;
import static com.gzz100.zbh.res.Common.STATUS_ON;
import static com.gzz100.zbh.res.Common.STATUS_READY;

public class MeetingInfoFragment extends BaseFragment {


    @BindView(R.id.tv_info_meetingName)
    TextView mtvMeetingName;
    @BindView(R.id.tv_info_startOrSign)
    TextView mTvStartOrSign;
    @BindView(R.id.tv_info_hostName)
    TextView mTvHostName;
    @BindView(R.id.tv_info_meetingPlace)
    TextView mTvMeetingPlace;
    @BindView(R.id.tv_info_meetingTime)
    TextView mTvMeetingTime;
    @BindView(R.id.rcv_agendas)
    RecyclerView mRcvAgendas;
    @BindView(R.id.tv_info_signInCount)
    TextView mTvSignInCount;
    @BindView(R.id.ll_info_delegates)
    LinearLayout llDelegatesPic;
    @BindView(R.id.rl_info_vote)
    RelativeLayout mRlVote;
    @BindView(R.id.rl_info_summary)
    RelativeLayout mRlSummary;
    @BindView(R.id.btn_info_edit)
    Button mBtnEdit;
    @BindView(R.id.btn_info_cancle)
    Button mBtnCancle;
    @BindView(R.id.ll_info_btnNotStart)
    LinearLayout mLlBtnNotStart;
    Unbinder unbinder;
    private String mMeetingId;
    private String mHostId;
    private String mRoomId;
    private User mUser;
    private MeetingRequest mRequest;
    private MeetingInfoEntity mMeetingInfoEntity;

    public static MeetingInfoFragment getNewInstance(String meetingId){
        Bundle bundle=new Bundle();
        MeetingInfoFragment fragment=new MeetingInfoFragment();
        bundle.putString("meetingId",meetingId);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_meeting_info, null);
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initVar();
        loadData();
    }

    private void initVar() {
        if (getArguments()!=null) {
            mMeetingId = getArguments().getString("meetingId");
        }
    }

    private void loadData() {
        mRequest = new MeetingRequest();

        Observer<HttpResult<MeetingInfoEntity>> observer=new Observer<HttpResult<MeetingInfoEntity>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HttpResult<MeetingInfoEntity> result) {
                mMeetingInfoEntity = result.getResult();
                setTopInfo();
                mRoomId = mMeetingInfoEntity.getMeetingPlaceId();
                setAgendaListView(mMeetingInfoEntity.getAgendaList());
                setDelegatesInfo();
                setMeetingStatus(mMeetingInfoEntity);
                setVoteData();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        mRequest.getSingleMeetingInfo(observer,mMeetingId);
    }

    private void setVoteData() {

    }

    private void setMeetingStatus(MeetingInfoEntity meetingInfo ) {
        List<MeetingInfoEntity.DelegateListBean> delegateList = meetingInfo.getDelegateList();
        mUser = User.getUserFromCache();
        mHostId = "";
        MeetingInfoEntity.DelegateListBean userDelegate = new MeetingInfoEntity.DelegateListBean();
        for (MeetingInfoEntity.DelegateListBean delegate : delegateList) {
            if (delegate.getDelegateRole()==ROLE_HOST) {
                mHostId =delegate.getUserId();
            }

            if (delegate.getUserId().equals(mUser.getUserId())){
                userDelegate = delegate;
            }

        }
        int meetingStatus = meetingInfo.getMeetingStatus();
        mLlBtnNotStart.setVisibility(View.GONE);
        switch (meetingStatus){
            case STATUS_READY://未开始
                if (mUser.getUserId().equals(meetingInfo.getCreatorId())){
                    if (TextUtils.isEmpty(userDelegate.getSignInTime())) {
                        mTvStartOrSign.setText("签到");
                    }else {
                        mTvStartOrSign.setText("已签到");
                    }
                    //会议创建人可在未开始时候编辑会议
                    mLlBtnNotStart.setVisibility(View.VISIBLE);
                }
                if (mUser.getUserId().equals(mHostId)){
                    //主持人身份
                    mTvStartOrSign.setText("开始会议");
                    mLlBtnNotStart.setVisibility(View.VISIBLE);
                }else {
                    //不是主持人
                    long startTime = TimeFormatUtil.formatTimeMillis(meetingInfo.getMeetingStartTime());
                    //提前十五分钟可签到
                    startTime -= 15*60*1000;
                    if (System.currentTimeMillis()>startTime) {
                        if (TextUtils.isEmpty(userDelegate.getSignInTime())) {
                            mTvStartOrSign.setText("签到");
                        }else {
                            mTvStartOrSign.setText("已签到");
                        }
                    }else {
                        mTvStartOrSign.setText("未开始");
                    }
                }
                break;
            case STATUS_ON://进行中
                if (TextUtils.isEmpty(userDelegate.getSignInTime())) {
                    mTvStartOrSign.setText("签到");
                }else {
                    mTvStartOrSign.setText("已签到");
                }
                if (mUser.getUserId().equals(mHostId)){
                    //主持人身份
                    mTvStartOrSign.setText("结束会议");
                }
                break;
            case STATUS_END://已结束
                mTvStartOrSign.setText("已结束");
                break;
        }
    }

    private void setAgendaListView(List<MeetingInfoEntity.AgendaListBean> agendaList) {
        if (agendaList!=null) {
            AgendaInfoAdapter adapter=new AgendaInfoAdapter(getContext(),agendaList);
            mRcvAgendas.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            mRcvAgendas.setLayoutManager(new LinearLayoutManager(getContext()));
            mRcvAgendas.setAdapter(adapter);
        }
    }

    private void setTopInfo(){
        StringBuilder meetingPlaceSb=new StringBuilder();
        meetingPlaceSb.append("会议地点: ");
        meetingPlaceSb.append(mMeetingInfoEntity.getMeetingPlaceName());
        mTvMeetingPlace.setText(meetingPlaceSb.toString());
        StringBuilder meetingNameSb=new StringBuilder();
        meetingNameSb.append("会议主题: ");
        meetingNameSb.append(mMeetingInfoEntity.getMeetingName());
        mtvMeetingName.setText(meetingNameSb.toString());
        for (MeetingInfoEntity.DelegateListBean delegateListBean : mMeetingInfoEntity.getDelegateList()) {
            if (delegateListBean.getDelegateRole()==1) {
                mTvHostName.setText("主持人: "+delegateListBean.getDelegateName());
            }
        }

        StringBuilder meetingTimeSb=new StringBuilder();
        meetingTimeSb.append("会议时间 ");
        meetingTimeSb.append(mMeetingInfoEntity.getMeetingStartTime());
        meetingTimeSb.append("-");
        String endTime = mMeetingInfoEntity.getMeetingEndTime().substring(mMeetingInfoEntity.getMeetingEndTime().lastIndexOf(" "));
        meetingTimeSb.append(endTime);
        mTvMeetingTime.setText(meetingTimeSb.toString());
    }


    private void setDelegatesInfo(){
        int signCount=0;
        List<MeetingInfoEntity.DelegateListBean> delegateList = mMeetingInfoEntity.getDelegateList();
        llDelegatesPic.removeAllViews();
        for (MeetingInfoEntity.DelegateListBean delegate : delegateList) {
            if (!TextUtils.isEmpty(delegate.getSignInTime())) {
                signCount++;
                int imageSize = DensityUtil.dp2px(getContext(), 36);
                int fontSize = DensityUtil.sp2px(getContext(), 16);

                ImageView iv = new ImageView(getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imageSize, imageSize);
                params.leftMargin = DensityUtil.dp2px(getContext(), 3);
                iv.setLayoutParams(params);
                iv.setImageDrawable(TextHeadPicUtil.getHeadPic(delegate.getDelegateName(), fontSize, imageSize));
                llDelegatesPic.addView(iv);
            }
        }
        StringBuilder sb=new StringBuilder();
        sb.append(signCount).append("/").append(delegateList.size()).append(" 签到");
       mTvSignInCount.setText(sb.toString());
    }


    @OnClick({R.id.tv_info_startOrSign, R.id.btn_info_edit, R.id.btn_info_cancle,R.id.rl_info_vote,
            R.id.rl_delegateBlock,R.id.ll_delegates,R.id.ll_info_delegates,R.id.rl_info_summary})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_delegateBlock:
            case R.id.ll_delegates:
            case R.id.ll_info_delegates:
                startParentFragment(DelegateFragment.newInstance(mMeetingId,mMeetingInfoEntity.getMeetingStatus()));
                break;
            case R.id.tv_info_startOrSign:
                startOrSignInMeeting();
                break;
            case R.id.btn_info_edit:
                startParentFragment(UpdateMeetingFragment.newInstance(mMeetingId,mRoomId));
                break;
            case R.id.btn_info_cancle:
                showCancleDialog();
                break;
            case R.id.rl_info_vote:
                startParentFragment(VoteListFragment.newInstance(mMeetingId,mHostId,mMeetingInfoEntity.getMeetingStatus()));
                break;
            case R.id.rl_info_summary:
                startParentFragment(SummaryFragment.newInstance(mMeetingId));
                break;
        }
    }

    private void startOrSignInMeeting() {
        String infoStr = mTvStartOrSign.getText().toString();

        if (infoStr.equals("签到")){
            mRequest.signInMeeting(new Observer<HttpResult>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(HttpResult result) {
                    mTvStartOrSign.setText("已签到");
                    loadData();
                }

                @Override
                public void onError(Throwable e) {
                    Toasty.error(_mActivity,e.getMessage()).show();
                }

                @Override
                public void onComplete() {

                }
            },mMeetingId);
        }else if (infoStr.equals("开始会议")){
            mRequest.startMeeting(new Observer<HttpResult>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(HttpResult result) {
                    mTvStartOrSign.setText("结束会议");
                    loadData();
                }

                @Override
                public void onError(Throwable e) {
                    Toasty.error(_mActivity,e.getMessage()).show();
                }

                @Override
                public void onComplete() {

                }
            },mMeetingId);
        }else if (infoStr.equals("结束会议")){
            mRequest.endMeeting(new Observer<HttpResult>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(HttpResult result) {
                    mTvStartOrSign.setText("已结束");
                }

                @Override
                public void onError(Throwable e) {
                    Toasty.error(_mActivity,e.getMessage()).show();
                }

                @Override
                public void onComplete() {

                }
            },mMeetingId);
        }

    }

    private void showCancleDialog() {
        new QMUIDialog.MessageDialogBuilder(getContext())
                .setMessage("要取消该会议吗？")
                .addAction("不了", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(0, "取消会议", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        cancleTheMeeting();
                    }
                })
                .show();
    }

    private void cancleTheMeeting() {
        Observer<HttpResult> observer=new Observer<HttpResult>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HttpResult result) {
                Logger.i("cancleTheMeeting pop");
                ((BaseFragment)getParentFragment()).pop();
            }

            @Override
            public void onError(Throwable e) {
                Toasty.normal(_mActivity,e.getMessage()).show();
            }

            @Override
            public void onComplete() {

            }
        };

        mRequest.cancleMeeting(observer,mMeetingId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveLoad(UpdateMsg msg){
        if (msg.getAction().equals(UpdateMsg.Action.updateMeeting)){
            loadData();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

}
