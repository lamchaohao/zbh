package com.gzz100.zbh.home.appointment.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseBackFragment;
import com.gzz100.zbh.data.ObserverImpl;
import com.gzz100.zbh.data.eventEnity.FileInfoEntity;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.ProgressSubscriber;
import com.gzz100.zbh.data.network.SubscriberOnNextListener;
import com.gzz100.zbh.data.network.request.UploadRequest;
import com.gzz100.zbh.home.appointment.adapter.SelectedDocsAdapter;
import com.gzz100.zbh.home.appointment.adapter.VoteListAdapter;
import com.gzz100.zbh.home.appointment.entity.VoteWrap;
import com.gzz100.zbh.utils.QbSdkFileUtil;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.widget.QMUITopBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import droidninja.filepicker.FilePickerConst;
import es.dmoral.toasty.Toasty;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by Lam on 2018/1/31.
 *
 */
@RuntimePermissions
public class AddFileVoteFragment extends BaseBackFragment {
    @BindView(R.id.topbar)
    QMUITopBar mTopbar;
    @BindView(R.id.tv_addFile)
    TextView mTvAddFile;
    @BindView(R.id.rcv_selectedDocs)
    RecyclerView mRcvSelectDoc;
    @BindView(R.id.rl_vote)
    RelativeLayout mRlVote;
    @BindView(R.id.rcv_addedVote)
    RecyclerView mRcvAddedVote;

    @BindView(R.id.bt_next_detail)
    Button mBtNextDetail;
    Unbinder unbinder;
    private int mVotePosition;
    private String mMeetingId;
    private List<File> docFileList;
    private List<VoteWrap> mVoteWrapList;
    private VoteListAdapter mVoteListAdapter;
    private SelectedDocsAdapter mAdapter;
    private ArrayList<String> mDocPaths;
    private boolean fileUploadComplete;
    private boolean voteComplete;
    private ObserverImpl<HttpResult> mUploadVoteObserver;
    private ProgressSubscriber<HttpResult> mUploadFilesubscriber;

    public static AddFileVoteFragment newInstance(String meetingId) {
        Bundle args = new Bundle();
        AddFileVoteFragment fragment = new AddFileVoteFragment();
        args.putString("meetingId", meetingId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_add_file_vote, null);
        unbinder = ButterKnife.bind(this, view);
        initVar();
        initView();
        EventBus.getDefault().register(this);
        return attachToSwipeBack(view);
    }

