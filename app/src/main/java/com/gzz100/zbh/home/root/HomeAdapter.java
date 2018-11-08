package com.gzz100.zbh.home.root;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.gzz100.zbh.base.BaseFragment;

import java.util.List;

/**
 * Created by Lam on 2018/1/18.
 */

public class HomeAdapter extends FragmentPagerAdapter {

    private List<BaseFragment> mPages;

    public HomeAdapter(FragmentManager fm,List<BaseFragment> pages) {
        super(fm);
        mPages = pages;
    }

    @Override
    public Fragment getItem(int position) {
        return mPages.get(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((Fragment) object).getView();
    }

    @Override
    public int getCount() {
        return mPages.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "apm";
            case 1:
                return "meeting";
            case 2:
                return "msg";
            default:
                return "mine";
        }
    }


}
