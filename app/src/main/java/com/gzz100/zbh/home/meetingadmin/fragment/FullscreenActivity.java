package com.gzz100.zbh.home.meetingadmin.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.gson.Gson;
import com.gzz100.zbh.R;
import com.gzz100.zbh.data.entity.DocumentEntity;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.DocumentRequest;
import com.gzz100.zbh.home.meetingadmin.adapter.CatalogAdapter;
import com.gzz100.zbh.home.meetingadmin.adapter.DocumentAdapter;
import com.gzz100.zbh.mimc.Constant;
import com.gzz100.zbh.mimc.MCUserManager;
import com.gzz100.zbh.mimc.MimcMsgHandler;
import com.gzz100.zbh.mimc.SyncCanvasBean;
import com.gzz100.zbh.mimc.SyncDocumentBean;
import com.gzz100.zbh.mimc.SyncMeetingBean;
import com.gzz100.zbh.mimc.TextMsg;
import com.gzz100.zbh.res.Common;
import com.gzz100.zbh.utils.GlideApp;
import com.gzz100.zbh.widget.SuperDrawingView;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.xiaomi.mimc.MIMCException;
import com.xiaomi.mimc.MIMCGroupMessage;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.gzz100.zbh.mimc.SyncMeetingBean.ACTION_CLEAR;
import static com.gzz100.zbh.mimc.SyncMeetingBean.ACTION_SWITCH_SPEAK;
import static com.gzz100.zbh.mimc.SyncMeetingBean.ACTION_SWITCH_STATUS;
import static com.qmuiteam.qmui.widget.popup.QMUIPopup.DIRECTION_BOTTOM;

public class FullscreenActivity extends AppCompatActivity implements MimcMsgHandler.OnHandlerMimcGroupMsgListener {
    @BindView(R.id.iv_catalog_switch)
    ImageView mIvCatalogSwitch;
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
    PhotoView mIvShowPPt;
    @BindView(R.id.webView)
    WebView mWebView;
    @BindView(R.id.rcv_content)
    RecyclerView mRcvCatalog;
    @BindView(R.id.sdv_canvas)
    SuperDrawingView mCanvasView;

