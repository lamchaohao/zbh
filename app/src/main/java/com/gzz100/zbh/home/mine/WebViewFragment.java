package com.gzz100.zbh.home.mine;

import android.view.LayoutInflater;
import android.view.View;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseBackFragment;
import com.orhanobut.logger.Logger;
import com.tencent.smtt.export.external.extension.interfaces.IX5WebChromeClientExtension;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Lam on 2018/2/7.
 */

public class WebViewFragment extends BaseBackFragment {
    @BindView(R.id.webView)
    WebView mWebView;
    Unbinder unbinder;

    public static WebViewFragment newInstance(){
        WebViewFragment fragment=new WebViewFragment();
        return fragment;
    }

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_webview, null);
        unbinder = ButterKnife.bind(this, view);

        initView();
        return attachToSwipeBack(view);
    }

    private void initView() {
        WebSettings settings = mWebView.getSettings();
        IX5WebChromeClientExtension extension = mWebView.getWebChromeClientExtension();
//        settings
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView webView, int i) {
                Logger.i("progress="+i);
            }

        });

        settings.setJavaScriptEnabled(true);
        //支持双指缩放
        settings.setSupportZoom(true);
        //设置内置缩放空间,如果设置为false,则不可缩放
        settings.setBuiltInZoomControls(true);
        //自适应屏幕
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        mWebView.loadUrl("http://x5.tencent.com/tbs/guide.html");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
