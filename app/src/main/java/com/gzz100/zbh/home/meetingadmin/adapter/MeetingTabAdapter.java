package com.gzz100.zbh.home.meetingadmin.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.gzz100.zbh.base.BaseFragment;

import java.util.List;

/**
 * Created by Lam on 2018/1/15.
 */

public class MeetingTabAdapter extends FragmentPagerAdapter {
    List<BaseFragment> mFragmentList;
    public MeetingTabAdapter(FragmentManager manager, List<BaseFragment> fragmentList) {
        super(manager);
        mFragmentList = fragmentList;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((Fragment) object).getView();
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

}
