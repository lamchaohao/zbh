package com.gzz100.zbh.base;

/**
 * Created by Lam on 2018/1/3.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseBackFragment extends BaseFragment {
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setParallaxOffset(0.5f);
        setSwipeBackEnable(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = onCreateView(inflater);
//        attachToSwipeBack(view);
        return onCreateView(inflater);
    }

    protected abstract View onCreateView(LayoutInflater inflater);

    public void startFragment(BaseBackFragment fragment){
            start(fragment);
    }

}