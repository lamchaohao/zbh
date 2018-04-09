package com.gzz100.zbh.account.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.data.entity.CompanyEntity;
import com.gzz100.zbh.utils.GlideApp;

import java.util.List;

/**
 * Created by Lam on 2018/3/13.
 */

public class CompanyAdapter extends RecyclerView.Adapter{

    private List<CompanyEntity> companyList;
    private Context mContext;
    private OnApplyClickListener mOnApplyClickListener;

    public CompanyAdapter(List<CompanyEntity> companyList, Context context) {
        this.companyList = companyList;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_company, parent, false);

        return new CompanyHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        CompanyHolder holder= (CompanyHolder) viewHolder;
        final CompanyEntity company = companyList.get(position);
        GlideApp.with(mContext)
                .load(company.getCompanyPic())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.ivHeadPic);
        holder.tvCompanyName.setText(company.getCompanyName());
        holder.btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnApplyClickListener!=null){
                    mOnApplyClickListener.onApplyClick(company,position);
                }
            }
        });
    }

    public interface OnApplyClickListener{
        void onApplyClick(CompanyEntity companyEntity,int position);
    }

    public void setOnApplyClickListener(OnApplyClickListener onApplyClickListener) {
        mOnApplyClickListener = onApplyClickListener;
    }

    @Override
    public int getItemCount() {
        return companyList.size();
    }

    static class CompanyHolder extends RecyclerView.ViewHolder{

        ImageView ivHeadPic;
        TextView tvCompanyName;
        Button btnApply;
        public CompanyHolder(View itemView) {
            super(itemView);
            ivHeadPic = itemView.findViewById(R.id.iv_companyPic);
            tvCompanyName = itemView.findViewById(R.id.tv_companyName);
            btnApply = itemView.findViewById(R.id.btn_joinIn);
        }
    }
}