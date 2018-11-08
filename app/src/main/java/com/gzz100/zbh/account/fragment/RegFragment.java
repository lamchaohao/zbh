package com.gzz100.zbh.account.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.gzz100.zbh.R;
import com.gzz100.zbh.account.adapter.RegPagerAdapter;
import com.gzz100.zbh.account.eventEntity.RegStepEvent;
import com.gzz100.zbh.base.BaseBackFragment;
import com.gzz100.zbh.base.BaseFragment;
import com.gzz100.zbh.utils.FragmentBackHandler;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.widget.QMUITopBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RegFragment extends BaseBackFragment implements FragmentBackHandler{

    @BindView(R.id.topbar)
    QMUITopBar mTopbar;
    @BindView(R.id.viewPager_reg)
    ViewPager mViewPagerReg;
    Unbinder unbinder;
    private List<BaseFragment> mFragmentList;
    private String mPhone;
    private String mVaildCode;
    private String mPassword;

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.reg_parent, null);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return attachToSwipeBack(view);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new RegPhoneFragment());
        mFragmentList.add(new RegCodeFragment());
        mFragmentList.add(new RegPswFragment());
        mFragmentList.add(new RegUserNameFragment());
        RegPagerAdapter pagerAdapter = new RegPagerAdapter(getChildFragmentManager(), mFragmentList);
        mViewPagerReg.setAdapter(pagerAdapter);
        mViewPagerReg.setOffscreenPageLimit(4);
        mViewPagerReg.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mTopbar.setTitle("注册");
                        break;
                    case 1:
                        mTopbar.setTitle("输入验证码");
                        break;
                    case 2:
                        mTopbar.setTitle("设置密码");
                        break;
                    case 3:
                        mTopbar.setTitle("最后一步");
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTopbar.setTitle("注册");
        mViewPagerReg.setCurrentItem(0);


        mTopbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBack();
            }
        });


    }


    public void onBack() {
        switch (mViewPagerReg.getCurrentItem()) {
            case 0:
                pop();
                break;
            case 1:
                mViewPagerReg.setCurrentItem(0);
                break;
            case 2:
                mViewPagerReg.setCurrentItem(1);
                break;
            case 3:
                mViewPagerReg.setCurrentItem(2);
                break;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNextStepClick(RegStepEvent event){
        switch (event.getPageClick()) {
            case 0:
                mPhone = event.getMessage();
                mViewPagerReg.setCurrentItem(1);
                ((RegCodeFragment)mFragmentList.get(1)).setPhoneFromParent(mPhone);
                break;
            case 1:
                mVaildCode = event.getMessage();
                mViewPagerReg.setCurrentItem(2);
                break;
            case 2:
                mPassword = event.getMessage();
                Logger.i(mPassword);
                mViewPagerReg.setCurrentItem(3);
                ((RegUserNameFragment)mFragmentList.get(3)).setDataFromParent(mPhone,mVaildCode,mPassword);
                break;
            case 3:
                pop();
                break;

        }

    }





    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }


    @Override
    public boolean onBackPressed() {
        onBack();
        return true;
    }
}
