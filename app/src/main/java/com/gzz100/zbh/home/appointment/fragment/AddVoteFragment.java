package com.gzz100.zbh.home.appointment.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseBackFragment;
import com.gzz100.zbh.home.appointment.adapter.AddVoteAdapter;
import com.gzz100.zbh.home.appointment.entity.VoteOption;
import com.gzz100.zbh.home.appointment.entity.VoteWrap;
import com.gzz100.zbh.utils.SuperGlideEngine;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by Lam on 2018/2/8.
 */
@RuntimePermissions
public class AddVoteFragment extends BaseBackFragment {
    private static final int REQUEST_CODE_CHOOSE = 256;
    @BindView(R.id.topbar)
    QMUITopBar mTopbar;
    Unbinder unbinder;
    @BindView(R.id.et_voteName)
    EditText mEtVoteName;
    @BindView(R.id.et_voteDescp)
    EditText mEtVoteDescp;
    @BindView(R.id.rcv_addVote)
    RecyclerView mRcvAddVote;

    private VoteWrap mVoteWrap;
    private AddVoteAdapter mAdapter;
    private int mSelectPos;
    private boolean isNewInstance;

    public static AddVoteFragment newInstance(VoteWrap vote){
        Bundle args = new Bundle();
        AddVoteFragment fragment = new AddVoteFragment();
        args.putParcelable("vote",vote);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_add_vote, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return attachToSwipeBack(view);
    }

    private void initView() {
        if (getArguments() != null) {
            mVoteWrap = getArguments().getParcelable("vote");
            if (mVoteWrap.getOptionList()==null){
                isNewInstance = true;
            }else {
                mEtVoteName.setText(mVoteWrap.getVoteName());
                mEtVoteDescp.setText(mVoteWrap.getVoteDespc());
            }
        }
        Button rightButton = mTopbar.addRightTextButton("删除", R.id.buttonSave);
        rightButton.setTextColor(Color.WHITE);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVoteWrap.setCode(VoteWrap.CODE_DELETE);
                EventBus.getDefault().post(mVoteWrap);
                pop();
            }
        });
        mTopbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
        mTopbar.setTitle("添加投票");

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
                AddVoteFragmentPermissionsDispatcher.showGallerySelectWithPermissionCheck(AddVoteFragment.this,position);
//                showGallerySelect(position);
            }

            @Override
            public void onSubmitClick(VoteWrap voteWrap) {
                if (TextUtils.isEmpty(mEtVoteName.getText())) {
                    mEtVoteName.setError("投票名称不能为空");
                    return;
                }
                mVoteWrap = voteWrap;
                if (isNewInstance) {
                    mVoteWrap.setCode(VoteWrap.CODE_ADD);
                }else {
                    mVoteWrap.setCode(VoteWrap.CODE_UPDATE);
                }
                mVoteWrap.setVoteName(mEtVoteName.getText().toString());
                mVoteWrap.setVoteDespc(mEtVoteDescp.getText().toString());
                EventBus.getDefault().post(mVoteWrap);
                pop();
            }

        });

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



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        AddVoteFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
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
        Toast.makeText(getContext(), "用户拒绝了授权并向你扔了一条狗", Toast.LENGTH_SHORT).show();
    }

    // 用户勾选了“不再提醒”时调用（可选）
    @OnNeverAskAgain({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showNeverAskForReadFile() {
        Toast.makeText(getContext(), "用户把你打入冷宫", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
