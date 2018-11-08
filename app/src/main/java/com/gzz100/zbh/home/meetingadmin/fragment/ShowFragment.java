package com.gzz100.zbh.home.meetingadmin.fragment;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseFragment;
import com.gzz100.zbh.data.ObserverImpl;
import com.gzz100.zbh.data.entity.DelegateEntity;
import com.gzz100.zbh.data.entity.DocumentEntity;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.DocumentRequest;
import com.gzz100.zbh.home.meetingadmin.activity.FullscreenActivity;
import com.gzz100.zbh.home.meetingadmin.adapter.CatalogAdapter;
import com.gzz100.zbh.home.meetingadmin.adapter.DocumentAdapter;
import com.gzz100.zbh.widget.ExQMUIPopup;
import com.gzz100.zbh.widget.LineDecoration;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;

import static com.qmuiteam.qmui.widget.popup.QMUIPopup.DIRECTION_BOTTOM;

public class ShowFragment extends BaseFragment {


    @BindView(R.id.ll_browse_file)
    LinearLayout mBtFile;
    @BindView(R.id.btRotation)
    ImageView mBtRotation;
    Unbinder unbinder;
    @BindView(R.id.webView)
    WebView mWebView;
    @BindView(R.id.rcv_content)
    RecyclerView mRcvCatalog;

    @BindView(R.id.fl_showMask)
    FrameLayout mFlMask;
    @BindView(R.id.fl_no_file_tips)
    FrameLayout mFlNoFileTips;
    @BindView(R.id.iv_catalog_switch)
    ImageView ivCatalog;

    private String mMeetingId;
    private List<String> picUrlList;
    private List<DelegateEntity> delegateList;
    private CatalogAdapter mCatalogAdapter;
    private List<DocumentEntity> mDocumentList;
    private ExQMUIPopup mFilePopup;
    private DocumentAdapter mDocumentAdapter;
    private long mGroupId;
    private ObserverImpl<HttpResult<List<DocumentEntity>>> mFileObserver;

    public static ShowFragment getNewInstance(String meetingId,long groupId) {
        Bundle bundle = new Bundle();
        ShowFragment fragment = new ShowFragment();
        bundle.putString("meetingId", meetingId);
        bundle.putLong("groupId",groupId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_show, null);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            mMeetingId = getArguments().getString("meetingId");
            mGroupId = getArguments().getLong("groupId");
        }
        initCatalogView();
        delegateList = new ArrayList<>();
        loadContent();
        initFilePopupWin();

