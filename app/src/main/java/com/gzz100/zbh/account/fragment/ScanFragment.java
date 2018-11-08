package com.gzz100.zbh.account.fragment;


import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.gzz100.zbh.R;
import com.gzz100.zbh.account.eventEntity.OnBackEvent;
import com.gzz100.zbh.account.User;
import com.gzz100.zbh.base.BaseFragment;
import com.gzz100.zbh.home.root.WebViewFragment;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

import static android.content.Context.VIBRATOR_SERVICE;

/**
 *
 */
public class ScanFragment extends BaseFragment implements QRCodeView.Delegate{

    @BindView(R.id.topbar)
    QMUITopBar mTopbar;
    @BindView(R.id.zxingview)
    ZXingView mZxingview;
    Unbinder unbinder;

    public ScanFragment() {
        // Required empty public constructor
    }


    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_scan, null, false);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return attachToSwipeBack(view);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    public void onStart() {
        mZxingview.startCamera();
        mZxingview.showScanRect();
        mZxingview.startSpot();
        super.onStart();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBackEvent(OnBackEvent backEvent){
        mZxingview.startCamera();
        mZxingview.showScanRect();
        mZxingview.startSpot();
    }


    @Override
    public void onStop() {
        mZxingview.stopCamera();
        super.onStop();
    }

    private void initView() {
        mTopbar.setTitle("扫一扫");
        mTopbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
        mZxingview.setDelegate(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mZxingview.stopCamera();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getContext().getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }
    @Override
    public void onScanQRCodeSuccess(final String result) {
        Logger.i("result = "+result);
        mZxingview.stopCamera();
        vibrate();
        if (result.startsWith("zbh:page")) {
            User user = User.getUserFromCache();
            byte[] encodeToken = Base64.encode(user.getToken().getBytes(), Base64.NO_WRAP);

            String url = result.substring(9);
            final StringBuilder sbUrl=new StringBuilder("http://");
            sbUrl.append(url).append("&")
                    .append("phone=")
                    .append(user.getPhone()).append("&")
                    .append("token=")
            .append(new String(encodeToken));

            new QMUIDialog.MessageDialogBuilder(_mActivity)
                    .setMessage(sbUrl.toString())
                    .addAction("去登录", new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            dialog.dismiss();

                            startFragment(WebViewFragment.newInstance(sbUrl.toString(),"登录"));
                        }
                    })
                    .addAction("重新扫描", new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            dialog.dismiss();
                            mZxingview.startCamera();
                            mZxingview.showScanRect();
                            mZxingview.startSpot();
                        }
                    })
                    .show();

        }else if (result.startsWith("http")){

            new QMUIDialog.MessageDialogBuilder(_mActivity)
                    .setMessage(result)
                    .addAction("打开网页", new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            dialog.dismiss();
                            startFragment(WebViewFragment.newInstance(result,"扫描结果"));
                        }
                    })
                    .addAction("重新扫描", new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            dialog.dismiss();
                            mZxingview.startCamera();
                            mZxingview.showScanRect();
                            mZxingview.startSpot();
                        }
                    })
                    .show();


        }else {
            new QMUIDialog.MessageDialogBuilder(_mActivity)
                    .setMessage(result)
                    .addAction("知道了", new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            dialog.dismiss();
                            pop();
                        }
                    })
                    .addAction("重新扫描", new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            dialog.dismiss();
                            mZxingview.startCamera();
                            mZxingview.showScanRect();
                            mZxingview.startSpot();
                        }
                    })
                    .show();
        }


    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
    }
}
