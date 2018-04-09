package com.gzz100.zbh.home.appointment.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.adapter.SelectTimeAdapter;
import com.gzz100.zbh.data.entity.MeetingRoomEntity;
import com.gzz100.zbh.utils.GlideApp;
import com.gzz100.zbh.utils.TimeFormatUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Lam on 2018/1/30.
 */

public class RoomAdapter extends RecyclerView.Adapter {

    private final int TYPE_ROOM = 112;
    private final int TYPE_STATE = 113;
    private final int TYPE_TIME_TABLE = 114;


    private int mStatePosition = -1;
    private int mTimetablePosition = -1;

    private Context mContext;
    private List<MeetingRoomEntity> mRoomList;

    OnItemClickListener mOnItemClickListener;

    public RoomAdapter(Context context) {
        mContext = context;
        mRoomList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case TYPE_ROOM:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_room, parent, false);
                viewHolder = new RoomHolder(view);
                break;
            case TYPE_STATE:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_state, parent, false);
                viewHolder = new StateHolder(view);
                break;
            case TYPE_TIME_TABLE:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_recycler_view, parent, false);
                viewHolder = new TimeHolder(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_ROOM:
                RoomHolder roomHolder = (RoomHolder) holder;
                bindDataToRoom(position,roomHolder);
                break;
            case TYPE_STATE:
                StateHolder stateHolder = (StateHolder) holder;
                bindState(stateHolder);
                break;
            case TYPE_TIME_TABLE:
                TimeHolder timeHolder= (TimeHolder) holder;
                bindTimeTable(timeHolder,position);
                break;
        }
    }

    private void bindState(StateHolder stateHolder) {
        if (mStatePosition==0){
            stateHolder.tvSuggestion.setVisibility(View.VISIBLE);
            stateHolder.tvState.setText("此时段无可预约会议室");
        }else {
            stateHolder.tvSuggestion.setVisibility(View.GONE);
            stateHolder.tvState.setText("此时段以下会议室不能预约");
        }
    }

    private void bindTimeTable(TimeHolder timeHolder,int position) {

        timeHolder.recyclerView.setLayoutManager(
                new GridLayoutManager(mContext,5,
                        GridLayoutManager.HORIZONTAL,false));
        SelectTimeAdapter adapter = new SelectTimeAdapter(mContext);
        int realPosition = getIndexInList(position);
        //这个是显示时间表的,但是时间表显示的内容是时间表在listview中的位置的前一个roomItem,所以位置要减去1
        realPosition--;
        if (realPosition>=mRoomList.size()){
            return;
        }
        MeetingRoomEntity roomEntity = mRoomList.get(realPosition);
        adapter.setBookedTime(roomEntity.getMeetingList());
        timeHolder.recyclerView.setAdapter(adapter);
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int pos = (hour-7)*5;
        timeHolder.recyclerView.smoothScrollToPosition(pos);
    }

    private void bindDataToRoom(final int position, RoomHolder holder){

        int index = getIndexInList(position);
        final MeetingRoomEntity room = mRoomList.get(index);
        GlideApp.with(mContext)
                .load(room.getMeetingPlacePic())
                .placeholder((R.drawable.ic_insert_chart_blue_500_48dp))
                .into(holder.mIvRoomPic);
        holder.mTvRoomName.setText(room.getMeetingPlaceName());
        holder.mTvCapcity.setText(room.getMeetingPlaceCapacity()+"人");
        String[] tabs = room.getMeetingPlaceTab().split("、");
        int size = tabs.length;

        switch (size) {
            case 4:
                holder.mTvTag4.setVisibility(View.VISIBLE);
                holder.mTvTag4.setText(tabs[3]);
            case 3:
                holder.mTvTag3.setVisibility(View.VISIBLE);
                holder.mTvTag3.setText(tabs[2]);
            case 2:
                holder.mTvTag2.setVisibility(View.VISIBLE);
                holder.mTvTag2.setText(tabs[1]);
            case 1:
                holder.mTvTag1.setVisibility(View.VISIBLE);
                holder.mTvTag1.setText(tabs[0]);
        }
        int unablePosition=index;
        if (mStatePosition!=-1){
            if (position>mStatePosition){
                unablePosition++;
            }
        }
        final int roomPosition = unablePosition;
        if (!room.isEnable()){
            holder.rootView.setBackgroundColor(Color.GRAY);
        }else {
            holder.rootView.setBackgroundColor(Color.WHITE);
        }
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClick(room, roomPosition);
                }
            }
        });

    }

    private int getIndexInList(int pos){

        int realPos = pos;
        if (mStatePosition!=-1){//stateItem展示出来了
            if (pos>mStatePosition){//是否比stateItem更后
                realPos--;
            }
        }
        if (mTimetablePosition!=-1){//显示了时间表
            if (pos>mTimetablePosition){//在时间表后面的
                realPos--;
            }
        }

        return realPos;
    }

    @Override
    public int getItemCount() {
        int size = mRoomList.size();
        if (mTimetablePosition != -1) {
            size++;
        }
        if (mStatePosition != -1) {
            size++;
        }
        return size;
    }

    @Override
    public int getItemViewType(int position) {

        if (position == mTimetablePosition) {
            return TYPE_TIME_TABLE;
        } else if (position == mStatePosition) {
            return TYPE_STATE;
        } else {
            return TYPE_ROOM;
        }
    }

    /**
     * 显示不可预约会议室的预约情况
     * @param roomPosition 会议室在recyclerview中的position
     */
    public int showTimetable(int roomPosition) {
        if (mTimetablePosition!=-1){
            hideTimetable();
        }else {
            this.mTimetablePosition = roomPosition + 1;
            notifyItemInserted(mTimetablePosition);

        }
        return mTimetablePosition;
    }

    public void hideTimetable() {
        int temp = mTimetablePosition;
        mTimetablePosition = -1;
        notifyItemRemoved(temp);
    }

    /**
     * 设置不能被预约的会议室
     * @param rooms
     */
    public void setUnableRoomData(List<MeetingRoomEntity> rooms,String startTime){
        for (MeetingRoomEntity meetingRoomEntity : mRoomList) {
            meetingRoomEntity.setEnable(true);//先重置,不然再次选择时间后可预约的会议室显示的不可预约
            for (MeetingRoomEntity room : rooms) {
                if (room.getMeetingPlaceId().equals(meetingRoomEntity.getMeetingPlaceId())) {
                    long startTimeMillis = TimeFormatUtil.formatTimeMillis(startTime);
                    for (MeetingRoomEntity.MeetingListBean meetingListBean : room.getMeetingList()) {
                        if (startTimeMillis>=meetingListBean.getStartTime()
                                &&startTimeMillis<meetingListBean.getEndTime()) {
                            meetingRoomEntity.setEnable(false);
                        }
                    }

                    meetingRoomEntity.setMeetingList(room.getMeetingList());
                }
                break;
            }
        }
        if (rooms==null||rooms.size()==0){
            for (MeetingRoomEntity entity : mRoomList) {
                entity.setEnable(true);
            }
        }
        List<MeetingRoomEntity> copy = new ArrayList<>();
        copy.addAll(mRoomList);
        refreshData(copy);
    }

    public void refreshData(List<MeetingRoomEntity> rooms){
        mRoomList.clear();
        hideTimetable();
        List<MeetingRoomEntity> disableRooms = new ArrayList<>();
        List<MeetingRoomEntity> enableRooms = new ArrayList<>();
        for (MeetingRoomEntity room : rooms) {
            if (!room.isEnable()) {
                disableRooms.add(room);
            }else {
                enableRooms.add(room);
            }
        }

        mRoomList.addAll(enableRooms);
        mRoomList.addAll(disableRooms);
        if (disableRooms.size()==0){
            hideState();
        }else {
            showState(enableRooms.size());//放在可预约会议室下方
        }
    }

    private void showState(int positionToShow){
        mStatePosition = positionToShow;
        notifyDataSetChanged();
    }

    private void hideState() {
        mStatePosition = -1;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    static class RoomHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_root)
        View rootView;
        @BindView(R.id.iv_room)
        ImageView mIvRoomPic;
        @BindView(R.id.tv_room_name)
        TextView mTvRoomName;
        @BindView(R.id.tv_tag1_room)
        TextView mTvTag1;
        @BindView(R.id.tv_tag2_room)
        TextView mTvTag2;
        @BindView(R.id.tv_tag3_room)
        TextView mTvTag3;
        @BindView(R.id.tv_tag4_room)
        TextView mTvTag4;
        @BindView(R.id.tv_capcity_room)
        TextView mTvCapcity;

        public RoomHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    static class TimeHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        public TimeHolder(View itemView) {
            super(itemView);
            recyclerView=itemView.findViewById(R.id.recyclerView);
        }
    }

    static class StateHolder extends RecyclerView.ViewHolder {
        TextView tvState;
        TextView tvSuggestion;

        public StateHolder(View itemView) {
            super(itemView);
            tvState = itemView.findViewById(R.id.tv_tips_state);
            tvSuggestion = itemView.findViewById(R.id.tv_suggestion_state);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(MeetingRoomEntity room,int position);
    }

}
