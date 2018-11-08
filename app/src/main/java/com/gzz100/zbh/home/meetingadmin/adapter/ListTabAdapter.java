package com.gzz100.zbh.home.meetingadmin.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.gzz100.zbh.base.BaseFragment;

import java.util.List;

/**
 * Created by Lam on 2018/7/25.
 */

public class ListTabAdapter extends FragmentStatePagerAdapter {

    List<BaseFragment> mFragmentList;

    public ListTabAdapter(FragmentManager manager, List<BaseFragment> fragmentList) {
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
            return "全部会议";
        }else {
            return "我创建的";
        }
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }
}