    private void initVar() {
        if (getArguments()!=null) {
            mMeetingId = getArguments().getString("meetingId");
        }
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
        mRcvSelectDoc.setLayoutManager(
                new GridLayoutManager(getContext(),1,
                GridLayoutManager.HORIZONTAL,false));
        mAdapter = new SelectedDocsAdapter(getContext(),docFileList);
        mRcvSelectDoc.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new SelectedDocsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(File file) {
                if (file==null){
                    AddFileVoteFragmentPermissionsDispatcher.showSelectFileWithPermissionCheck(AddFileVoteFragment.this);
                }else {
                    QbSdkFileUtil.openFile(_mActivity,file);
                }

            }

            @Override
            public void onItemClose(int position) {
                mDocPaths.remove(position);
                docFileList.remove(position);
                mAdapter.notifyDataSetChanged();
            }
        });
        mVoteWrapList = new ArrayList<>();
        mRcvAddedVote.setLayoutManager( new GridLayoutManager(getContext(),1,
                GridLayoutManager.HORIZONTAL,false));
        mVoteListAdapter = new VoteListAdapter(getContext(),mVoteWrapList);
        mRcvAddedVote.setAdapter(mVoteListAdapter);
        mVoteListAdapter.setOnItemClickListener(new VoteListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos,VoteWrap voteWrap) {
                mVotePosition = pos;
                startFragment(AddVoteFragment.newInstance(voteWrap));
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        AddFileVoteFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE})
    public void showSelectFile(){
        startFragment(SelectFileFragment.newInstance(mDocPaths));
//        FilePickerBuilder.getInstance().setMaxCount(10)
//                .setActivityTheme(R.style.LibAppTheme)
//                .setSelectedFiles(mDocPaths)
//                .pickFile(this);
    }

    // 向用户说明为什么需要这些权限（可选）
    @OnShowRationale({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE})
    void showRationaleForReadFile(final PermissionRequest request) {
        new AlertDialog.Builder(getContext())
                .setMessage("需要开启读写外部存储权限才能浏览文件")
                .setPositiveButton("允许", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .show();
    }

    // 用户拒绝授权回调（可选）
    @OnPermissionDenied({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE})
    void showDeniedForReadFile() {
        Toast.makeText(getContext(), "用户拒绝了授权", Toast.LENGTH_SHORT).show();
    }

    // 用户勾选了“不再提醒”时调用（可选）
    @OnNeverAskAgain({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE})
    void showNeverAskForReadFile() {
        Toast.makeText(getContext(), "需要开启时请前往手机应用设置开启权限", Toast.LENGTH_LONG).show();
    }

    @OnClick({R.id.tv_addFile, R.id.rl_vote, R.id.bt_next_detail})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_addFile:
                AddFileVoteFragmentPermissionsDispatcher.showSelectFileWithPermissionCheck(this);
                break;
            case R.id.rl_vote:
//               startFragment(new SelectVoteModeFragment());
                VoteWrap voteWrap =new VoteWrap();
                voteWrap.setSingle(true);
                startFragment(AddVoteFragment.newInstance(voteWrap));

                break;
            case R.id.bt_next_detail:
                uploadFile();
                uploadVote();
                break;
        }
    }

    private void uploadVote() {

        UploadRequest request = new UploadRequest();
        mUploadVoteObserver = new ObserverImpl<HttpResult>() {
            int completeCount = 0;

            @Override
            protected void onResponse(HttpResult result) {
                completeCount++;
                Logger.i("complete=="+completeCount);
                if (completeCount==mVoteWrapList.size()){
                    voteComplete=true;
                    if (fileUploadComplete){
                        pop();
                    }
                }
            }

            @Override
            protected void onFailure(Throwable e) {
                Toasty.error(_mActivity,e.getMessage()).show();
            }
        };

        for (VoteWrap voteWrap : mVoteWrapList) {
            request.uploadVoteList(mUploadVoteObserver,voteWrap,mMeetingId);
        }

    }

    private void uploadFile() {
        final UploadRequest request = new UploadRequest();
        SubscriberOnNextListener<HttpResult> subscriberOnNextListener = new SubscriberOnNextListener<HttpResult>() {
            @Override
            public void onNext(HttpResult result) {
                Logger.i("SubscriberOnNextListener == "+request.toString());
            }
        };
        mUploadFilesubscriber = new ProgressSubscriber<HttpResult>(subscriberOnNextListener,getContext()) {
            @Override
            public void onNext(HttpResult result) {
                super.onNext(result);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                Logger.e("onError="+e.getMessage());
            }

            @Override
            public void onComplete() {
                super.onComplete();
                fileUploadComplete=true;
                if (voteComplete){
                    pop();
                }
                Toasty.info(getContext(),"文件上传成功").show();

            }
        };

        request.uploadFileList(mUploadFilesubscriber,docFileList,mMeetingId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveVote(VoteWrap voteWrap){
        switch (voteWrap.getCode()) {
            case VoteWrap.CODE_DELETE://delete
                mVoteWrapList.remove(mVotePosition);
                break;
            case VoteWrap.CODE_ADD://add
                mVoteWrapList.add(voteWrap);
                break;
            case VoteWrap.CODE_UPDATE://update
                mVoteWrapList.set(mVotePosition,voteWrap);
                break;
        }
        mVoteListAdapter.notifyDataSetChanged();
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
        if (mUploadVoteObserver!=null) {
            mUploadVoteObserver.cancleRequest();
        }
        if (mUploadFilesubscriber!=null) {
            mUploadFilesubscriber.cancleRequest();
        }
    }

}
