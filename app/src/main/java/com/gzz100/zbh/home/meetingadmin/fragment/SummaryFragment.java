package com.gzz100.zbh.home.meetingadmin.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.gzz100.zbh.R;
import com.gzz100.zbh.account.User;
import com.gzz100.zbh.base.BaseBackFragment;
import com.gzz100.zbh.res.Common;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.tencent.smtt.export.external.extension.interfaces.IX5WebChromeClientExtension;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Lam on 2018/5/7.
 */

public class SummaryFragment extends BaseBackFragment {


    @BindView(R.id.topbar)
    QMUITopBar mTopbar;
    @BindView(R.id.webView)
    WebView mWebView;
    Unbinder unbinder;
    private String mMeetingId;


    public static SummaryFragment newInstance(String meetingId){
        SummaryFragment fragment=new SummaryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("meetingId",meetingId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_summary, null);

        unbinder = ButterKnife.bind(this, view);
        return attachToSwipeBack(view);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVar();
        initView();
    }

    private void initVar() {
//        mTopBar
        if (getArguments()!=null) {
            mMeetingId = getArguments().getString("meetingId");
            mTopbar.setTitle("会议纪要");
        }
        mTopbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });

        Button textButton = mTopbar.addRightTextButton("保存", R.id.textButtonId);
        textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callJsSave();
            }
        });
    }

    private void callJsSave() {
        final int version = Build.VERSION.SDK_INT;
        // 因为该方法在 Android 4.4 版本才可使用，所以使用时需进行版本判断
        if (version < 18) {
            mWebView.loadUrl("javascript:uploadMeetingSummary()");
        } else {
            mWebView.evaluateJavascript("javascript:uploadMeetingSummary()", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    //此处为 js 返回的结果

                }
            });
        }
    }

    private void initView() {
        User user = User.getUserFromCache();
        StringBuilder urlSb=new StringBuilder(Common.BASE_URL);
        urlSb.append("editor.html?meetingId=");
        urlSb.append(mMeetingId);
        urlSb.append("&userId=");
        urlSb.append(user.getUserId());

        WebSettings settings = mWebView.getSettings();
        IX5WebChromeClientExtension extension = mWebView.getWebChromeClientExtension();

        settings.setJavaScriptEnabled(true);
        //支持双指缩放
        settings.setSupportZoom(true);
        //设置内置缩放空间,如果设置为false,则不可缩放
        settings.setBuiltInZoomControls(true);
        //自适应屏幕
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        mWebView.loadUrl(urlSb.toString());
        //捕捉弹窗
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(getContext());
                b.setTitle("上传结果");
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                        pop();
                    }
                });
                b.setCancelable(false);
                b.create().show();
                return true;
            }

        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
