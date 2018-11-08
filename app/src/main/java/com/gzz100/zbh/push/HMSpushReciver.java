package com.gzz100.zbh.push;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.gzz100.zbh.data.eventEnity.PushUpdateEntity;
import com.gzz100.zbh.res.Common;
import com.huawei.hms.support.api.push.PushReceiver;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

/**
 * Created by Lam on 2018/1/25.
 */

public class HMSpushReciver extends PushReceiver {
    public static final String ACTION_UPDATEUI = "action.updateUI";
    public static String token;
    @Override
    public void onToken(Context context, String tokenIn, Bundle extras) {
        Logger.e("get token: token=" + tokenIn);
        String belongId = extras.getString("belongId");
        token = tokenIn;
        Common.PUSH_TOKEN = token;

        Intent intent = new Intent();
        intent.setAction(ACTION_UPDATEUI);
        intent.putExtra("log", "belongId为:" + belongId + " Token为:" + token);
        context.sendBroadcast(intent);
    }
    @Override
    public boolean onPushMsg(Context context, byte[] msg, Bundle extras) {
        try {
            //CP可以自己解析消息内容，然后做相应的处理
            String content = new String(msg, "UTF-8");
            Logger.e("收到PUSH透传消息,消息内容为:" + content);
            Logger.e("extras: "+extras.toString());
            byte[] ext = extras.getByteArray("pushMsg");
            Intent intent = new Intent();
            intent.setAction(ACTION_UPDATEUI);
            intent.putExtra("log", "收到PUSH透传消息,消息内容为:" + content);
            JSONObject jsonObject=new JSONObject(content);
            String type = (String) jsonObject.get("type");
            if (type.equals("2")){
                //加入公司通知
                EventBus.getDefault().post(new PushUpdateEntity(PushUpdateEntity.PassthrougMsgType.joinCompany));
            }else if (type.equals("3")){
                PushUpdateEntity pushUpdateEntity = new PushUpdateEntity(PushUpdateEntity.PassthrougMsgType.updateUnreadMsg);
                String meetingNum = (String) extras.get("meetingNum");
                String messageNum = (String) extras.get("messageNum");
                pushUpdateEntity.setMeetingNum(meetingNum);
                pushUpdateEntity.setMessageNum(messageNum);
                EventBus.getDefault().post(pushUpdateEntity);
            }
            context.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public void onEvent(Context context, Event event, Bundle extras) {
        if (Event.NOTIFICATION_OPENED.equals(event) || Event.NOTIFICATION_CLICK_BTN.equals(event)) {
            int notifyId = extras.getInt(BOUND_KEY.pushNotifyId, 0);
            Intent intent = new Intent();
            intent.setAction(ACTION_UPDATEUI);
            intent.putExtra("log", "收到通知栏消息点击事件,notifyId:" + notifyId);
            context.sendBroadcast(intent);
            if (0 != notifyId) {
                NotificationManager manager = (NotificationManager) context
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancel(notifyId);
            }
        }

        String message = extras.getString(BOUND_KEY.pushMsgKey);
        super.onEvent(context, event, extras);
    }
    @Override
    public void onPushState(Context context, boolean pushState) {
        Logger.i("push state="+pushState);

        Intent intent = new Intent();
        intent.setAction(ACTION_UPDATEUI);
        intent.putExtra("log", "Push连接状态为:" + pushState);
        context.sendBroadcast(intent);
    }
}
