package com.gzz100.zbh.push;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.gzz100.zbh.home.message.MsgActivity;
import com.gzz100.zbh.data.eventEnity.PushUpdateEntity;
import com.gzz100.zbh.res.Common;
import com.orhanobut.logger.Logger;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;

/**
 * 小米的推送
 * Created by Lam on 2018/1/23.
 */

public class PushReceiver extends PushMessageReceiver {
    private String mRegId;
    private long mResultCode = -1;
    private String mReason;
    private String mCommand;
    private String mMessage;
    private String mTopic;
    private String mAlias;
    private String mUserAccount;
    private String mStartTime;
    private String mEndTime;

    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage message) {
        Logger.i("PassThroughMessage message.titile ="+message.getTitle()+",message="+message.toString());
        mMessage = message.getContent();
        if(!TextUtils.isEmpty(message.getTopic())) {
            mTopic=message.getTopic();
        } else if(!TextUtils.isEmpty(message.getAlias())) {
            mAlias=message.getAlias();
        } else if(!TextUtils.isEmpty(message.getUserAccount())) {
            mUserAccount=message.getUserAccount();
        }
        if (message.getExtra()==null) {
            Logger.i("message.getExtra()");
            return;
        }
        String type = message.getExtra().get("type");
        if (type.equals("2")){
            EventBus.getDefault().post(new PushUpdateEntity(PushUpdateEntity.PassthrougMsgType.joinCompany));
        }else if (type.equals("3")){
            PushUpdateEntity pushUpdateEntity = new PushUpdateEntity(PushUpdateEntity.PassthrougMsgType.updateUnreadMsg);
            String meetingNum = message.getExtra().get("meetingNum");
            String messageNum = message.getExtra().get("messageNum");
            pushUpdateEntity.setMeetingNum(meetingNum);
            pushUpdateEntity.setMessageNum(messageNum);
            EventBus.getDefault().post(pushUpdateEntity);
        }
        EventBus.getDefault().post(new PushUpdateEntity(PushUpdateEntity.PassthrougMsgType.updateMeetingReceive));

    }
    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage message) {
        mMessage = message.getContent();
        if(!TextUtils.isEmpty(message.getTopic())) {
            mTopic=message.getTopic();
        } else if(!TextUtils.isEmpty(message.getAlias())) {
            mAlias=message.getAlias();
        } else if(!TextUtils.isEmpty(message.getUserAccount())) {
            mUserAccount=message.getUserAccount();
        }
        Logger.i("message.titile ="+message.getTitle()+",message="+message.toString());
        Map<String, String> extra = message.getExtra();
        String meetingId = extra.get("meetingId");
        if (!TextUtils.isEmpty(meetingId)) {
            String meetingName = extra.get("meetingName");
            String meetingPlaceName = extra.get("meetingPlaceName");
            String meetingStartTime = extra.get("meetingStartTime");
            String meetingEndTime = extra.get("meetingEndTime");
            Logger.i(meetingId+","+meetingName+","+meetingPlaceName+","+meetingStartTime+","+meetingEndTime);
        }

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String title = message.getTitle();
        String description = message.getDescription();
        intent.putExtra("title",title);
        intent.putExtra("description",description);
        intent.setClass(context, MsgActivity.class);
        context.startActivity(intent);
    }
    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage message) {
        mMessage = message.getContent();
        if(!TextUtils.isEmpty(message.getTopic())) {
            mTopic=message.getTopic();
        } else if(!TextUtils.isEmpty(message.getAlias())) {
            mAlias=message.getAlias();
        } else if(!TextUtils.isEmpty(message.getUserAccount())) {
            mUserAccount=message.getUserAccount();
        }
    }
    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_UNSET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mTopic = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mTopic = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SET_ACCEPT_TIME.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mStartTime = cmdArg1;
                mEndTime = cmdArg2;
            }
        }
    }
    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
                Common.PUSH_TOKEN=mRegId;
                Logger.i("xiaomi push regId="+mRegId);
            }
        }
    }
}
