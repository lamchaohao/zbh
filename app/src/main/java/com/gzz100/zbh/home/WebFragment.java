package com.gzz100.zbh.home;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gzz100.zbh.R;
import com.gzz100.zbh.home.appointment.fragment.ApmDetailFragment;
import com.gzz100.zbh.base.BaseFragment;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class WebFragment extends BaseFragment {

    @BindView(R.id.btFile)
    Button mBtFile;
    @BindView(R.id.bthost)
    Button mBthost;
    @BindView(R.id.btRotation)
    ImageView mBtRotation;
    @BindView(R.id.btpause)
    ImageView mBtpause;
    @BindView(R.id.ivEdit)
    ImageView mIvEdit;
    @BindView(R.id.ivRotation2)
    ImageView mIvRotation2;
    @BindView(R.id.ivpause2)
    ImageView mIvpause2;
    @BindView(R.id.ivedit2)
    ImageView mIvedit2;
    @BindView(R.id.ib_more)
    ImageButton mIbMore;
    @BindView(R.id.ib_showMore)
    ImageButton mIbShowMore;
    private QMUIPopup mNormalPopup;
    @BindView(R.id.toolBar)
    LinearLayout toolLayout;
    @BindView(R.id.hsView)
    HorizontalScrollView hsView;
    boolean isExpanded;

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View inflate = inflater.inflate(R.layout.fragment_web, null);
        ButterKnife.bind(this, inflate);
        Logger.i("WebFragment onCreateView");
        return inflate;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Logger.i("WebFragment onDetach");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.i("WebFragment onDestroy");
    }

    @OnClick({R.id.btRotation, R.id.btpause, R.id.ivEdit, R.id.ivRotation2, R.id.ivpause2, R.id.ivedit2,R.id.ib_more,R.id.ib_showMore})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btRotation:
                startParentFragment(new ApmDetailFragment());
                break;
            case R.id.btpause:
                startUpApplication("com.alibaba.android.rimet");
                break;
            case R.id.ivEdit:
                MiPushClient.setAlias(getContext(),"vivoX6play",null);
                break;
            case R.id.ivRotation2:
                break;
            case R.id.ivpause2:
                break;
            case R.id.ivedit2:
                break;
            case R.id.ib_more:
                initNormalPopupIfNeed();
                mNormalPopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_CENTER);
                mNormalPopup.setPreferredDirection(QMUIPopup.DIRECTION_BOTTOM);
                mNormalPopup.show(view);
                break;
            case R.id.ib_showMore:

                if (isExpanded) {
                    hsView.smoothScrollTo(-230,0);
                    mIbShowMore.setImageResource(R.drawable.ic_chevron_right_black_24dp);
                }else {
                    hsView.smoothScrollTo(230,0);
                    mIbShowMore.setImageResource(R.drawable.ic_chevron_left_black_24dp);
                }
                isExpanded=!isExpanded;
                break;
        }
    }

    private void initNormalPopupIfNeed() {
        if (mNormalPopup == null) {
            mNormalPopup = new QMUIPopup(getContext(), QMUIPopup.DIRECTION_NONE);
            View inflate = LayoutInflater.from(getContext()).inflate(R.layout.content_more_button, null);
            mNormalPopup.setContentView(inflate);
        }
    }

    /**
     * <功能描述> 启动应用程序
     *
     * @return void [返回类型说明]
     */
    private void startUpApplication(String pkg) {
        PackageManager packageManager = getContext().getPackageManager();
        PackageInfo packageInfo = null;
        try {
            // 获取指定包名的应用程序的PackageInfo实例
            packageInfo = packageManager.getPackageInfo(pkg, 0);
        } catch (PackageManager.NameNotFoundException e) {
            // 未找到指定包名的应用程序
            e.printStackTrace();
            // 提示没有GPS Test Plus应用
            Toast.makeText(getContext(), "没有此应用",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (packageInfo != null) {
            // 已安装应用
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            resolveIntent.setPackage(packageInfo.packageName);
            List<ResolveInfo> apps = packageManager.queryIntentActivities(
                    resolveIntent, 0);
            ResolveInfo ri = null;
            try {
                ri = apps.iterator().next();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            if (ri != null) {
                // 获取应用程序对应的启动Activity类名
                String className = ri.activityInfo.name;
                // 启动应用程序对应的Activity
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                ComponentName componentName = new ComponentName(pkg, className);
                intent.setComponent(componentName);
                startActivity(intent);
            }
        }
    }


}
