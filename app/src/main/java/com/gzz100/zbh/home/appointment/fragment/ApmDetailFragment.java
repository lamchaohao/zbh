package com.gzz100.zbh.home.appointment.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.gzz100.zbh.R;
import com.gzz100.zbh.adapter.SelectTimeAdapter;
import com.gzz100.zbh.base.BaseFragment;
import com.gzz100.zbh.data.entity.MeetingEntity;
import com.gzz100.zbh.data.entity.MeetingRoomEntity;
import com.gzz100.zbh.data.entity.Staff;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.AppointmentRequest;
import com.gzz100.zbh.data.network.request.MeetingRoomRequest;
import com.gzz100.zbh.home.appointment.entity.Agenda;
import com.gzz100.zbh.home.appointment.entity.StaffWrap;
import com.gzz100.zbh.home.appointment.entity.TimeBlock;
import com.gzz100.zbh.utils.DensityUtil;
import com.gzz100.zbh.utils.GlideApp;
import com.gzz100.zbh.utils.TextHeadPicUtil;
import com.gzz100.zbh.utils.TimeFormatUtil;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.QMUITabSegment;
import com.qmuiteam.qmui.widget.QMUITopBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ApmDetailFragment extends BaseFragment {


    @BindView(R.id.rcv_apm_detail_time)
    RecyclerView mRcvApmDetailTime;
    Unbinder unbinder;
    @BindView(R.id.tabs_apm_detail)
    QMUITabSegment mTabSegment;
    @BindView(R.id.topbar)
    QMUITopBar mTopbar;
    @BindView(R.id.tv_date_detail)
    TextView mTvDate;
    @BindView(R.id.tv_time_detail)
    TextView mTvTime;
    @BindView(R.id.tv_time_howlong)
    TextView mTvTimeHowlong;
    @BindView(R.id.et_meetingName_detail)
    EditText mEtMeetingName;
    @BindView(R.id.tv_nameCount_detail)
    TextView mTvNameCount;
    @BindView(R.id.iv_hostPic_detail)
    ImageView mIvHostPic;
    @BindView(R.id.rl_host)
    RelativeLayout mRlHost;
    @BindView(R.id.ll_delegateList_detail)
    LinearLayout mLlDelegateListView;
    @BindView(R.id.rl_delegate_detail)
    RelativeLayout mRlDelegate;
    @BindView(R.id.tv_agendaSum_detail)
    TextView mTvAgendaSum;
    @BindView(R.id.rl_agenda_detail)
    RelativeLayout mRlAgenda;
    @BindView(R.id.rl_copy_detail)
    RelativeLayout mRlCopy;
    @BindView(R.id.ll_copyList_detail)
    LinearLayout mLlCopyListView;
    @BindView(R.id.iv_summaryPic_detail)
    ImageView mIvSummaryPic;
    @BindView(R.id.rl_summary_detail)
    RelativeLayout mRlSummary;
    @BindView(R.id.bt_next_detail)
    Button mBtNext;
    @BindView(R.id.tv_delegateSum)
    TextView mTvDelegateSum;
    @BindView(R.id.tv_copySum)
    TextView mTvCopySum;
    @BindView(R.id.iv_room)
    ImageView mIvRoom;
    @BindView(R.id.tv_room_name)
    TextView mTvRoomName;
    @BindView(R.id.tv_capcity_room)
    TextView mTvCapcityRoom;
    @BindView(R.id.tv_tag1_room)
    TextView mTvTag1Room;
    @BindView(R.id.tv_tag2_room)
    TextView mTvTag2Room;
    @BindView(R.id.tv_tag3_room)
    TextView mTvTag3Room;
    @BindView(R.id.tv_tag4_room)
    TextView mTvTag4Room;
    @BindView(R.id.rl_remind)
    RelativeLayout mRlRemind;

    private String mRoomId;
    private MeetingRoomEntity mMeetingRoomEntity;
    private SelectTimeAdapter mAdapter;
    public static final int RC_HOST = 366;
    public static final int RC_DELEGATE = 367;
    public static final int RC_SUMMARY = 368;
    public static final int RC_COPY = 369;
    private List<Staff> mCopyStaffList;
    private List<Staff> mDelegateList;
    private Staff mHostStaff;
    private Staff mSummaryStaff;
    private List<Agenda> mAddedAgendas;
    private long mStartTime;
    private long mEndTime;

    public static ApmDetailFragment newInstance(MeetingRoomEntity room) {
        Bundle args = new Bundle();
        ApmDetailFragment fragment = new ApmDetailFragment();
        args.putParcelable("room", room);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View inflate = inflater.inflate(R.layout.fragment_apm_detail, null);
        unbinder = ButterKnife.bind(this, inflate);
        EventBus.getDefault().register(this);
        return inflate;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initVar();
        initTopbar();
        initMeetingRoom();
        initRecyclerView();
        loadData(TimeFormatUtil.formatDate(System.currentTimeMillis()));
        initTabs();
    }

    private void initMeetingRoom() {
        GlideApp.with(getContext())
                .load(mMeetingRoomEntity.getMeetingPlacePic())
                .placeholder((R.drawable.ic_insert_chart_blue_500_48dp))
                .into(mIvRoom);
        mTvRoomName.setText(mMeetingRoomEntity.getMeetingPlaceName());
        mTvCapcityRoom.setText(mMeetingRoomEntity.getMeetingPlaceCapacity()+"人");
        String[] tabs = mMeetingRoomEntity.getMeetingPlaceTab().split("、");
        int size = tabs.length;

        switch (size) {
            case 4:
                mTvTag4Room.setVisibility(View.VISIBLE);
                mTvTag4Room.setText(tabs[3]);
            case 3:
                mTvTag3Room.setVisibility(View.VISIBLE);
                mTvTag3Room.setText(tabs[2]);
            case 2:
                mTvTag2Room.setVisibility(View.VISIBLE);
                mTvTag2Room.setText(tabs[1]);
            case 1:
                mTvTag1Room.setVisibility(View.VISIBLE);
                mTvTag1Room.setText(tabs[0]);
        }
    }


    private void initVar() {
        if (getArguments() != null) {
            mMeetingRoomEntity = getArguments().getParcelable("room");
            mRoomId = mMeetingRoomEntity.getMeetingPlaceId();
            Logger.i("room=" + mMeetingRoomEntity);
        }
        mCopyStaffList = new ArrayList<>();
        mDelegateList = new ArrayList<>();
    }

    private void initTopbar() {
        mTopbar.setTitle("预约会议");
    }

    private void initTabs() {
        mTopbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
        int normalColor = QMUIResHelper.getAttrColor(getActivity(), R.attr.qmui_config_color_black);
        int selectColor = QMUIResHelper.getAttrColor(getActivity(), R.attr.qmui_config_color_blue);
        mTabSegment.setDefaultNormalColor(normalColor);
        mTabSegment.setDefaultSelectedColor(selectColor);
        QMUITabSegment.Tab todayTabs = new QMUITabSegment.Tab("今天");
        QMUITabSegment.Tab tomorrowTab = new QMUITabSegment.Tab("明天");
        QMUITabSegment.Tab dayAfterTomrwTab = new QMUITabSegment.Tab("后天");
        QMUITabSegment.Tab selectDataTab = new QMUITabSegment.Tab("选择日期");
        mTabSegment.addTab(todayTabs)
                .addTab(tomorrowTab)
                .addTab(dayAfterTomrwTab)
                .addTab(selectDataTab);
        mTabSegment.setMode(QMUITabSegment.MODE_FIXED);
        mTabSegment.setHasIndicator(true);
        mTabSegment.addOnTabSelectedListener(new QMUITabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
                long timeInMillis = System.currentTimeMillis();
                switch (index) {
                    case 0:
                        String today = TimeFormatUtil.formatDate(timeInMillis);
                        loadData(today);
                        break;
                    case 1:
                        timeInMillis += 60000 * 60 * 24;
                        String tomorrow = TimeFormatUtil.formatDate(timeInMillis);
                        loadData(tomorrow);
                        break;
                    case 2:
                        timeInMillis += 60000 * 60 * 24 * 2;
                        String afterTomorrow = TimeFormatUtil.formatDate(timeInMillis);
                        loadData(afterTomorrow);
                        break;
                    case 3:
                        showSelectTimeDialog();
                        break;
                }
            }

            @Override
            public void onTabUnselected(int index) {

            }

            @Override
            public void onTabReselected(int index) {
                if (index == 3) {
                    showSelectTimeDialog();
                }
            }

            @Override
            public void onDoubleTap(int index) {

            }
        });
        mTabSegment.selectTab(0);
    }

    private void showSelectTimeDialog() {
        boolean[] option = new boolean[]{true, true, true, false, false, false};//显示类型 默认全部显示
        TimePickerView pvTime = new TimePickerView.Builder(getContext(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                Logger.i("date=" + date);
            }
        }).setType(option)
                .build();
        //注：根据需求来决定是否使用该方法（一般是精确到秒的情况），
        // 此项可以在弹出选择器的时候重新设置当前时间，
        // 避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
        pvTime.setDate(Calendar.getInstance());
        pvTime.show();
    }

    private void loadData(String startTime) {
        MeetingRoomRequest request = new MeetingRoomRequest();
        mAdapter.setAppointmentDate(startTime);
        request.getMeetingListByRoomId(new Observer<HttpResult<List<MeetingRoomEntity>>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HttpResult<List<MeetingRoomEntity>> result) {
                List<MeetingRoomEntity> roomEntities = result.getResult();
                if (roomEntities.size() == 1) {
                    List<MeetingRoomEntity.MeetingListBean> meetingList = roomEntities.get(0).getMeetingList();
                    mAdapter.setBookedTime(meetingList);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        }, mRoomId, startTime);
    }

    private void initRecyclerView() {
        mRcvApmDetailTime.setLayoutManager(new GridLayoutManager(getContext(), 5, GridLayoutManager.HORIZONTAL, false));

        mRcvApmDetailTime.setHasFixedSize(false);
        mAdapter = new SelectTimeAdapter(getContext());
        mRcvApmDetailTime.setAdapter(mAdapter);

//        mAdapter.setBookedTime(bookedTimes);
        mAdapter.setOnItemClickListener(new SelectTimeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TimeBlock timeBlock) {

            }
        });
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int pos = (hour - 7) * 5;
        mRcvApmDetailTime.smoothScrollToPosition(pos + 15);
        mAdapter.setOnBookTimeChangeListener(new SelectTimeAdapter.OnBookTimeChangeListener() {
            @Override
            public void onBookTimeChange(long startTime, long endTime) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                calendar.setTimeInMillis(startTime);
                String start = sdf.format(calendar.getTime());
                calendar.setTimeInMillis(endTime);
                String end = sdf.format(calendar.getTime());
                mStartTime = startTime;
                mEndTime = endTime;
                mTvTime.setText(start + " - " + end);
                long cost = (endTime - startTime) / 60000;
                mTvTimeHowlong.setText(cost + "分钟");
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAgendaAddedCallBack(List<Agenda> agendas) {
        mAddedAgendas = agendas;
        for (Agenda agenda : mAddedAgendas) {
            Logger.i("agendaName=" + agenda.getAgendaName());

        }
        mTvAgendaSum.setText(mAddedAgendas.size() + "个");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaffSelectedCallBack(StaffWrap staffWrap) {
        int imageSize = DensityUtil.dp2px(getContext(), 36);
        int fontSize = DensityUtil.sp2px(getContext(), 16);
        switch (staffWrap.getRequestCode()) {
            case RC_COPY:
                mCopyStaffList = staffWrap.getStaffList();
                mLlCopyListView.removeAllViews();
                if (mCopyStaffList.size() == 0) {
                    mLlCopyListView.setVisibility(View.GONE);
                } else {
                    mLlCopyListView.setVisibility(View.VISIBLE);
                }
                for (Staff staff : mCopyStaffList) {
                    ImageView iv = new ImageView(getContext());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imageSize, imageSize);
                    params.leftMargin = DensityUtil.dp2px(getContext(), 3);
                    iv.setLayoutParams(params);
                    iv.setImageDrawable(TextHeadPicUtil.getHeadPic(staff.getUserName(), fontSize, imageSize));
                    mLlCopyListView.addView(iv);
                }

                mTvCopySum.setText(mCopyStaffList.size() + "人");
                break;
            case RC_DELEGATE:
                mDelegateList = staffWrap.getStaffList();
                mLlDelegateListView.removeAllViews();
                if (mDelegateList.size() == 0) {
                    mLlDelegateListView.setVisibility(View.GONE);
                } else {
                    mLlDelegateListView.setVisibility(View.VISIBLE);
                }
                for (Staff staff : mDelegateList) {
                    ImageView iv = new ImageView(getContext());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imageSize, imageSize);
                    params.leftMargin = DensityUtil.dp2px(getContext(), 3);
                    iv.setLayoutParams(params);
                    iv.setImageDrawable(TextHeadPicUtil.getHeadPic(staff.getUserName(), fontSize, imageSize));
                    mLlDelegateListView.addView(iv);
                }
                mTvDelegateSum.setText(mDelegateList.size() + "人");
                break;
            case RC_HOST:
                mHostStaff = staffWrap.getStaffList().get(0);
                mIvHostPic.setImageDrawable(TextHeadPicUtil.getHeadPic(mHostStaff.getUserName(), fontSize, imageSize));
                break;
            case RC_SUMMARY:
                mSummaryStaff = staffWrap.getStaffList().get(0);
                mIvSummaryPic.setImageDrawable(TextHeadPicUtil.getHeadPic(mSummaryStaff.getUserName(), fontSize, imageSize));
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    @OnClick({R.id.rl_host, R.id.rl_delegate_detail, R.id.rl_agenda_detail,R.id.rl_remind,
            R.id.rl_summary_detail, R.id.rl_copy_detail, R.id.bt_next_detail})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_host:
                long hostId = -1;
                if (mHostStaff != null) {
                    hostId = Long.parseLong(mHostStaff.getUserId());
                }
                startFragment(MultiChosePersonFragment.newInstance(MultiChosePersonFragment.SingleChoice, RC_HOST, new long[]{hostId}));
                break;
            case R.id.rl_delegate_detail:
                long[] delegateIds = new long[mDelegateList.size()];
                for (int i = 0; i < mDelegateList.size(); i++) {
                    delegateIds[i] = Long.parseLong(mDelegateList.get(i).getUserId());
                }
                startFragment(MultiChosePersonFragment.newInstance(MultiChosePersonFragment.MultiChoices, RC_DELEGATE, delegateIds));
                break;
            case R.id.rl_agenda_detail:
                startFragment(AddAgendaFragment.newInstance());
                break;
            case R.id.rl_summary_detail:
                long summaryId = -1;
                if (mSummaryStaff != null && mSummaryStaff.getUserId() != null) {
                    summaryId = Long.parseLong(mSummaryStaff.getUserId());
                }
                startFragment(MultiChosePersonFragment.newInstance(MultiChosePersonFragment.SingleChoice, RC_SUMMARY, new long[]{summaryId}));
                break;
            case R.id.rl_copy_detail:
                long[] copyToIds = new long[mCopyStaffList.size()];
                for (int i = 0; i < mCopyStaffList.size(); i++) {
                    copyToIds[i] = Long.parseLong(mCopyStaffList.get(i).getUserId());
                }
                startFragment(MultiChosePersonFragment.newInstance(MultiChosePersonFragment.MultiChoices, RC_COPY, copyToIds));
                break;
            case R.id.rl_remind:
//                startFragment(new RemindTimeFragment());
                startFragment(AddFileVoteFragment.newInstance("1038"));
                break;
            case R.id.bt_next_detail:
                addMeeting();
                break;
        }
    }

    private void addMeeting() {
        if (!checkData()) {
            return;
        }
        Observer observer = new Observer<HttpResult<MeetingEntity>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HttpResult<MeetingEntity> result) {
                Logger.i(result.getCode() + "," + result.getResult().toString());
                startWithPop(AddFileVoteFragment.newInstance(result.getResult().getMeetingId()));
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
            }
        };

        AppointmentRequest request = new AppointmentRequest();

        String startTime = TimeFormatUtil.formatDateAndTime(mStartTime);
        String endTime = TimeFormatUtil.formatDateAndTime(mEndTime);
        JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < mAddedAgendas.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("agendaName", mAddedAgendas.get(i).getAgendaName());
                jsonObject.put("userId", mAddedAgendas.get(i).getStaff().getUserId());
                jsonArray.put(i, jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Logger.i(jsonArray.toString());

        StringBuilder delegateIdList = new StringBuilder("[");
        for (int i = 0; i < mDelegateList.size(); i++) {
            delegateIdList.append(mDelegateList.get(i).getUserId());
            if (i != mDelegateList.size() - 1) {
                delegateIdList.append(",");
            } else {
                delegateIdList.append("]");
            }
        }
        StringBuilder copyIdList = new StringBuilder("[");
        for (int i = 0; i < mDelegateList.size(); i++) {
            copyIdList.append(mDelegateList.get(i).getUserId());
            if (i != mDelegateList.size() - 1) {
                copyIdList.append(",");
            } else {
                copyIdList.append("]");
            }
        }

        Logger.i("delegateIdList:" + delegateIdList.toString());
        request.addMeeting(observer, mEtMeetingName.getText().toString(),
                mRoomId, startTime, endTime, mHostStaff.getUserId(),
                mSummaryStaff.getUserId(), copyIdList.toString(), jsonArray.toString(), delegateIdList.toString());

    }

    private boolean checkData() {
        if (TextUtils.isEmpty(mEtMeetingName.getText())) {
            mEtMeetingName.setError("会议名称不能为空");
            return false;
        }
        if (mStartTime == 0 || mEndTime == 0) {
            Toasty.info(getContext(), "请选择会议时间").show();
            return false;
        }
        if (mHostStaff == null) {
            Toasty.info(getContext(), "请选择主持人").show();
            return false;
        }
        if (mDelegateList.size() < 1) {
            Toasty.info(getContext(), "请添加参会人员").show();
            return false;
        }
        if (mSummaryStaff == null) {
            mSummaryStaff = new Staff();
        }
        if (mAddedAgendas == null) {
            mAddedAgendas = new ArrayList<>();
        }
        return true;
    }
}
