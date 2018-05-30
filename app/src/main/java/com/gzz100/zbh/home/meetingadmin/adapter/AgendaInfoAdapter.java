package com.gzz100.zbh.home.meetingadmin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.data.entity.MeetingInfoEntity;

import java.util.List;

/**
 * Created by Lam on 2018/3/23.
 */

public class AgendaInfoAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<MeetingInfoEntity.AgendaListBean> agendaList;

    public AgendaInfoAdapter(Context context, List<MeetingInfoEntity.AgendaListBean> agendaList) {
        mContext = context;
        this.agendaList = agendaList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_agenda_info, parent, false);
        return new AgendaHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AgendaHolder viewHolder = (AgendaHolder) holder;
        MeetingInfoEntity.AgendaListBean agenda = agendaList.get(position);
        viewHolder.tvSpeaker.setText("主讲人: "+agenda.getSpeaker());
        int index = position+1;
        viewHolder.tvAgendaName.setText("议程"+index+": "+agenda.getAgendaName());
    }

    @Override
    public int getItemCount() {
        return agendaList.size();
    }

    static class AgendaHolder extends RecyclerView.ViewHolder{
        TextView tvAgendaName;
        TextView tvSpeaker;
        public AgendaHolder(View itemView) {
            super(itemView);
            tvAgendaName = itemView.findViewById(R.id.tv_info_agendaName);
            tvSpeaker = itemView.findViewById(R.id.tv_info_speaker);
        }
    }

}
