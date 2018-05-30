package com.gzz100.zbh.home.meetingadmin.fragment;

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
import com.gzz100.zbh.data.entity.DelegateEntity;
import com.gzz100.zbh.data.entity.Staff;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.MeetingRequest;
import com.gzz100.zbh.home.appointment.entity.StaffWrap;
import com.gzz100.zbh.home.appointment.fragment.MultiChosePersonFragment;
import com.gzz100.zbh.home.meetingadmin.adapter.DelegatesAdapter;
import com.gzz100.zbh.res.Common;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.widget.QMUITopBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.gzz100.zbh.home.appointment.fragment.ApmDetailFragment.RC_DELEGATE;
import static com.gzz100.zbh.home.appointment.fragment.MultiChosePersonFragment.MultiChoices;

/**
 * Created by Lam on 2018/4/12.
 */

public class DelegateFragment extends BaseBackFragment {

    @BindView(R.id.rcv_delegates)
    RecyclerView mRcvDelegates;
    Unbinder unbinder;
    @BindView(R.id.topbar)
    QMUITopBar mTopbar;
    private String mMeetingId;
    private Button mTextButton;
    private MeetingRequest mRequest;
    private List<DelegateEntity> mDelegates;
    private DelegatesAdapter mAdapter;
    private int mMeetingStatus;

    public static DelegateFragment newInstance(String meetingId,int status) {
        DelegateFragment fragment = new DelegateFragment();
        Bundle bundle = new Bundle();
        bundle.putString("meetingId", meetingId);
        bundle.putInt("meetingStatus",status);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_delegate, null);
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, view);
        return attachToSwipeBack(view);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            mMeetingId = getArguments().getString("meetingId");
            mMeetingStatus = getArguments().getInt("meetingStatus");
        }
        initTopBar();
        loadData();
    }



    private void initTopBar() {
        mTopbar.setTitle("参会人员");
        mTopbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
        mTextButton = mTopbar.addRightTextButton("新增人员", R.id.textButtonId);
        mTextButton.setTextColor(Color.WHITE);
        if (mMeetingStatus== Common.STATUS_END) {
            mTextButton.setVisibility(View.GONE);
        }

        mRequest = new MeetingRequest();
        mDelegates = new ArrayList<>();
        mRcvDelegates.setLayoutManager(new LinearLayoutManager(getContext()));
        mRcvDelegates.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        mAdapter = new DelegatesAdapter(getContext(),mDelegates);
        mRcvDelegates.setAdapter(mAdapter);

    }

    private void loadData() {

        Observer<HttpResult<List<DelegateEntity>>> observer = new Observer<HttpResult<List<DelegateEntity>>>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HttpResult<List<DelegateEntity>> result) {
                if (result.getResult()!=null){
                    mDelegates.clear();
                    mDelegates.addAll(result.getResult());
                }

                loadDataIntoView(mDelegates);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };


        mRequest.getDelegates(observer, mMeetingId);

    }

    private void loadDataIntoView(List<DelegateEntity> delegates) {

        mAdapter.notifyDataSetChanged();

        final long[] delegatesIds= new long[delegates.size()];

        for (int i = 0; i < delegates.size(); i++) {
            delegatesIds[i] = Long.parseLong(delegates.get(i).getUserId());
        }
        mTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startFragment(MultiChosePersonFragment.newInstance(MultiChoices,RC_DELEGATE,delegatesIds));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddNewDelegateCallBack(StaffWrap staffWrap){
        List<Staff> staffs = staffWrap.getStaffList();
        List<String> needToAddIds =new ArrayList<>();
        List<String> delegateIds=new ArrayList<>();
        //结果回调
        for (Staff staff : staffs) {
            needToAddIds.add(staff.getUserId());
        }
        //已有的参会人员
        for (DelegateEntity delegate : mDelegates) {
            delegateIds.add(delegate.getUserId());
        }
        //未添加的id
        needToAddIds.removeAll(delegateIds);

        StringBuilder sb=new StringBuilder();
        sb.append("[");
        for (int i = 0; i < needToAddIds.size(); i++) {
            sb.append(needToAddIds.get(i));
            if (i!=needToAddIds.size()-1){
                sb.append(",");
            }
        }
        sb.append("]");
        Logger.i("userIdList="+sb.toString());
        addDelegate(sb.toString());
    }


    public void addDelegate(String userIdList){
        Observer<HttpResult> observer = new Observer<HttpResult>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HttpResult result) {
                loadData();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        mRequest.addDelegate(observer,mMeetingId,userIdList);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }
}
