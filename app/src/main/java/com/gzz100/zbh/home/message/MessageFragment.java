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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MessageFragment extends BaseFragment {


    @BindView(R.id.rcv_msg)
    RecyclerView mRcvMsg;
    View mRootView;
    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    protected View onCreateView(LayoutInflater inflater) {
        if (mRootView==null){
            mRootView= inflater.inflate(R.layout.fragment_message, null);
            ButterKnife.bind(this, mRootView);
        }

        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        loadData();
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
                initView(messageList);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        request.getMessages(observable,0,20);
    }

    private void initView(List<MessageEntity> messageList) {


        mRcvMsg.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
//        List<MsgEntity> msgEntities=new ArrayList<>();
//        for (int i = 0; i < 20; i++) {
//            MsgEntity msgEntity=new MsgEntity("你已加入广州市中佰信息科技有限公司,开始你的表演吧");
//            msgEntity.setKeyWord("广州市中佰信息科技有限公司");
//            msgEntity.setUpdateTime("2018/01/29 12:48");
//            msgEntities.add(msgEntity);
//        }

        MessageAdapter adapter=new MessageAdapter(messageList,getContext());
        mRcvMsg.setAdapter(adapter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
