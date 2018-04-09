package com.gzz100.zbh.home.appointment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.data.entity.Staff;
import com.gzz100.zbh.utils.TextHeadPicUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lam on 2018/2/5.
 */

public class SearchResultAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<Staff> mStaffList;
    private final int TYPE_NONE=121;
    private final int TYPE_NORMAL=122;
    private OnSearchStaffClickListener mOnSearchStaffClickListener;

    public SearchResultAdapter(Context context) {
        mContext = context;
        mStaffList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_staff, parent, false);

        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ResultViewHolder viewHolder = (ResultViewHolder) holder;
        if (getItemViewType(position)==TYPE_NORMAL){
            final Staff staff = mStaffList.get(position);
            viewHolder.tvStaffName.setText(staff.getUserName());
            viewHolder.ivStaffPic.setImageDrawable(TextHeadPicUtil.getHeadPic(staff.getUserName()));
            viewHolder.cbIsSelect.setChecked(staff.isSelect());
            viewHolder.tvState.setVisibility(View.GONE);
            viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    staff.setSelect(!staff.isSelect());
                    if (mOnSearchStaffClickListener!=null){
                        mOnSearchStaffClickListener.onSearchStaffCheck(staff);
                    }
                    notifyItemChanged(position);
                }
            });
        }else {
            viewHolder.tvStaffName.setText("没有查找到相关人员");
            viewHolder.ivStaffPic.setImageResource(R.drawable.ic_message_primary_24dp);
            viewHolder.cbIsSelect.setVisibility(View.GONE);
            viewHolder.tvState.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (mStaffList.size()==0){
            return TYPE_NONE;
        }else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return mStaffList.size()==0?1:mStaffList.size();
    }

    private static class ResultViewHolder extends RecyclerView.ViewHolder{
        View rootView;
        TextView tvState;
        CheckBox cbIsSelect;
        ImageView ivStaffPic;
        TextView tvStaffName;

        public ResultViewHolder(View view) {
            super(view);
            rootView = view.findViewById(R.id.ll_staff_root);
            tvState = view.findViewById(R.id.tv_staff_state);
            cbIsSelect = view.findViewById(R.id.cb_staff);
            ivStaffPic = view.findViewById(R.id.iv_staff_pic);
            tvStaffName =view.findViewById(R.id.tv_staffName);
        }

    }

    public List<Staff> getStaffList() {
        return mStaffList;
    }

    public void setOnSearchStaffClickListener(OnSearchStaffClickListener onSearchStaffClickListener) {
        mOnSearchStaffClickListener = onSearchStaffClickListener;
    }

    public interface OnSearchStaffClickListener{
        void onSearchStaffCheck(Staff staff);
    }

    public void add(Staff staff){
        mStaffList.add(staff);
        notifyItemInserted(mStaffList.size());
    }

    public void addAll(List<Staff> staffs){
        mStaffList.addAll(staffs);
        notifyDataSetChanged();
    }

    public void remove(Staff staff){
        mStaffList.remove(staff);
        notifyDataSetChanged();
    }

    public void removeAll(List<Staff> staffs){
        mStaffList.removeAll(staffs);
        notifyDataSetChanged();
    }

    public void clearAll(){
        mStaffList.clear();
        notifyDataSetChanged();
    }

    public void setDataSet(List<Staff> staffs){
        mStaffList=staffs;
        notifyDataSetChanged();
    }
}

