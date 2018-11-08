package com.gzz100.zbh.home.meetingadmin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseBackFragment;
import com.gzz100.zbh.data.ObserverImpl;
import com.gzz100.zbh.data.entity.MeetingRoomEntity;
import com.gzz100.zbh.data.eventEnity.MeetingRoomEventEntity;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.MeetingRoomRequest;
import com.gzz100.zbh.home.appointment.adapter.RoomAdapter;
import com.gzz100.zbh.utils.TimeFormatUtil;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.FalsifyHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;

/**
 * Created by Lam on 2018/9/13.
 */

public class SelectMeetingRoomFragment extends BaseBackFragment {
    @BindView(R.id.topbar)
    QMUITopBar mTopbar;
    @BindView(R.id.rcv_meetingRoom)
    RecyclerView mRcvRoom;
    @BindView(R.id.iv_empty_room)
    ImageView mIvEmpty;
    @BindView(R.id.prl_refresh)
    SmartRefreshLayout mRefreshLayout;
    Unbinder unbinder;
    private RoomAdapter mAdapter;

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_select_meeting_room, null);

        unbinder = ButterKnife.bind(this, view);
        return attachToSwipeBack(view);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTopbar();
        initView();
        loadRoomData();
    }

    private void initTopbar() {
        mTopbar.setTitle("选择会议室");
        mTopbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
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
                    EventBus.getDefault().post(new MeetingRoomEventEntity(room.getMeetingPlaceId(),room.getMeetingPlaceName()));
                    pop();
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

    private void loadData(List<MeetingRoomEntity> roomList) {
        if (roomList!=null){
            mAdapter.refreshData(roomList);
            if (roomList.size()==0){
                mIvEmpty.setVisibility(View.VISIBLE);
            }else {
                mIvEmpty.setVisibility(View.GONE);
            }
        }else{
            mIvEmpty.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
