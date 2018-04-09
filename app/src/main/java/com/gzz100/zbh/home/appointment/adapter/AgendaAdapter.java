package com.gzz100.zbh.home.appointment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gzz100.zbh.R;
import com.gzz100.zbh.data.entity.Staff;
import com.gzz100.zbh.home.appointment.entity.Agenda;
import com.gzz100.zbh.utils.DensityUtil;
import com.gzz100.zbh.utils.TextHeadPicUtil;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

/**
 * an adapter for add agenda
 * Created by Lam on 2018/2/6.
 */

public class AgendaAdapter extends RecyclerView.Adapter {

    private List<Agenda> mAgendaList;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private OnSelectCountChangeListener mOnSelectCountChangeListener;
    private int headPicFontSize;
    private int headPicSize;
    private Map<Integer,EditText> titleMap;
    private static final int TYPE_ADD_BUTTON =1;
    private static final int TYPE_NORMAL=2;

    public AgendaAdapter(Context context) {
        mAgendaList = new ArrayList<>();
        mContext = context;
        headPicFontSize = DensityUtil.dp2px(mContext, 16);
        headPicSize = DensityUtil.dp2px(mContext, 36);
        titleMap = new HashMap<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==TYPE_ADD_BUTTON){
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_add_button, parent, false);
            return new AddButtonHolder(view);
        }else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_agenda, parent, false);
            return new AgendaHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int pos) {

        if (getItemViewType(pos)==TYPE_ADD_BUTTON) {

            AddButtonHolder addHolder= (AddButtonHolder) holder;
            addHolder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    add();
                }
            });
            return;
        }
        final AgendaHolder viewHolder = (AgendaHolder) holder;
        final int position =pos;
        final Agenda agenda = mAgendaList.get(position);
        if (agenda.getStaff()!=null){
            viewHolder.ivSpeakerPic.setImageDrawable(TextHeadPicUtil.getHeadPic(agenda.getStaff().getUserName(),
                    headPicFontSize,headPicSize));
        }else {
            viewHolder.ivSpeakerPic.setImageResource(R.drawable.ic_add_circle_outline_black_24dp);
        }
        EditText editText = titleMap.get(agenda.hashCode());
        if (editText!=null){
            Logger.i("title="+editText.getText().toString()+", pos="+position);
            viewHolder.etAgendaTitle.setText(editText.getText());
        }else {
            viewHolder.etAgendaTitle.setText("");
            Logger.i("editText==null"+", pos="+position);
        }

        titleMap.put(agenda.hashCode(),viewHolder.etAgendaTitle);
        if (mOnItemClickListener != null) {
            viewHolder.rlSelectSpeaker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(viewHolder.getAdapterPosition(),agenda);
                }
            });
        }
        viewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               remove(position,agenda);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAgendaList.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position==mAgendaList.size()){
            return TYPE_ADD_BUTTON;
        }else {
            return TYPE_NORMAL;
        }
    }

    static class AgendaHolder extends RecyclerView.ViewHolder {
        EditText etAgendaTitle;
        ImageView ivSpeakerPic;
        RelativeLayout rlSelectSpeaker;
        ImageView ivDelete;
        public AgendaHolder(View itemView) {
            super(itemView);
            etAgendaTitle = itemView.findViewById(R.id.et_agendaTitle);
            ivSpeakerPic = itemView.findViewById(R.id.iv_speakerPic);
            rlSelectSpeaker = itemView.findViewById(R.id.rl_selectSpeaker);
            ivDelete = itemView.findViewById(R.id.iv_delete);
        }
    }

    static class AddButtonHolder extends RecyclerView.ViewHolder{
        View rootView;
        public AddButtonHolder(View itemView) {
            super(itemView);
            rootView = itemView.findViewById(R.id.rootView);
        }
    }


    public void add(){
        mAgendaList.add(new Agenda());
        Logger.i("add size="+mAgendaList.size());
        notifyItemInserted(mAgendaList.size());
    }

    public void remove(int position,Agenda agenda){
        if (mAgendaList.size()>1) {
            mAgendaList.remove(position);
            notifyItemRemoved(position);
            //防止错位,采用局部刷新位置,重新刷新viewholder 使得点击事件不粗乱
            if (position != mAgendaList.size()) {
                notifyItemRangeChanged(position, mAgendaList.size() - position);
            }
            titleMap.remove(agenda.hashCode());
            Logger.i("remove map="+titleMap.size());
            Logger.i("remove position="+position+" ,size = "+mAgendaList.size());
        }else {
            Toasty.warning(mContext,"至少保留一个议程").show();
        }

    }

    public void setAgendaSpeaker(int pos,Staff speaker){
        mAgendaList.get(pos).setStaff(speaker);
        notifyItemChanged(pos);
    }

    public List<Agenda> getAgendaList(){
        for (Agenda agenda : mAgendaList) {
            EditText editText = titleMap.get(agenda.hashCode());
            agenda.setAgendaName(editText.getText().toString());
        }
        return mAgendaList;
    }

    public void onChanged() {
        if (mOnSelectCountChangeListener != null) {
            mOnSelectCountChangeListener.onSelectCountChange(mAgendaList.size());
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnSelectCountChange(OnSelectCountChangeListener onSelectCountChange) {
        mOnSelectCountChangeListener = onSelectCountChange;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, Agenda agenda);
    }

    public interface OnSelectCountChangeListener {
        void onSelectCountChange(int count);
    }
}
