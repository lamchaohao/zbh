package com.gzz100.zbh.home.root;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.gzz100.zbh.R;
import com.gzz100.zbh.account.GuideFragment;
import com.gzz100.zbh.account.ScanFragment;
import com.gzz100.zbh.base.BaseFragment;
import com.gzz100.zbh.home.appointment.fragment.ApmRootFragment;
import com.gzz100.zbh.home.meetingadmin.fragment.MeetingPreviewFragment;
import com.gzz100.zbh.home.message.MessageFragment;
import com.gzz100.zbh.home.mine.MineFragment;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.QMUITabSegment;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    ViewPager mViewpagerHome;
    @BindView(R.id.tabs_home)
    QMUITabSegment mTabSegment;
    private List<BaseFragment> mFragmentList;
    private HomeAdapter mPagerAdapter ;
    boolean mIsJoined;
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
        return inflate;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTopBar.setTitle("中佰会");
        mTopBar.addRightImageButton(R.drawable.ic_screen_rotation_black_24dp,R.id.imageButtonId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragmentPermissionsDispatcher.showCameraWithPermissionCheck(HomeFragment.this);
            }
        });
        if (getArguments()!=null) {
            mIsJoined = getArguments().getBoolean("isJoined");
        }
        initTabs();
        initPagers();

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
        Logger.i("showCamera");
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
        Toast.makeText(getContext(), "用户拒绝了授权并向你扔了一条狗", Toast.LENGTH_SHORT).show();
    }

    // 用户勾选了“不再提醒”时调用（可选）
    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void showNeverAskForCamera() {
        Toast.makeText(getContext(), "用户把你打入冷宫", Toast.LENGTH_SHORT).show();
    }
    private void initTabs() {
        int normalColor = QMUIResHelper.getAttrColor(getActivity(), R.attr.qmui_config_color_black);
        int selectColor = QMUIResHelper.getAttrColor(getActivity(), R.attr.qmui_config_color_blue);
        mTabSegment.setDefaultNormalColor(normalColor);
        mTabSegment.setDefaultSelectedColor(selectColor);
        QMUITabSegment.Tab apm = new QMUITabSegment.Tab(
                ContextCompat.getDrawable(getContext(), R.drawable.ic_today_grey_700_24dp),
                ContextCompat.getDrawable(getContext(), R.drawable.ic_today_primary_24dp),
                "预约", false
        );
        QMUITabSegment.Tab meeting = new QMUITabSegment.Tab(
                ContextCompat.getDrawable(getContext(), R.drawable.ic_work_grey_700_24dp),
                ContextCompat.getDrawable(getContext(), R.drawable.ic_work_primary_24dp),
                "会议", false
        );
        QMUITabSegment.Tab message = new QMUITabSegment.Tab(
                ContextCompat.getDrawable(getContext(), R.drawable.ic_message_grey_700_24dp),
                ContextCompat.getDrawable(getContext(), R.drawable.ic_message_primary_24dp),
                "消息", false
        );
        QMUITabSegment.Tab mine = new QMUITabSegment.Tab(
                ContextCompat.getDrawable(getContext(), R.drawable.ic_person_grey_700_24dp),
                ContextCompat.getDrawable(getContext(), R.drawable.ic_person_primary_24dp),
                "我的", false
        );

        QMUITabSegment.Tab guide = new QMUITabSegment.Tab(
                ContextCompat.getDrawable(getContext(), R.drawable.ic_message_grey_700_24dp),
                ContextCompat.getDrawable(getContext(), R.drawable.ic_message_primary_24dp),
                "主页", false
        );

        message.showSignCountView(getContext(),3);
        if (mIsJoined) {
            mTabSegment.addTab(apm)
                    .addTab(meeting)
                    .addTab(message)
                    .addTab(mine);
        }else {
            mTabSegment.addTab(guide)
                    .addTab(mine);
        }

    }

    private void initPagers() {
        mFragmentList = new ArrayList<>();
        if (mIsJoined) {
            mFragmentList.add(new ApmRootFragment());
            mFragmentList.add(new MeetingPreviewFragment());
            mFragmentList.add(new MessageFragment());
        }else {
            mFragmentList.add(new GuideFragment());
        }


        mFragmentList.add(new MineFragment());

        mPagerAdapter = new HomeAdapter(getChildFragmentManager(),mFragmentList);
        mViewpagerHome.setAdapter(mPagerAdapter);
        mPagerAdapter.notifyDataSetChanged();
        mTabSegment.setupWithViewPager(mViewpagerHome, false);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Logger.i("OnDestroy");
    }
}
