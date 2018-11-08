package com.gzz100.zbh.account.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.gzz100.zbh.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lam on 2018/8/4.
 */

public class RegPagerAdapter extends FragmentPagerAdapter {

    List<BaseFragment> fragmentList = new ArrayList<>();

    public RegPagerAdapter(FragmentManager fm, List<BaseFragment> fragments) {
        super(fm);
        fragmentList=fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }



}