    private String mMeetingId;
    private List<DocumentEntity> mDocumentList;
    private List<String> picUrlList;
    private CatalogAdapter mAdapter;
    private SyncHandler mHandler;
    private MCUserManager mUserManager;
    private long mGroupId;
    private Gson mGson;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_fullscreen);
        ButterKnife.bind(this);
        initVar();
        initHandler();
        initMimc();
        initCanvasView();
        loadDocumentData();
    }


    private void initMimc() {
        mUserManager = MCUserManager.getInstance();
        mUserManager.getMsgHandler().addMsgListener(this);
        try {
            if (mUserManager.getUser()!=null){
                mUserManager.getUser().login();
            }else {
                Logger.i("mUserManager.getUser()=null");
            }
        } catch (MIMCException e) {
            e.printStackTrace();
        }
    }

    private void initHandler() {
        mHandler = new SyncHandler();
    }

    private void initVar() {
        mGson = new Gson();
        mDocumentList=new ArrayList<>();
        mMeetingId = getIntent().getStringExtra("meetingId");
        mGroupId = getIntent().getLongExtra("groupId", 0);
        loadContent();
    }

    @OnClick({R.id.btFile, R.id.bthost, R.id.btRotation, R.id.btpause, R.id.ivEdit,R.id.iv_catalog_switch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btFile:
                if (mDocumentList != null) {
                    showDocumentList(mDocumentList);
                }
                break;
            case R.id.bthost:
                if (mCanvasView.getVisibility()== View.VISIBLE) {
                    mCanvasView.setVisibility(View.GONE);
                }else {
                    mCanvasView.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btRotation:
                if(mCanvasView.getVisibility()== View.VISIBLE) {
                mCanvasView.setVisibility(View.GONE);
                }else {
                    mCanvasView.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btpause:
                SyncMeetingBean syncMeeting = new SyncMeetingBean(ACTION_SWITCH_STATUS);
                syncMeeting.setMeetingStau(Common.STATUS_PAUSE);
                try {
                    mUserManager.sendGroupMsg(mGroupId,mGson.toJson(syncMeeting),Constant.SYNC_MEETING);
                } catch (MIMCException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.ivEdit:
                SyncMeetingBean sync = new SyncMeetingBean(ACTION_CLEAR);
                String clearJson = mGson.toJson(sync);
                try {
                    mCanvasView.clearAll();
                    mUserManager.sendGroupMsg(mGroupId,clearJson,Constant.SYNC_MEETING);
                } catch (MIMCException e) {
                    e.printStackTrace();
                }

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

    private void sendDocumentMsg(DocumentEntity doc){
        SyncDocumentBean syncDoc=new SyncDocumentBean(SyncDocumentBean.ACTION_DOC);
        syncDoc.setDocId(doc.getDocumentId());
        syncDoc.setDocUrl(doc.getDocumentPath());
        syncDoc.setDocType(doc.getDocumentType());
        try {
            mUserManager.sendGroupMsg(mGroupId,mGson.toJson(syncDoc),Constant.SYNC_DOCUMENT);
        } catch (MIMCException e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
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
                sendDocumentMsg(documentEntities.get(pos));
                loadDocument(documentEntities.get(pos));
            }

            @Override
            public void onAddFileClick() {

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
                mIvCatalogSwitch.setVisibility(View.GONE);
                mRcvCatalog.setVisibility(View.GONE);
                mWebView.setVisibility(View.VISIBLE);
                mWebView.loadUrl(doc.getDocumentPath());
            }else {
                mIvCatalogSwitch.setVisibility(View.VISIBLE);
                mWebView.setVisibility(View.GONE);
                mIvShowPPt.setVisibility(View.VISIBLE);
                mRcvCatalog.setVisibility(View.VISIBLE);
                picUrlList.clear();
                if (doc.getPictureList()!=null){
                    picUrlList.addAll(doc.getPictureList());
                    if (picUrlList.size()>0){
                        loadPic(picUrlList.get(0));
                    }
                }
                mAdapter.notifyDataSetChanged();

                mAdapter.setOnItemClickListener(new CatalogAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(final int pos, String url) {
                        mAdapter.setPositivePPt(pos);
                        mRcvCatalog.setVisibility(View.GONE);
                        sendPicMsg(url);
                        loadPic(url);
                    }

                    @Override
                    public void onLongClick(int pos, String url) {

                    }
                });

            }

        }


    }

    private void sendPicMsg(String url) {

        SyncDocumentBean syncDoc=new SyncDocumentBean(SyncDocumentBean.ACTION_PIC);
        syncDoc.setPicUrl(url);
        try {
            mUserManager.sendGroupMsg(mGroupId,mGson.toJson(syncDoc),Constant.SYNC_DOCUMENT);
        } catch (MIMCException e) {
            e.printStackTrace();
        }

    }

    private void loadPic(String url) {
        GlideApp.with(FullscreenActivity.this)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        mIvShowPPt.setImageDrawable(resource);
                        // 如果return true; 则 into(iv) 不起作用， 要手动设置图片
                        return true;
                    }
                })
                .override(Target.SIZE_ORIGINAL)
                .encodeQuality(100)
                .into(mIvShowPPt);
    }

    private void initCanvasView() {
        mCanvasView.setOnDrawListener(new SuperDrawingView.OnDrawListener() {
            @Override
            public void onDrawCompleted(List<SyncCanvasBean> syncPointList) {
                try {

                    mUserManager.sendSyncCanvasMsg(mGroupId,syncPointList);
                    mUserManager.sendGroupMsg(mGroupId, mGson.toJson(syncPointList), Constant.TEXT);
                } catch (MIMCException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onGroupTextMsgReceived(TextMsg textMsg) {

    }

    @Override
    public void onCanvasMsgReceived(final List<SyncCanvasBean> syncCanvasBeans) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCanvasView.onSyncCanvasMsgReceived(syncCanvasBeans);
            }
        });
    }

    @Override
    public void onDocumentReceived(final SyncDocumentBean syncDocumentBean) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (syncDocumentBean.getActionType()== SyncDocumentBean.ACTION_DOC) {
                    DocumentEntity documentEntity=new DocumentEntity();
                    documentEntity.setDocumentId(syncDocumentBean.getDocId());
                    documentEntity.setDocumentPath(syncDocumentBean.getDocUrl());
                    documentEntity.setDocumentType(syncDocumentBean.getDocType());
                    for (DocumentEntity entity : mDocumentList) {
                        if (entity.getDocumentId().equals(syncDocumentBean.getDocId())) {
                            loadDocument(entity);
                            break;
                        }

                    }

                }else {
                    loadPic(syncDocumentBean.getPicUrl());
                }
            }
        });
    }

    @Override
    public void onMeetingActionReceived(final SyncMeetingBean syncMeetingBean) {
        switch (syncMeetingBean.getActionType()) {
            case ACTION_CLEAR:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mCanvasView.clearAll();
                    }
                });
                break;
            case ACTION_SWITCH_STATUS:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String statu=syncMeetingBean.getMeetingStau()==Common.STATUS_PAUSE?"暂停":"开始";
                        Toasty.normal(FullscreenActivity.this,"会议"+statu).show();
                    }
                });

                break;
            case ACTION_SWITCH_SPEAK:

                break;
        }

    }

    @Override
    public void onSendGroupMsgTimeout(MIMCGroupMessage mimcGroupMessage) {

    }


    class SyncHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

            }
        }
    }


}
