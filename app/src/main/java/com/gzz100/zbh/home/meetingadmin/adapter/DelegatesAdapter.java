package com.gzz100.zbh.home.meetingadmin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.gzz100.zbh.R;
import com.gzz100.zbh.data.entity.DelegateEntity;
import com.gzz100.zbh.utils.TextHeadPicUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Lam on 2018/4/12.
 */

public class DelegatesAdapter extends RecyclerView.Adapter {


    private Context mContext;
    private List<DelegateEntity> mdelegateList;

    public DelegatesAdapter(Context context, List<DelegateEntity> mdelegateList) {
        mContext = context;
        this.mdelegateList = mdelegateList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_delegate, parent, false);
        return new DelegateHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        DelegateEntity delegateEntity = mdelegateList.get(position);
        DelegateHolder holder = (DelegateHolder) viewHolder;

        holder.tvDelegateName.setText(delegateEntity.getDelegateName());
        holder.tvNamePosition.setText(delegateEntity.getPositionName());
//        holder.i
        TextDrawable headPic = TextHeadPicUtil.getHeadPic(delegateEntity.getDelegateName());
        holder.ivDelegatePic.setImageDrawable(headPic);
        if (!TextUtils.isEmpty(delegateEntity.getSignInTime())) {
            holder.tvSigned.setVisibility(View.VISIBLE);
        }else {
            holder.tvSigned.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mdelegateList.size();
    }

    static class DelegateHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_delegatePic)
        ImageView ivDelegatePic;
        @BindView(R.id.tv_delegateName)
        TextView tvDelegateName;
        @BindView(R.id.tv_name_position)
        TextView tvNamePosition;
        @BindView(R.id.ll_signed)
        View tvSigned;

        public DelegateHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(DelegateHolder.this,itemView);
        }
    }
}
