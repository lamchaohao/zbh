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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseFragment;
import com.gzz100.zbh.data.entity.DelegateEntity;
import com.gzz100.zbh.data.entity.DocumentEntity;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.DocumentRequest;
import com.gzz100.zbh.data.network.request.MeetingRequest;
import com.gzz100.zbh.home.meetingadmin.adapter.CatalogAdapter;
import com.gzz100.zbh.home.meetingadmin.adapter.DocumentAdapter;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
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
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.qmuiteam.qmui.widget.popup.QMUIPopup.DIRECTION_BOTTOM;

public class ShowFragment extends BaseFragment {


    @BindView(R.id.btFile)
    Button mBtFile;
    @BindView(R.id.btRotation)
    ImageView mBtRotation;
    Unbinder unbinder;
    @BindView(R.id.webView)
    WebView mWebView;
    @BindView(R.id.rcv_content)
    RecyclerView mRcvCatalog;
    @BindView(R.id.iv_catalog_switch)
    ImageView mIvCatalogSwitch;
    @BindView(R.id.fl_showMask)
    FrameLayout mFlMask;
    private String mMeetingId;
    private List<String> picUrlList;
    private List<DelegateEntity> delegateList;
    private CatalogAdapter mAdapter;
    private List<DocumentEntity> mDocumentList;
    private QMUIPopup mFilePopup;
    private DocumentAdapter mDocumentAdapter;
    private long mGroupId;

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
        delegateList = new ArrayList<>();
        loadContent();
        initFilePopupWin();
    }

    private void initFilePopupWin() {
        mDocumentList = new ArrayList<>();
        mDocumentAdapter = new DocumentAdapter(getContext(), mDocumentList);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_recycler_view, null);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        mFilePopup = new QMUIPopup(getContext(), DIRECTION_BOTTOM);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
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



    @OnClick({R.id.btFile, R.id.btRotation, R.id.iv_catalog_switch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btFile:
                loadDocumentData();
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

    private void loadDelegates() {
        Observer<HttpResult<List<DelegateEntity>>> observer = new Observer<HttpResult<List<DelegateEntity>>>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HttpResult<List<DelegateEntity>> result) {
                if (result.getResult()!=null){
                    delegateList.clear();
                    delegateList.addAll(result.getResult());
                }

                showDelegateList();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        MeetingRequest request=new MeetingRequest();
        request.getDelegates(observer,mMeetingId);
    }

    private void showDelegateList() {
//        ChoseSpeakerAdapter adapter = new ChoseSpeakerAdapter(getContext(), delegateList);
//        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_recycler_view, null);
//        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
//        QMUIPopup popup = new QMUIPopup(getContext(), DIRECTION_BOTTOM);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
//        recyclerView.setAdapter(adapter);
//
//        popup.setContentView(view);
//        popup.show(mBthost);
    }

    private void loadDocumentData() {

        Observer<HttpResult<List<DocumentEntity>>> observer = new Observer<HttpResult<List<DocumentEntity>>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HttpResult<List<DocumentEntity>> docResult) {
                showDocumentList(docResult.getResult());
            }

            @Override
            public void onError(Throwable e) {
                Toasty.error(getContext(), e.getMessage()).show();
            }

            @Override
            public void onComplete() {

            }
        };
        DocumentRequest request = new DocumentRequest();
        request.getDocumentList(observer, mMeetingId);

    }

    private void showDocumentList(final List<DocumentEntity> documentEntities) {

        if (documentEntities!=null){
            mDocumentList.clear();
            mDocumentList.addAll(documentEntities);
            mDocumentAdapter.notifyDataSetChanged();
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
        mAdapter = new CatalogAdapter(picUrlList,getContext());
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
                mFlMask.setVisibility(View.GONE);
                mIvCatalogSwitch.setVisibility(View.GONE);
                mRcvCatalog.setVisibility(View.GONE);
                mWebView.setVisibility(View.VISIBLE);
                mWebView.loadUrl(doc.getDocumentPath());
            }else {
                mIvCatalogSwitch.setVisibility(View.VISIBLE);
                mWebView.setVisibility(View.VISIBLE);
                mWebView.loadUrl(doc.getDocumentPath());
                mRcvCatalog.setVisibility(View.VISIBLE);
                picUrlList.clear();
                if (doc.getPictureList()!=null){
                    picUrlList.addAll(doc.getPictureList());

                }
                mAdapter.notifyDataSetChanged();

                mAdapter.setOnItemClickListener(new CatalogAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(final int pos, String url) {
                        mAdapter.setPositivePPt(pos);
                        mRcvCatalog.setVisibility(View.GONE);
                        mFlMask.setVisibility(View.GONE);
                        callJS(pos+1);
                    }

                    @Override
                    public void onLongClick(int pos, String url) {
                        showPreView(pos, url);
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

    private void showPreView(int pos, String url) {

//        QMUIPopup popup = new QMUIPopup(getContext(), DIRECTION_BOTTOM);
//        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_imageview_full, null);
//        ImageView imageView = view.findViewById(R.id.image_view);
//
//        FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(DensityUtil.dp2px(getContext(),240),
//                ViewGroup.LayoutParams.WRAP_CONTENT);
//        imageView.setLayoutParams(params);
//        GlideApp.with(this)
//                .load(url)
//                .encodeQuality(100)
//                .into(imageView);
//
//        popup.setContentView(view);
//        popup.show(mBthost);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
