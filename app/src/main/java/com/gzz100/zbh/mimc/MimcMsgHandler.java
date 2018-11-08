package com.gzz100.zbh.mimc;

import android.annotation.SuppressLint;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.xiaomi.mimc.MIMCGroupMessage;
import com.xiaomi.mimc.MIMCMessage;
import com.xiaomi.mimc.MIMCMessageHandler;
import com.xiaomi.mimc.MIMCServerAck;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Lam on 2018/5/7.
 */

public class MimcMsgHandler implements MIMCMessageHandler {

    @SuppressLint("UseSparseArrays")
    private List<OnHandlerMimcGroupMsgListener> mReceiveMsgListenerList=new ArrayList<>();
    private String mAccountId;
    private Gson mGson=new Gson();

    public MimcMsgHandler(String accountId) {
        this.mAccountId = accountId;
    }

    @Override
    public void handleMessage(List<MIMCMessage> list) {

    }

    /**
     * 接收群聊消息
     * MIMCGroupMessage类
     * String packetId 消息ID
     * long groupId 群ID
     * long sequence 序列号
     * String fromAccount 发送方帐号
     * byte[] payload 消息体
     * long timestamp 时间戳
     */
    @Override
    public void handleGroupMessage(List<MIMCGroupMessage> packets) {
        Logger.i("handleGroupMessage packets.size="+packets.size());
        for (int i = 0; i < packets.size(); i++) {
            MIMCGroupMessage mimcGroupMessage = packets.get(i);
            if (!mAccountId.equals(mimcGroupMessage.getFromAccount())) {
                String payload = new String(mimcGroupMessage.getPayload());
                Logger.i("receive:"+payload);
                BaseMCMsg msg=null;
                if (payload.contains("version")&&payload.contains("msgBody")&&payload.contains("timestamp")){
                    msg = mGson.fromJson(new String(mimcGroupMessage.getPayload()), BaseMCMsg.class);
                }else {
                    return;
                }


                switch (msg.getMsgType()) {
                    case Constant.TEXT:
                        TextMsg textMsg = new TextMsg(msg.getMsgBody());
                        textMsg.setFromAccount(mimcGroupMessage.getFromAccount());
                        textMsg.setGroupId(mimcGroupMessage.getGroupId());

                        for (OnHandlerMimcGroupMsgListener onHandlerMimcGroupMsgListener : mReceiveMsgListenerList) {
                            onHandlerMimcGroupMsgListener.onGroupTextMsgReceived(textMsg);
                        }
                        break;
                    case Constant.SYNC_CANVAS:
                        Type type = new TypeToken<List<SyncCanvasBean>>() {}.getType();
                        List<SyncCanvasBean> syncPointList= mGson.fromJson(msg.getMsgBody(), type);
                        for (SyncCanvasBean syncCanvasBean : syncPointList) {
                            syncCanvasBean.setGroupId(mimcGroupMessage.getGroupId());
                            syncCanvasBean.setFromAccount(mimcGroupMessage.getFromAccount());
                        }
                        for (OnHandlerMimcGroupMsgListener onHandlerMimcGroupMsgListener : mReceiveMsgListenerList) {
                            onHandlerMimcGroupMsgListener.onCanvasMsgReceived(syncPointList);
                        }
                        break;
                    case Constant.SYNC_DOCUMENT:
                        SyncDocumentBean syncDoc = mGson.fromJson(msg.getMsgBody(), SyncDocumentBean.class);
                        syncDoc.setGroupId(mimcGroupMessage.getGroupId());
                        syncDoc.setFromAccount(mimcGroupMessage.getFromAccount());
                        for (OnHandlerMimcGroupMsgListener onHandlerMimcGroupMsgListener : mReceiveMsgListenerList) {
                            onHandlerMimcGroupMsgListener.onDocumentReceived(syncDoc);
                        }
                        break;
                    case Constant.SYNC_MEETING:
                        SyncMeetingBean SyncMeeting = mGson.fromJson(msg.getMsgBody(), SyncMeetingBean.class);
                        SyncMeeting.setGroupId(mimcGroupMessage.getGroupId());
                        SyncMeeting.setFromAccount(mimcGroupMessage.getFromAccount());
                        for (OnHandlerMimcGroupMsgListener onHandlerMimcGroupMsgListener : mReceiveMsgListenerList) {
                            onHandlerMimcGroupMsgListener.onMeetingActionReceived(SyncMeeting);
                        }
                        break;
                }
            }
        }
    }

    @Override
    public void handleServerAck(MIMCServerAck mimcServerAck) {

    }

    @Override
    public void handleSendMessageTimeout(MIMCMessage mimcMessage) {

    }

    @Override
    public void handleSendGroupMessageTimeout(MIMCGroupMessage mimcGroupMessage) {
        for (OnHandlerMimcGroupMsgListener onHandlerMimcGroupMsgListener : mReceiveMsgListenerList) {
            onHandlerMimcGroupMsgListener.onSendGroupMsgTimeout(mimcGroupMessage);
        }
    }

    public void addMsgListener(OnHandlerMimcGroupMsgListener listener){
        mReceiveMsgListenerList.add(listener);
    }

    public void removeListener(OnHandlerMimcGroupMsgListener listener){
        mReceiveMsgListenerList.remove(listener);
    }


    public static interface OnHandlerMimcGroupMsgListener{
        void onGroupTextMsgReceived(TextMsg textMsg);
        void onCanvasMsgReceived(List<SyncCanvasBean> syncCanvasBean);
        void onDocumentReceived(SyncDocumentBean syncDocumentBean);
        void onMeetingActionReceived(SyncMeetingBean syncMeetingBean);
        void onSendGroupMsgTimeout(MIMCGroupMessage mimcGroupMessage);
    }


}
