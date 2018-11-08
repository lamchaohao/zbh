package com.gzz100.zbh.home.meetingadmin.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseBackFragment;
import com.gzz100.zbh.data.ObserverImpl;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.UploadRequest;
import com.gzz100.zbh.home.appointment.adapter.AddVoteAdapter;
import com.gzz100.zbh.home.appointment.entity.VoteOption;
import com.gzz100.zbh.home.appointment.entity.VoteWrap;
import com.gzz100.zbh.utils.GlideApp;
import com.gzz100.zbh.utils.MD5Utils;
import com.gzz100.zbh.utils.SuperGlideEngine;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by Lam on 2018/10/17.
 */
@RuntimePermissions
public class UpdateVoteFragment extends BaseBackFragment {
    private static final int REQUEST_CODE_CHOOSE = 256;
    @BindView(R.id.topbar)
    QMUITopBar mTopbar;
    @BindView(R.id.et_voteName)
    EditText mEtVoteName;
    @BindView(R.id.et_voteDescp)
    EditText mEtVoteDescp;
    @BindView(R.id.rcv_addVote)
    RecyclerView mRcvAddVote;
    @BindView(R.id.rl_changeMode)
    RelativeLayout mRlChangeMode;
    @BindView(R.id.tv_vote_mode)
    TextView mTvMode;

    private Unbinder unbinder;
    private VoteWrap mVoteWrap;
    private AddVoteAdapter mAdapter;
    private int mSelectPos;
    private PicHandler mHandler;
    private String mMeetingId;
    private String mVoteId;

    public static UpdateVoteFragment newInstance(VoteWrap vote,String voteId,String meetingId){
        Bundle args = new Bundle();
        UpdateVoteFragment fragment = new UpdateVoteFragment();
        args.putParcelable("vote",vote);
        args.putString("voteId",voteId);
        args.putString("meetingId",meetingId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_add_vote, null);
        unbinder = ButterKnife.bind(this, view);
        return attachToSwipeBack(view);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVar();
        initView();
        restoreData();
    }

    private void initVar() {
        if (getArguments() != null) {
            mVoteWrap = getArguments().getParcelable("vote");
            if (mVoteWrap.getOptionList()!=null){
                mEtVoteName.setText(mVoteWrap.getVoteName());
                mEtVoteDescp.setText(mVoteWrap.getVoteDespc());
            }

            mMeetingId = getArguments().getString("meetingId");
            mVoteId = getArguments().getString("voteId");


        }




    }

    private static class PicHandler extends Handler{
        private final UpdateVoteFragment updateVoteFragment;

        private PicHandler(UpdateVoteFragment fragment) {
            updateVoteFragment = fragment;
        }

        @Override
        public void handleMessage(Message msg) {
            updateVoteFragment.handleMessage(msg);
        }
    }

    public void handleMessage(Message msg){
        VoteOption option = mVoteWrap.getOptionList().get(msg.what);
        option.setPicFile(msg.getData().getString("file"));
        Logger.i("handleMessage id="+msg.what);
        mAdapter.notifyItemChanged(msg.what);
    }

    private void restoreData() {

        mHandler = new PicHandler(this);

        for (int i = 0; i < mVoteWrap.getOptionList().size(); i++) {
            final VoteOption option = mVoteWrap.getOptionList().get(i);
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        File file = GlideApp.with(getContext())
                                .downloadOnly()
                                .load(option.getPicFile())
                                .submit().get();
                        File parentFile = new File(Environment.getExternalStorageDirectory() + "/zbh/cache");
                        parentFile.mkdir();
                        String picFile = option.getPicFile();
                        String suffix = option.getPicFile().substring(picFile.lastIndexOf("."));

                        String fileMD5 = MD5Utils.getFileMD5(file);
                        String newPath =parentFile.getAbsolutePath()+"/"+fileMD5+suffix;
                        copyFile(file.getAbsolutePath(),newPath);

                        Message message = mHandler.obtainMessage();
                        Bundle bundle=new Bundle();
                        message.what=finalI;
                        bundle.putString("file",newPath);
                        message.setData(bundle);
                        mHandler.sendMessage(message);
                        Logger.i(file.getAbsolutePath());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }


    }



