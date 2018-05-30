package com.gzz100.zbh.home.appointment.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseBackFragment;
import com.gzz100.zbh.home.appointment.entity.VoteWrap;
import com.qmuiteam.qmui.widget.QMUITopBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectVoteModeFragment extends BaseBackFragment {

    @BindView(R.id.topbar)
    QMUITopBar mTopbar;
    @BindView(R.id.cv_singlechoice)
    CardView mCvSinglechoice;
    @BindView(R.id.cv_multichoice)
    CardView mCvMultichoice;


    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View inflate = inflater.inflate(R.layout.fragment_select_vote_mode, null, false);
        ButterKnife.bind(this,inflate);
        return attachToSwipeBack(inflate);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTopbar();
    }

    private void initTopbar() {
        mTopbar.setTitle("选择模式");
        mTopbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.cv_singlechoice, R.id.cv_multichoice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cv_singlechoice:
                VoteWrap voteWrap =new VoteWrap();
                voteWrap.setSingle(true);
                startWithPop(AddVoteFragment.newInstance(voteWrap));
                break;
            case R.id.cv_multichoice:
                VoteWrap vote =new VoteWrap();
                vote.setSingle(false);
                startWithPop(AddVoteFragment.newInstance(vote));
                break;
        }
    }
}
