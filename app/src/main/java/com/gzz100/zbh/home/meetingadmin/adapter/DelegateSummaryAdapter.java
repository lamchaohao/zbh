package com.gzz100.zbh.home.meetingadmin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.gzz100.zbh.R;
import com.gzz100.zbh.data.entity.DelegateSummaryEntity;
import com.gzz100.zbh.utils.TextHeadPicUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Lam on 2018/5/24.
 */

public class DelegateSummaryAdapter extends RecyclerView.Adapter {

    private List<DelegateSummaryEntity.DelegateBean> mDelegateList;
    private Context mContext;

    public DelegateSummaryAdapter(List<DelegateSummaryEntity.DelegateBean> delegateList, Context context) {
        mDelegateList = delegateList;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_delegate_summary, parent, false);
        return new SummaryHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SummaryHolder viewHolder = (SummaryHolder) holder;
        DelegateSummaryEntity.DelegateBean bean = mDelegateList.get(position);
        viewHolder.tvDelegateName.setText(bean.getUserName());
        viewHolder.tvNamePosition.setText(bean.getPositionName());
        TextDrawable headPic = TextHeadPicUtil.getHeadPic(bean.getUserName());
        viewHolder.ivDelegatePic.setImageDrawable(headPic);

    }

    @Override
    public int getItemCount() {
        return mDelegateList.size();
    }

    static class SummaryHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_delegatePic)
        ImageView ivDelegatePic;
        @BindView(R.id.tv_delegateName)
        TextView tvDelegateName;
        @BindView(R.id.tv_name_position)
        TextView tvNamePosition;
        public SummaryHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(SummaryHolder.this,itemView);
        }
    }
}
