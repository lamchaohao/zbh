package com.gzz100.zbh.home.meetingadmin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.data.entity.VoteDetailEntity;
import com.gzz100.zbh.utils.DensityUtil;
import com.gzz100.zbh.utils.GlideApp;
import com.gzz100.zbh.utils.TextHeadPicUtil;

import java.util.ArrayList;
import java.util.List;

import static com.gzz100.zbh.res.Common.STATUS_END;

/**
 * Created by Lam on 2018/4/10.
 */

public class VoteOptionAdapter extends RecyclerView.Adapter{

    private Context mContext;
    private List<VoteDetailEntity.VoteOptionListBean> mOptionList;
    private SparseBooleanArray mSelectedPositions = new SparseBooleanArray();
    private int mMaxCount;
    private OnVoteItemClickListener mOnVoteItemClickListener;
    private VoteDetailEntity voteDetail;
    private int imageSize ;
    private int fontSize ;
    private int voteCountSum;

    public VoteOptionAdapter(Context context, VoteDetailEntity voteDetailEntity) {
        mContext = context;
        voteDetail = voteDetailEntity;
        mOptionList =  voteDetailEntity.getVoteOptionList();
        mMaxCount = voteDetailEntity.getVoteSelectableNum();
        imageSize = DensityUtil.dp2px(mContext, 36);
        fontSize = DensityUtil.sp2px(mContext, 16);
        updateSumVote();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_vote_option, parent, false);
        return new OptionHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        OptionHolder holder = (OptionHolder) viewHolder;

        final VoteDetailEntity.VoteOptionListBean optionBean = mOptionList.get(position);
        holder.tvOptionName.setText(optionBean.getVoteOptionName());
        GlideApp.with(mContext)
                .load(optionBean.getVoteDocumentPath())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.ivOptionPic);

        if (TextUtils.isEmpty(optionBean.getVoteDocumentPath())) {
            holder.ivOptionPic.setVisibility(View.GONE);
        }else {
            holder.ivOptionPic.setVisibility(View.VISIBLE);
        }


        holder.ivOptionPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnVoteItemClickListener!=null){
                    mOnVoteItemClickListener.onPicClick(optionBean.getVoteDocumentPath(),v);
                }
            }
        });

        if (voteDetail.getVoteStatus()==STATUS_END) {
            bindResultHolder(holder,position);
        }else {
            bindVoteHolder(holder,position);
        }


    }

    private void bindVoteHolder(OptionHolder holder, final int position) {
        final VoteDetailEntity.VoteOptionListBean optionBean = mOptionList.get(position);

        //选中状态
        holder.checkBox.setChecked(isItemChecked(position));

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean itemChecked = isItemChecked(position);
                if (itemChecked) {//之前选上的可取消
                    setItemChecked(position, false);
                }else {//要选为true先要检查可投多少个
                    if (getSelectedItem().size()<mMaxCount) {
                        setItemChecked(position, true);
                    }
                }
                notifyItemChanged(position);
                if (mOnVoteItemClickListener!=null){
                    mOnVoteItemClickListener.onItemClick(position,optionBean);
                }
            }
        });

        holder.tvResultPercent.setVisibility(View.GONE);
        holder.tvResultScore.setVisibility(View.GONE);
        holder.pbResult.setVisibility(View.GONE);
        holder.ivShowVoter.setVisibility(View.GONE);
        holder.llVoterListView.setVisibility(View.GONE);
    }

    private void bindResultHolder(final OptionHolder holder, int position) {
        VoteDetailEntity.VoteOptionListBean optionListBean = voteDetail.getVoteOptionList().get(position);
        holder.checkBox.setVisibility(View.GONE);
        holder.tvResultPercent.setVisibility(View.VISIBLE);
        holder.tvResultScore.setVisibility(View.VISIBLE);
        holder.pbResult.setVisibility(View.VISIBLE);

        if (voteDetail.getVoteAnonymous()!=1) {
            holder.ivShowVoter.setVisibility(View.VISIBLE);
            holder.ivShowVoter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.llVoterListView.getVisibility()== View.GONE) {
                        holder.llVoterListView.setVisibility(View.VISIBLE);
                    }else{
                        holder.llVoterListView.setVisibility(View.GONE);
                    }
                }
            });
        }else {
            //匿名不显示信息
            holder.ivShowVoter.setVisibility(View.GONE);
        }
        for (VoteDetailEntity.VoteOptionListBean.UserListBean userBean : optionListBean.getUserList()) {
            ImageView iv = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imageSize, imageSize);
            params.leftMargin = DensityUtil.dp2px(mContext, 3);
            iv.setLayoutParams(params);
            iv.setImageDrawable(TextHeadPicUtil.getHeadPic(userBean.getUserName(), fontSize, imageSize));
            holder.llVoterListView.addView(iv);
        }

        holder.tvResultScore.setText(optionListBean.getUserList().size()+"票");
        if (voteCountSum==0){
            voteCountSum=-1;
        }
        float percent = optionListBean.getUserList().size()*100 / voteCountSum;

        StringBuffer sb=new StringBuffer();
        sb.append(percent);
        sb.append("%");
        holder.tvResultPercent.setText(sb.toString());
        holder.pbResult.setProgress((int) percent);
