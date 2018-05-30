package com.gzz100.zbh.home.meetingadmin.fragment;


import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseBackFragment;
import com.gzz100.zbh.data.entity.DownloadFileEntity;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.AttachRequest;
import com.gzz100.zbh.data.network.request.MeetingRequest;
import com.gzz100.zbh.home.meetingadmin.adapter.DownloadDocAdapter;
import com.gzz100.zbh.home.root.WebViewFragment;
import com.gzz100.zbh.utils.FileDownloadManager;
import com.gzz100.zbh.utils.ShareToWeixin;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.widget.QMUIEmptyView;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class DownloadFileFragment extends BaseBackFragment {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;
    @BindView(R.id.emptyView)
    QMUIEmptyView mEmptyView;
    @BindView(R.id.rcv_downloadDoc)
    RecyclerView mRcvDownloadDoc;
    @BindView(R.id.btn_save)
    Button mBtnSave;
    Unbinder unbinder;

    private List<DownloadFileEntity> mCanDownloadList;
    private DownloadDocAdapter mAdapter;
    private String mMeetingId;

    public static DownloadFileFragment newInstance(String meetingId) {
        DownloadFileFragment fileFragment = new DownloadFileFragment();
        Bundle bundle = new Bundle();
        bundle.putString("meetingId", meetingId);
        fileFragment.setArguments(bundle);
        return fileFragment;
    }


    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_download_file, null, false);
        unbinder = ButterKnife.bind(this, view);
        return attachToSwipeBack(view);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVar();
        initTopbar();
        initView();
        loadData();
    }

    private void initVar() {
        if (getArguments()!=null) {
            mMeetingId = getArguments().getString("meetingId");
        }
    }

    private void loadData() {
        MeetingRequest request=new MeetingRequest();

        Observer<HttpResult<List<DownloadFileEntity>>> observer=new Observer<HttpResult<List<DownloadFileEntity>>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HttpResult<List<DownloadFileEntity>> result) {
                List<DownloadFileEntity> fileList = result.getResult();
                if (fileList!=null) {
                    mCanDownloadList.addAll(fileList);
                    mAdapter.notifyDataSetChanged();
                    if (mCanDownloadList.size()==0){
                        mEmptyView.show();
                    }else {
                        mEmptyView.hide();
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                Toasty.error(getContext().getApplicationContext(),e.getMessage()).show();
            }

            @Override
            public void onComplete() {

            }
        };

        request.getDownloadFile(observer,mMeetingId);

    }

    private void initView() {
        mCanDownloadList=new ArrayList<>();
        mRcvDownloadDoc.setLayoutManager(new LinearLayoutManager(getContext()));
        mRcvDownloadDoc.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        mAdapter = new DownloadDocAdapter(mCanDownloadList,getContext());
        mRcvDownloadDoc.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new DownloadDocAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos, DownloadFileEntity fileEntity) {
                startFragment(WebViewFragment.newInstance(fileEntity.getDocumentPath(),fileEntity.getDocumentName()));
            }
        });
    }

    private void initTopbar() {
        mTopBar.setTitle("下载文件");
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
    }



    @OnClick(R.id.btn_save)
    void onSaveClick(View view){

        List<DownloadFileEntity> selectedFile = mAdapter.getSelectedFile();
        for (DownloadFileEntity entity : selectedFile) {
            Logger.i(entity.getDocumentName());
        }

        showActionMenu();

    }
    private void showActionMenu() {
        final int TAG_SHARE_WECHAT_FRIEND = 0;
        final int TAG_SHARE_WECHAT_MOMENT = 1;
        final int TAG_SHARE_WEIBO = 2;
        final int TAG_SHARE_CHAT = 3;
        final int TAG_SHARE_LOCAL = 4;
        QMUIBottomSheet.BottomGridSheetBuilder builder = new QMUIBottomSheet.BottomGridSheetBuilder(getActivity());
        builder.addItem(R.mipmap.icon_more_operation_share_friend, "分享到微信", TAG_SHARE_WECHAT_FRIEND, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.icon_more_operation_share_moment, "分享到朋友圈", TAG_SHARE_WECHAT_MOMENT, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.icon_more_operation_share_weibo, "添加到收藏", TAG_SHARE_WEIBO, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
//                .addItem(R.mipmap.icon_more_operation_share_chat, "分享到邮箱", TAG_SHARE_CHAT, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.icon_more_operation_save, "保存到本地", TAG_SHARE_LOCAL, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomGridSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView) {
                        dialog.dismiss();
                        int tag = (int) itemView.getTag();
                        switch (tag) {
                            case TAG_SHARE_WECHAT_FRIEND:

                                break;
                            case TAG_SHARE_WECHAT_MOMENT:
                                Toast.makeText(getActivity(), "分享到朋友圈", Toast.LENGTH_SHORT).show();
                                break;
                            case TAG_SHARE_WEIBO:
                                addToMyStar();
                                break;
                            case TAG_SHARE_CHAT:
//                                sendToEmail();
                                break;
                            case TAG_SHARE_LOCAL:
                                downloadFile();
                                break;
                        }
                    }
                }).build().show();
    }


    private void addToMyStar() {

        AttachRequest request = new AttachRequest();

        Observer<HttpResult> observer=new Observer<HttpResult>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HttpResult result) {
                Toasty.success(getContext().getApplicationContext(),"已收藏").show();
            }

            @Override
            public void onError(Throwable e) {
                Toasty.error(getContext().getApplicationContext(),e.getMessage()).show();
            }

            @Override
            public void onComplete() {

            }
        };

        String[] docIdArr=new String[mAdapter.getSelectedFile().size()];
        for (int i = 0; i < mAdapter.getSelectedFile().size(); i++) {
            docIdArr[i]=mAdapter.getSelectedFile().get(i).getDocumentId();
        }
        Gson gson=new Gson();
        String docIdListStr = gson.toJson(docIdArr);
        request.addDocumentToStar(observer,docIdListStr);
    }


    private void downloadFile() {

        FileDownloadManager downloadManager = FileDownloadManager.getInstance(getContext().getApplicationContext());

        List<DownloadFileEntity> selectedFile = mAdapter.getSelectedFile();
        for (DownloadFileEntity fileEntity : selectedFile) {
            downloadManager.startDownload
                    (fileEntity.getDocumentDownloadPath(),fileEntity.getDocumentName(),
                            "none",getFileUri(fileEntity.getDocumentName()));
        }
        if (selectedFile.size()!=0){
            Toasty.normal(getContext(),"开始下载文档").show();
        }
    }

    private Uri getFileUri(String fileName){
        File file=new File(Environment.getExternalStorageDirectory()+"/zbh/"+fileName);
        Uri contentUri=Uri.fromFile(file);
        return contentUri;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
