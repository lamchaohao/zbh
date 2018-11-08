package com.gzz100.zbh.home.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseBackFragment;
import com.gzz100.zbh.data.ObserverImpl;
import com.gzz100.zbh.data.entity.MessageEntity;
import com.gzz100.zbh.data.entity.UnReadEntity;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.MessageRequest;
import com.gzz100.zbh.home.meetingadmin.fragment.MeetingParentFragment;
import com.gzz100.zbh.data.eventEnity.PushUpdateEntity;
import com.gzz100.zbh.utils.TimeFormatUtil;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.widget.QMUITopBar;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

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
    @BindView(R.id.tv_msg_tab1)
    TextView mTvMsgTab1;
    @BindView(R.id.tv_msg_tab2)
    TextView mTvMsgTab2;
    @BindView(R.id.tv_msg_tab3)
    TextView mTvMsgTab3;
    @BindView(R.id.tv_msg_tab4)
    TextView mTvMsgTab4;
    @BindView(R.id.ll_msg_extension)
    CardView mLlMsgExtension;
    private MessageEntity mMessageEntity;


    public static MessageDetailFragment newInstance(MessageEntity messageEntity) {
        MessageDetailFragment fragment = new MessageDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("msg", messageEntity);
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
        setReadMessage();
    }

    private void setReadMessage() {
        MessageRequest request = new MessageRequest();
        request.readMessage(new ObserverImpl<HttpResult>() {
            @Override
            protected void onResponse(HttpResult result) {
                Logger.i("messageID = "+mMessageEntity.getMessageId()+" 设为已读");
                loadUnread();
            }

            @Override
            protected void onFailure(Throwable e) {

            }
        },mMessageEntity.getMessageId());

    }


    private void loadUnread() {

        MessageRequest request=new MessageRequest();
        request.getUnreadCount(new ObserverImpl<HttpResult<UnReadEntity>>() {
            @Override
            protected void onResponse(HttpResult<UnReadEntity> result) {
                UnReadEntity entity = result.getResult();
                if (!TextUtils.isEmpty(entity.getMeetingUnread())) {
                    PushUpdateEntity pushUpdateEntity = new PushUpdateEntity(PushUpdateEntity.PassthrougMsgType.updateUnreadMeeting);
                    pushUpdateEntity.setMessageNum(entity.getMeetingUnread());
                    EventBus.getDefault().post(pushUpdateEntity);

                }
                if (!TextUtils.isEmpty(entity.getMessageUnread())){
                    PushUpdateEntity pushUpdateEntity = new PushUpdateEntity(PushUpdateEntity.PassthrougMsgType.updateUnreadMsg);
                    pushUpdateEntity.setMessageNum(entity.getMessageUnread());
                    EventBus.getDefault().post(pushUpdateEntity);
                }

            }

            @Override
            protected void onFailure(Throwable e) {

            }
        });

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

        if (getArguments() != null) {
            mMessageEntity = getArguments().getParcelable("msg");
            mTvMsgTitle.setText(mMessageEntity.getMessageTitle());
            mTvMsgBody.setText(mMessageEntity.getMessageDescription());

            String extraStr = mMessageEntity.getExtra();

            if (TextUtils.isEmpty(extraStr)){
                mLlMsgExtension.setVisibility(View.GONE);
                return;
            }
            try {
                JSONObject jsonObject=new JSONObject(extraStr);
                int type = jsonObject.getInt("type");
                String meetingStr = jsonObject.getString("meeting");
                Logger.i("meetingStr = "+meetingStr);
                Gson gson=new Gson();
                final MsgExtra.MeetingExtra extra = gson.fromJson(meetingStr, MsgExtra.MeetingExtra.class);
                Logger.i(extra.toString());
                if (extra!=null&&type==1){
                    mTvMsgTab1.setText( "会议名称: "+extra.meetingName);
                    mTvMsgTab2.setText( "会议地点: "+ extra.meetingPlaceName);
                    mTvMsgTab3.setText( "开始时间: "+ TimeFormatUtil.formatDateAndTime(extra.meetingStartTime));
                    mTvMsgTab4.setText( "结束时间: "+ TimeFormatUtil.formatDateAndTime(extra.meetingEndTime));
                }

                mLlMsgExtension.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mimcTopicId = extra.mimcTopicId;
                        if (mimcTopicId==null) {
                            mimcTopicId = "0";
                        }
                        startFragment(MeetingParentFragment.getNewInstance
                                (extra.meetingId,extra.meetingName, Long.parseLong(mimcTopicId)));
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().post(new FinishObject());
        unbinder.unbind();
    }
}
