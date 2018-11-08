package com.gzz100.zbh.home.root;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.gzz100.zbh.R;
import com.gzz100.zbh.account.User;
import com.gzz100.zbh.account.fragment.GuideFragment;
import com.gzz100.zbh.account.fragment.ScanFragment;
import com.gzz100.zbh.base.BaseFragment;
import com.gzz100.zbh.data.ObserverImpl;
import com.gzz100.zbh.data.entity.UnReadEntity;
import com.gzz100.zbh.data.eventEnity.PushUpdateEntity;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.MessageRequest;
import com.gzz100.zbh.data.network.request.UserLoginRequest;
import com.gzz100.zbh.home.appointment.fragment.ApmRootFragment;
import com.gzz100.zbh.home.meetingadmin.fragment.CalendarFragment;
import com.gzz100.zbh.home.meetingadmin.fragment.MeetingPreviewFragment;
import com.gzz100.zbh.home.message.MessageFragment;
import com.gzz100.zbh.home.mine.MineFragment;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.QMUITabSegment;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.QMUIViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by Lam on 2018/1/3.
 */
@RuntimePermissions
public class HomeFragment extends BaseFragment {
    @BindView(R.id.topbar)
    QMUITopBar mTopBar;
    @BindView(R.id.viewpager_home)
    QMUIViewPager mViewpagerHome;
    @BindView(R.id.tabs_home)
    QMUITabSegment mTabSegment;
    private List<BaseFragment> mFragmentList;
    private HomeAdapter mPagerAdapter ;
    boolean mIsJoined;
    private QMUIAlphaImageButton mCalendarView;
    private QMUIAlphaImageButton mScanView;
    private DatePickerDialog mDatePickerDialog;

    public static HomeFragment newInstance(boolean isJoined) {

        Bundle args = new Bundle();
        args.putBoolean("isJoined",isJoined);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View inflate = inflater.inflate(R.layout.fragment_home, null);
        ButterKnife.bind(this, inflate);
        EventBus.getDefault().register(this);
        return inflate;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
        initVar();
        initTabs();
        initPagers();
        loadUnread();
    }


    private void initView() {
        mTopBar.setTitle("中佰会");

        if (getArguments()!=null) {
            mIsJoined = getArguments().getBoolean("isJoined");
        }

        mScanView = mTopBar.addRightImageButton(R.drawable.ic_scan, R.id.imageButtonId);
        mScanView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragmentPermissionsDispatcher.showCameraWithPermissionCheck(HomeFragment.this);
            }
        });

        mCalendarView = mTopBar.addRightImageButton(R.drawable.ic_calendar, R.id.imageButton);
        mCalendarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int year = Calendar.getInstance().get(Calendar.YEAR);
