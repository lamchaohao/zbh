package com.gzz100.zbh.home.mine;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.gzz100.zbh.R;
import com.gzz100.zbh.account.LoginFragment;
import com.gzz100.zbh.account.SearchCompFragment;
import com.gzz100.zbh.account.User;
import com.gzz100.zbh.base.BaseFragment;
import com.gzz100.zbh.data.entity.ApplyEntity;
import com.gzz100.zbh.utils.DensityUtil;
import com.gzz100.zbh.utils.TextHeadPicUtil;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 */
@RuntimePermissions
public class MineFragment extends BaseFragment {

    @BindView(R.id.tv_userName_mine)
    TextView mTvUserName;
    @BindView(R.id.tv_job_mine)
    TextView mTvjob;
    @BindView(R.id.tv_statu_mine)
    TextView mTvStatu;
    @BindView(R.id.tv_file_mine)
    TextView mTvFile;
    Unbinder unbinder;
    @BindView(R.id.btn_joinCompany_mine)
    Button mBtnJoinCompany;
    @BindView(R.id.iv_mine_compPic)
    ImageView mIvCompPic;
    @BindView(R.id.tv_star_mine)
    TextView mTvStar;
    @BindView(R.id.tv_about_mine)
    TextView mTvAbout;
    @BindView(R.id.tv_setting_mine)
    TextView mTvSetting;
    private View mRootView;

    public MineFragment() {
        // Required empty public constructor
    }


    @Override
    protected View onCreateView(LayoutInflater inflater) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_mine, null);
            ButterKnife.bind(this, mRootView);

        }

        unbinder = ButterKnife.bind(this, mRootView);
        EventBus.getDefault().register(this);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }


    private void initView() {
        User user = User.getUserFromCache();

        mTvUserName.setText(user.getUserName());
        if (user.getCompanyId().equals("0")||user.getCompanyId()==null){
            mBtnJoinCompany.setVisibility(View.VISIBLE);
            if (user.getApply()!=null){
                StringBuffer sb=new StringBuffer("等待");
                sb.append(user.getApply()
                        .getCompanyNameX())
                        .append("审核");
                mTvStatu.setText(sb.toString());
                mBtnJoinCompany.setVisibility(View.GONE);
                mTvjob.setVisibility(View.GONE);
            }else {
                mTvStatu.setText("你还没加入企业");
                mBtnJoinCompany.setVisibility(View.VISIBLE);
            }
        }else {
            mBtnJoinCompany.setVisibility(View.GONE);
            mTvStatu.setText(user.getCompanyName());
            mTvjob.setVisibility(View.VISIBLE);
            mTvjob.setText(user.getPositionName());
        }
        TextDrawable headPic = TextHeadPicUtil.getHeadPic(user.getUserName(),48, DensityUtil.dp2px(getContext(),64));
        mIvCompPic.setImageDrawable(headPic);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        MineFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE})
    public void showFile() {
        startParentFragment(new MineFileFragment());
        Logger.i("showFile");
    }

    // 向用户说明为什么需要这些权限（可选）
    @OnShowRationale({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE})
    void showInfoForPermissionRequest(final PermissionRequest request) {
        new AlertDialog.Builder(getContext())
                .setMessage("需要开启读写外部存储权限才能浏览文件")
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
    @OnPermissionDenied({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE})
    void showDeniedForCamera() {
        Toast.makeText(getContext(), "用户拒绝了授权并向你扔了一条狗", Toast.LENGTH_SHORT).show();
    }

    // 用户勾选了“不再提醒”时调用（可选）
    @OnNeverAskAgain({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE})
    void showNeverAskForCamera() {
        Toast.makeText(getContext(), "用户把你打入冷宫", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    @OnClick({R.id.tv_star_mine,R.id.rl_nameCard_mine,R.id.tv_about_mine, R.id.tv_setting_mine, R.id.tv_file_mine,R.id.tv_logout_mine,R.id.btn_joinCompany_mine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_about_mine:
                startParentFragment(new AboutFragment());
                break;
            case R.id.tv_setting_mine:
                startParentFragment(new SettingFragment());
                break;
            case R.id.tv_file_mine:
                //点击后开始权限询问,同意后执行showFile()
                MineFragmentPermissionsDispatcher.showFileWithPermissionCheck(MineFragment.this);
                break;
            case R.id.tv_logout_mine:
                logout();
                break;
            case R.id.btn_joinCompany_mine:
                startParentFragment(new SearchCompFragment());
                break;
            case R.id.rl_nameCard_mine:
                startParentFragment(new NameCardFragment());
                break;
            case R.id.tv_star_mine:
                startParentFragment(new MineStarFragment());
                break;

        }
    }

    /**
     * 在个人信息页面更改了用户名后信息更新
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNameChange(ApplyEntity applyEntity){
        initView();
    }

    private void logout() {
        new QMUIDialog.MessageDialogBuilder(getContext())
                .setTitle("退出")
                .setMessage("确定要退出登录吗？")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(0, "退出", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        if (User.logout()) {
                            dialog.dismiss();
                            ((BaseFragment)getParentFragment()).startWithPop(new LoginFragment());
                        }
                    }
                })
                .show();
    }


}
