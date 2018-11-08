package com.gzz100.zbh.home.meetingadmin.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gzz100.zbh.R;
import com.gzz100.zbh.account.User;
import com.gzz100.zbh.base.BaseBackFragment;
import com.gzz100.zbh.data.ObserverImpl;
import com.gzz100.zbh.data.entity.VoteDetailEntity;
import com.gzz100.zbh.data.eventEnity.LoadVoteEvent;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.VoteRequest;
import com.gzz100.zbh.home.appointment.entity.VoteOption;
import com.gzz100.zbh.home.appointment.entity.VoteWrap;
import com.gzz100.zbh.home.meetingadmin.activity.PhotoActivity;
import com.gzz100.zbh.home.meetingadmin.adapter.VoteOptionAdapter;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.widget.QMUITopBar;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;

import static com.gzz100.zbh.res.Common.STATUS_END;
import static com.gzz100.zbh.res.Common.STATUS_ON;
import static com.gzz100.zbh.res.Common.STATUS_READY;

/**
 * Created by Lam on 2018/4/10.
 */

public class VoteFragment extends BaseBackFragment {


    @BindView(R.id.topbar)
    QMUITopBar mTopbar;
    @BindView(R.id.tv_vote_name)
    TextView mTvVoteName;
    @BindView(R.id.tv_vote_mode)
    TextView mTvVoteMode;
    @BindView(R.id.tv_vote_hideName)
    TextView mTvVoteHideName;
    @BindView(R.id.iv_vote_status)
    ImageView mIvVoteStaus;
    @BindView(R.id.tv_vote_start)
    TextView mTvVoteStart;
    @BindView(R.id.tv_vote_summary)
    TextView mTvVoteSummary;
    @BindView(R.id.tv_vote_selectableNum)
    TextView tvMaxSelection;
    @BindView(R.id.rcv_vote)
    RecyclerView mRcvVote;
    @BindView(R.id.btn_vote_comfirm)
    Button mBtnVoteComfirm;
    Unbinder unbinder;
    String mMeetingId;
    String mVoteId;
    private String mHostId;
    private VoteDetailEntity mVoteEntity;
    private VoteOptionAdapter mAdapter;
    private VoteRequest mRequest;
    private ObserverImpl<HttpResult<VoteDetailEntity>> mDataObserver;
    private ObserverImpl mUploadResultObserver;
    private ObserverImpl mStartVoteObserver;
    private ObserverImpl mEndVoteObserver;


    public static VoteFragment newInstance(String meetingId,String voteId,String hostId){
        VoteFragment fragment = new VoteFragment();
        Bundle bundle=new Bundle();
        bundle.putString("meetingId",meetingId);
        bundle.putString("voteId",voteId);
        bundle.putString("hostId",hostId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_vote, null);
        unbinder = ButterKnife.bind(this, view);
        if (getArguments()!=null) {
            mMeetingId = getArguments().getString("meetingId");
            mVoteId = getArguments().getString("voteId");
            mHostId = getArguments().getString("hostId");
        }
        return attachToSwipeBack(view);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTopbar();
        loadData();

    }

    private void initTopbar() {
        mTopbar.setTitle("投票");
        mTopbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
    }

    private void loadData() {

        mDataObserver = new ObserverImpl<HttpResult<VoteDetailEntity>>() {

            @Override
            protected void onResponse(HttpResult<VoteDetailEntity> result) {
                mVoteEntity = result.getResult();
                initView(mVoteEntity);
            }

            @Override
            protected void onFailure(Throwable e) {
                Toasty.error(_mActivity,e.getMessage()).show();
            }
        };

        mRequest = new VoteRequest();
        mRequest.getVoteInfo(mDataObserver,mMeetingId,mVoteId);

    }

