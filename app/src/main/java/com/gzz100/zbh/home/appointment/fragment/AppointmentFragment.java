package com.gzz100.zbh.home.appointment.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseFragment;
import com.gzz100.zbh.data.entity.MeetingRoomEntity;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.MeetingRoomRequest;
import com.gzz100.zbh.home.appointment.adapter.RoomAdapter;
import com.gzz100.zbh.utils.TimeFormatUtil;
import com.qmuiteam.qmui.widget.QMUIEmptyView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.FalsifyHeader;
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
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


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
    @BindView(R.id.empty_view)
    QMUIEmptyView mEmptyView;
    private RoomAdapter mAdapter;
    private Unbinder unbinder;
    private List<String> mHourList;
    private List<List<String>> mMinuteWrapList;


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

        request.getMeetingRooms(new Observer<HttpResult<List<MeetingRoomEntity>>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HttpResult<List<MeetingRoomEntity>> result) {
                loadData(result.getResult());
            }

            @Override
            public void onError(Throwable e) {
                mRefreshLayout.finishRefresh(false);
            }

            @Override
            public void onComplete() {
                mRefreshLayout.finishRefresh();
            }
        });

    }

    private void initView() {
        mRcvRoom.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        mRcvRoom.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new RoomAdapter(getContext());
        mAdapter.setOnItemClickListener(new RoomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MeetingRoomEntity room, int position) {
                if (room.isEnable()) {
                    ((BaseFragment) getParentFragment())
                            .startParentFragment(ApmDetailFragment.newInstance(room.getMeetingPlaceId(),
                                    room.getMeetingPlaceName(), room.getMeetingPlaceCapacity(),
                                    room.getMeetingPlaceTab(), room.getMeetingPlacePic()));
                } else {
                    int i = mAdapter.showTimetable(position);
                    if (i != -1) {
                        mRcvRoom.smoothScrollToPosition(i);
                    }
                }

            }
        });
        mRcvRoom.setAdapter(mAdapter);

        mRefreshLayout.setRefreshHeader(new FalsifyHeader(getContext()));
        mRefreshLayout.setEnableLoadMore(false);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                loadRoomData();
            }
        });
    }

    private void loadData(List<MeetingRoomEntity> roomList) {
        if (roomList!=null){
            mAdapter.refreshData(roomList);
            if (roomList.size()==0){
                mEmptyView.show();
            }else {
                mEmptyView.hide();
            }
        }else{
            mEmptyView.show();
        }
    }


    @OnClick({R.id.tv_apm_select_date, R.id.tv_apm_select_time, R.id.tv_apm_select_people})
    public void onViewClicked(View view) {
        boolean[] option = new boolean[]{false, true, true, false, false, false};//显示类型 默认全部显示
        switch (view.getId()) {
            case R.id.tv_apm_select_date:

                TimePickerView pvTime = new TimePickerView.Builder(getContext(), new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        mTvApmSelectDate.setText(TimeFormatUtil.formatMonth(date));
                        showSelectTimeDialog();
                    }
                }).setType(option)
                        .build();
                //注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
                pvTime.setDate(Calendar.getInstance());
                pvTime.show();
                break;
            case R.id.tv_apm_select_time:
                showDurationDialog();
                break;
            case R.id.tv_apm_select_people:
                List<String> peopleList = new ArrayList<>();
                peopleList.add("1-10人");
                peopleList.add("11-20人");
                peopleList.add("21-30人");
                peopleList.add("31-40人");
                OptionsPickerView picker = new OptionsPickerView.Builder(getContext(), new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {

                    }
                }).build();
                picker.setPicker(peopleList);
                picker.show();
                break;
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

    private void showSelectTimeDialog() {
        mHourList.clear();
        mMinuteWrapList.clear();

        Calendar instance = Calendar.getInstance(Locale.CHINESE);
        int hour = instance.get(Calendar.HOUR_OF_DAY);
        int minute = instance.get(Calendar.MINUTE);
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
//
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
        // TODO: 2018/3/20
        //不能预约的会议室,要显示当天一整天的所有预约会议情况,而不是从指定的时间开始,不然前面的会议都看不到
        MeetingRoomRequest request = new MeetingRoomRequest();
        final String date = startTime.substring(0, 11);//截取日期 2018-05-26
        request.getAvailableMeetingRooms(new Observer<HttpResult<List<MeetingRoomEntity>>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HttpResult<List<MeetingRoomEntity>> result) {
                List<MeetingRoomEntity> unaviailbleRooms = result.getResult();
                mAdapter.setUnableRoomData(unaviailbleRooms, startTime);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        }, date);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
