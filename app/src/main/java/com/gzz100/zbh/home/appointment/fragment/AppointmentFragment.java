package com.gzz100.zbh.home.appointment.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseFragment;
import com.gzz100.zbh.data.ObserverImpl;
import com.gzz100.zbh.data.entity.MeetingRoomEntity;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.MeetingRoomRequest;
import com.gzz100.zbh.home.appointment.adapter.RoomAdapter;
import com.gzz100.zbh.utils.TimeFormatUtil;
import com.gzz100.zbh.widget.LineDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;


public class AppointmentFragment extends BaseFragment {


    @BindView(R.id.tv_apm_select_date)
    TextView mTvApmSelectDate;
    @BindView(R.id.tv_apm_select_time)
    TextView mTvApmDuration;
    @BindView(R.id.tv_apm_select_people)
    TextView mTvApmSelectPeople;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.rcv_apm_room)
    RecyclerView mRcvRoom;
    @BindView(R.id.iv_empty_room)
    ImageView mEmptyView;
    private RoomAdapter mAdapter;
    private Unbinder unbinder;
    private List<String> mHourList;
    private List<List<String>> mMinuteWrapList;
    List<MeetingRoomEntity> mRoomList;

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_appointment, null);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVar();
        initView();
        loadRoomData();
    }

    private void initVar() {
        mHourList = new ArrayList<>();
        mMinuteWrapList = new ArrayList<>();
    }

    private void loadRoomData() {
        MeetingRoomRequest request = new MeetingRoomRequest();
        Date time = Calendar.getInstance().getTime();
        String dateStr = TimeFormatUtil.formatDate(time);

        request.getMeetingRooms(new ObserverImpl<HttpResult<List<MeetingRoomEntity>>>() {

            @Override
            public void onComplete() {
                mRefreshLayout.finishRefresh();
            }

            @Override
            protected void onResponse(HttpResult<List<MeetingRoomEntity>> result) {
                loadData(result.getResult());
            }

            @Override
            protected void onFailure(Throwable e) {
                mRefreshLayout.finishRefresh(false);
                Toasty.error(_mActivity,e.getMessage()).show();
            }
        });

    }

    private void initView() {
        mRcvRoom.addItemDecoration(new LineDecoration(getContext()));
        mRcvRoom.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new RoomAdapter(getContext());
        mAdapter.setOnItemClickListener(new RoomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MeetingRoomEntity room, int position) {
                if (room.isEnable()) {
                    ((BaseFragment) getParentFragment())
                            .startParentFragment(ApmDetailFragment.newInstance(room.getMeetingPlaceId(),
                                    room.getMeetingPlaceName(), room.getMeetingPlaceCapacity(),
                                    room.getMeetingPlaceTab(), room.getMeetingPlacePic(),room.getNeedApply()));
                } else {
                    int i = mAdapter.showTimetable(position);
                    if (i != -1) {
                        mRcvRoom.smoothScrollToPosition(i);
                    }
                }

            }
        });
        mRcvRoom.setAdapter(mAdapter);

        mRefreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
        mRefreshLayout.setEnableLoadMore(false);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mTvApmSelectDate.setText("会议日期");
                mTvApmDuration.setText("会议时长");
                mTvApmSelectPeople.setText("会议人数");
                loadRoomData();
            }
        });
    }

    private void loadData(List<MeetingRoomEntity> roomList) {
        if (roomList!=null){
            mAdapter.refreshData(roomList);
            mRoomList = roomList;
            if (roomList.size()==0){
                mEmptyView.setVisibility(View.VISIBLE);
            }else {
                mEmptyView.setVisibility(View.GONE);
            }
        }else{
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }


    @OnClick({R.id.rl_apm_date, R.id.rl_apm_last, R.id.rl_apm_people})
    public void onViewClicked(View view) {
        final boolean[] option = new boolean[]{false, true, true, false, false, false};//显示类型 默认全部显示
        switch (view.getId()) {
            case R.id.rl_apm_date:

                TimePickerView pvTime = new TimePickerView.Builder(getContext(), new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        mTvApmSelectDate.setText(TimeFormatUtil.formatMonth(date));
                        showSelectTimeDialog(date.getDate());
                    }
                }).setType(option)
                        .build();
                //注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
                pvTime.setDate(Calendar.getInstance());
                pvTime.show();
                break;
            case R.id.rl_apm_last:
                showDurationDialog();
                break;
            case R.id.rl_apm_people:
                final List<String> peopleList = new ArrayList<>();
                peopleList.add("1-10人");
                peopleList.add("11-20人");
                peopleList.add("21-30人");
                peopleList.add("31-40人");
                OptionsPickerView picker = new OptionsPickerView.Builder(getContext(), new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {


                        mTvApmSelectPeople.setText(peopleList.get(options1));
                        int maxCapacity=0;
                        switch (options1) {
                            case 0:
                                maxCapacity=10;
                                break;
                            case 1:
                                maxCapacity=20;
                                break;
                            case 2:
                                maxCapacity=30;
                                break;
                            case 3:
                                maxCapacity=40;
                                break;
                        }
                        filter("",maxCapacity);
                    }
                }).build();
                picker.setPicker(peopleList);
                picker.show();
                break;
        }
    }

    private void filter(String time ,int maxCapacity) {

        List<MeetingRoomEntity> datas = mAdapter.getDatas();
        List<MeetingRoomEntity> filterData = new ArrayList<>();
        if (datas!=null) {
            for (MeetingRoomEntity roomEntity : datas) {
                if (roomEntity.getMeetingPlaceCapacity()<maxCapacity) {
                    roomEntity.setEnable(false);
                }else {
                    roomEntity.setEnable(true);
                    filterData.add(roomEntity);
                }
            }
            mAdapter.notifyDataSetChanged();
        }

    }

    private void showDurationDialog() {
        final List<String> durationList = new ArrayList<>();
        durationList.add("15分钟");
        durationList.add("30分钟");
        durationList.add("45分钟");
        durationList.add("60分钟");
        durationList.add("90分钟");
        durationList.add("120分钟");
        OptionsPickerView picker = new OptionsPickerView.Builder(getContext(), new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                mTvApmDuration.setText(durationList.get(options1));
            }
        }).build();
        picker.setPicker(durationList);
        picker.show();
    }

    private void showSelectTimeDialog(int selectedDate) {
        mHourList.clear();
        mMinuteWrapList.clear();

        int date = Calendar.getInstance().get(Calendar.DATE);

        Calendar instance = Calendar.getInstance(Locale.CHINESE);
        int hour = instance.get(Calendar.HOUR_OF_DAY);
        int minute = instance.get(Calendar.MINUTE);

        if (selectedDate != date) {
            List<String> minuteList = new ArrayList<>();
            minuteList.add("00");
            minuteList.add("15");
            minuteList.add("30");
            minuteList.add("45");
            for (int i=7;i<23;i++){
                mHourList.add(i + "");
                mMinuteWrapList.add(minuteList);
            }

        }else {
            for (int i = minute < 45 ? hour : hour + 1; i < 24; i++) {
                mHourList.add(i + "");
                if (i == hour) {
                    List<String> spMinuteList = new ArrayList<>();
                    if (minute > 30) {
                        spMinuteList.add("45");//前面已经判断了>45
                        mMinuteWrapList.add(spMinuteList);
                    } else {
                        if (minute > 15) {
                            spMinuteList.add("30");
                            spMinuteList.add("45");
                            mMinuteWrapList.add(spMinuteList);
                        } else {
                            spMinuteList.add("15");
                            spMinuteList.add("30");
                            spMinuteList.add("45");
                            mMinuteWrapList.add(spMinuteList);
                        }
                    }
                } else {
                    List<String> minuteList = new ArrayList<>();
                    minuteList.add("00");
                    minuteList.add("15");
                    minuteList.add("30");
                    minuteList.add("45");
                    mMinuteWrapList.add(minuteList);
                }
            }

        }
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(getContext(), new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                StringBuilder sb = new StringBuilder(mTvApmSelectDate.getText().toString());
                sb.append(" ");
                sb.append(mHourList.get(options1));
                sb.append(":");
                sb.append(mMinuteWrapList.get(options1).get(option2));
                mTvApmSelectDate.setText(sb.toString());

                StringBuilder sbStartTime=new StringBuilder();
                sbStartTime.append(Calendar.getInstance().get(Calendar.YEAR));
                sbStartTime.append("-");
                sbStartTime.append(mTvApmSelectDate.getText());
                startQueryData(sbStartTime.toString());

            }
        }).setLabels("时", "分", "秒").build();
        pvOptions.setPicker(mHourList, mMinuteWrapList);
        pvOptions.show();
    }

    private void startQueryData(final String startTime) {

        MeetingRoomRequest request = new MeetingRoomRequest();
        final String date = startTime.substring(0, 11);//截取日期 2018-05-26
        request.getAvailableMeetingRooms(new ObserverImpl<HttpResult<List<MeetingRoomEntity>>>() {

            @Override
            protected void onResponse(HttpResult<List<MeetingRoomEntity>> result) {
                List<MeetingRoomEntity> unaviailbleRooms = result.getResult();
                mAdapter.setUnableRoomData(unaviailbleRooms, startTime);
            }

            @Override
            protected void onFailure(Throwable e) {
                Toasty.error(getContext(),e.getMessage()).show();
            }
        }, date);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
