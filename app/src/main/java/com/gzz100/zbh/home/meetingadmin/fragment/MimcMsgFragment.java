package com.gzz100.zbh.home.meetingadmin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gzz100.zbh.R;
import com.gzz100.zbh.account.User;
import com.gzz100.zbh.base.BaseFragment;
import com.gzz100.zbh.home.meetingadmin.adapter.MCMsgAdapter;
import com.gzz100.zbh.mimc.Constant;
import com.gzz100.zbh.mimc.MCUserManager;
import com.gzz100.zbh.mimc.MimcMsgHandler;
import com.gzz100.zbh.mimc.SyncCanvasBean;
import com.gzz100.zbh.mimc.SyncDocumentBean;
import com.gzz100.zbh.mimc.SyncMeetingBean;
import com.gzz100.zbh.mimc.TextMsg;
import com.orhanobut.logger.Logger;
import com.xiaomi.mimc.MIMCException;
import com.xiaomi.mimc.MIMCGroupMessage;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lam on 2018/5/7.
 */

public class MimcMsgFragment extends BaseFragment implements MimcMsgHandler.OnHandlerMimcGroupMsgListener{

    @BindView(R.id.rcv_mimc_msg)
    RecyclerView mRecyclerView;
    @BindView(R.id.et_msg)
    EditText etMsg;
    @BindView(R.id.btn_send)
    Button btnSend;
    private MCMsgAdapter mAdapter;
    private MCUserManager mUserManager;
    private long mGroupId;
    private View mView;
    private String mUserId;


    public static MimcMsgFragment newInstance(long groupId){
        MimcMsgFragment fragment=new MimcMsgFragment();
        Bundle bundle=new Bundle();
        bundle.putLong("groupId",groupId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        if (mView==null){
            mView = inflater.inflate(R.layout.fragment_mimc_message, null);
            ButterKnife.bind(this, mView);
        }
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVar();
        initView();
    }

    private void initVar() {
        if (getArguments()!=null) {
            mGroupId = getArguments().getLong("groupId");
        }
    }

    private void initView() {
        mUserId = User.getUserFromCache().getUserId();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mAdapter = new MCMsgAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);

        mUserManager = MCUserManager.getInstance();
        mUserManager.newUser(mUserId,this);
        try {
            if (mUserManager.getUser()!=null){
                mUserManager.getUser().login();
            }else {
                Logger.i("mUserManager.getUser()=null");
            }
        } catch (MIMCException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onGroupTextMsgReceived(final TextMsg textMsg) {
        _mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mGroupId==(textMsg.getGroupId())) {
                    mAdapter.addMsg(textMsg);
                    mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount()-1);
                }
            }
        });

        Logger.i(textMsg.getContent());
    }

    @Override
    public void onCanvasMsgReceived(List<SyncCanvasBean> syncCanvasBeans) {

    }

    @Override
    public void onDocumentReceived(SyncDocumentBean syncDocumentBean) {

    }

    @Override
    public void onMeetingActionReceived(SyncMeetingBean syncMeetingBean) {

    }

    @Override
    public void onSendGroupMsgTimeout(MIMCGroupMessage mimcGroupMessage) {
        Logger.e("SendGroupMsgTimeout:"+new String(mimcGroupMessage.getPayload()));
    }

    @OnClick(R.id.btn_send)
    void onSendClick(View view){
        String content = etMsg.getText().toString();
        if (TextUtils.isEmpty(content)) {
            return;
        }
        try {
            etMsg.setText("");
            Logger.i("mGroupId="+mGroupId+",content="+content);
            mUserManager.sendGroupMsg(mGroupId, content, Constant.TEXT);
            TextMsg textMsg = new TextMsg(content);
            textMsg.setFromAccount(mUserId);
            textMsg.setGroupId(mGroupId);
            mAdapter.addMsg(textMsg);
            mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount()-1);
        } catch (MIMCException e) {
            e.printStackTrace();
            Logger.i(e.getMessage());
        }
    }
}
