package com.gzz100.zbh.home.meetingadmin.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseFragment;
import com.gzz100.zbh.home.meetingadmin.adapter.ListTabAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Lam on 2018/1/15.
 */

public class MeetingPreviewFragment extends BaseFragment {
    @BindView(R.id.tabSegment_preview)
    TabLayout mTabSegment;
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

        List<BaseFragment> fragmentList = new ArrayList<>();
        fragmentList.add(MeetingListFragment.getNewInstance(false));
        fragmentList.add(MeetingListFragment.getNewInstance(true));

        mContentViewPager.setAdapter(new ListTabAdapter(getChildFragmentManager(),fragmentList));
        mTabSegment.setupWithViewPager(mContentViewPager,false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
