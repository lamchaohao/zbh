package com.gzz100.zbh.home.appointment.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.gzz100.zbh.R;
import com.gzz100.zbh.home.appointment.entity.VoteOption;
import com.gzz100.zbh.home.appointment.entity.VoteWrap;
import com.gzz100.zbh.utils.GlideApp;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Lam on 2018/2/9.
 */

public class AddVoteAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private Context mContext;
    private static final int TYPE_ADD_BUTTON=101;
    private static final int TYPE_NORMAL=102;
    private OnPicClickListener mOnPicClickListener;
    private List<VoteOption> mOptionList;
    private Map<Integer,EditText> mOptionNameMap;
    private VoteWrap mVoteWrap;
    private TextView tvMaxSelect;


    public AddVoteAdapter(Context context,@NonNull VoteWrap voteWrap) {
        mContext = context;
        mVoteWrap = voteWrap;
        mOptionNameMap = new HashMap<>();
        if (voteWrap.getOptionList()==null){
            mOptionList = new ArrayList<>();
            mOptionList.add(new VoteOption());
            mOptionList.add(new VoteOption());
        }else {
            mOptionList = voteWrap.getOptionList();

        }



    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==TYPE_ADD_BUTTON){
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_button, parent, false);
            return new AddButtonHolder(view);
        }else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_add_vote, parent, false);

            return new AddVoteHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int viewType = getItemViewType(position);
        if (viewType==TYPE_ADD_BUTTON){
            AddButtonHolder viewHolder = (AddButtonHolder) holder;
            viewHolder.btnAddOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    add();
                }
            });
            if (mVoteWrap.isSingle()){
                viewHolder.rlMultiSelect.setVisibility(View.GONE);
            }else {
                viewHolder.rlMultiSelect.setVisibility(View.VISIBLE);
            }
            viewHolder.btnAddMaxCount.setOnClickListener(this);
            viewHolder.mBtMinus.setOnClickListener(this);
            viewHolder.btnSubmit.setOnClickListener(this);
            tvMaxSelect = viewHolder.mTvMaxSelcted;
            tvMaxSelect.setText(mVoteWrap.getMaxCount()+"");
            if (mVoteWrap.isAutoStart()) {
                viewHolder.rdgVoteStartTime.check(R.id.rdbtn_atMeetingStart);
            }else {
                viewHolder.rdgVoteStartTime.check(R.id.rdbtn_byHandle);
            }
            viewHolder.rdgVoteStartTime.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId==R.id.rdbtn_atMeetingStart){
                        mVoteWrap.setAutoStart(true);
                    }else {
                        mVoteWrap.setAutoStart(false);
                    }
                }
            });
            viewHolder.swcVoteNoName.setChecked(mVoteWrap.isHideName());
            viewHolder.swcVoteNoName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mVoteWrap.setHideName(isChecked);
                }
            });
        }else {
            final VoteOption option = mOptionList.get(position);
            final AddVoteHolder voteHolder = (AddVoteHolder) holder;

            GlideApp.with(mContext)
                    .load(option.getPicFile())
                    .placeholder(R.drawable.ic_empty_zhihu)
                    .into(voteHolder.ivOptionPic);

            EditText editText = mOptionNameMap.get(option.hashCode());
            if (editText==null){
                voteHolder.etOption.setText(option.getOptionName());

            }else {
                voteHolder.etOption.setText(editText.getText());
            }
            mOptionNameMap.put(option.hashCode(),voteHolder.etOption);

            voteHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //避免出现 parameter must be a descendant of this view错误而crash掉
                    //所以先要移除edittext的焦点才能刷新recyclerview
                    voteHolder.etOption.clearFocus();
                    remove(option,position);

                }
            });
            voteHolder.ivOptionPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnPicClickListener!=null){
                        mOnPicClickListener.onPicClick(position,option);
                    }
                }
            });

        }
    }

    @Override
    public int getItemCount() {

        return mOptionList.size()+1;
    }


    @Override
    public int getItemViewType(int position) {
        if (position==mOptionList.size()){
            return TYPE_ADD_BUTTON;
        }else {
            return TYPE_NORMAL;
        }
    }

    public void add(){
        mOptionList.add(new VoteOption());
        notifyItemInserted(mOptionList.size());
    }

    public void remove(VoteOption option,int pos){
        if (mOptionList.size()<=2){
            Toast.makeText(mContext, "选项数不能少于2", Toast.LENGTH_SHORT).show();
            return;
        }
        mOptionNameMap.remove(option.hashCode());
        mOptionList.remove(option);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, mOptionList.size() - pos);
        //移除后相应的最多选项也要重置
        if (mVoteWrap.getMaxCount()>mOptionList.size()){
            mVoteWrap.setMaxCount(mOptionList.size());
            tvMaxSelect.setText(mOptionList.size()+"");
        }
    }

    public void setPicFile(int position,File file) {
        mOptionList.get(position).setPicFile(file);
        notifyItemChanged(position);
    }

    public void setOnPicClickListener(OnPicClickListener onPicClickListener) {
        mOnPicClickListener = onPicClickListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_add:
                if (mVoteWrap.getMaxCount()<mOptionList.size()){
                    int maxSelectable = mVoteWrap.getMaxCount();
                    maxSelectable++;
                    mVoteWrap.setMaxCount(maxSelectable);
                    tvMaxSelect.setText(maxSelectable+"");
                }else {
                    Toast.makeText(mContext, "选项数目没这么多", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.bt_minus:

                if (mVoteWrap.getMaxCount()>1){
                    int maxSelectable = mVoteWrap.getMaxCount();
                    maxSelectable--;
                    mVoteWrap.setMaxCount(maxSelectable);
                    tvMaxSelect.setText(maxSelectable+"");
                }else {
                    Toast.makeText(mContext, "已经最小了", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btn_submit:
                if (mOnPicClickListener!=null){
                    for (VoteOption voteOption : mOptionList) {
                        EditText editText = mOptionNameMap.get(voteOption.hashCode());
                        if (editText!=null){
                            if (TextUtils.isEmpty(editText.getText())) {
                                editText.setError("选项不能为空");
                                return;
                            }
                            voteOption.setOptionName(editText.getText().toString());
                        }
                    }
                    mVoteWrap.setOptionList(mOptionList);
                    mOnPicClickListener.onSubmitClick(mVoteWrap);
                }
                break;
        }
    }

    static class AddVoteHolder extends RecyclerView.ViewHolder{
        EditText etOption;
        ImageView ivDelete;
        ImageView ivOptionPic;

        public AddVoteHolder(View itemView) {
            super(itemView);
            etOption = itemView.findViewById(R.id.et_voteOption);
            ivDelete = itemView.findViewById(R.id.iv_deleteOption);
            ivOptionPic = itemView.findViewById(R.id.iv_addOptionPic);
        }
    }

    static class AddButtonHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.imageButton)
        ImageButton btnAddOption;
        @BindView(R.id.rl_multiSelect)
        View rlMultiSelect;
        @BindView(R.id.rdg_voteStartTime)
        RadioGroup rdgVoteStartTime;
        @BindView(R.id.swc_voteNoName)
        Switch swcVoteNoName;
        @BindView(R.id.bt_add)
        Button btnAddMaxCount;
        @BindView(R.id.tv_maxSelcted)
        TextView mTvMaxSelcted;
        @BindView(R.id.bt_minus)
        Button mBtMinus;
        @BindView(R.id.btn_submit)
        Button btnSubmit;
        public AddButtonHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnPicClickListener{
        void onPicClick(int position,VoteOption option);
        void onSubmitClick(VoteWrap voteWrap);
    }
}