//                int month= Calendar.getInstance().get(Calendar.MONTH);
//                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                        mDatePickerDialog.dismiss();
//                    }
//                };
//                mDatePickerDialog =new DatePickerDialog(getContext(), listener, year,month,0);
//                mDatePickerDialog.show();

                startFragment(new CalendarFragment());
            }
        });
        mCalendarView.setVisibility(View.GONE);
    }

    private void initVar() {

        mFragmentList = new ArrayList<>();
        mPagerAdapter = new HomeAdapter(getChildFragmentManager(),mFragmentList);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        HomeFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    public void showCamera(){
        startFragment(new ScanFragment());
    }

    // 向用户说明为什么需要这些权限（可选）
    @OnShowRationale(Manifest.permission.CAMERA)
    void showRationaleForCamera(final PermissionRequest request) {
        new AlertDialog.Builder(getContext())
                .setMessage("需要开启相机权限")
                .setPositiveButton("允许", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .show();
    }
    // 用户拒绝授权回调（可选）
    @OnPermissionDenied(Manifest.permission.CAMERA)
    void showDeniedForCamera() {
        Toasty.error(getContext(), "您拒绝了授权,扫描功能无法使用", Toast.LENGTH_SHORT).show();
    }

    // 用户勾选了“不再提醒”时调用（可选）
    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void showNeverAskForCamera() {
        Toasty.error(getContext(), "不好,无法完成授权").show();
    }
    private void initTabs() {
        int normalColor = QMUIResHelper.getAttrColor(getActivity(), R.attr.qmui_config_color_black);
        int selectColor = QMUIResHelper.getAttrColor(getActivity(), R.attr.qmui_config_color_blue);
        mTabSegment.setDefaultNormalColor(normalColor);
        mTabSegment.setDefaultSelectedColor(selectColor);
        QMUITabSegment.Tab apm = new QMUITabSegment.Tab(
                ContextCompat.getDrawable(getContext(), R.drawable.ic_home_appointment_normal),
                ContextCompat.getDrawable(getContext(), R.drawable.ic_home_appointment_selected),
                "预约", false
        );
        QMUITabSegment.Tab meeting = new QMUITabSegment.Tab(
                ContextCompat.getDrawable(getContext(), R.drawable.ic_home_meeting_normal),
                ContextCompat.getDrawable(getContext(), R.drawable.ic_home_meeting_selected),
                "会议", false
        );
        QMUITabSegment.Tab message = new QMUITabSegment.Tab(
                ContextCompat.getDrawable(getContext(), R.drawable.ic_home_message_normal),
                ContextCompat.getDrawable(getContext(), R.drawable.ic_home_message_selected),
                "消息", false
        );
        QMUITabSegment.Tab mine = new QMUITabSegment.Tab(
                ContextCompat.getDrawable(getContext(), R.drawable.ic_home_mine_normal),
                ContextCompat.getDrawable(getContext(), R.drawable.ic_home_mine_selected),
                "我的", false
        );

        QMUITabSegment.Tab guide = new QMUITabSegment.Tab(
                ContextCompat.getDrawable(getContext(), R.drawable.ic_home_message_normal),
                ContextCompat.getDrawable(getContext(), R.drawable.ic_home_message_selected),
                "主页", false
        );
        message.showSignCountView(getContext(),5);
        apm.setTextColor(getResources().getColor(R.color.colorTextNormal),getResources().getColor(R.color.colorTextSelected));
        guide.setTextColor(getResources().getColor(R.color.colorTextNormal),getResources().getColor(R.color.colorTextSelected));
        message.setTextColor(getResources().getColor(R.color.colorTextNormal),getResources().getColor(R.color.colorTextSelected));
        mine.setTextColor(getResources().getColor(R.color.colorTextNormal),getResources().getColor(R.color.colorTextSelected));
//        message.showSignCountView(getContext(),102);
        meeting.setTextColor(getResources().getColor(R.color.colorTextNormal),getResources().getColor(R.color.colorTextSelected));
        if (mIsJoined) {
            mTabSegment.addTab(apm)
                    .addTab(meeting)
                    .addTab(message)
                    .addTab(mine);
        }else {
            mTabSegment.addTab(guide)
                    .addTab(message)
                    .addTab(mine);
        }

    }




    private void initPagers() {
        mViewpagerHome.setOffscreenPageLimit(3);

        if (mIsJoined) {
            mFragmentList.add(new ApmRootFragment());
            mFragmentList.add(new MeetingPreviewFragment());

        }else {
            mFragmentList.add(new GuideFragment());
        }

        mFragmentList.add(new MessageFragment());
        mFragmentList.add(new MineFragment());


        mViewpagerHome.setAdapter(mPagerAdapter);
        mPagerAdapter.notifyDataSetChanged();
        mTabSegment.setupWithViewPager(mViewpagerHome, false);
//        mTabSegment.setOnTabClickListener(new QMUITabSegment.OnTabClickListener() {
//            @Override
//            public void onTabClick(int index) {
//                if (mFragmentList.size()==4&&index==1){
//                    mTabSegment.getTab(index).hideSignCountView();
//                }
//            }
//        });
//        mTabSegment.setOnTabClickListener();
        mViewpagerHome.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mFragmentList.size()==4) {
                    switch (position) {
                        case 0:
                            mTopBar.setTitle("预约会议");
                            break;
                        case 1:
                            mTopBar.setTitle("会议管理");
                            break;
                        case 2:
                            mTopBar.setTitle("消息");
                            break;
                        case 3:
                            mTopBar.setTitle("个人中心");
                            break;
                    }
                }
                if (position==1){
                    mScanView.setVisibility(View.GONE);
                    mCalendarView.setVisibility(View.VISIBLE);
                }else {
                    mScanView.setVisibility(View.VISIBLE);
                    mCalendarView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void loadUnread() {

        MessageRequest request=new MessageRequest();
        request.getUnreadCount(new ObserverImpl<HttpResult<UnReadEntity>>() {
            @Override
            protected void onResponse(HttpResult<UnReadEntity> result) {
                UnReadEntity entity = result.getResult();
                if (!TextUtils.isEmpty(entity.getMeetingUnread())) {
                    int unreadCount = Integer.parseInt(entity.getMeetingUnread());
                    if (unreadCount==0) {
                        mTabSegment.hideSignCountView(1);
                    }else {
                        mTabSegment.showSignCountView(getContext(),1,unreadCount);
                    }
                }
                if (!TextUtils.isEmpty(entity.getMessageUnread())){
                    int unreadCount = Integer.parseInt(entity.getMessageUnread());
                    if (unreadCount==0) {
                        mTabSegment.hideSignCountView(2);
                    }else {
                        mTabSegment.showSignCountView(getContext(),2,unreadCount);
                    }
                }

                mTabSegment.notifyDataChanged();
            }

            @Override
            protected void onFailure(Throwable e) {

            }
        });

    }



    public enum HomePage{
        appointment,meeting,message,mine
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPage(HomePage page){
        switch (page) {
            case mine:
                mTabSegment.selectTab(3);
                break;
            case meeting:
                mTabSegment.selectTab(1);
                break;
            case message:
                mTabSegment.selectTab(2);
                break;
            case appointment:
                mTabSegment.selectTab(0);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPushReceive(PushUpdateEntity entity){
        switch (entity.getMsgType()) {
            case joinCompany:
                updateUserData();
                break;
            case updateUnreadMsg:
                if (!TextUtils.isEmpty(entity.getMessageNum())){
                    int unreadCount = Integer.parseInt(entity.getMessageNum());
                    if (unreadCount==0) {
                        mTabSegment.hideSignCountView(2);
                    }else {
                        mTabSegment.showSignCountView(getContext(),2,unreadCount);
                    }
                }
                mTabSegment.notifyDataChanged();
                break;
            case updateUnreadMeeting:
                if (!TextUtils.isEmpty(entity.getMeetingNum())){
                    int unreadCount = Integer.parseInt(entity.getMeetingNum());
                    if (unreadCount==0) {
                        mTabSegment.hideSignCountView(1);
                    }else {
                        mTabSegment.showSignCountView(getContext(),1,unreadCount);
                    }
                }
                mTabSegment.notifyDataChanged();
                break;
            case meetingDecrement:
                int signCount = mTabSegment.getSignCount(1);
                signCount--;
                if (signCount>=1){
                    mTabSegment.showSignCountView(getContext(),1,signCount);
                }else {
                    mTabSegment.hideSignCountView(1);
                }
                break;
        }

    }



    private void updateUserData(){
        ObserverImpl<HttpResult<User>> observer = new ObserverImpl<HttpResult<User>>() {

            @Override
            protected void onResponse(HttpResult<User> userResult) {
                userResult.getResult().setLogin(true);
                User.save(userResult.getResult());
                startWithPop(HomeFragment.newInstance(true));
            }

            @Override
            protected void onFailure(Throwable e) {
                Toasty.error(getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        UserLoginRequest.getInstance().updateToken(observer);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        Logger.i("OnDestroy");
    }
}
