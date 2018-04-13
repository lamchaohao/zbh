package com.gzz100.zbh.home.appointment.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gzz100.zbh.R;
import com.gzz100.zbh.data.entity.MeetingRoomEntity;
import com.gzz100.zbh.home.appointment.entity.BookedTime;
import com.gzz100.zbh.home.appointment.entity.TimeBlock;
import com.gzz100.zbh.utils.DensityUtil;
import com.gzz100.zbh.utils.TimeFormatUtil;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * 选择时间方块视图中的适配器
 * Created by Lam on 2018/1/9.
 */

public class SelectTimeAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private final static int TYPE_TIME=100;
    private final static int TYPE_ENABLE=200;
    private final static int TYPE_GONE_TIME=300;
    private final static int TYPE_FULL=400;
    private List<TimeBlock> mTimeBlockList;
    private int lastPos=-1;
    private int currentSelectStartTimePos = -1;
    private int currentSelectEndTimePos = -1;

    private OnItemClickListener mOnItemClickListener;
    private OnBookTimeChangeListener mOnBookTimeChangeListener;
    public SelectTimeAdapter(Context context) {
        mContext = context;
        mTimeBlockList = new ArrayList<>();
        initTimes(TimeFormatUtil.formatDate(Calendar.getInstance().getTime()));
    }

    private void initTimes(String date) {
        List<TimeBlock> timeBlocks=new ArrayList<>();
        for (int i = 0; i < 64; i++) {
            TimeBlock timeBlock =new TimeBlock();
            Calendar calendar = Calendar.getInstance();
            int left= i%4;
            int minute=left*15;
            long millis = TimeFormatUtil.formatDateToMillis(date);
            calendar.setTimeInMillis(millis);
            calendar.set(Calendar.HOUR_OF_DAY,7+i/4);
            calendar.set(Calendar.MINUTE,minute);
            calendar.set(Calendar.SECOND,0);
            timeBlock.setTime(calendar);
            timeBlocks.add(timeBlock);
        }
        setTimeData(timeBlocks);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup inflate = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.item_select_time, parent,false);
        switch (viewType){
            case TYPE_GONE_TIME:
                inflate.removeView(inflate.findViewById(R.id.tv_item_select_time));
                break;
            case TYPE_ENABLE:
                inflate.removeView(inflate.findViewById(R.id.tv_item_select_time));
                break;
            case TYPE_FULL:
                inflate.removeView(inflate.findViewById(R.id.iv_item_select_time));
                break;
            case TYPE_TIME:
                inflate.removeView(inflate.findViewById(R.id.iv_item_select_time));
                break;
        }
        return new TimeBlockViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        int itemViewType = getItemViewType(position);
        final TimeBlockViewHolder blockHolder = (TimeBlockViewHolder) holder;
        switch (itemViewType){
            case TYPE_GONE_TIME:
                blockHolder.imageView.setImageResource(R.drawable.ic_not_interested_black_18dp);
                blockHolder.imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                blockHolder.rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toasty.info(mContext,"光阴一去不复返").show();
                    }
                });
                break;
            case TYPE_ENABLE:
                final TimeBlock realTimeBlock = getRealTimeBlock(position);

                if (realTimeBlock.isSelect()) {
                    blockHolder.rootView.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
                    blockHolder.imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    blockHolder.imageView.setImageResource(R.drawable.ic_check_white_24dp);
                }else {
//                    blockHolder.rootView.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
                    blockHolder.imageView.setImageResource(0);
                    blockHolder.rootView.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
                }
                blockHolder.rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener!=null){
                            changeSelectTime(position);
                            mOnItemClickListener.onItemClick(realTimeBlock);
                        }
                    }
                });
                break;
            case TYPE_FULL:
                TimeBlock timeBlock = getRealTimeBlock(position);
                String departmentName = timeBlock.getMeetingBean().getDepartmentName();
                String dptName = "";
                if (departmentName.length()>=2) {
                    dptName = departmentName.substring(0, 2);
                }else {
                    dptName = "预";
                }
                blockHolder.textView.setText(dptName);
                blockHolder.rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimeBlock block = getRealTimeBlock(position);
                        showInfo(block,blockHolder.rootView);
                    }
                });
                break;
            case TYPE_TIME:
                blockHolder.textView.setText((position/5+7)+":00");
                blockHolder.rootView.setBackgroundColor(Color.TRANSPARENT);
                blockHolder.textView.setGravity(Gravity.BOTTOM|Gravity.CENTER);
                blockHolder.textView.setHeight(DensityUtil.dp2px(mContext,24));
                break;
        }

    }

    private void showInfo(TimeBlock timeBlock,View rootView){
        final QMUIPopup qmuiPopup = new QMUIPopup(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_simple_meeting_info, null);
        qmuiPopup.setContentView(view);
        MeetingRoomEntity.MeetingListBean meeting = timeBlock.getMeetingBean();
        ((TextView)view.findViewById(R.id.tv_simple_room)).setText(meeting.getMeetingId());
        ((TextView)view.findViewById(R.id.tv_simple_staff)).setText(meeting.getApplicant());
        StringBuilder sb=new StringBuilder();
        String start = TimeFormatUtil.formatDateAndTime(meeting.getStartTime());
        String end = TimeFormatUtil.formatDateAndTime(meeting.getEndTime());
        sb.append(start).append("到").append("\n").append(end);
        ((TextView)view.findViewById(R.id.tv_simple_time)).setText(sb.toString());
        ((TextView)view.findViewById(R.id.tv_simple_depart)).setText(meeting.getDepartmentName());
        view.findViewById(R.id.btn_simple_comfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qmuiPopup.dismiss();
            }
        });
        qmuiPopup.show(rootView);
    }

    /**
     * 改变所选择的时间块
     * @param initPos 在recyclerView中的位置
     */
    private void changeSelectTime(int initPos) {
        int currentPressPos = getRealPosition(initPos);
        if (lastPos == -1){
            currentSelectStartTimePos=currentPressPos;
            currentSelectEndTimePos=currentPressPos;
            lastPos=currentPressPos;
            mTimeBlockList.get(currentPressPos).setSelect(true);
            notifyItemChanged(initPos);
            if (mOnBookTimeChangeListener!=null){
                long maxTime = mTimeBlockList.get(currentSelectEndTimePos).getTime().getTimeInMillis();
                maxTime+=15*60000;
                long minTime = mTimeBlockList.get(currentSelectStartTimePos).getTime().getTimeInMillis();
                mOnBookTimeChangeListener.onBookTimeChange(minTime,maxTime);
            }
            return;
        }
        if (currentPressPos > currentSelectStartTimePos
                &&currentPressPos < currentSelectEndTimePos){
            //1.所选择的时间块处于之前区间中的,有前有后
            currentSelectEndTimePos=currentPressPos;
        }else if (currentPressPos<currentSelectStartTimePos
                &&currentPressPos<currentSelectEndTimePos){
            //2.所选择的时间块为之前区间更前的,无前有后
            for (int i = currentPressPos; i < currentSelectStartTimePos; i++) {
                if (mTimeBlockList.get(i).isBooked()) {
                    Toast.makeText(mContext, "所选时间段已有预约,不能太贪心", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            currentSelectStartTimePos=currentPressPos;
        }else if (currentPressPos>currentSelectStartTimePos
                &&currentPressPos>currentSelectEndTimePos){
            //3.所选择的时间块为之前区间更后的,有前无后
            for (int i = currentSelectEndTimePos; i < currentPressPos; i++) {
                if (mTimeBlockList.get(i).isBooked()) {
                    Toast.makeText(mContext, "所选时间段已有预约,不能太贪心", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
           currentSelectEndTimePos=currentPressPos;
        }else if (currentPressPos==currentSelectStartTimePos){
            //4.所选择的时间块为起始或终点
            //4.1 为起始,则全不选
            for (TimeBlock timeBlock : mTimeBlockList) {
                timeBlock.setSelect(false);
            }
            lastPos=-1;
            currentSelectStartTimePos=-1;
            currentSelectEndTimePos=-1;
            notifyDataSetChanged();
            if (mOnBookTimeChangeListener!=null){
                mOnBookTimeChangeListener.onBookTimeChange(0,0);
            }
            return;
        }else if (currentPressPos==currentSelectEndTimePos){
            //4.2 为终点,则终点反选
            currentSelectEndTimePos--;
        }

//        lastPos=currentPressPos;
        for (int i = 0; i < mTimeBlockList.size(); i++) {
            if (i>=currentSelectStartTimePos&&i<=currentSelectEndTimePos){
                mTimeBlockList.get(i).setSelect(true);
            }else {
                mTimeBlockList.get(i).setSelect(false);
            }
        }
        if (mOnBookTimeChangeListener!=null){
            long maxTime = mTimeBlockList.get(currentSelectEndTimePos).getTime().getTimeInMillis();
            maxTime+=15*60000;
            long minTime = mTimeBlockList.get(currentSelectStartTimePos).getTime().getTimeInMillis();
            mOnBookTimeChangeListener.onBookTimeChange(minTime,maxTime);
        }
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        //每小时分4个小格子,每个小格子代表15分钟,从07时开始到22时,共16小时,每小时5格,其中一格显示整点时间,共计16*5=80
        return 80;
    }

    @Override
    public int getItemViewType(int position) {
        if (position%5==0){
            return TYPE_TIME;
        }else {
            TimeBlock realTimeBlock = getRealTimeBlock(position);
            if (realTimeBlock.isBooked()) {
                return TYPE_FULL;
            }else if (realTimeBlock.isGone()){
                return TYPE_GONE_TIME;
            }else {
                return TYPE_ENABLE;
            }
        }
    }

    private void setTimeData(List<TimeBlock> timeBlocks){
        mTimeBlockList.clear();
        long currentTime = System.currentTimeMillis();
        for (TimeBlock timeBlock : timeBlocks) {
            long timeInMillis = timeBlock.getTime().getTimeInMillis();
            if (timeInMillis<currentTime){//过去的时间
                timeBlock.setGone(true);
            }
            mTimeBlockList.add(timeBlock);
        }
        notifyDataSetChanged();
    }

    /**
     * 设置选择的时间,主要是为了从其他页面传参,无需手动点击选时间
     * @param bookedTime 选择的时间区域,分开始时间和结束时间
     */
    public void setSelectTimeBlock(BookedTime bookedTime){
        Calendar startTime = bookedTime.getStartTime();
        Calendar endTime = bookedTime.getEndTime();
        boolean isFirst=true;
        for (int i = 0; i < mTimeBlockList.size(); i++) {
            TimeBlock timeBlock = mTimeBlockList.get(i);
            long timeInMillis = timeBlock.getTime().getTimeInMillis();
            if (timeInMillis>=startTime.getTimeInMillis()&&timeInMillis<=endTime.getTimeInMillis()) {
                if (isFirst) {
                    //设置相关的参数,避免点击时候错误
                    currentSelectStartTimePos=i;
                    isFirst=false;
                }
                currentSelectEndTimePos=i;
                lastPos = i;
                timeBlock.setSelect(true);
            }
            long currentTime = System.currentTimeMillis();
            if (timeInMillis<currentTime){
                timeBlock.setGone(true);
            }
        }
        int tempPos = this.currentSelectEndTimePos;

        changeSelectTime(getInitialPos(tempPos));

    }

    /**
     * 设置预定的时间表
     * @param bookedTimeList 预定的时间表,时间表里有分段的预定时间
     */
    public void setBookedTime(List<MeetingRoomEntity.MeetingListBean> bookedTimeList){
        for (MeetingRoomEntity.MeetingListBean bookedTime : bookedTimeList) {

            for (TimeBlock timeBlock : mTimeBlockList) {
                long timeInMillis = timeBlock.getTime().getTimeInMillis();
                if (timeInMillis>=bookedTime.getStartTime()&&timeInMillis<bookedTime.getEndTime()) {
                    timeBlock.setBooked(true);
                    timeBlock.setMeetingBean(bookedTime);
                }
                long currentTime = System.currentTimeMillis();
                if (timeInMillis<currentTime){
                    timeBlock.setGone(true);
                }
            }

        }
        notifyDataSetChanged();
    }


    public void setAppointmentDate(String date){
        initTimes(date);
    }

    /**
     * 获取真正的时间块,在RecyclerView中是有多种类型的子view的,而timeblock又是独立放在一个list中,
     * 此方法提供传入recyclerview中的position获取在list集合中的timeBlock
     * @param position recyclerView中的position
     * @return
     */
    private TimeBlock getRealTimeBlock(int position){
        int index= position-(position/5+1);
        return mTimeBlockList.get(index);
    }

    /**
     * 根据TimeBlockList中的index来获取原始recyclerView中的position
     * @param realPos TimeBlockList中的index
     * @return 原始recyclerView中的position
     */
    private int getInitialPos(int realPos){
        int initPos = ((realPos + 1) * 5) / 4;
        return initPos;
    }

    /**
     * 根据传入的recyclerView中的position,获取在TimeBlockList集合中的index
     * @param position recyclerView中的position
     * @return TimeBlockList集合中的index
     */
    private int getRealPosition(int position){
        return position-(position/5+1);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnBookTimeChangeListener(OnBookTimeChangeListener onBookTimeChangeListener) {
        mOnBookTimeChangeListener = onBookTimeChangeListener;
    }

    static class TimeBlockViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;
        FrameLayout rootView;
        public TimeBlockViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_item_select_time);
            imageView = itemView.findViewById(R.id.iv_item_select_time);
            rootView = itemView.findViewById(R.id.fl_item_root);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(TimeBlock timeBlock);
    }
    public interface OnBookTimeChangeListener{
        void onBookTimeChange(long startTime,long endTime);
    }
}
