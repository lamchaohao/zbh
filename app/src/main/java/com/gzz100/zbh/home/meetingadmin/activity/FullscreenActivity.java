package com.gzz100.zbh.home.meetingadmin.activity;

import android.content.DialogInterface;
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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.gson.Gson;
import com.gzz100.zbh.R;
import com.gzz100.zbh.data.ObserverImpl;
import com.gzz100.zbh.data.entity.DelegateEntity;
import com.gzz100.zbh.data.entity.DocumentEntity;
import com.gzz100.zbh.data.eventEnity.MimcLoginStatus;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.DocumentRequest;
import com.gzz100.zbh.data.network.request.MeetingRequest;
import com.gzz100.zbh.home.meetingadmin.adapter.CatalogAdapter;
import com.gzz100.zbh.home.meetingadmin.adapter.ChoseSpeakerAdapter;
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
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.xiaomi.mimc.MIMCConstant;
import com.xiaomi.mimc.MIMCException;
import com.xiaomi.mimc.MIMCGroupMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import droidninja.filepicker.utils.FileUtils;
import es.dmoral.toasty.Toasty;

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
    @BindView(R.id.tv_loginStatu)
    TextView tvLoginStatu;
    @BindView(R.id.btRotation)
    ImageView mBtRotation;
    @BindView(R.id.btpause)
    ImageView mBtpause;
    @BindView(R.id.swc_zoom_edit)
    Switch mSwcZoomEdit;
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
    private List<DelegateEntity> mDelegateList;
    private SyncHandler mHandler;
    private MCUserManager mUserManager;
    private long mGroupId;
    private Gson mGson;
    private QMUIPopup mPopup;
    private DocumentAdapter mDocumentAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_fullscreen);
        ButterKnife.bind(this);
        initVar();
        initHandler();
        initMimc();
        initCanvasView();
        initFilePopupWin();
        initSwitch();
    }

    private void initSwitch() {

        mSwcZoomEdit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //绘画模式
                    mCanvasView.setVisibility(View.VISIBLE);
                }else {
                    //可放大模式
                    mCanvasView.setVisibility(View.GONE);
                }


            }
        });

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
        mDelegateList = new ArrayList<>();
        mDocumentList=new ArrayList<>();
        mMeetingId = getIntent().getStringExtra("meetingId");
        mGroupId = getIntent().getLongExtra("groupId", 0);
        loadContent();
    }


    private void initFilePopupWin() {
        mDocumentAdapter = new DocumentAdapter(this, mDocumentList);
        View view = LayoutInflater.from(this).inflate(R.layout.item_recycler_view, null);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        mPopup = new QMUIPopup(this, DIRECTION_BOTTOM);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mDocumentAdapter);
        mDocumentAdapter.setOnItemClickListener(new DocumentAdapter.OnItemClickListener() {
            @Override
            public void onClick(int pos, DocumentEntity documentEntity) {
                Logger.i(mDocumentList.get(pos).getDocumentName());
                sendDocumentMsg(mDocumentList.get(pos));
                loadDocument(mDocumentList.get(pos));
            }

            @Override
            public void onAddFileClick() {
                mPopup.dismiss();
            }
        });
        mPopup.setContentView(view);

    }



    @OnClick({R.id.btFile, R.id.bthost, R.id.btRotation, R.id.btpause, R.id.iv_color_piker,R.id.iv_catalog_switch,R.id.tv_loginStatu,R.id.iv_clear_path})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btFile:
                loadDocumentData();
                if (mDocumentList != null) {
                    mDocumentAdapter.notifyDataSetChanged();
                    mPopup.show(mBtFile);
                }
                break;
            case R.id.bthost:
                loadDelegates();
                break;
            case R.id.btRotation:
               finish();
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
            case R.id.iv_color_piker:
                showColorPicker();

                break;
            case R.id.iv_clear_path:

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
                if (mRcvCatalog.getVisibility()== View.VISIBLE) {
                    mRcvCatalog.setVisibility(View.GONE);
                }else {
                    mRcvCatalog.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.tv_loginStatu:
                if (mUserManager.getUser()!=null){
                    try {
                        mUserManager.getUser().login();
                    } catch (MIMCException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private void showColorPicker() {
        final String[] items = new String[]{"黑色", "红色", "蓝色","绿色"};
        final int checkedIndex = 1;
        new QMUIDialog.CheckableDialogBuilder(FullscreenActivity.this)
                .setCheckedIndex(checkedIndex)
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(FullscreenActivity.this, "你选择了 " + items[which], Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .show();

    }


    private void loadDelegates() {
        ObserverImpl<HttpResult<List<DelegateEntity>>> observer = new ObserverImpl<HttpResult<List<DelegateEntity>>>() {

            @Override
            protected void onResponse(HttpResult<List<DelegateEntity>> result) {
                if (result.getResult()!=null){
                    mDelegateList.clear();
                    mDelegateList.addAll(result.getResult());
                }

                showDelegateList();
            }

            @Override
            protected void onFailure(Throwable e) {
                Toasty.error(FullscreenActivity.this,e.getMessage()).show();
            }
        };

        MeetingRequest request=new MeetingRequest();
        request.getDelegates(observer,mMeetingId);
    }

    private void showDelegateList() {
        ChoseSpeakerAdapter adapter = new ChoseSpeakerAdapter(this, mDelegateList);
        View view = LayoutInflater.from(this).inflate(R.layout.item_recycler_view, null);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        QMUIPopup popup = new QMUIPopup(this, DIRECTION_BOTTOM);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

        popup.setContentView(view);
        popup.show(mBthost);
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

        ObserverImpl<HttpResult<List<DocumentEntity>>> observer = new ObserverImpl<HttpResult<List<DocumentEntity>>>() {

            @Override
            protected void onResponse(HttpResult<List<DocumentEntity>> listHttpResult) {
                mDocumentList.clear();
                mDocumentList.addAll(listHttpResult.getResult());
                mDocumentAdapter.notifyDataSetChanged();
                Logger.i("DocumentList:"+mDocumentList.size());
            }

            @Override
            protected void onFailure(Throwable e) {
                Toasty.error(FullscreenActivity.this, e.getMessage()).show();
            }
        };
        DocumentRequest request = new DocumentRequest();
        request.getDocumentList(observer, mMeetingId);

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

            if (FileUtils.isDocFile(doc.getDocumentType())||FileUtils.isExcelFile(doc.getDocumentType())) {
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginStatuChange(MimcLoginStatus status){
        switch (status.status) {
            case MIMCConstant.STATUS_LOGIN_SUCCESS:
                tvLoginStatu.setText("在线");
                break;
            case MIMCConstant.STATUS_LOGIN_FAIL:
                tvLoginStatu.setText("登录失败");
                break;
            case MIMCConstant.STATUS_LOGOUT:
                tvLoginStatu.setText("离线");
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
