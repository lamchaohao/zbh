package com.gzz100.zbh.home.meetingadmin.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gzz100.zbh.R;
import com.gzz100.zbh.data.entity.MeetingEntity;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

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
        holder.mTvMeetingTime.setText(meeting.getMeetingStartTime()+"--"+meeting.getMeetingEndTime());
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


        holder.rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new QMUIDialog.MessageDialogBuilder(mContext)
                        .setTitle("标题")
                        .setMessage("确定要删除吗？")
                        .addAction("取消", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                        .addAction(0, "删除", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                                mMetingBeans.remove(position);
                                notifyItemRemoved(position);
                                dialog.dismiss();
                            }
                        })
                        .show();
                return false;
            }
        });
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
        @BindView(R.id.item_root)
        LinearLayout rootView;
        public MeetingHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
