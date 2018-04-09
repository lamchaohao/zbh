package com.gzz100.zbh.home.appointment.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseBackFragment;
import com.gzz100.zbh.data.entity.Staff;
import com.gzz100.zbh.home.appointment.adapter.AgendaAdapter;
import com.gzz100.zbh.home.appointment.entity.Agenda;
import com.gzz100.zbh.home.appointment.entity.StaffWrap;
import com.qmuiteam.qmui.widget.QMUITopBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Lam on 2018/2/7.
 */

public class AddAgendaFragment extends BaseBackFragment {

    @BindView(R.id.topbar)
    QMUITopBar mTopbar;
    @BindView(R.id.rcv_agenda)
    RecyclerView mRcvAgenda;
    Unbinder unbinder;
//    @BindView(R.id.iv_addAgenda)
//    ImageView mIvAddAgenda;
    private AgendaAdapter mAdapter;
    private Staff mStaff;

    public static AddAgendaFragment newInstance() {
        AddAgendaFragment fragment = new AddAgendaFragment();
        return fragment;
    }

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_add_agenda, null);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return attachToSwipeBack(view);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        mTopbar.setTitle("添加议程");
        mTopbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });

        Button textButton = mTopbar.addRightTextButton("保存", R.id.textButtonId);
        textButton.setTextColor(Color.WHITE);

        textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Agenda> agendaList = mAdapter.getAgendaList();

                EventBus.getDefault().post(agendaList);
                pop();
            }
        });
        mRcvAgenda.setLayoutManager(new LinearLayoutManager(getContext()));
        mRcvAgenda.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        mAdapter = new AgendaAdapter(getContext());
        mRcvAgenda.setAdapter(mAdapter);
        mAdapter.add();
        mAdapter.setOnItemClickListener(new AgendaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Agenda agenda) {
                long ids= -1;
                if (mStaff!=null) {
                    ids=Long.parseLong(mStaff.getUserId());
                }
                startFragment(MultiChosePersonFragment.newInstance(MultiChosePersonFragment.SingleChoice,position,new long[]{ids}));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaffSelectedCallBack(StaffWrap staffWrap){
        int requestCode = staffWrap.getRequestCode();
        mStaff = staffWrap.getStaffList().get(0);
        mAdapter.setAgendaSpeaker(requestCode,mStaff);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

}
