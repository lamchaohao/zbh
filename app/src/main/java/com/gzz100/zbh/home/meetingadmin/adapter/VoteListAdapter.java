package com.gzz100.zbh.home.meetingadmin.adapter;

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
import com.gzz100.zbh.data.entity.VoteEntity;

import java.util.List;

/**
 * Created by Lam on 2018/4/3.
 */

public class VoteListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<VoteEntity> voteList;

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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        VoteHolder voteHolder = (VoteHolder) holder;
        VoteEntity voteEntity = voteList.get(position);
        SpannableStringBuilder ssb = new SpannableStringBuilder(voteEntity.getVoteName());
        int voteSelectableNum = voteEntity.getVoteSelectableNum();
        ssb.append("【");
        ssb.append(voteSelectableNum==1?"单选":"多选");
        ssb.append("】");
        ssb.setSpan(new ForegroundColorSpan(0xFF2196F3),voteEntity.getVoteName().length(),ssb.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        voteHolder.tvVoteName.setText(ssb);
        voteHolder.tvVoteDesc.setText(voteEntity.getVoteDescription());
        String staus="";
        switch (voteEntity.getVoteStatus()) {
            case 1:
                staus="进行中";
                break;
            case 2:
                staus="未开始";
                break;
            case 3:
                staus="已结束";
                break;
        }
        voteHolder.tvStaus.setText(staus);
    }

    @Override
    public int getItemCount() {
        return voteList.size();
    }

    static class VoteHolder extends RecyclerView.ViewHolder{

        TextView tvStaus;
        TextView tvVoteName;
        TextView tvVoteDesc;

        public VoteHolder(View itemView) {
            super(itemView);
            tvStaus = itemView.findViewById(R.id.tv_vote_staus);
            tvVoteName = itemView.findViewById(R.id.tv_vote_name);
            tvVoteDesc = itemView.findViewById(R.id.tv_vote_desc);
        }
    }
}
