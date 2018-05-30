package com.gzz100.zbh.home.message.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.data.entity.MessageEntity;
import com.gzz100.zbh.utils.DensityUtil;
import com.gzz100.zbh.utils.TextHeadPicUtil;
import com.gzz100.zbh.utils.TimeFormatUtil;

import java.util.List;

/**
 * Created by Lam on 2018/1/30.
 */

public class MessageAdapter extends RecyclerView.Adapter {

    private List<MessageEntity> mMsgEntityList;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private ShapeDrawable mShapeDrawable;

    public MessageAdapter(List<MessageEntity> msgEntityList, Context context) {
        mMsgEntityList = msgEntityList;
        mContext = context;
        mShapeDrawable = new ShapeDrawable(new Shape() {
            @Override
            public void draw(Canvas canvas, Paint paint) {
                int radius = DensityUtil.dp2px(mContext, 6);
                canvas.drawCircle(radius,radius, radius,paint);
            }
        });
    }


    static {

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_msg, parent, false);
        return new MsgViewHodler(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MsgViewHodler viewHodler = (MsgViewHodler) holder;
        final MessageEntity messageEntity = mMsgEntityList.get(position);
        final String msgBody = messageEntity.getMessageDescription();
        SpannableStringBuilder ssb = new SpannableStringBuilder(msgBody);
//        ssb.setSpan(new ForegroundColorSpan(0xFF2196F3),0,3, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        viewHodler.tvMsgBody.setText(ssb);
        viewHodler.tvTitle.setText(messageEntity.getMessageTitle());
        String time = TimeFormatUtil.formatDateAndTime(messageEntity.getCreateTime());
        viewHodler.tvMsgTimeStamp.setText(time);
        viewHodler.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClick(v,messageEntity);
                }
            }
        });

        ShapeDrawable shapeDrawable = new ShapeDrawable(mShapeDrawable.getShape());

        shapeDrawable.getPaint().setColor(TextHeadPicUtil.getColor(msgBody));
        viewHodler.ivDot.setBackground(shapeDrawable);
    }


    public interface OnItemClickListener{
        void onItemClick(View view,MessageEntity msgEntity);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return mMsgEntityList.size();
    }

    static class MsgViewHodler extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvMsgBody;
        TextView tvMsgTimeStamp;
        View rootView;
        ImageView ivDot;
        public MsgViewHodler(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_msg_title);
            tvMsgBody = itemView.findViewById(R.id.tv_msg_body);
            tvMsgTimeStamp = itemView.findViewById(R.id.tv_msg_timeStamp);
            ivDot = itemView.findViewById(R.id.iv_msgDot);
            rootView = itemView.findViewById(R.id.rootView);
        }
    }
}
