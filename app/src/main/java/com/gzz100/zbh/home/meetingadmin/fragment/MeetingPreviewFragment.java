package com.gzz100.zbh.home.meetingadmin.fragment;

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
 * Created by Lam on 2018/1/15.
 */

public class MeetingPreviewFragment extends BaseFragment {
    @BindView(R.id.tabSegment_preview)
    QMUITabSegment mTabSegment;
    @BindView(R.id.contentViewPager)
    ViewPager mContentViewPager;
    View mRootView;
    @Override
    protected View onCreateView(LayoutInflater inflater) {
        if (mRootView==null) {
            mRootView = inflater.inflate(R.layout.fragment_meeting_preview, null);
            ButterKnife.bind(this,mRootView);
            initView();
        }
        return mRootView;
    }

    private void initView() {

        mTabSegment.reset();
        mTabSegment.addTab(new QMUITabSegment.Tab("我参加的"));
        mTabSegment.addTab(new QMUITabSegment.Tab("我创建的"));
        mTabSegment.setMode(QMUITabSegment.MODE_FIXED);
        mTabSegment.setHasIndicator(true);
        List<BaseFragment> fragmentList = new ArrayList<>();
        fragmentList.add(MeetingListFragment.getNewInstance(false));
        fragmentList.add(MeetingListFragment.getNewInstance(true));

        mContentViewPager.setAdapter(new MeetingTabAdapter(getChildFragmentManager(),fragmentList));
        mTabSegment.setupWithViewPager(mContentViewPager,false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
