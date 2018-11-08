package com.gzz100.zbh.home.appointment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.data.entity.Staff;
import com.gzz100.zbh.utils.DensityUtil;
import com.gzz100.zbh.utils.TextHeadPicUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lam on 2018/2/6.
 */

public class SelectedStaffAdapter extends RecyclerView.Adapter {
    private List<Staff> mStaffList;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private OnSelectCountChangeListener mOnSelectCountChangeListener;
    private int headPicFontSize ;
    private int headPicSize ;
    public SelectedStaffAdapter(Context context) {
        mStaffList = new ArrayList<>();
        mContext = context;
        headPicFontSize = DensityUtil.dp2px(mContext,10);
        headPicSize = DensityUtil.dp2px(mContext,30);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_selected_staff, parent, false);
        return new SelectedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        SelectedViewHolder viewHolder = (SelectedViewHolder) holder;
        final Staff staff = mStaffList.get(position);
        viewHolder.ivHeadPic.setImageDrawable(TextHeadPicUtil.getHeadPic(staff.getUserName(),headPicFontSize,headPicSize));
        final int layoutPosition = viewHolder.getLayoutPosition();
        if (mOnItemClickListener!=null){
            viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(layoutPosition,staff);
                }
            });
        }
    }

    public void add(Staff staff){
        mStaffList.add(staff);
        onChanged();
        notifyItemInserted(mStaffList.size());
    }

    public void addAll(List<Staff> staffs){
        mStaffList.addAll(staffs);
        onChanged();
        notifyDataSetChanged();
    }

    public void remove(Staff staff){
        mStaffList.remove(staff);
        onChanged();
        notifyDataSetChanged();
    }

    public void removeAll(List<Staff> staffs){
        mStaffList.removeAll(staffs);
        onChanged();
        notifyDataSetChanged();
    }

    public void clearAll(){
        mStaffList.clear();
        notifyDataSetChanged();
    }

    public void setStaffList(List<Staff> staffList) {
        mStaffList = staffList;
        onChanged();
        notifyDataSetChanged();
    }

    public List<Staff> getSelectedStaffList() {
        return mStaffList;
    }

    @Override
    public int getItemCount() {
        return mStaffList.size();
    }

    static class SelectedViewHolder extends RecyclerView.ViewHolder{
        private View rootView;
        private ImageView ivHeadPic;
        public SelectedViewHolder(View itemView) {
            super(itemView);
            rootView = itemView.findViewById(R.id.rl_rootView);
            ivHeadPic = itemView.findViewById(R.id.ivPicHead);
        }
    }


    public void onChanged(){
        if (mOnSelectCountChangeListener!=null){
            mOnSelectCountChangeListener.onSelectCountChange(mStaffList.size());
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnSelectCountChange(OnSelectCountChangeListener onSelectCountChange) {
        mOnSelectCountChangeListener = onSelectCountChange;
    }

    public interface OnItemClickListener{
        void onItemClick(int position,Staff staff);
    }
    public interface OnSelectCountChangeListener{
        void onSelectCountChange(int count);
    }
}
