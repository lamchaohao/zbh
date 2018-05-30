package com.gzz100.zbh.home.meetingadmin.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseFragment;
import com.gzz100.zbh.home.meetingadmin.adapter.MeetingTabAdapter;
import com.qmuiteam.qmui.widget.QMUITabSegment;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.QMUIViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MeetingParentFragment extends BaseFragment {


    @BindView(R.id.topbar)
    QMUITopBar mTopbar;
    @BindView(R.id.tabSegment_parent)
    QMUITabSegment mTabSegment;
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
        initTabs();
        mContentViewPager.setOffscreenPageLimit(2);
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
//        int normalColor = QMUIResHelper.getAttrColor(getActivity(), R.attr.qmui_config_color_gray_6);
//        int selectColor = QMUIResHelper.getAttrColor(getActivity(), R.attr.qmui_config_color_blue);
//        mTabSegment.setDefaultNormalColor(normalColor);
//        mTabSegment.setDefaultSelectedColor(selectColor);
        QMUITabSegment.Tab msgTab = new QMUITabSegment.Tab("会议信息");
        QMUITabSegment.Tab showTab = new QMUITabSegment.Tab("文件演示");
        QMUITabSegment.Tab chatTab = new QMUITabSegment.Tab("讨论");

        mTabSegment.addTab(msgTab)
                .addTab(showTab)
                .addTab(chatTab);
        mTabSegment.setHasIndicator(true);
        List<BaseFragment> fragmentList = new ArrayList<>();
        fragmentList.add(MeetingInfoFragment.getNewInstance(mMeetingId));
        fragmentList.add(ShowFragment.getNewInstance(mMeetingId,mGroupId));
        fragmentList.add(MimcMsgFragment.newInstance(mGroupId));
        mContentViewPager.setAdapter(new MeetingTabAdapter(getChildFragmentManager(),fragmentList));
        mTabSegment.setupWithViewPager(mContentViewPager,false);
//        mTabSegment.
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