    public void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            File newFile = new File(newPath);
            for (File file : newFile.getParentFile().listFiles()) {
                if (file.getName().equals(newFile.getName())) {
                    Logger.i("已存在同名文件:"+file.getName());
                    return;
                }
            }
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newFile);
                byte[] buffer = new byte[1444];
                int length;
                while ( (byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
//                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void initView() {

        mTvMode.setText(mVoteWrap.isSingle()?"单选":"多选");

        Button rightButton = mTopbar.addRightTextButton("删除", R.id.buttonSave);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2018/10/17
            }
        });
        mTopbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
        mTopbar.setTitle("修改投票");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);

        mRcvAddVote.setLayoutManager(layoutManager);

        mAdapter = new AddVoteAdapter(getContext(),mVoteWrap);
        mRcvAddVote.setAdapter(mAdapter);

        mAdapter.setOnPicClickListener(new AddVoteAdapter.OnPicClickListener() {
            @Override
            public void onPicClick(int position, VoteOption option) {
                mSelectPos = position;
                UpdateVoteFragmentPermissionsDispatcher.showGallerySelectWithPermissionCheck(UpdateVoteFragment.this,position);
            }

            @Override
            public void onSubmitClick(VoteWrap voteWrap) {
                if (TextUtils.isEmpty(mEtVoteName.getText())) {
                    mEtVoteName.setError("投票名称不能为空");
                    return;
                }
                mVoteWrap = voteWrap;
                mVoteWrap.setCode(VoteWrap.CODE_UPDATE);
                mVoteWrap.setVoteName(mEtVoteName.getText().toString());
                mVoteWrap.setVoteDespc(mEtVoteDescp.getText().toString());

                updateToServer();
            }

        });


    }

    private void updateToServer() {

        UploadRequest request=new UploadRequest();
        ObserverImpl<HttpResult> observer=new ObserverImpl<HttpResult>() {
            @Override
            protected void onResponse(HttpResult result) {
                Toasty.info(_mActivity,"更新投票成功").show();

            }

            @Override
            protected void onFailure(Throwable e) {
                Toasty.info(_mActivity,e.getMessage()).show();
            }
        };
        request.updateVote(observer,mVoteWrap,mMeetingId,mVoteId);

    }


    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void showGallerySelect(int position){
        mSelectPos = position;
        Matisse.from(getActivity())
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                .countable(true)
                .maxSelectable(1)
                .imageEngine(new SuperGlideEngine())
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .forResult(REQUEST_CODE_CHOOSE);
    }

    // 向用户说明为什么需要这些权限（可选）
    @OnShowRationale({Manifest.permission.WRITE_EXTERNAL_STORAGE})
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
    @OnPermissionDenied({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showDeniedForReadFile() {
        Toast.makeText(getContext(), "用户拒绝了授权", Toast.LENGTH_SHORT).show();
    }

    // 用户勾选了“不再提醒”时调用（可选）
    @OnNeverAskAgain({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showNeverAskForReadFile() {
        Toast.makeText(getContext(), "如需开启,请在应用管理设置", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.i("call back onActivityResult requestCode" + requestCode + ",resultCode=" + resultCode);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            List<Uri> uris = Matisse.obtainResult(data);
            for (Uri uri : uris) {
                File file = uri2File(uri);
                Logger.i("Matisse = " + file.getAbsolutePath());
                Logger.i("Matisse = " + uri.getPath());
                mAdapter.setPicFile(mSelectPos, file);
            }

        }
    }

    @OnClick(R.id.rl_changeMode)
    void onSelectModeChange(View view){
        final String[] items=new String[]{"多选","单选"};
        new QMUIDialog.CheckableDialogBuilder(getContext())
                .setCheckedIndex(mVoteWrap.isSingle()?1:0)
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mVoteWrap.setSingle(which==1);
                        mAdapter.notifyDataSetChanged();
                        if (which==1){
                            mTvMode.setText("单选");
                        } else {
                            mTvMode.setText("多选");
                        }
                        dialog.dismiss();
                    }
                }).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        UpdateVoteFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    private File uri2File(Uri uri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContext().getContentResolver().query(uri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();

        return new File(res);
    }

}
