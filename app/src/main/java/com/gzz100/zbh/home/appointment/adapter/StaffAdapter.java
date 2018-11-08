package com.gzz100.zbh.home.appointment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.data.entity.Staff;
import com.gzz100.zbh.home.appointment.entity.Department;
import com.gzz100.zbh.utils.DensityUtil;
import com.gzz100.zbh.utils.TextHeadPicUtil;

import java.util.List;

/**
 *
 * Created by Lam on 2018/2/2.
 */

public class StaffAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<Department> mDepartmentList;
    private OnDepartmentSelectListener mOnDepartmentSelectListener;
    private OnStaffSelectListener mOnStaffSelectListener;
    private boolean isMultiChoice;

    public StaffAdapter(Context context, List<Department> departmentList,boolean isMulti) {
        mContext = context;
        mDepartmentList = departmentList;
        this.isMultiChoice = isMulti;
    }

    @Override
    public int getGroupCount() {
        return mDepartmentList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mDepartmentList.get(groupPosition).getStaffs().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mDepartmentList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mDepartmentList.get(groupPosition).getStaffs().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = convertView;
        DepartmentHolder holder = null;
        if(view == null){
            holder = new DepartmentHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_department, parent,false);
            holder.cbChoice=view.findViewById(R.id.cb_department);
            holder.tvDepartmentName = view.findViewById(R.id.tv_departName);
            holder.tvDepartmentNums =view.findViewById(R.id.tv_departStaffCount);
            holder.ivArrow = view.findViewById(R.id.iv_arrow);
            view.setTag(holder);
        }else{
            holder = (DepartmentHolder)view.getTag();
        }
        //判断是否已经打开列表
        Department department = mDepartmentList.get(groupPosition);
        holder.tvDepartmentName.setText(department.getDepartmentName());
        holder.tvDepartmentNums.setText("("+department.getStaffs().size()+"人)");
        if (isMultiChoice) {
            holder.cbChoice.setChecked(department.isSelect());
            holder.cbChoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnDepartmentSelectListener!=null) {
                        mOnDepartmentSelectListener.onDepartmentCheck(((CheckBox)v).isChecked(),groupPosition);
                    }
                }
            });
        }else {
            holder.cbChoice.setVisibility(View.GONE);
        }

        if (isExpanded) {
            holder.ivArrow.setImageResource(R.drawable.ic_arrow_down);
        }else {
            holder.ivArrow.setImageResource(R.drawable.ic_arrow_right);
        }

        return view;

    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = convertView;
        StaffHolder holder = null;
        if(view == null){
            holder = new StaffHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_staff, parent,false);
            holder.rootView = view.findViewById(R.id.ll_staff_root);
            holder.tvState = view.findViewById(R.id.tv_staff_state);
            holder.cbIsSelect = view.findViewById(R.id.cb_staff);
            holder.ivStaffPic = view.findViewById(R.id.iv_staff_pic);
            holder.tvStaffName =view.findViewById(R.id.tv_staffName);
            holder.tvPosition = view.findViewById(R.id.tv_staff_position);
            view.setTag(holder);
        }else{
            holder = (StaffHolder)view.getTag();
        }
        //判断是否已经打开列表
        final Staff staff = mDepartmentList.get(groupPosition).getStaffs().get(childPosition);
        int fontSize = DensityUtil.sp2px(mContext, 12);
        holder.ivStaffPic.setImageDrawable(TextHeadPicUtil.getHeadPic(staff.getUserName(),fontSize));
        holder.tvStaffName.setText(staff.getUserName());
        holder.tvPosition.setText(staff.getPositionName());
        if (staff.isSelect()) {
            holder.tvState.setVisibility(View.VISIBLE);
            holder.ivStaffPic.setImageAlpha(128);
            holder.tvStaffName.setAlpha(0.5f);
//            holder.rootView.setBackgroundColor(Color.parseColor("#E0E0E0"));
        }else {
//            holder.rootView.setBackground(null);
            holder.ivStaffPic.setImageAlpha(255);
            holder.tvStaffName.setAlpha(1.0f);
            holder.tvState.setVisibility(View.GONE);
        }

        holder.cbIsSelect.setChecked(staff.isSelect());
        holder.cbIsSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnStaffSelectListener!=null){
                    mOnStaffSelectListener.onStaffCheck(((CheckBox)v).isChecked(),groupPosition,childPosition);
                }
            }
        });
        return view;
    }

    public void setOnStaffSelectListener(OnStaffSelectListener onStaffSelectListener) {
        mOnStaffSelectListener = onStaffSelectListener;
    }

    public void setOnDepartmentSelectListener(OnDepartmentSelectListener onDepartmentSelectListener) {
        mOnDepartmentSelectListener = onDepartmentSelectListener;
    }



    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class DepartmentHolder{
        CheckBox cbChoice;
        TextView tvDepartmentName;
        TextView tvDepartmentNums;
        ImageView ivArrow;
    }

    class StaffHolder{
        View rootView;
        TextView tvState;
        CheckBox cbIsSelect;
        ImageView ivStaffPic;
        TextView tvStaffName;
        TextView tvPosition;
    }

    public interface OnDepartmentSelectListener{
        void onDepartmentCheck(boolean isCheck,int groupPosition);
    }
    public interface OnStaffSelectListener{
        void onStaffCheck(boolean isCheck,int groupPosition,int childPosition);
    }
}
