package com.gzz100.zbh.home.meetingadmin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
                holder.ivStatus.setImageResource(R.drawable.ic_statu_on_meeting);
                break;
            case 2:
                holder.ivStatus.setImageResource(R.drawable.ic_statu_on_ready);
                break;
            case 3:
                holder.ivStatus.setImageResource(R.drawable.ic_statu_end);
                break;
        }
        //1表示待审核，2表示通过，3表示不通过，4表示不用审核'
        if (meeting.getMeetingApplyStatus()!=4) {
            switch (meeting.getMeetingApplyStatus()) {
                case 1:
                    holder.ivStatus.setImageResource(R.drawable.ic_statu_checking);
                    break;
                case 2:
                    holder.ivStatus.setImageResource(R.drawable.ic_statu_check_success);
                    break;
                case 3:
                    holder.ivStatus.setImageResource(R.drawable.ic_statu_check_fail);
                    break;
            }
        }

        if (TextUtils.isEmpty(meeting.getUnread())) {
            holder.ivUnreadSign.setVisibility(View.GONE);
        }else {
            holder.ivUnreadSign.setVisibility(View.VISIBLE);
        }

        switch (meeting.getMeetingRole()) {
            case 1:
                holder.ivRole.setVisibility(View.VISIBLE);
                holder.ivRole.setImageResource(R.drawable.ic_microphone);
                break;
            case 2:
                holder.ivRole.setVisibility(View.VISIBLE);
                holder.ivRole.setImageResource(R.drawable.ic_summary);
                break;
            default:
                holder.ivRole.setVisibility(View.GONE);
                    break;
        }

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener!=null) {
                    mOnItemClickListener.onItemClick(position);
                }
            }
        });

        holder.rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onItemLongClick(position);
                }
                return true;
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
        void onItemLongClick(int pos);
    }

    class MeetingHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_meetingStatus)
        ImageView ivStatus;
        @BindView(R.id.iv_meetingRole)
        ImageView ivRole;
        @BindView(R.id.tv_meetingName)
        TextView mTvMeetingName;
        @BindView(R.id.tv_meetingTime)
        TextView mTvMeetingTime;
        @BindView(R.id.tv_meetingPlace)
        TextView mTvMeetingPlace;
        @BindView(R.id.item_root)
        LinearLayout rootView;
        @BindView(R.id.iv_unread_sign)
        ImageView ivUnreadSign;
        public MeetingHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
