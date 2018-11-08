package com.gzz100.zbh.home.meetingadmin.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseBackFragment;
import com.gzz100.zbh.data.ObserverImpl;
import com.gzz100.zbh.data.entity.DocumentEntity;
import com.gzz100.zbh.data.eventEnity.FileInfoEntity;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.DocumentRequest;
import com.gzz100.zbh.data.network.request.UploadRequest;
import com.gzz100.zbh.home.appointment.adapter.SelectedDocsAdapter;
import com.gzz100.zbh.home.appointment.fragment.SelectFileFragment;
import com.gzz100.zbh.home.meetingadmin.adapter.CloudFileAdapter;
import com.gzz100.zbh.home.root.WebViewFragment;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.tencent.smtt.sdk.QbSdk;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import droidninja.filepicker.FilePickerConst;
import es.dmoral.toasty.Toasty;
import io.reactivex.disposables.Disposable;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.qmuiteam.qmui.widget.dialog.QMUITipDialog.Builder.ICON_TYPE_SUCCESS;

@RuntimePermissions
public class ManageFileFragment extends BaseBackFragment {


    @BindView(R.id.topbar)
    QMUITopBar mTopbar;
    @BindView(R.id.rcv_selectedDocs)
    RecyclerView mRcvSelectedDocs;
    @BindView(R.id.rcv_existDocs)
    RecyclerView mRcvExistDocs;
    Unbinder unbinder;

    private String mMeetingId;
    private List<File> docFileList;
    private SelectedDocsAdapter mAdapter;
    private ArrayList<String> mDocPaths;
    private List<DocumentEntity> mDocumentEntityList;
    private QMUITipDialog mUploadDialog;
    private QMUITipDialog mCompleteDialog;
    private UploadRequest mRequest;
    private CloudFileAdapter mCloudFileAdapter;
    private ObserverImpl<HttpResult<List<DocumentEntity>>> mDataObserver;
    private ObserverImpl<HttpResult> mUploadFileObserver;

    public static ManageFileFragment newInstance(String meetingId) {
        Bundle args = new Bundle();
        ManageFileFragment fragment = new ManageFileFragment();
        args.putString("meetingId", meetingId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_manage_file, null);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return attachToSwipeBack(view);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initVar();
        initView();
        initTipDialog();
        loadCloudFile();

    }

    private void initTipDialog() {
        mUploadDialog = new QMUITipDialog.Builder(getContext())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在上传")
                .create();

        mCompleteDialog = new QMUITipDialog.Builder(getContext())
                .setIconType(ICON_TYPE_SUCCESS)
                .setTipWord("上传完成")
                .create();
    }


    private void initVar() {
        mRequest = new UploadRequest();
        if (getArguments()!=null) {
            mMeetingId = getArguments().getString("meetingId");
        }
        mTopbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
    }

