package com.gzz100.zbh.home.message;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.data.entity.MessageEntity;
import com.gzz100.zbh.utils.TimeFormatUtil;

import java.util.List;

/**
 * Created by Lam on 2018/1/30.
 */

public class MessageAdapter extends RecyclerView.Adapter {

    private List<MessageEntity> mMsgEntityList;
    private Context mContext;

    public MessageAdapter(List<MessageEntity> msgEntityList, Context context) {
        mMsgEntityList = msgEntityList;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_msg, parent, false);
        return new MsgViewHodler(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MsgViewHodler viewHodler = (MsgViewHodler) holder;
        String msgBody = mMsgEntityList.get(position).getMessageDescription();
        SpannableStringBuilder ssb = new SpannableStringBuilder(msgBody);
        ssb.setSpan(new ForegroundColorSpan(0xFF2196F3),0,3, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        viewHodler.tvMsgBody.setText(ssb);
        String time = TimeFormatUtil.formatDateAndTime(mMsgEntityList.get(position).getCreateTime());
        viewHodler.tvMsgTimeStamp.setText(time);
    }

    @Override
    public int getItemCount() {
        return mMsgEntityList.size();
    }

    static class MsgViewHodler extends RecyclerView.ViewHolder {
        private TextView tvMsgBody;
        private TextView tvMsgTimeStamp;
        public MsgViewHodler(View itemView) {
            super(itemView);
            tvMsgBody = itemView.findViewById(R.id.tv_msg_body);
            tvMsgTimeStamp = itemView.findViewById(R.id.tv_msg_timeStamp);
        }


    }
}
