package com.gzz100.zbh.home.appointment.fragment;

import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseFragment;
import com.gzz100.zbh.home.meetingadmin.adapter.MeetingTabAdapter;
import com.qmuiteam.qmui.widget.QMUITabSegment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Lam on 2018/1/30.
 */

public class ApmRootFragment extends BaseFragment {
    @BindView(R.id.tabSegment_apm)
    QMUITabSegment mTabSegmentApm;
    @BindView(R.id.contentViewPager)
    ViewPager mContentViewPager;
    View mRootView;
    @Override
    protected View onCreateView(LayoutInflater inflater) {
        if (mRootView==null){
            mRootView = inflater.inflate(R.layout.fragment_apm_root, null);
            ButterKnife.bind(this, mRootView);
            initView();
        }

        return mRootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void initView() {
        mTabSegmentApm.reset();
        mTabSegmentApm.addTab(new QMUITabSegment.Tab("预约会议"));
        mTabSegmentApm.addTab(new QMUITabSegment.Tab("历史模板"));
        mTabSegmentApm.setMode(QMUITabSegment.MODE_FIXED);
        mTabSegmentApm.setHasIndicator(true);
        List<BaseFragment> fragmentList = new ArrayList<>();
        fragmentList.add(new AppointmentFragment());
        fragmentList.add(new TemplateFragment());

        mContentViewPager.setAdapter(new MeetingTabAdapter(getChildFragmentManager(),fragmentList));
        mTabSegmentApm.setupWithViewPager(mContentViewPager,false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
