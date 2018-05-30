package com.gzz100.zbh.home.appointment.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseBackFragment;
import com.qmuiteam.qmui.widget.QMUITopBar;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class RemindTimeFragment extends BaseBackFragment implements RadioGroup.OnCheckedChangeListener{


    @BindView(R.id.topbar)
    QMUITopBar mTopbar;

    Unbinder unbinder;
    @BindView(R.id.none_notify)
    RadioButton mNoneNotify;
    @BindView(R.id.min15_notify)
    RadioButton mMin15Notify;
    @BindView(R.id.min30_notify)
    RadioButton mMin30Notify;
    @BindView(R.id.hour1_notify)
    RadioButton mHour1Notify;
    @BindView(R.id.hour2_notify)
    RadioButton mHour2Notify;
    @BindView(R.id.day1_notify)
    RadioButton mDay1Notify;
    @BindView(R.id.day2_notify)
    RadioButton mDay2Notify;
    @BindView(R.id.week1_notify)
    RadioButton mWeek1Notify;
    @BindView(R.id.rdg_remind)
    RadioGroup mRdgRemind;
    String mNotifyTime;

    public static RemindTimeFragment newInstance(String notifyTime) {
        RemindTimeFragment fragment = new RemindTimeFragment();
        Bundle bundle=new Bundle();
        bundle.putString("notify",notifyTime);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View inflate = inflater.inflate(R.layout.fragment_remind_time, null);
        unbinder = ButterKnife.bind(this, inflate);
        initTopBar();
        return attachToSwipeBack(inflate);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRadioGroup();
    }

    private void initRadioGroup() {

        mRdgRemind.setOnCheckedChangeListener(this);
        switch (mNotifyTime) {
            case "15":
                mRdgRemind.check(R.id.min15_notify);
                break;
            case "30":
                mRdgRemind.check(R.id.min30_notify);
                break;
            case "60":
                mRdgRemind.check(R.id.hour1_notify);
                break;
            case "120":
                mRdgRemind.check(R.id.hour2_notify);
                break;
            case "1d":
                mRdgRemind.check(R.id.day1_notify);
                break;
            case "2d":
                mRdgRemind.check(R.id.day2_notify);
                break;
            case "7d":
                mRdgRemind.check(R.id.week1_notify);
                break;
            case "no":
                mRdgRemind.check(R.id.none_notify);
                break;
        }
    }

    private void initTopBar() {
        if (getArguments()!=null) {
            mNotifyTime = getArguments().getString("notify");
        }
        mTopbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });

        mTopbar.setTitle("提醒时间");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {


        switch (checkedId){
            case R.id.none_notify:
                EventBus.getDefault().post("no");
                break;
            case R.id.min15_notify:
                EventBus.getDefault().post("15");
                break;
            case R.id.min30_notify:
                EventBus.getDefault().post("30");
                break;
            case R.id.hour1_notify:
                EventBus.getDefault().post("60");
                break;
            case R.id.hour2_notify:
                EventBus.getDefault().post("120");
                break;
            case R.id.day1_notify:
                EventBus.getDefault().post("1d");
                break;
            case R.id.day2_notify:
                EventBus.getDefault().post("2d");
                break;
            case R.id.week1_notify:
                EventBus.getDefault().post("7d");
                break;
        }


    }



}