    private void initView(VoteDetailEntity vote) {
        initDelete();
        initRecyclerView();
        initBottomButton();
        mTvVoteName.setText(vote.getVoteName());
        if (vote.getVoteAnonymous()==1) {
            mTvVoteHideName.setText("【匿名】");
        }else {
            mTvVoteHideName.setText("【公开】");
        }
        if (vote.getVoteSelectableNum()==1) {
            mTvVoteMode.setText("【单选】");
        }else {
            mTvVoteMode.setText("【多选】");
        }
//	投票状态，1表示进行中，2表示未开始，3表示已结束
        switch (vote.getVoteStatus()) {
            case 1:
               mIvVoteStaus.setImageResource(R.drawable.ic_statu_on_meeting);
                break;
            case 2:
                mIvVoteStaus.setImageResource(R.drawable.ic_statu_on_ready);
                break;
            case 3:
                mIvVoteStaus.setImageResource(R.drawable.ic_statu_end);
                break;
        }
        StringBuilder sb=new StringBuilder();
        sb.append(vote.getVoterNum());
        sb.append("人 已投");
        StringBuffer selectSB=new StringBuffer();
        selectSB.append("【可选项:");
        selectSB.append(vote.getVoteSelectableNum()).append("】");
        tvMaxSelection.setText(selectSB.toString());
        mTvVoteSummary.setText(sb.toString());

    }

