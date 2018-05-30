package com.gzz100.zbh.home.mine;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseBackFragment;
import com.gzz100.zbh.data.entity.UpdateInfo;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.AttachRequest;
import com.gzz100.zbh.home.root.WebViewFragment;
import com.gzz100.zbh.res.Common;
import com.gzz100.zbh.utils.FileDownloadManager;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Lam on 2018/5/10.
 */

public class AboutFragment extends BaseBackFragment {
    @BindView(R.id.topbar)
    QMUITopBar mTopbar;
    Unbinder unbinder;

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_about, null);
        unbinder = ButterKnife.bind(this, view);
        return attachToSwipeBack(view);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTopbar();
    }

    private void initTopbar() {
        mTopbar.setTitle("关于中佰会");
        mTopbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
    }

    @OnClick({R.id.tv_check_update, R.id.tv_version_info, R.id.tv_feedback})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_check_update:
                checkUpdate();
                break;
            case R.id.tv_version_info:
//                versionintroduction.html
                startFragment(WebViewFragment.newInstance(Common.BASE_URL+"versionintroduction.html?versionCode="+getPackageInfo().versionCode,"版本介绍"));
                break;
            case R.id.tv_feedback:
                startFragment(new FeedbackFragment());
                break;
        }
    }

    private void checkUpdate() {

        AttachRequest request =new AttachRequest();
        request.checkUpdate(new Observer<HttpResult<UpdateInfo>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HttpResult<UpdateInfo> result) {
                UpdateInfo info = result.getResult();
                if (info!=null) {
                    showHasUpdate(info);
                }else {
                    Toasty.normal(getContext(),"已是最新版本").show();
                }
            }

            @Override
            public void onError(Throwable e) {
                Toasty.error(getContext().getApplicationContext(),e.getMessage()).show();
            }

            @Override
            public void onComplete() {

            }
        }, getPackageInfo().versionCode+"", getPackageInfo().versionName);

    }

    private void showHasUpdate(final UpdateInfo info) {
        new QMUIDialog.MessageDialogBuilder(getContext())
                .setMessage(info.getWhatsNew())
                .setTitle("发现新版本")
                .addAction(0, "更新", QMUIDialogAction.ACTION_PROP_POSITIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        Toasty.normal(getContext(),"开始下载").show();
                        FileDownloadManager.getInstance(getContext()).startDownlodApk(info.getDownloadUrl(),info.getVersionName());
                    }
                })
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .show();

    }


   private PackageInfo getPackageInfo(){
        PackageInfo packageInfo= null;
        try {
            packageInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
