package com.gzz100.zbh.home.message;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseFragment;
import com.gzz100.zbh.data.ObserverImpl;
import com.gzz100.zbh.data.entity.MessageEntity;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.MessageRequest;
import com.gzz100.zbh.home.message.adapter.MessageAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.FalsifyHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observer;

public class MessageFragment extends BaseFragment {

    @BindView(R.id.prl_refresh)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.rcv_msg)
    RecyclerView mRcvMsg;
    View mRootView;
    @BindView(R.id.iv_empty_msg)
    ImageView mEmptyView;
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
            public void onItemClick(int position, MessageEntity msgEntity) {
                startParentFragment(MessageDetailFragment.newInstance(msgEntity));
                mMessageList.get(position).setUnread("");
                mAdapter.notifyItemChanged(position);
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
//        mRefreshLayout.setRefreshFooter(new FalsifyFooter(getContext()));
    }

    private void loadMore() {
        MessageRequest request = new MessageRequest();
        Observer observer = new ObserverImpl<HttpResult<List<MessageEntity>>>() {

            @Override
            public void onComplete() {
                mRefreshLayout.finishLoadMore();
            }

            @Override
            protected void onResponse(HttpResult<List<MessageEntity>> result) {
                List<MessageEntity> messageList = result.getResult();
                if (messageList!=null){
                    offset +=messageList.size();
                    mMessageList.addAll(messageList);
                    mAdapter.notifyDataSetChanged();
                }

            }

            @Override
            protected void onFailure(Throwable e) {
                Toasty.error(_mActivity,e.getMessage()).show();
                mRefreshLayout.finishLoadMore();
            }
        };
        request.getMessages(observer, offset, 20);
    }



    private void loadData() {
        MessageRequest request = new MessageRequest();
        Observer<HttpResult<List<MessageEntity>>> observable = new ObserverImpl<HttpResult<List<MessageEntity>>>() {

            @Override
            public void onComplete() {
                mRefreshLayout.finishRefresh();
            }

            @Override
            protected void onResponse(HttpResult<List<MessageEntity>> result) {
                List<MessageEntity> messageList = result.getResult();
                if (messageList!=null) {
                    offset=messageList.size();
                    loadIntoView(messageList);
                }

            }

            @Override
            protected void onFailure(Throwable e) {
                Toasty.error(_mActivity,e.getMessage()).show();
                mRefreshLayout.finishRefresh(false);
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
                mEmptyView.setVisibility(View.VISIBLE);
            }else {
                mEmptyView.setVisibility(View.GONE);
            }
        }else {
            mEmptyView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
