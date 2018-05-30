package com.gzz100.zbh.home.message;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseFragment;
import com.gzz100.zbh.data.entity.MessageEntity;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.MessageRequest;
import com.gzz100.zbh.home.message.adapter.MessageAdapter;
import com.qmuiteam.qmui.widget.QMUIEmptyView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.FalsifyFooter;
import com.scwang.smartrefresh.layout.header.FalsifyHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MessageFragment extends BaseFragment {

    @BindView(R.id.prl_refresh)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.rcv_msg)
    RecyclerView mRcvMsg;
    View mRootView;
    @BindView(R.id.empty_view)
    QMUIEmptyView mEmptyView;
    Unbinder unbinder;
    private List<MessageEntity> mMessageList;
    private MessageAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private int offset=0;


    @Override
    protected View onCreateView(LayoutInflater inflater) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_message, null);
            ButterKnife.bind(this, mRootView);
        }

        unbinder = ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView();
        loadData();

    }


    private void initView() {
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        mRcvMsg.setLayoutManager(mLayoutManager);

        mMessageList = new ArrayList<>();
        mAdapter = new MessageAdapter(mMessageList, getContext());
        mRcvMsg.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new MessageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, MessageEntity msgEntity) {
                startParentFragment(MessageDetailFragment.newInstance(msgEntity));
            }
        });

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

    private void loadMore() {
        MessageRequest request = new MessageRequest();
        Observer<HttpResult<List<MessageEntity>>> observable = new Observer<HttpResult<List<MessageEntity>>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HttpResult<List<MessageEntity>> result) {
                List<MessageEntity> messageList = result.getResult();
                offset +=messageList.size();
                mMessageList.addAll(messageList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {
                mRefreshLayout.finishLoadMore();
            }

            @Override
            public void onComplete() {
                mRefreshLayout.finishLoadMore();
            }
        };
        request.getMessages(observable, offset, 20);
    }



    private void loadData() {
        MessageRequest request = new MessageRequest();
        Observer<HttpResult<List<MessageEntity>>> observable = new Observer<HttpResult<List<MessageEntity>>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HttpResult<List<MessageEntity>> result) {
                List<MessageEntity> messageList = result.getResult();
                offset=messageList.size();
                loadIntoView(messageList);
            }

            @Override
            public void onError(Throwable e) {
                mRefreshLayout.finishRefresh(false);
            }

            @Override
            public void onComplete() {
                mRefreshLayout.finishRefresh();
            }
        };
        request.getMessages(observable, 0, 20);
    }

    private void loadIntoView(List<MessageEntity> messageList) {

        if (messageList != null) {
            mMessageList.clear();
            mMessageList.addAll(messageList);
            mAdapter.notifyDataSetChanged();
            if (mMessageList.size()==0) {
                mEmptyView.show();
            }else {
                mEmptyView.hide();
            }
        }else {
            mEmptyView.show();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