    private void initDelete() {
        User user = User.getUserFromCache();
        if (user.getUserId().equals(mHostId)&&mVoteEntity.getVoteStatus()==STATUS_READY){
            mTopbar.addRightTextButton("删除",R.id.textButtonId).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteVote();
                }
            });

        }

    }

    private void deleteVote() {
        VoteRequest request=new VoteRequest();
        request.deleteVote(new ObserverImpl<HttpResult>() {
            @Override
            protected void onResponse(HttpResult result) {
                Toasty.success(getContext(),"投票已删除").show();
                EventBus.getDefault().post(new LoadVoteEvent());
                pop();
            }

            @Override
            protected void onFailure(Throwable e) {
                Toasty.error(getContext(),e.getMessage()).show();
            }
        },mVoteId);

    }

    private void initRecyclerView() {
        mRcvVote.setLayoutManager(new LinearLayoutManager(getContext()));
        mRcvVote.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        mAdapter = new VoteOptionAdapter(getContext(),mVoteEntity);
        mRcvVote.setAdapter(mAdapter);

        mAdapter.setOnVoteItemClickListener(new VoteOptionAdapter.OnVoteItemClickListener() {

            @Override
            public void onPicClick(String url, View view) {
                transition(url,view);
            }

            @Override
            public void onItemClick(int pos, VoteDetailEntity.VoteOptionListBean bean) {

            }
        });

    }

    private void initBottomButton() {
        User user = User.getUserFromCache();
        mTvVoteStart.setVisibility(View.GONE);
        switch (mVoteEntity.getVoteStatus()) {
            case STATUS_ON:
                boolean hasVoted = false;
                //已投票
                for (String id : mVoteEntity.getVoterIdList()) {
                    if (user.getUserId().equals(id)){
                        //1.如果已投票id列表存在用户id
                        hasVoted = true;
                        mBtnVoteComfirm.setText("已投票");
                        mTvVoteStart.setVisibility(View.GONE);

                    }
                }
                if (!hasVoted) {
                    //2.未投票
                    mBtnVoteComfirm.setVisibility(View.VISIBLE);
                    mBtnVoteComfirm.setText("投票");
                }
                //开始投票按钮.主持人控制
                if (user.getUserId().equals(mHostId)){
                    mTvVoteStart.setVisibility(View.VISIBLE);
                    mTvVoteStart.setText("结束投票");
                }
                break;
            case STATUS_READY:
                if (user.getUserId().equals(mHostId)){
                    //2.1未开始 主持人可以编辑投票
                    mBtnVoteComfirm.setText("编辑投票");
                    mTvVoteStart.setVisibility(View.VISIBLE);
                    mTvVoteStart.setText("开始投票");
                }else {
                    //2.2未开始 参会人员不可投票
                    mBtnVoteComfirm.setText("未开始");
                }
                break;
            case STATUS_END:
                mBtnVoteComfirm.setText("已结束");
                mTvVoteStart.setVisibility(View.GONE);
                break;
        }

    }

    @OnClick(R.id.btn_vote_comfirm)
    void onComfirmBtnClick(View view){
        String btnText = mBtnVoteComfirm.getText().toString();
        if (btnText.equals("投票")){

            String[] voteIdArr=new String[mAdapter.getSelectedItem().size()];
            for (int i = 0; i < mAdapter.getSelectedItem().size(); i++) {
                voteIdArr[i] = mAdapter.getSelectedItem().get(i).getVoteOptionId();
            }

            Gson gson=new Gson();
            String optionIdJson = gson.toJson(voteIdArr);
            Logger.i("optionJson="+optionIdJson);
            mUploadResultObserver = new ObserverImpl() {
                @Override
                protected void onResponse(Object o) {
                    mBtnVoteComfirm.setText("已投票");
                    loadData();
                }

                @Override
                protected void onFailure(Throwable e) {
                    Toasty.error(_mActivity,e.getMessage()).show();
                }
            };
            mRequest.uploadVoteResult(mUploadResultObserver,optionIdJson,mVoteId);

        }else if (btnText.equals("编辑投票")){
            VoteWrap voteWrap=new VoteWrap();
            List<VoteOption> voteOptions = new ArrayList<>();
            for (VoteDetailEntity.VoteOptionListBean optionBean : mVoteEntity.getVoteOptionList()) {
                VoteOption option = new VoteOption();
                option.setOptionName(optionBean.getVoteOptionName());
                option.setPicFile(optionBean.getVoteDocumentPath());
                voteOptions.add(option);
            }
            voteWrap.setVoteName(mVoteEntity.getVoteName());
            voteWrap.setVoteDespc(mVoteEntity.getVoteDescription());
            voteWrap.setMaxCount(mVoteEntity.getVoteSelectableNum());
            voteWrap.setSingle(mVoteEntity.getVoteSelectableNum()==1);
            voteWrap.setMaxCount(mVoteEntity.getVoteSelectableNum());
            voteWrap.setHideName(mVoteEntity.getVoteAnonymous()==1);
            voteWrap.setAutoStart(mVoteEntity.getVoteModel()==1);
            voteWrap.setOptionList(voteOptions);
            startFragment(UpdateVoteFragment.newInstance(voteWrap,mVoteId,mMeetingId));
        }
    }

    @OnClick(R.id.tv_vote_start)
    void onStartVoteClick(View view){

        String tvStr = mTvVoteStart.getText().toString();
        if (tvStr.equals("开始投票")) {
            mStartVoteObserver = new ObserverImpl() {
                @Override
                protected void onResponse(Object o) {
                    mTvVoteStart.setText("结束投票");
                    mBtnVoteComfirm.setText("投票");
                }

                @Override
                protected void onFailure(Throwable e) {
                    Toasty.error(_mActivity,e.getMessage()).show();
                }
            };
            mRequest.startVote(mStartVoteObserver,mVoteId);


        }else if (tvStr.equals("结束投票")){
            mEndVoteObserver = new ObserverImpl<HttpResult>() {
                @Override
                protected void onResponse(HttpResult o) {
                    mTvVoteStart.setText("已结束");
                }

                @Override
                protected void onFailure(Throwable e) {
                    Toasty.error(_mActivity,e.getMessage()).show();
                }
            };
            mRequest.endVote(mEndVoteObserver,mVoteId);

        }

    }

    private void transition(String url, View view) {

        if (Build.VERSION.SDK_INT < 21) {
            Intent intent = new Intent(_mActivity, PhotoActivity.class);
            intent.putExtra("picUrl",url);
            startActivity(intent);
        } else {
            Intent intent = new Intent(_mActivity, PhotoActivity.class);
            intent.putExtra("picUrl",url);
//            ActivityOptionsCompat options = ActivityOptionsCompat.
//                    makeScaleUpAnimation(view, 0,0, DensityUtil.getScreenWidth(_mActivity),DensityUtil.getScreenHeight(_mActivity));
//            startActivity(intent, options.toBundle());
            startActivity(intent);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        mDataObserver.cancleRequest();
        if (mUploadResultObserver!=null){
            mUploadResultObserver.cancleRequest();
        }
        if (mStartVoteObserver!=null){
            mStartVoteObserver.cancleRequest();
        }
        if (mEndVoteObserver!=null) {
            mEndVoteObserver.cancleRequest();
        }
    }
}
