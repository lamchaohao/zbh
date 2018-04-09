package com.gzz100.zbh.account;


import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseFragment;
import com.orhanobut.logger.Logger;

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
        initView();
        return view;
    }

    @Override
    public void onStart() {
        mZxingview.startCamera();
        mZxingview.showScanRect();
        mZxingview.startSpot();
        super.onStart();
    }

    @Override
    public void onStop() {
        mZxingview.stopCamera();
        super.onStop();
    }

    private void initView() {
        mZxingview.setDelegate(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getContext().getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }
    @Override
    public void onScanQRCodeSuccess(String result) {
        Logger.i("result = "+result);
        mZxingview.startSpot();
        vibrate();
        Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
    }
}
