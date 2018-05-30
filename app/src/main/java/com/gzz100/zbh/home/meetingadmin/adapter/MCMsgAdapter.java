package com.gzz100.zbh.home.meetingadmin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.account.User;
import com.gzz100.zbh.mimc.TextMsg;
import com.gzz100.zbh.utils.TextHeadPicUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lam on 2018/5/8.
 */

public class MCMsgAdapter extends RecyclerView.Adapter {
    private static final int MSG_MINE=101;
    private static final int MSG_OTHER=102;
    private Context mContext;
    private List<TextMsg> msgList;

    public MCMsgAdapter(Context context) {
        mContext = context;
        this.msgList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType==MSG_MINE){
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_mc_msg_mine, parent, false);
            return new MsgHolder(view);
        }else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_mc_msg, parent, false);
            return new MsgHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MsgHolder viewHolder= (MsgHolder) holder;
        TextMsg textMsg = msgList.get(position);
        viewHolder.ivAccountPic.setImageDrawable(TextHeadPicUtil.getHeadPic(textMsg.getFromAccount()));
        viewHolder.tvMsgCotent.setText(textMsg.getContent());

    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }

    public void addMsg(TextMsg textMsg){
        msgList.add(textMsg);
        notifyItemInserted(msgList.size());
    }

    @Override
    public int getItemViewType(int position) {
        if (msgList.get(position).getFromAccount().equals(User.getUserFromCache().getUserId())) {
            return MSG_MINE;
        }else {
            return MSG_OTHER;
        }

    }

    static class MsgHolder extends RecyclerView.ViewHolder{

        ImageView ivAccountPic;
        TextView tvMsgCotent;
        public MsgHolder(View itemView) {
            super(itemView);
            ivAccountPic = itemView.findViewById(R.id.iv_userPic);
            tvMsgCotent = itemView.findViewById(R.id.tv_msg);
        }
    }
}
