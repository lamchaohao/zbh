package com.gzz100.zbh.home.appointment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.data.entity.TemplateEntity;
import com.gzz100.zbh.utils.TimeFormatUtil;

import java.util.List;

/**
 * Created by Lam on 2018/7/27.
 */

public class TemplateAdapter extends RecyclerView.Adapter {

    List<TemplateEntity> mTemplateList ;
    Context mContext;
    OnItemClickListener mOnItemClickListener;

    public TemplateAdapter(List<TemplateEntity> templateList, Context context) {
        mTemplateList = templateList;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_template, parent, false);

        return new TempHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        TempHolder holder = (TempHolder) viewHolder;
        TemplateEntity entity = mTemplateList.get(position);
        holder.tvMeetingName.setText(entity.getMeetingName());
        holder.tvMeetingTime.setText(TimeFormatUtil.formatTime(Long.parseLong(entity.getMeetingStartTime()))+"--"+TimeFormatUtil.formatTime(Long.parseLong(entity.getMeetingEndTime())));
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClick(position);
                }
            }
        });
        holder.rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onItemLongPress(position);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTemplateList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public class TempHolder extends RecyclerView.ViewHolder{

        TextView tvMeetingName;
        TextView tvMeetingTime;
        View rootView;

        public TempHolder(View itemView) {
            super(itemView);
            tvMeetingName = itemView.findViewById(R.id.tv_meetingPlace);
            tvMeetingTime = itemView.findViewById(R.id.tv_template_time);
            rootView = itemView.findViewById(R.id.rootView);

        }


    }

    public interface OnItemClickListener{
        void onItemClick(int pos);
        void onItemLongPress(int pos);
    }


}
