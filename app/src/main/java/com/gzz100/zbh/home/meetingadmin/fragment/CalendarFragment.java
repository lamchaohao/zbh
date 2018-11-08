package com.gzz100.zbh.home.meetingadmin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseBackFragment;
import com.gzz100.zbh.data.ObserverImpl;
import com.gzz100.zbh.data.entity.MeetingEntity;
import com.gzz100.zbh.data.eventEnity.PushUpdateEntity;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.MeetingRequest;
import com.gzz100.zbh.home.meetingadmin.adapter.MeetingListAdapter;
import com.gzz100.zbh.res.Common;
import com.gzz100.zbh.widget.calendarview.GroupItemDecoration;
import com.gzz100.zbh.widget.calendarview.GroupRecyclerView;
import com.gzz100.zbh.widget.calendarview.MeetingGroupAdapter;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;

/**
 * Created by Lam on 2018/10/15.
 */

public class CalendarFragment extends BaseBackFragment implements
        CalendarView.OnCalendarSelectListener,
        CalendarView.OnYearChangeListener{

    @BindView(R.id.calendarLayout)
    CalendarLayout mCalendarLayout;
    @BindView(R.id.tv_month_day)
    TextView mTvMonthDay;
    @BindView(R.id.tv_year)
    TextView mTvYear;
    @BindView(R.id.tv_lunar)
    TextView mTvLunar;
    @BindView(R.id.ib_calendar)
    ImageView mIbCalendar;
    @BindView(R.id.tv_current_day)
    TextView mTvCurrentDay;
    @BindView(R.id.fl_current)
    FrameLayout mFlCurrent;
    @BindView(R.id.rl_tool)
    RelativeLayout mRlTool;
    @BindView(R.id.calendarView)
    CalendarView mCalendarView;
    @BindView(R.id.recyclerView)
    GroupRecyclerView mRecyclerView;
    Unbinder unbinder;
    private int mYear;
    private List<MeetingEntity> mMeetings;
    private MeetingGroupAdapter mAdapter;
    private MeetingRequest mRequest;

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_calendar, null);
        unbinder = ButterKnife.bind(this, view);
        return attachToSwipeBack(view);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
    }

    protected void initView() {
        mCalendarLayout.expand();
        mTvMonthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCalendarLayout.isExpand()) {
                    mCalendarView.showYearSelectLayout(mYear);
                    return;
                }
                mCalendarView.showYearSelectLayout(mYear);
                mTvLunar.setVisibility(View.GONE);
                mTvYear.setVisibility(View.GONE);
                mTvMonthDay.setText(String.valueOf(mYear));
            }
        });
        mFlCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.scrollToCurrent();
            }
        });
        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnYearChangeListener(this);
        mTvYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        Logger.i("year="+mYear);
        mTvMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        mTvLunar.setText("今日");
        mTvCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));
    }

    private void initData(){
        mMeetings = new ArrayList();
        mAdapter = new MeetingGroupAdapter(getContext(), mMeetings);
        mRequest = new MeetingRequest();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new GroupItemDecoration<String,MeetingEntity>());
        mRecyclerView.setAdapter(mAdapter);

        StringBuilder sb=new StringBuilder();
        sb.append(mCalendarView.getCurYear());
        if (mCalendarView.getCurMonth()<10){
            sb.append("0"+mCalendarView.getCurMonth());
        }else {
            sb.append(mCalendarView.getCurMonth());
        }

        if (mCalendarView.getCurDay()<10){
            sb.append("0").append(mCalendarView.getCurDay());
        }else {
            sb.append(mCalendarView.getCurDay());
        }


        loadData(sb.toString());
        mAdapter.setOnItemClickListener(new MeetingListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                dealOnMeetingItemClick(pos);
            }

            @Override
            public void onItemLongClick(int pos) {

            }
        });
    }

    private void dealOnMeetingItemClick(int pos) {
        MeetingEntity meetingEntity = mMeetings.get(pos);
        meetingEntity.setUnread("");
        mAdapter.notifyItemChanged(pos);
        PushUpdateEntity pushUpdateEntity = new PushUpdateEntity(PushUpdateEntity.PassthrougMsgType.meetingDecrement);
        EventBus.getDefault().post(pushUpdateEntity);
        if(meetingEntity.getMeetingStatus()== Common.STATUS_END){
            startFragment(FinishedMeetingFragment.getNewInstance
                    (meetingEntity.getMeetingId(),meetingEntity.getMeetingName()));
//
        }else {
            long groupId=0;
            if (!TextUtils.isEmpty(meetingEntity.getMimcTopicId())) {
                groupId = Long.parseLong(meetingEntity.getMimcTopicId());
            }
            startFragment(MeetingParentFragment.getNewInstance(meetingEntity.getMeetingId(),meetingEntity.getMeetingName(), groupId));
        }
    }

    private void loadData(String time){
        Logger.i(time);
        ObserverImpl<HttpResult<List<MeetingEntity>>> observer = new ObserverImpl<HttpResult<List<MeetingEntity>>>() {

            @Override
            protected void onResponse(HttpResult<List<MeetingEntity>> result) {
                List<MeetingEntity> meetingList = result.getResult();

                if (meetingList!=null){
                    mMeetings.clear();
                    mMeetings.addAll(meetingList);
                    mAdapter.notifyDataSetChanged();

                }else {
                    Toasty.error(getContext(),"今日无会议").show();
                }
            }

            @Override
            protected void onFailure(Throwable e) {
                Toasty.error(getContext(),e.getMessage()).show();
            }
        };

        mRequest.getMeetingListByTime(observer,0,40,time);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        mTvLunar.setVisibility(View.VISIBLE);
        mTvYear.setVisibility(View.VISIBLE);
        mTvMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        mTvYear.setText(String.valueOf(calendar.getYear()));
        mTvLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();

        mMeetings.clear();
        mAdapter.notifyDataSetChanged();

        int curMonth = calendar.getMonth();

        StringBuilder sb=new StringBuilder();
        sb.append(mCalendarView.getCurYear());
        if (curMonth<10){
            sb.append("0"+curMonth);
        }else {
            sb.append(curMonth);
        }

        if (calendar.getDay()<10){
            sb.append("0"+calendar.getDay());
        }else {
            sb.append(calendar.getDay());
        }

        loadData(sb.toString());
    }

    @Override
    public void onYearChange(int year) {
        mTvMonthDay.setText(String.valueOf(year));
    }
}
