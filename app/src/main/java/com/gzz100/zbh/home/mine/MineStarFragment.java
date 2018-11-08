package com.gzz100.zbh.home.mine;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseBackFragment;
import com.gzz100.zbh.data.ObserverImpl;
import com.gzz100.zbh.data.entity.StarFileEntity;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.AttachRequest;
import com.gzz100.zbh.home.mine.adapter.StarFileAdapter;
import com.gzz100.zbh.home.root.WebViewFragment;
import com.gzz100.zbh.utils.FileDownloadManager;
import com.gzz100.zbh.utils.ShareToWeixin;
import com.qmuiteam.qmui.widget.QMUIEmptyView;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.FalsifyFooter;
import com.scwang.smartrefresh.layout.header.FalsifyHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Lam on 2018/4/24.
 */

public class MineStarFragment extends BaseBackFragment {
    @BindView(R.id.topbar)
    QMUITopBar mTopbar;
    @BindView(R.id.emptyView)
    QMUIEmptyView mEmptyView;
    @BindView(R.id.rcv_myStar)
    RecyclerView mRcvMyStar;
    @BindView(R.id.prl_refresh)
    SmartRefreshLayout mRefreshLayout;
    Unbinder unbinder;
    private AttachRequest mRequest;
    private int offset;
    private StarFileAdapter mAdapter;
    private List<StarFileEntity> mFileList;
    private ObserverImpl<HttpResult<List<StarFileEntity>>> mObserver;

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_mine_star, null);

        unbinder = ButterKnife.bind(this, view);

        return attachToSwipeBack(view);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTopbar();
        initVar();
        initRefreshLayout();
        loadData();
    }

    private void initVar() {
        mRequest = new AttachRequest();
        mFileList = new ArrayList<>();
        mRcvMyStar.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mRcvMyStar.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        mAdapter = new StarFileAdapter(mFileList,getContext());
        mRcvMyStar.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new StarFileAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos, StarFileEntity fileEntity) {
                startFragment(WebViewFragment.newInstance(fileEntity.getDocumentPath(),fileEntity.getDocumentName()));
            }

            @Override
            public void onShareClick(int pos, StarFileEntity fileEntity) {
                showShareFile(fileEntity);
            }
        });
    }

    private void showShareFile(final StarFileEntity fileEntity) {
        final int TAG_SHARE_WECHAT_FRIEND = 0;
        final int TAG_SHARE_WECHAT_MOMENT = 1;
        final int TAG_SHARE_WEIBO = 2;
        final int TAG_SHARE_CHAT = 3;
        final int TAG_SHARE_LOCAL = 4;
        QMUIBottomSheet.BottomGridSheetBuilder builder = new QMUIBottomSheet.BottomGridSheetBuilder(getActivity());
        builder.addItem(R.mipmap.icon_more_operation_share_friend, "分享到微信", TAG_SHARE_WECHAT_FRIEND, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.icon_more_operation_share_moment, "分享到朋友圈", TAG_SHARE_WECHAT_MOMENT, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.icon_more_operation_share_weibo, "取消收藏", TAG_SHARE_WEIBO, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
//                .addItem(R.mipmap.icon_more_operation_share_chat, "分享到邮箱", TAG_SHARE_CHAT, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.icon_more_operation_save, "下载到本地", TAG_SHARE_LOCAL, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomGridSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView) {
                        dialog.dismiss();
                        int tag = (int) itemView.getTag();
                        switch (tag) {
                            case TAG_SHARE_WECHAT_FRIEND:
                                ShareToWeixin.shareDocumentToWechat(_mActivity,fileEntity.getDocumentPath(),fileEntity.getDocumentName(),fileEntity.getDocumentType(),false);
                                break;
                            case TAG_SHARE_WECHAT_MOMENT:
                                ShareToWeixin.shareDocumentToWechat(_mActivity,fileEntity.getDocumentPath(),fileEntity.getDocumentName(),fileEntity.getDocumentType(),true);
                                break;
                            case TAG_SHARE_WEIBO:
                                cancleStar(fileEntity);
                                break;
                            case TAG_SHARE_CHAT:
//                                sendToEmail();
                                break;
                            case TAG_SHARE_LOCAL:
                               downloadFile(fileEntity);
                                break;
                        }
                    }
                }).build().show();
    }


    private void cancleStar(final StarFileEntity fileEntity) {
        Observer<HttpResult> observer=new Observer<HttpResult>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HttpResult result) {
                Toasty.success(getContext().getApplicationContext(),"已取消").show();
                mAdapter.removeStar(fileEntity);
            }

            @Override
            public void onError(Throwable e) {
                Toasty.error(getContext().getApplicationContext(),e.getMessage()).show();
            }

            @Override
            public void onComplete() {

            }
        };
        mRequest.deleteDocumentFromStar(observer,"["+fileEntity.getDocumentId()+"]");

    }

    private void initRefreshLayout() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                loadData();
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                loadMore();
            }
        });
        mRefreshLayout.setRefreshHeader(new FalsifyHeader(getContext()));
        mRefreshLayout.setRefreshFooter(new FalsifyFooter(getContext()));
    }

    private void loadData() {

        mObserver = new ObserverImpl<HttpResult<List<StarFileEntity>>>() {

            @Override
            public void onComplete() {
                mRefreshLayout.finishRefresh();
            }

            @Override
            protected void onResponse(HttpResult<List<StarFileEntity>> result) {
                List<StarFileEntity> fileList = result.getResult();
                if (fileList!=null){
                    offset = fileList.size();
                    mFileList.clear();
                    mFileList.addAll(fileList);
                    mAdapter.notifyDataSetChanged();
                    if (fileList.size()==0){
                        mEmptyView.show("暂无收藏","");
                        mRcvMyStar.setVisibility(View.GONE);
                    }else {
                        mEmptyView.hide();
                        mRcvMyStar.setVisibility(View.VISIBLE);
                    }
                }else {
                    mEmptyView.hide();
                }
            }

            @Override
            protected void onFailure(Throwable e) {
                mRefreshLayout.finishRefresh(false);
            }
        };

        mRequest.getStarFile(mObserver,0,20);


    }

    private void loadMore() {
        Observer<HttpResult<List<StarFileEntity>>> observer = new Observer<HttpResult<List<StarFileEntity>>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HttpResult<List<StarFileEntity>> result) {
                List<StarFileEntity> fileList = result.getResult();
                if (fileList!=null){
                    offset += fileList.size();
                    mFileList.addAll(fileList);
                    mAdapter.notifyDataSetChanged();
                }else {
                    mEmptyView.hide();
                }
            }

            @Override
            public void onError(Throwable e) {
                mRefreshLayout.finishLoadMore(false);
            }

            @Override
            public void onComplete() {
                mRefreshLayout.finishLoadMore();
            }
        };

        mRequest.getStarFile(observer,offset,10);
    }

    private void initTopbar() {
        mTopbar.setTitle("我的收藏");
        mTopbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
    }



    private void downloadFile(StarFileEntity fileEntity) {

        FileDownloadManager downloadManager = FileDownloadManager.getInstance(getContext().getApplicationContext());

        downloadManager.startDownload
                (fileEntity.getDocumentDownloadPath(),fileEntity.getDocumentName(),
                        "none",getFileUri(fileEntity.getDocumentName()));
        Toasty.normal(getContext(),"开始下载文档").show();
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
        mObserver.cancleRequest();
    }
}
