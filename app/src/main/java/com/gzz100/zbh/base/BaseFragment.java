package com.gzz100.zbh.base;

/**
 * Created by Lam on 2018/1/3.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.yokeyword.fragmentation_swipeback.SwipeBackFragment;

public abstract class BaseFragment extends SwipeBackFragment {
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return onCreateView(inflater);
    }

    protected abstract View onCreateView(LayoutInflater inflater);

    public void startFragment(BaseFragment fragment){
            start(fragment);
    }

    public void startParentFragment(BaseFragment fragment){
        ((BaseFragment)getParentFragment()).start(fragment);
    }

}