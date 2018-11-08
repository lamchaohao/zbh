package com.gzz100.zbh.home.meetingadmin.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.gzz100.zbh.base.BaseFragment;

import java.util.List;

/**
 * Created by Lam on 2018/1/15.
 */

public class MeetingTabAdapter extends FragmentStatePagerAdapter {
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

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position==0){
            return "预约会议";
        }else if (position==1){
            return "历史模板";
        }else {
            return "讨论";
        }
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

}