    private void initView() {
        mTopbar.setTitle("添加附件");
        mTopbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
        docFileList =new ArrayList<>();
        mRcvSelectedDocs.setLayoutManager(
                new GridLayoutManager(getContext(),1,
                        GridLayoutManager.HORIZONTAL,false));
        mAdapter = new SelectedDocsAdapter(getContext(),docFileList);
        mRcvSelectedDocs.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new SelectedDocsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(File file) {
                if (file==null){
                    ManageFileFragmentPermissionsDispatcher.showSelectFileWithPermissionCheck(ManageFileFragment.this);
                }else {
                    HashMap<String,String> configValue = new HashMap<>();
                    //“true”表示是进入打开方式选择界面，如果不设置或设置为“false” ，则进入 miniqb 浏览器模式。
                    configValue.put("style","1");
                    configValue.put("local","false");
                    configValue.put("topBarBgColor","#607D8B");
                    QbSdk.openFileReader(getContext(),file.getAbsolutePath(),configValue,null);
                }

            }

            @Override
            public void onItemClose(int position) {
                mDocPaths.remove(position);
                docFileList.remove(position);
                mAdapter.notifyDataSetChanged();
            }
        });

    }




    private void loadCloudFile() {
        DocumentRequest request=new DocumentRequest();
        mDocumentEntityList=new ArrayList<>();
        mDataObserver = new ObserverImpl<HttpResult<List<DocumentEntity>>>() {

            @Override
            protected void onResponse(HttpResult<List<DocumentEntity>> documentResult) {
                if (documentResult.getResult()!=null){
                    mDocumentEntityList.clear();
                    mDocumentEntityList.addAll(documentResult.getResult());
                }
                initExistDocView();
            }

            @Override
            protected void onFailure(Throwable e) {
                Toasty.error(_mActivity, e.getMessage()).show();
            }
        };

        request.getDocumentList(mDataObserver, mMeetingId);
    }


    private void initExistDocView() {
        mRcvExistDocs.setLayoutManager(
                new GridLayoutManager(getContext(),2,
                        GridLayoutManager.HORIZONTAL,true));

        mCloudFileAdapter = new CloudFileAdapter(getContext(),mDocumentEntityList);
        mRcvExistDocs.setAdapter(mCloudFileAdapter);

        mCloudFileAdapter.setOnItemClickListener(new CloudFileAdapter.OnItemClickListener() {
            @Override
            public void onRemoveClick(int position, DocumentEntity documentEntity) {
                deleteFileById(position,documentEntity);
            }

            @Override
            public void onItemClick(int position, DocumentEntity documentEntity) {
                startFragment(WebViewFragment.newInstance(documentEntity.getDocumentPath(),documentEntity.getDocumentName()));
            }
        });

    }


    private void deleteFileById(final int position, DocumentEntity documentEntity){

        ObserverImpl<HttpResult> observer=new ObserverImpl<HttpResult>() {

            @Override
            protected void onResponse(HttpResult result) {
                mDocumentEntityList.remove(position);
                mCloudFileAdapter.notifyItemRemoved(position);
                mCloudFileAdapter.notifyDataSetChanged();
                final QMUITipDialog dialog=new QMUITipDialog.Builder(getContext())
                        .setTipWord("已删除")
                        .setIconType(ICON_TYPE_SUCCESS)
                        .create();
                dialog.show();
                mRcvExistDocs.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                },1000);
            }

            @Override
            protected void onFailure(Throwable e) {
                Toasty.error(_mActivity, e.getMessage()).show();
            }
        };
        mRequest.deleteFile(observer,mMeetingId,documentEntity.getDocumentId());
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSelectedResult(FileInfoEntity fileInfoEntity){
        if (fileInfoEntity.getFileNameList()!=null) {
            mDocPaths = new ArrayList<>();
            docFileList.clear();
            mDocPaths.addAll(fileInfoEntity.getFileNameList());
            for (String docPath : mDocPaths) {
                File file = new File(docPath);
                docFileList.add(file);
            }
            mAdapter.notifyDataSetChanged();
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FilePickerConst.REQUEST_CODE_DOC:
                if(resultCode== Activity.RESULT_OK && data!=null) {
                    mDocPaths = new ArrayList<>();
                    docFileList.clear();
                    mDocPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));
                    for (String docPath : mDocPaths) {
                        File file = new File(docPath);
                        docFileList.add(file);
                    }
                    mAdapter.notifyDataSetChanged();
                }
                break;
        }

    }


    @SuppressLint("NeedOnRequestPermissionsResult")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ManageFileFragmentPermissionsDispatcher.onRequestPermissionsResult(this,requestCode,grantResults);
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE})
    public void showSelectFile(){


        final int TAG_OPEN_PICKER=1;
        final int TAG_OPEN_USUAL=2;
        QMUIBottomSheet.BottomGridSheetBuilder builder = new QMUIBottomSheet.BottomGridSheetBuilder(getActivity());
        builder.addItem(R.mipmap.icon_more_operation_share_friend, "常用文件夹", TAG_OPEN_USUAL, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.icon_more_operation_share_moment, "打开文件选择器", TAG_OPEN_PICKER, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomGridSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView) {
                        dialog.dismiss();
                        String type=null;

                        int tag = (int) itemView.getTag();
                        switch (tag) {
                            case TAG_OPEN_USUAL:
//                                FilePickerBuilder.getInstance().setMaxCount(10)
//                                    .setActivityTheme(R.style.LibAppTheme)
//                                    .setSelectedFiles(mDocPaths)
//                                    .enableSelectAll(true)
//                                    .enableImagePicker(true)
//                                    .pickFile(_mActivity);
//                                startFragment(LatestFileFragment.newInstance(mDocPaths));
                                break;
                            case TAG_OPEN_PICKER:
                                startFragment(SelectFileFragment.newInstance(mDocPaths));
                                break;
                        }
                    }
                }).build().show();

    }


    @OnClick({R.id.tv_addFile,R.id.btn_upload})
    public void onViewClicked(View view) {
        if (view.getId()==R.id.btn_upload){
            uploadFiles();
        }else {
            ManageFileFragmentPermissionsDispatcher.showSelectFileWithPermissionCheck(this);
        }
    }

    private void uploadFiles() {

        mUploadFileObserver = new ObserverImpl<HttpResult>() {
            @Override
            public void onSubscribe(Disposable d) {
                mUploadDialog.show();
            }

            @Override
            protected void onResponse(HttpResult result) {
                mUploadDialog.dismiss();
                mCompleteDialog.show();
                mRcvSelectedDocs.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mCompleteDialog.dismiss();
                    }
                }, 500);

                loadCloudFile();
            }

            @Override
            protected void onFailure(Throwable e) {
                mUploadDialog.dismiss();
                Toasty.error(_mActivity,e.getMessage()).show();
            }
        };

        mRequest.uploadFileList(mUploadFileObserver,docFileList,mMeetingId);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
        if (mDataObserver!=null) {
            mDataObserver.cancleRequest();
        }

        if (mUploadFileObserver!=null) {
            mUploadFileObserver.cancleRequest();
        }
    }

}