//        voteDetail.getVoterNum()


    }

    public void updateSumVote(){
        voteCountSum = 0;
        for (VoteDetailEntity.VoteOptionListBean voteOptionListBean : voteDetail.getVoteOptionList()) {
            voteCountSum += voteOptionListBean.getUserList().size();
        }
    }

    private void setItemChecked(int position, boolean isChecked) {
        mSelectedPositions.put(position, isChecked);
        int size = getSelectedItem().size();
//        if (mTvTips!=null){
//            mTvTips.setText("已选择" + size + "项");
//        }
    }

    private boolean isItemChecked(int position) {
        return mSelectedPositions.get(position);
    }

    //获得选中条目的结果
    public ArrayList<VoteDetailEntity.VoteOptionListBean> getSelectedItem() {
        ArrayList<VoteDetailEntity.VoteOptionListBean> selectList = new ArrayList<>();
        for (int i = 0; i < mOptionList.size(); i++) {
            if (isItemChecked(i)) {
                selectList.add(mOptionList.get(i));
            }
        }
        return selectList;
    }

    public interface OnVoteItemClickListener{
        void onPicClick(String url,View view);
        void onItemClick(int pos,VoteDetailEntity.VoteOptionListBean bean);
    }

    public void setOnVoteItemClickListener(OnVoteItemClickListener onVoteItemClickListener) {
        mOnVoteItemClickListener = onVoteItemClickListener;
    }

    @Override
    public int getItemCount() {
        return mOptionList.size();
    }

    static class OptionHolder extends RecyclerView.ViewHolder{

        CheckBox checkBox;
        TextView tvOptionName;
        ImageView ivOptionPic;
        View rootView;
        TextView tvResultScore;
        TextView tvResultPercent;
        ImageView ivShowVoter;
        ProgressBar pbResult;
        LinearLayout llVoterListView;
        public OptionHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.cb_option_select);
            tvOptionName = itemView.findViewById(R.id.tv_option_text);
            ivOptionPic = itemView.findViewById(R.id.iv_OptionPic);
            rootView = itemView.findViewById(R.id.rootView);

            tvResultScore = itemView.findViewById(R.id.tv_vote_score);
            tvResultPercent = itemView.findViewById(R.id.tv_vote_percent);
            ivShowVoter = itemView.findViewById(R.id.iv_showVoter);
            pbResult = itemView.findViewById(R.id.pb_voteResult);
            llVoterListView = itemView.findViewById(R.id.ll_voted_user);
        }
    }
}
