package com.gzz100.zbh.home.meetingadmin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.data.entity.DocumentEntity;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.DocumentRequest;
import com.gzz100.zbh.home.meetingadmin.adapter.CatalogAdapter;
import com.gzz100.zbh.home.meetingadmin.adapter.DocumentAdapter;
import com.gzz100.zbh.utils.GlideApp;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.qmuiteam.qmui.widget.popup.QMUIPopup.DIRECTION_BOTTOM;

public class FullscreenActivity extends AppCompatActivity {
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
    Unbinder unbinder;
    @BindView(R.id.iv_showPPt)
    ImageView mIvShowPPt;
    @BindView(R.id.webView)
    WebView mWebView;
    @BindView(R.id.rcv_content)
    RecyclerView mRcvCatalog;

    private String mMeetingId;
    private List<DocumentEntity> mDocumentList;
    private List<String> picUrlList;
    private CatalogAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mMeetingId = getIntent().getStringExtra("meetingId");
        loadContent();
    }

    @OnClick({R.id.btFile, R.id.bthost, R.id.btRotation, R.id.btpause, R.id.ivEdit,R.id.iv_catalog_switch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btFile:
                loadDocumentData();
                break;
            case R.id.bthost:
                break;
            case R.id.btRotation:
                finish();
                break;
            case R.id.btpause:
                break;
            case R.id.ivEdit:
                break;
            case R.id.iv_catalog_switch:
                if (mRcvCatalog.getVisibility()== View.GONE) {
                    mRcvCatalog.setVisibility(View.VISIBLE);
                }else {
                    mRcvCatalog.setVisibility(View.GONE);
                }
                break;
        }
    }
    private void loadDocumentData() {

        Observer<HttpResult<List<DocumentEntity>>> observer = new Observer<HttpResult<List<DocumentEntity>>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HttpResult<List<DocumentEntity>> listHttpResult) {
                mDocumentList = listHttpResult.getResult();
                if (mDocumentList != null) {
                    showDocumentList(mDocumentList);
                }
            }

            @Override
            public void onError(Throwable e) {
                Toasty.error(FullscreenActivity.this, e.getMessage()).show();
            }

            @Override
            public void onComplete() {

            }
        };
        DocumentRequest request = new DocumentRequest();
        request.getDocumentList(observer, mMeetingId);

    }

    private void showDocumentList(final List<DocumentEntity> documentEntities) {
        DocumentAdapter adapter = new DocumentAdapter(this, documentEntities);
        View view = LayoutInflater.from(this).inflate(R.layout.item_recycler_view, null);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        QMUIPopup popup = new QMUIPopup(this, DIRECTION_BOTTOM);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new DocumentAdapter.OnItemClickListener() {
            @Override
            public void onClick(int pos, DocumentEntity documentEntity) {
                Logger.i(mDocumentList.get(pos).getDocumentName());
                loadDocument(documentEntities.get(pos));
            }
        });
        popup.setContentView(view);
        popup.show(mBtFile);


    }

    private void loadContent() {

        mIvShowPPt.setVisibility(View.GONE);
        initWebView();
        initContentView();
    }

    private void initContentView() {
        mRcvCatalog.setLayoutManager(new LinearLayoutManager(this));
        mRcvCatalog.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        picUrlList = new ArrayList<>();
        mAdapter = new CatalogAdapter(picUrlList,this);
        mRcvCatalog.setAdapter(mAdapter);

//        mRcvShowPPt.setLayoutManager(new LinearLayoutManager(this));
//        mRcvShowPPt.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
//        mPptAdapter = new PPTAdapter(picUrlList,this);
//        mRcvShowPPt.setAdapter(mPptAdapter);

    }

    private void initWebView() {
        WebSettings settings = mWebView.getSettings();
        //使用js
        settings.setJavaScriptEnabled(true);
        //支持双指缩放
        settings.setSupportZoom(true);
        //设置内置缩放空间,如果设置为false,则不可缩放
        settings.setBuiltInZoomControls(true);
        //自适应屏幕
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        //隐藏原生的缩放控件
        settings.setDisplayZoomControls(false);

    }


    private void loadDocument(DocumentEntity doc){

        if(doc!=null){

            if (doc.getDocumentType().equals("doc")||doc.getDocumentType().equals("docx")
                    ||doc.getDocumentType().equals("xls")||doc.getDocumentType().equals("xlsx")) {

                mIvShowPPt.setVisibility(View.GONE);
                mWebView.setVisibility(View.VISIBLE);
                mWebView.loadUrl(doc.getDocumentPath());

            }else {
                picUrlList.clear();
                picUrlList.addAll(doc.getPictureList());
                mAdapter.notifyDataSetChanged();
                mWebView.setVisibility(View.GONE);
                mIvShowPPt.setVisibility(View.VISIBLE);
                mRcvCatalog.setVisibility(View.VISIBLE);

                if (doc.getPictureList()!=null&&doc.getPictureList().size()>0) {
                    GlideApp.with(this)
                            .load(doc.getPictureList().get(0))
                            .encodeQuality(100)
                            .into(mIvShowPPt);
                }

                mAdapter.setOnItemClickListener(new CatalogAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int pos, String url) {
                        GlideApp.with(FullscreenActivity.this)
                                .load(url)
                                .encodeQuality(100)
                                .into(mIvShowPPt);
//                        mRcvShowPPt.smoothScrollToPosition(pos);
                    }

                    @Override
                    public void onLongClick(int pos, String url) {

                    }
                });
            }

        }

    }


}
