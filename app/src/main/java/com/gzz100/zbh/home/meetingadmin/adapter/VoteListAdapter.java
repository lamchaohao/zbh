package com.gzz100.zbh.home.meetingadmin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.data.entity.VoteEntity;

import java.util.List;

/**
 * Created by Lam on 2018/4/3.
 */

public class VoteListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<VoteEntity> voteList;
    private OnItemClickListener mOnItemClickListener;
    public VoteListAdapter(Context context, List<VoteEntity> voteList) {
        mContext = context;
        this.voteList = voteList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_vote_preview, parent, false);
        return new VoteHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        VoteHolder voteHolder = (VoteHolder) holder;
        final VoteEntity voteEntity = voteList.get(position);
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        int voteSelectableNum = voteEntity.getVoteSelectableNum();
        ssb.append("【");
        ssb.append(voteSelectableNum==1?"单选":"多选");
        ssb.append("】");
        voteHolder.tvVoteMode.setText(ssb.toString());
        voteHolder.tvVoteName.setText(voteEntity.getVoteName());
        voteHolder.tvVoteDesc.setText(voteEntity.getVoteDescription());
        String staus="";
        switch (voteEntity.getVoteStatus()) {
            case 1:
               voteHolder.ivStatus.setImageResource(R.drawable.ic_statu_on_meeting);
                break;
            case 2:
                voteHolder.ivStatus.setImageResource(R.drawable.ic_statu_on_ready);
                break;
            case 3:
                voteHolder.ivStatus.setImageResource(R.drawable.ic_statu_end);
                break;
        }
        voteHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClick(voteEntity,position);
                }
            }
        });
    }

    public interface OnItemClickListener{
        void onItemClick(VoteEntity vote,int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return voteList.size();
    }

    static class VoteHolder extends RecyclerView.ViewHolder{

        ImageView ivStatus;
        TextView tvVoteName;
        TextView tvVoteMode;
        TextView tvVoteDesc;
        View rootView;
        public VoteHolder(View itemView) {
            super(itemView);
            ivStatus = itemView.findViewById(R.id.iv_vote_status);
            tvVoteName = itemView.findViewById(R.id.tv_vote_name);
            tvVoteMode = itemView.findViewById(R.id.tv_vote_mode);
            tvVoteDesc = itemView.findViewById(R.id.tv_vote_desc);
            rootView = itemView.findViewById(R.id.ll_rootview);
        }
    }
}
