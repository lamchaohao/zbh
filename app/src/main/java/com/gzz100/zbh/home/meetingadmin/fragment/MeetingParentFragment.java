package com.gzz100.zbh.home.meetingadmin.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseBackFragment;
import com.gzz100.zbh.base.BaseFragment;
import com.gzz100.zbh.home.meetingadmin.adapter.InfoTabAdapter;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.QMUIViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MeetingParentFragment extends BaseBackFragment {


    @BindView(R.id.topbar)
    QMUITopBar mTopbar;
    @BindView(R.id.tabSegment_parent)
    TabLayout mTabSegment;
    @BindView(R.id.contentViewPager)
    QMUIViewPager mContentViewPager;
    Unbinder unbinder;
    private String mMeetingId;
    private String mMeetingName;
    private long mGroupId;

    public static MeetingParentFragment getNewInstance(String meetingId,String meetingName,long groupId){
        Bundle bundle=new Bundle();
        MeetingParentFragment fragment=new MeetingParentFragment();
        bundle.putString("meetingId",meetingId);
        bundle.putString("meetingName",meetingName);
        bundle.putLong("groupId",groupId);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_meeting_parent, null);
        unbinder = ButterKnife.bind(this, view);
        return attachToSwipeBack(view);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initVar();
        initTopbar();
        initTabs();
        mContentViewPager.setOffscreenPageLimit(2);
    }

    private void initTopbar() {
        mTopbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });

    }

    private void initVar() {
        if (getArguments()!=null) {
            mMeetingId = getArguments().getString("meetingId");
            mMeetingName = getArguments().getString("meetingName");
            mGroupId = getArguments().getLong("groupId");
        }
        mTopbar.setTitle("会议-"+mMeetingName);
    }

    private void initTabs() {
        List<BaseFragment> fragmentList = new ArrayList<>();
        fragmentList.add(MeetingInfoFragment.getNewInstance(mMeetingId,mMeetingName));
        fragmentList.add(ShowFragment.getNewInstance(mMeetingId,mGroupId));
        fragmentList.add(MimcMsgFragment.newInstance(mGroupId));
        mContentViewPager.setAdapter(new InfoTabAdapter(getChildFragmentManager(),fragmentList));
        mTabSegment.setupWithViewPager(mContentViewPager,false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