        loadDocumentData(false);
    }

    private void initCatalogView() {


    }

    private void initFilePopupWin() {
        mDocumentList = new ArrayList<>();
        mDocumentAdapter = new DocumentAdapter(getContext(), mDocumentList);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_file_recyclerview, null);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        mFilePopup = new ExQMUIPopup(getContext(), DIRECTION_BOTTOM);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new LineDecoration(getContext()));
        recyclerView.setAdapter(mDocumentAdapter);
        mDocumentAdapter.setOnItemClickListener(new DocumentAdapter.OnItemClickListener() {
            @Override
            public void onClick(int pos, DocumentEntity documentEntity) {
                loadDocument(mDocumentList.get(pos));
                mFilePopup.dismiss();
            }

            @Override
            public void onAddFileClick() {
                startParentFragment(ManageFileFragment.newInstance(mMeetingId));
                mFilePopup.dismiss();
            }
        });
        mFilePopup.setContentView(view);

    }



    @OnClick({R.id.ll_browse_file, R.id.btRotation, R.id.iv_catalog_switch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_browse_file:
                loadDocumentData(true);
                break;
            case R.id.btRotation:
                Intent intent = new Intent(getContext(), FullscreenActivity.class);
                intent.putExtra("meetingId",mMeetingId);
                intent.putExtra("groupId",mGroupId);
                startActivity(intent);
                break;
            case R.id.iv_catalog_switch:
                if (mRcvCatalog.getVisibility()== View.GONE) {
                    mRcvCatalog.setVisibility(View.VISIBLE);
                    mFlMask.setVisibility(View.VISIBLE);
                }else {
                    mRcvCatalog.setVisibility(View.GONE);
                    mFlMask.setVisibility(View.GONE);
                }
                break;
        }
    }



    private void loadDocumentData(final boolean showDialog) {

        mFileObserver = new ObserverImpl<HttpResult<List<DocumentEntity>>>() {

            @Override
            protected void onResponse(HttpResult<List<DocumentEntity>> docResult) {
                if (showDialog){
                    showDocumentList(docResult.getResult());
                }else {
                    if (docResult.getResult()!=null) {
                        if (docResult.getResult().size()==0) {
                            mFlNoFileTips.setVisibility(View.VISIBLE);
                        }else {
                            mFlNoFileTips.setVisibility(View.GONE);
                        }
                    }else {
                        mFlNoFileTips.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            protected void onFailure(Throwable e) {
                Toasty.error(getContext(), e.getMessage()).show();
            }
        };
        DocumentRequest request = new DocumentRequest();
        request.getDocumentList(mFileObserver, mMeetingId);

    }

    private void showDocumentList(final List<DocumentEntity> documentEntities) {

        if (documentEntities!=null){
            mDocumentList.clear();
            mDocumentList.addAll(documentEntities);
            mDocumentAdapter.notifyDataSetChanged();
        }
        if (mDocumentList.size()==0) {
            mFlNoFileTips.setVisibility(View.VISIBLE);
        }else {
            mFlNoFileTips.setVisibility(View.GONE);
        }
        mFilePopup.show(mBtFile);
    }

    private void loadContent() {

        initWebView();
        initContentView();
    }

    private void initContentView() {
        mRcvCatalog.setLayoutManager(new LinearLayoutManager(getContext()));
        mRcvCatalog.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        picUrlList = new ArrayList<>();
        mCatalogAdapter = new CatalogAdapter(picUrlList,getContext());
        mRcvCatalog.setAdapter(mCatalogAdapter);

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
            mFlNoFileTips.setVisibility(View.GONE);
            if (doc.getDocumentType().equals("doc")||doc.getDocumentType().equals("docx")
                    ||doc.getDocumentType().equals("xls")||doc.getDocumentType().equals("xlsx")) {
                mFlMask.setVisibility(View.GONE);
                ivCatalog.setVisibility(View.GONE);
                mRcvCatalog.setVisibility(View.GONE);
                mWebView.setVisibility(View.VISIBLE);
                mWebView.loadUrl(doc.getDocumentPath());
            }else {
                ivCatalog.setVisibility(View.VISIBLE);
                mWebView.setVisibility(View.VISIBLE);
                mWebView.loadUrl(doc.getDocumentPath());
                mRcvCatalog.setVisibility(View.VISIBLE);
                picUrlList.clear();
                if (doc.getPictureList()!=null){
                    picUrlList.addAll(doc.getPictureList());

                }
                mCatalogAdapter.notifyDataSetChanged();

                mCatalogAdapter.setOnItemClickListener(new CatalogAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(final int pos, String url) {
                        mCatalogAdapter.setPositivePPt(pos);
                        mRcvCatalog.setVisibility(View.GONE);
                        mFlMask.setVisibility(View.GONE);
                        callJS(pos+1);
                    }

                    @Override
                    public void onLongClick(int pos, String url) {

                    }
                });

            }

        }

    }

    private void callJS(int posId){
        final int version = Build.VERSION.SDK_INT;
        // 因为该方法在 Android 4.4 版本才可使用，所以使用时需进行版本判断
        if (version < 18) {
            mWebView.loadUrl("javascript:jumpById("+posId+")");
        } else {
            mWebView.evaluateJavascript("javascript:jumpById("+posId+")", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    //此处为 js 返回的结果

                }
            });
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mFileObserver!=null){
            mFileObserver.cancleRequest();
        }
        unbinder.unbind();
    }

}
