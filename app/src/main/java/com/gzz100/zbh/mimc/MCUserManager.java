package com.gzz100.zbh.mimc;

import com.google.gson.Gson;
import com.gzz100.zbh.data.eventEnity.MimcLoginStatus;
import com.orhanobut.logger.Logger;
import com.xiaomi.mimc.MIMCException;
import com.xiaomi.mimc.MIMCOnlineStatusListener;
import com.xiaomi.mimc.MIMCUser;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Lam on 2018/5/8.
 */

public class MCUserManager {

    private long appId = 2882303761517707027L;

    private MIMCUser mUser;
    private int mStatus;
    private final static MCUserManager instance = new MCUserManager();
    public static MCUserManager getInstance() {
        return instance;
    }
    private String mAppAccount;
    private MimcMsgHandler mMimcMsgHandler;
    private Gson mGson=new Gson();

    /**
     * 创建用户
     * @param appAccount APP自己维护的用户帐号，不能为null
     * @return 返回新创建的用户
     */
    public MIMCUser createUser(String appAccount,MimcMsgHandler.OnHandlerMimcGroupMsgListener msgHandler){
        if (appAccount == null) return null;

        this.mAppAccount = appAccount;
        mUser = new MIMCUser(appId, appAccount);
        mMimcMsgHandler = new MimcMsgHandler(mAppAccount);
        mMimcMsgHandler.addMsgListener(msgHandler);

        // 注册相关监听，必须
        mUser.registerTokenFetcher(new TokenFetcher());
        mUser.registerMessageHandler(mMimcMsgHandler);
        mUser.registerOnlineStatusListener(new OnlineStatusListener());
        try {
            mUser.login();
        } catch (MIMCException e) {
            e.printStackTrace();
            Logger.i(e.getMessage());
        }
        return mUser;
    }

    public void setMsgHandler(MimcMsgHandler msgHandler){
        if (mUser!=null) {
            mUser.registerMessageHandler(msgHandler);
        }
    }

    public MimcMsgHandler getMsgHandler() {
        return mMimcMsgHandler;
    }

    public void sendGroupMsg(long groupID, String content, int msgType) throws MIMCException {
        BaseMCMsg msg = new BaseMCMsg();
        msg.setVersion(Constant.VERSION);
        msg.setMsgId(msg.getMsgId());
        msg.setMsgType(msgType);
        msg.setTimestamp(System.currentTimeMillis());
        msg.setMsgBody(content);
        String json = mGson.toJson(msg);
        mUser.sendGroupMessage(groupID, json.getBytes());
        Logger.i("sendGroupMsg="+content);
    }

    public void sendSyncCanvasMsg(long groupID, List<SyncCanvasBean> syncBeans) throws MIMCException {

        BaseMCMsg msg = new BaseMCMsg();
        msg.setVersion(Constant.VERSION);
        msg.setMsgId(msg.getMsgId());
        msg.setMsgType(Constant.SYNC_CANVAS);
        msg.setTimestamp(System.currentTimeMillis());
        String content = mGson.toJson(syncBeans);
        msg.setMsgBody(content);
        String json = mGson.toJson(msg);
        mUser.sendGroupMessage(groupID, json.getBytes());
    }

    public MIMCUser getUser() {
        return mUser;
    }

    public int getStatus() {
        return mStatus;
    }

    class OnlineStatusListener implements MIMCOnlineStatusListener{
        /**
         * 在线状态发生改变
         * @param status 在线状态：MIMCConstant.STATUS_LOGIN_SUCCESS 在线，STATUS_LOGOUT 下线，STATUS_LOGIN_FAIL 登录失败
         * @param code 状态码
         * @param msg 状态描述
         */
        @Override
        public void onStatusChanged(int status, int code, String msg) {
            mStatus = status;
            MimcLoginStatus loginStatus = new MimcLoginStatus();
            loginStatus.code=code;
            loginStatus.status=status;
            loginStatus.msg= msg;
            EventBus.getDefault().post(loginStatus);
            Logger.i("status="+status+",msg="+msg+",code="+code);
        }
    }

}
