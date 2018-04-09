package com.gzz100.zbh.home.appointment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.home.appointment.entity.VoteWrap;

import java.util.List;

/**
 * Created by Lam on 2018/3/7.
 */

public class VoteListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<VoteWrap> mVoteList;

    private OnItemClickListener mOnItemClickListener;

    public VoteListAdapter(Context context, List<VoteWrap> voteList) {
        mContext = context;
        this.mVoteList = voteList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_vote, parent, false);
        return new VoteHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        VoteHolder viewHolder = (VoteHolder) holder;
        final VoteWrap voteWrap = mVoteList.get(position);
        viewHolder.tvVoteName.setText(voteWrap.getVoteName());
        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClick(position,voteWrap);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVoteList.size();
    }

    public interface OnItemClickListener{
        void onItemClick(int pos,VoteWrap voteWrap);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    static class VoteHolder extends RecyclerView.ViewHolder{
        TextView tvVoteName;
        View rootView;
        public VoteHolder(View itemView) {
            super(itemView);
            tvVoteName = itemView.findViewById(R.id.tv_voteName);
            rootView = itemView.findViewById(R.id.rootView);
        }
    }

}
