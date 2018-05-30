package com.gzz100.zbh.home.mine;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseBackFragment;
import com.gzz100.zbh.home.mine.adapter.FileAdapter;
import com.gzz100.zbh.utils.FragmentBackHandler;
import com.gzz100.zbh.utils.ShareToWeixin;
import com.qmuiteam.qmui.widget.QMUIEmptyView;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.FalsifyHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.smtt.sdk.QbSdk;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.gzz100.zbh.res.Common.AUTHORITIES;

/**
 * Created by Lam on 2018/4/24.
 */

public class MineFileFragment extends BaseBackFragment implements FragmentBackHandler {

    Unbinder unbinder;
    @BindView(R.id.topbar)
    QMUITopBar mTopbar;
    @BindView(R.id.empty_view)
    QMUIEmptyView mEmptyView;
    @BindView(R.id.rcv_file)
    RecyclerView mRcvFile;
    @BindView(R.id.prl_refresh)
    SmartRefreshLayout mRefreshLayout;

    FileAdapter mAdapter;
    File mRootFile;


    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_mine_file, null);
        unbinder = ButterKnife.bind(this, view);
        return attachToSwipeBack(view);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVar();
        initTopbar();
        initView();
        initRefreshLayout();
    }


    private void initTopbar() {
        mTopbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mAdapter.onBackPress()) {
                    pop();
                }
            }
        });
    }

    private void initView() {

        mRcvFile.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new FileAdapter(getContext(),mRootFile);
        mRcvFile.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRcvFile.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new FileAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(File file, int position) {
                HashMap<String, String> configValue = new HashMap<>();
                //“true”表示是进入打开方式选择界面，如果不设置或设置为“false” ，则进入 miniqb 浏览器模式。
                configValue.put("style", "1");
                configValue.put("local", "false");
                configValue.put("topBarBgColor", "#F5F5F5");
                QbSdk.openFileReader(getContext(), file.getAbsolutePath(), configValue, null);

            }

            @Override
            public void onItemShareClick(File file, int position) {
                showActionMenu(file);

            }
        });


        mAdapter.setOnItemCountChangeListener(new FileAdapter.OnItemCountChangeListener() {
            @Override
            public void onChange(int count) {
                if (count==0){
                    mEmptyView.show();
                }else {
                    mEmptyView.hide();
                }
            }
        });
    }

    private void showActionMenu(final File file) {
        final int TAG_SHARE_WECHAT_FRIEND = 0;
        final int TAG_SHARE_WEIBO = 2;
        final int TAG_SHARE_CHAT = 3;
        final int TAG_SHARE_LOCAL = 4;
        QMUIBottomSheet.BottomGridSheetBuilder builder = new QMUIBottomSheet.BottomGridSheetBuilder(getActivity());
        builder.addItem(R.mipmap.icon_more_operation_share_friend, "分享到微信", TAG_SHARE_WECHAT_FRIEND, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.icon_more_operation_share_chat, "邮件发送", TAG_SHARE_CHAT, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.icon_more_operation_share_weibo, "其他发送", TAG_SHARE_WEIBO, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.icon_more_operation_save, "删除文件", TAG_SHARE_LOCAL, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomGridSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView) {
                        dialog.dismiss();
                        int tag = (int) itemView.getTag();
                        switch (tag) {
                            case TAG_SHARE_WECHAT_FRIEND:
                                ShareToWeixin.shareFileToWX(getContext(),file);
                                break;
                            case TAG_SHARE_WEIBO:
                                showMoreShare(file);
                                break;
                            case TAG_SHARE_CHAT:
                                sendToEmail(file);
                                break;
                            case TAG_SHARE_LOCAL:
                                boolean delete = file.delete();
                                if (delete){
                                    mAdapter.refresh();
                                }

                                break;
                        }
                    }
                }).build().show();
    }

    private void showMoreShare(File file) {
        Intent intent = new Intent();
        Uri contentUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            contentUri = FileProvider.getUriForFile(getContext(), AUTHORITIES, file);
        } else {
            contentUri=Uri.fromFile(file);
        }

        intent.setAction(Intent.ACTION_SEND);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(contentUri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra(Intent.EXTRA_STREAM, contentUri);
        try {
            startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void sendToEmail(File file) {
        Intent intent=new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.putExtra(Intent.EXTRA_SUBJECT, file.getName());//邮件主题

        ArrayList<Uri> fileUris = new ArrayList();
        Uri contentUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            contentUri = FileProvider.getUriForFile(getContext(), AUTHORITIES, file);
        } else {
            contentUri=Uri.fromFile(file);
        }
        fileUris.add(contentUri);
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, fileUris);
        intent.setType("application/pdf");
        Intent.createChooser(intent, "Choose Email Client");
        startActivity(intent);
    }




    private void initRefreshLayout() {


        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mAdapter.refresh();
                mRefreshLayout.finishRefresh(1500,true);//传入false表示刷新失败
            }
        });

        mRefreshLayout.setRefreshHeader(new FalsifyHeader(getContext()));
    }

    private void initVar() {
        mRootFile = new File(Environment.getExternalStorageDirectory() + "/zbh");
        if (!mRootFile.exists()) {
            mRootFile.mkdir();
        }
        mTopbar.setTitle("我的文件");
        mTopbar.setSubTitle(mRootFile.getPath());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public boolean onBackPressed() {

        return mAdapter.onBackPress();
    }
}
