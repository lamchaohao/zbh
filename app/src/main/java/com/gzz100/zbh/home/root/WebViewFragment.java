package com.gzz100.zbh.home.root;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseBackFragment;
import com.gzz100.zbh.utils.ShareToWeixin;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.tencent.smtt.export.external.extension.interfaces.IX5WebChromeClientExtension;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Lam on 2018/2/7.
 */

public class WebViewFragment extends BaseBackFragment {
    @BindView(R.id.topbar)
    QMUITopBar mTopBar;
    @BindView(R.id.webView)
    WebView mWebView;
    Unbinder unbinder;
    private String mUrl;
    private String mTitle;

    public static WebViewFragment newInstance(String url,String title){
        WebViewFragment fragment=new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url",url);
        bundle.putString("title",title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_webview, null);
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
            mUrl = getArguments().getString("url");
            mTitle = getArguments().getString("title");
            mTopBar.setTitle(mTitle);
        }
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });

        QMUIAlphaImageButton imageButton = mTopBar.addRightImageButton(R.drawable.ic_more_vert_grey_500_24dp, R.id.imageButtonId);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShareSelection();
            }
        });
    }

    private void showShareSelection() {
        final int TAG_SHARE_WECHAT_FRIEND = 0;
        final int TAG_SHARE_WECHAT_MOMENT = 1;
        QMUIBottomSheet.BottomGridSheetBuilder builder = new QMUIBottomSheet.BottomGridSheetBuilder(getActivity());
        builder.addItem(R.mipmap.icon_more_operation_share_friend, "分享到微信", TAG_SHARE_WECHAT_FRIEND, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.icon_more_operation_share_moment, "分享到朋友圈", TAG_SHARE_WECHAT_MOMENT, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomGridSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView) {
                        dialog.dismiss();
                        String type=null;
                        if (mTitle.contains(".")) {
                            type = mTitle.substring(mTitle.lastIndexOf("."));
                        }else {
                            type = "html";
                        }

                        int tag = (int) itemView.getTag();
                        switch (tag) {
                            case TAG_SHARE_WECHAT_FRIEND:
                                ShareToWeixin.shareDocumentToWechat(_mActivity,mUrl,mTitle,type,false);
                                break;
                            case TAG_SHARE_WECHAT_MOMENT:
                                ShareToWeixin.shareDocumentToWechat(_mActivity,mUrl,mTitle,type,true);
                                break;
                        }
                    }
                }).build().show();
    }




    private void initView() {
        WebSettings settings = mWebView.getSettings();
        IX5WebChromeClientExtension extension = mWebView.getWebChromeClientExtension();
//        settings
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView webView, int i) {

            }

        });

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
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
        mWebView.loadUrl(mUrl);
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
