package com.gzz100.zbh.home.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseBackFragment;
import com.gzz100.zbh.data.entity.MessageEntity;
import com.qmuiteam.qmui.widget.QMUITopBar;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Lam on 2018/4/16.
 */

public class MessageDetailFragment extends BaseBackFragment {
    @BindView(R.id.topbar)
    QMUITopBar mTopbar;
    @BindView(R.id.tv_msg_title)
    TextView mTvMsgTitle;
    @BindView(R.id.tv_msg_body)
    TextView mTvMsgBody;
    Unbinder unbinder;
    private MessageEntity mMessageEntity;


    public static MessageDetailFragment newInstance(MessageEntity messageEntity){
        MessageDetailFragment fragment = new MessageDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("msg",messageEntity);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_message_detail, null);

        unbinder = ButterKnife.bind(this, view);
        return attachToSwipeBack(view);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        mTopbar.setTitle("消息详情");
        mTopbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new FinishObject());
                pop();
            }
        });

        if (getArguments()!=null) {
            mMessageEntity = getArguments().getParcelable("msg");
            mTvMsgTitle.setText(mMessageEntity.getMessageTitle());
            mTvMsgBody.setText(mMessageEntity.getMessageDescription());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().post(new FinishObject());
        unbinder.unbind();
    }
}
