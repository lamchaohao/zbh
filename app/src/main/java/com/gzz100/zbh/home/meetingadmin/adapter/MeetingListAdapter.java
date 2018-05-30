package com.gzz100.zbh.home.meetingadmin.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.data.entity.MeetingEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Lam on 2018/1/15.
 */

public class MeetingListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<MeetingEntity> mMetingBeans;
    private OnItemClickListener mOnItemClickListener;

    public MeetingListAdapter(Context context, List<MeetingEntity> metingBeans) {
        mContext = context;
        mMetingBeans = metingBeans;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_meeting, parent, false);
        return new MeetingHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        MeetingHolder holder = (MeetingHolder) viewHolder;
        MeetingEntity meeting = mMetingBeans.get(position);
        holder.mTvMeetingName.setText(meeting.getMeetingName());
        holder.mTvMeetingPlace.setText(meeting.getMeetingPlaceName());

        StringBuilder meetingTimeSb=new StringBuilder();
        meetingTimeSb.append("会议时间 ");
        meetingTimeSb.append(meeting.getMeetingStartTime());
        meetingTimeSb.append("-");
        String endTime = meeting.getMeetingEndTime().substring(meeting.getMeetingEndTime().lastIndexOf(" "));
        meetingTimeSb.append(endTime);
        holder.mTvMeetingTime.setText(meetingTimeSb.toString());

        switch (meeting.getMeetingStatus()) {
            case 1:
                holder.tvStatus.setText("进行中");
                holder.tvStatus.setBackgroundColor(Color.parseColor("#29B6F6"));
                break;
            case 2:
                holder.tvStatus.setText("未开始");
                holder.tvStatus.setBackgroundColor(Color.parseColor("#616161"));
                break;
            case 3:
                holder.tvStatus.setText("已结束");
                holder.tvStatus.setBackgroundColor(Color.parseColor("#BDBDBD"));
                break;
        }
        //1表示待审核，2表示通过，3表示不通过，4表示不用审核'
        if (meeting.getMeetingApplyStatus()!=4) {
            holder.tvApplyStatus.setVisibility(View.VISIBLE);
            switch (meeting.getMeetingApplyStatus()) {
                case 1:
                    holder.tvApplyStatus.setText("待审核");
                    holder.tvApplyStatus.setBackgroundColor(Color.parseColor("#29B6F6"));
                    break;
                case 2:
                    holder.tvApplyStatus.setText("通过");
                    holder.tvApplyStatus.setBackgroundColor(Color.parseColor("#29B6F6"));
                    break;
                case 3:
                    holder.tvApplyStatus.setText("不通过");
                    holder.tvApplyStatus.setBackgroundColor(Color.parseColor("#BDBDBD"));
                    break;
            }
        }else {
            holder.tvApplyStatus.setVisibility(View.GONE);
        }

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener!=null) {
                    mOnItemClickListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMetingBeans.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(int pos);
    }

    class MeetingHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_meetingStatus)
        TextView tvStatus;
        @BindView(R.id.tv_meetingName)
        TextView mTvMeetingName;
        @BindView(R.id.tv_meetingTime)
        TextView mTvMeetingTime;
        @BindView(R.id.tv_meetingPlace)
        TextView mTvMeetingPlace;
        @BindView(R.id.tv_meetingApplyStatus)
        TextView tvApplyStatus;
        @BindView(R.id.item_root)
        LinearLayout rootView;
        public MeetingHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
