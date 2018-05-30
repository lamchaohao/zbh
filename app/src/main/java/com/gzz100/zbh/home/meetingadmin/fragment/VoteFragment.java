package com.gzz100.zbh.home.meetingadmin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.account.User;
import com.gzz100.zbh.base.BaseBackFragment;
import com.gzz100.zbh.data.entity.VoteDetailEntity;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.VoteRequest;
import com.gzz100.zbh.home.meetingadmin.adapter.VoteOptionAdapter;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.ArrayList;

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
    @BindView(R.id.tv_vote_staus)
    TextView mTvVoteStaus;
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

        Observer<HttpResult<VoteDetailEntity>> observer = new Observer<HttpResult<VoteDetailEntity>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HttpResult<VoteDetailEntity> result) {
                mVoteEntity = result.getResult();
                initView(mVoteEntity);
            }

            @Override
            public void onError(Throwable e) {
                Toasty.error(_mActivity,e.getMessage()).show();
            }

            @Override
            public void onComplete() {

            }
        };

        mRequest = new VoteRequest();
        mRequest.getVoteInfo(observer,mMeetingId,mVoteId);

    }

    private void initView(VoteDetailEntity vote) {
        initRecyclerView();
        initBottomButton();
        mTvVoteName.setText(vote.getVoteName());
//        mTvVoteStaus.setText();
        if (vote.getVoteAnonymous()==1) {
            mTvVoteHideName.setText("匿名");
        }else {
            mTvVoteHideName.setText("公开");
        }
        if (vote.getVoteSelectableNum()==1) {
            mTvVoteMode.setText("单选");
        }else {
            mTvVoteMode.setText("多选");
        }
//	投票状态，1表示进行中，2表示未开始，3表示已结束
        switch (vote.getVoteStatus()) {
            case 1:
                mTvVoteStaus.setText("进行中");
                break;
            case 2:
                mTvVoteStaus.setText("未开始");
                break;
            case 3:
                mTvVoteStaus.setText("已结束");
                break;
        }
        StringBuilder sb=new StringBuilder();
        sb.append(vote.getVoterNum());
        sb.append("人 已投");
        StringBuffer selectSB=new StringBuffer();
        selectSB.append("可选项:");
        selectSB.append(vote.getVoteSelectableNum());
        tvMaxSelection.setText(selectSB.toString());
        mTvVoteSummary.setText(sb.toString());


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
                        mVoteEntity.setVoteStatus(STATUS_END);
                        mAdapter.notifyDataSetChanged();
                        mBtnVoteComfirm.setText("已投票");
                        mVoteEntity.setVoteStatus(STATUS_END);
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
            StringBuilder optionIdSb=new StringBuilder();
            optionIdSb.append("[");
            ArrayList<VoteDetailEntity.VoteOptionListBean> selectedItem = mAdapter.getSelectedItem();
            int i =0;
            for (VoteDetailEntity.VoteOptionListBean voteOption : selectedItem) {
                i++;
                optionIdSb.append(voteOption.getVoteOptionId());
                if (i!=selectedItem.size()){
                    optionIdSb.append(",");
                }
            }
            optionIdSb.append("]");

            mRequest.uploadVoteResult(new Observer<HttpResult>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(HttpResult result) {
                    mBtnVoteComfirm.setText("已投票");
                    loadData();
                }

                @Override
                public void onError(Throwable e) {
                    Toasty.error(_mActivity,e.getMessage()).show();
                }

                @Override
                public void onComplete() {

                }
            },optionIdSb.toString(),mVoteId);

        }
    }

    @OnClick(R.id.tv_vote_start)
    void onStartVoteClick(View view){

        String tvStr = mTvVoteStart.getText().toString();
        if (tvStr.equals("开始投票")) {

            mRequest.startVote(new Observer<HttpResult>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(HttpResult result) {
                    mTvVoteStart.setText("结束投票");
                    mBtnVoteComfirm.setText("投票");
                }

                @Override
                public void onError(Throwable e) {
                    Toasty.error(_mActivity,e.getMessage()).show();
                }

                @Override
                public void onComplete() {

                }
            },mVoteId);


        }else if (tvStr.equals("结束投票")){
            mRequest.endVote(new Observer<HttpResult>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(HttpResult result) {
                    mTvVoteStart.setText("已结束");
                }

                @Override
                public void onError(Throwable e) {
                    Toasty.error(_mActivity,e.getMessage()).show();
                }

                @Override
                public void onComplete() {

                }
            },mVoteId);

        }

    }

    private void transition(String url, View view) {

        Intent intent = new Intent(_mActivity, PhotoActivity.class);
        intent.putExtra("picUrl",url);
        startActivity(intent);

//        if (Build.VERSION.SDK_INT < 21) {
//            Intent intent = new Intent(_mActivity, PhotoActivity.class);
//            intent.putExtra("picUrl",url);
//            startActivity(intent);
//        } else {
//            Intent intent = new Intent(_mActivity, PhotoActivity.class);
//            intent.putExtra("picUrl",url);
//            ActivityOptionsCompat options = ActivityOptionsCompat.
//                    makeSceneTransitionAnimation(_mActivity, view, getString(R.string.transition));
//            startActivity(intent, options.toBundle());
//        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
