package com.gzz100.zbh.base;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Created by Lam on 2018/1/3.
 */

public abstract class BaseFragmentActivity extends SupportActivity {
    private static final String TAG = "BaseFragmentActivity";
    /**
     * 设置动画，也可以使用setFragmentAnimator()设置
     */
    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        // 设置默认Fragment动画  默认竖向(和安卓5.0以上的动画相同)
//        return super.onCreateFragmentAnimator();
        // 设置横向(和安卓4.x动画相同)
        return new DefaultHorizontalAnimator();
//        return new SpeedHorizontalAnimator();
        // 设置自定义动画
//        return new FragmentAnimator(enter,exit,popEnter,popExit);
    }
}
