package com.gzz100.zbh.home.mine;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.gzz100.zbh.BuildConfig;
import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseBackFragment;
import com.gzz100.zbh.home.mine.adapter.FileAdapter;
import com.gzz100.zbh.utils.FragmentBackHandler;
import com.gzz100.zbh.utils.QbSdkFileUtil;
import com.gzz100.zbh.utils.ShareToWeixin;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.widget.QMUIEmptyView;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.FalsifyHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;

import static com.gzz100.zbh.res.Common.DOWNLOAD_PATH;

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
    @BindView(R.id.btn_file_move)
    Button mBtnFileMove;
    @BindView(R.id.btn_file_delete)
    Button mBtnFileDelete;
    @BindView(R.id.ll_file_bottomview)
    LinearLayout mLlFileBottomview;


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

        mTopbar.addRightImageButton(R.drawable.ic_more_horiz_grey_500_24dp, R.id.imageButtonId)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showBottomsheetView();
                    }
                });


    }



    private void initRefreshLayout() {


        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mAdapter.refresh();
                mRefreshLayout.finishRefresh(1500, true);//传入false表示刷新失败
            }
        });

        mRefreshLayout.setRefreshHeader(new FalsifyHeader(getContext()));
    }

    private void initVar() {
        mRootFile = new File(DOWNLOAD_PATH);
        if (!mRootFile.exists()) {
            mRootFile.mkdir();
        }
        mTopbar.setTitle("我的下载");
        mTopbar.setSubTitle(mRootFile.getPath());
    }


    @OnClick({R.id.btn_file_move, R.id.btn_file_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_file_move:
                if (mAdapter.isEditMode()) {
                    List<File> selectedFiles = mAdapter.getSelectedFiles();
                    mAdapter.setEditMode(false);
                    mLlFileBottomview.setVisibility(View.VISIBLE);
                }else {
                    Toasty.normal(_mActivity,"已复制").show();
                    mLlFileBottomview.setVisibility(View.GONE);
                }

                break;
            case R.id.btn_file_delete:
                comfirmDelete();
                break;
        }
    }

    private void comfirmDelete() {
        new QMUIDialog.MessageDialogBuilder(getContext())
                .setTitle("删除文件")
                .setMessage("确定要删除所选文件？")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(0, "删除", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        for (File file : mAdapter.getSelectedFiles()) {
                            deleteFile(file);
                        }
                        mAdapter.refresh();
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void deleteFile(File file){
        if (file.isDirectory()) {
            for (File childFile : file.listFiles()) {
                deleteFile(childFile);
            }
            file.delete();//空文件夹后删除
        }else {
            file.delete();
        }
    }

    private void showBottomsheetView() {
        new QMUIBottomSheet.BottomListSheetBuilder(getActivity())
                .addItem("新建文件夹")
                .addItem("编辑")
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        dialog.dismiss();
                        if (position == 0) {
                            createFolder(mAdapter.getCurrentPath());
                        } else {
                            mAdapter.setEditMode(true);
                        }

                    }
                })
                .build()
                .show();
    }

    private void createFolder(final String path) {
        //1.show dialog
        QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(getContext());
        final EditText editText = builder.getEditText();
        builder.setPlaceholder("新建文件夹")
                .setTitle("命名")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(0, "确定", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        String fileName = path + File.separator + editText.getText().toString();
                        File file = new File(fileName);
                        Logger.i(fileName);
                        if (file.mkdir()) {
                            mAdapter.refresh();
                        } else {
                            Toasty.error(_mActivity, "文件夹已存在").show();
                        }
                    }
                })
                .show();

    }

    private void initView() {

        mRcvFile.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new FileAdapter(getContext(), mRootFile);
        mRcvFile.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRcvFile.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new FileAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(File file, int position) {
                QbSdkFileUtil.openFile(getContext(),file);
            }

            @Override
            public void onItemShareClick(File file, int position) {
                showActionMenu(file);
            }

            @Override
            public void onEditModeChange(boolean isEditMode) {

                if (isEditMode) {
                    mTopbar.removeAllRightViews();
                    mTopbar.addRightTextButton("取消", R.id.imageButtonId).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mAdapter.setEditMode(false);
                        }
                    });
                    mLlFileBottomview.setVisibility(View.VISIBLE);
                } else {
                    mTopbar.removeAllRightViews();
                    mTopbar.addRightImageButton(R.drawable.ic_more_horiz_grey_500_24dp, R.id.imageButtonId)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showBottomsheetView();
                                }
                            });
                    mLlFileBottomview.setVisibility(View.GONE);
                }

            }
        });


        mAdapter.setOnItemCountChangeListener(new FileAdapter.OnItemCountChangeListener() {
            @Override
            public void onChange(int count) {
                if (count == 0) {
                    mEmptyView.show("暂无文件","");
                    mRcvFile.setVisibility(View.GONE);
                } else {
                    mEmptyView.hide();
                    mRcvFile.setVisibility(View.VISIBLE);
                }
            }
        });
        if (mAdapter.getFileList().size()==0) {
            mEmptyView.show("暂无文件","");
        }else {
            mEmptyView.hide();
        }
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
                                ShareToWeixin.shareFileToWX(getContext(), file);
                                break;
                            case TAG_SHARE_WEIBO:
                                showMoreShare(file);
                                break;
                            case TAG_SHARE_CHAT:
                                sendToEmail(file);
                                break;
                            case TAG_SHARE_LOCAL:
                                boolean delete = file.delete();
                                if (delete) {
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            contentUri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".FileProvider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendToEmail(File file) {
        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.putExtra(Intent.EXTRA_SUBJECT, file.getName());//邮件主题

        ArrayList<Uri> fileUris = new ArrayList();
        Uri contentUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            contentUri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".FileProvider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        fileUris.add(contentUri);
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, fileUris);
        intent.setType("application/pdf");
        Intent.createChooser(intent, "Choose Email Client");
        try {
            startActivity(intent);
        } catch (Exception e) {
            Toasty.error(_mActivity, e.getMessage()).show();

        }

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
