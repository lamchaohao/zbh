package com.gzz100.zbh.home.meetingadmin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.data.entity.DelegateEntity;

import java.util.List;

/**
 * Created by Lam on 2018/4/13.
 */

public class ChoseSpeakerAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<DelegateEntity> mDelegateList;

    public ChoseSpeakerAdapter(Context context, List<DelegateEntity> mdelegateList) {
        mContext = context;
        this.mDelegateList = mdelegateList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_speaker, parent, false);
        return new SpeakerHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        SpeakerHolder holder = (SpeakerHolder) viewHolder;
        DelegateEntity delegateEntity = mDelegateList.get(position);
        holder.tvName.setText(delegateEntity.getDelegateName());
        switch (delegateEntity.getDelegateRole()) {
            case 1:
                holder.tvRole.setText("主持人");
                break;
            case 2:
                holder.tvRole.setText("主讲人");
                break;
            case 3:
                holder.tvRole.setText("参会员");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mDelegateList.size();
    }

    static class SpeakerHolder extends RecyclerView.ViewHolder {
        TextView tvRole;
        TextView tvName;
        public SpeakerHolder(View itemView) {
            super(itemView);
            tvRole = itemView.findViewById(R.id.tv_speaker_role);
            tvName = itemView.findViewById(R.id.tv_speaker_name);
        }
    }

}
