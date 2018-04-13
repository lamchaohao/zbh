package com.gzz100.zbh.home.meetingadmin.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.gzz100.zbh.utils.DensityUtil;
import com.gzz100.zbh.utils.TextHeadPicUtil;
import com.gzz100.zbh.utils.TimeFormatUtil;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

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
    private User mUser;
    private MeetingRequest mRequest;

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
                MeetingInfoEntity info = result.getResult();
                setTopInfo(info);
                setAgendaListView(info.getAgendaList());
                setDelegatesInfo(info.getDelegateList());
                setMeetingStatus(info);
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
        int meetingStatus = meetingInfo.getMeetingStatus();
        List<MeetingInfoEntity.DelegateListBean> delegateList = meetingInfo.getDelegateList();
        mUser = User.getUserFromCache();
        mHostId = "";
        MeetingInfoEntity.DelegateListBean userDelegate = new MeetingInfoEntity.DelegateListBean();
        for (MeetingInfoEntity.DelegateListBean delegate : delegateList) {
            if (delegate.getDelegateRole()==1) {
                mHostId =delegate.getDelegateId();
                break;
            }

            if (delegate.getUserId().equals(mUser.getUserId())){
                userDelegate = delegate;
            }

        }
        mLlBtnNotStart.setVisibility(View.GONE);
        switch (meetingStatus){
            case STATUS_READY://未开始
                if (mUser.getUserId().equals(meetingInfo.getCreatorId())){
                    mTvStartOrSign.setText("签到");
                    //会议创建人可在未开始时候编辑会议
                    mLlBtnNotStart.setVisibility(View.VISIBLE);
                }
                if (mUser.getUserId().equals(mHostId)){
                    mTvStartOrSign.setText("开始会议");
                }else {
                    long startTime = TimeFormatUtil.formatDateToMillis(meetingInfo.getMeetingStartTime());
                    startTime -= 15*60*1000;
                    if (System.currentTimeMillis()>startTime) {
                        mTvStartOrSign.setText("签到");
                    }else {
                        mTvStartOrSign.setText("未开始");
                    }
                }
                break;
            case STATUS_ON://进行中
                if (userDelegate.getSignInTime()==null) {
                    mTvStartOrSign.setText("签到");
                }else {
                    mTvStartOrSign.setText("已签到");
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

    private void setTopInfo(MeetingInfoEntity info){
        mTvMeetingPlace.setText("会议地点: "+info.getMeetingPlaceName());
        mtvMeetingName.setText("会议主题: "+info.getMeetingName());
        for (MeetingInfoEntity.DelegateListBean delegateListBean : info.getDelegateList()) {
            if (delegateListBean.getDelegateRole()==1) {
                mTvHostName.setText("主持人: "+delegateListBean.getDelegateName());
            }
        }
        mTvMeetingTime.setText("会议时间 "+info.getMeetingStartTime()+"-"+info.getMeetingEndTime());
    }


    private void setDelegatesInfo(List<MeetingInfoEntity.DelegateListBean> delegateList){
        int signCount=0;
        for (MeetingInfoEntity.DelegateListBean delegate : delegateList) {
            if (delegate.getSignInTime()!=null) {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_info_startOrSign, R.id.btn_info_edit, R.id.btn_info_cancle,R.id.rl_info_vote,R.id.rl_delegateBlock,R.id.ll_delegates,R.id.ll_info_delegates})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_delegateBlock:
            case R.id.ll_delegates:
            case R.id.ll_info_delegates:
                startParentFragment(DelegateFragment.newInstance(mMeetingId));
                break;
            case R.id.tv_info_startOrSign:

                break;
            case R.id.btn_info_edit:
                break;
            case R.id.btn_info_cancle:
                showCancleDialog();
                break;
            case R.id.rl_info_vote:
                startParentFragment(VoteListFragment.newInstance(mMeetingId,mHostId));
                break;
        }
    }

    private void showCancleDialog() {
        new QMUIDialog.MessageDialogBuilder(getContext())
                .setMessage("要取消该会议吗？")
                .addAction("关闭", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(0, "取消会议", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
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
                ((BaseFragment)getParentFragment()).pop();
            }

            @Override
            public void onError(Throwable e) {
                Toasty.normal(getContext(),e.getMessage()).show();
            }

            @Override
            public void onComplete() {

            }
        };

        mRequest.cancleMeeting(observer,mMeetingId);
    }

}
