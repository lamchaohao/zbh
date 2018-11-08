package com.gzz100.zbh.home.appointment.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseBackFragment;
import com.gzz100.zbh.data.ObserverImpl;
import com.gzz100.zbh.data.entity.Staff;
import com.gzz100.zbh.data.entity.UpdateMeetingEntity;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.TemplateRequest;
import com.gzz100.zbh.home.appointment.adapter.SelectTimeAdapter;
import com.gzz100.zbh.home.appointment.entity.Agenda;
import com.gzz100.zbh.home.appointment.entity.BookedTime;
import com.gzz100.zbh.home.appointment.entity.StaffWrap;
import com.gzz100.zbh.utils.DensityUtil;
import com.gzz100.zbh.utils.TextHeadPicUtil;
import com.gzz100.zbh.utils.TimeFormatUtil;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import io.reactivex.disposables.Disposable;

import static com.gzz100.zbh.home.appointment.fragment.ApmDetailFragment.RC_COPY;
import static com.gzz100.zbh.home.appointment.fragment.ApmDetailFragment.RC_DELEGATE;
import static com.gzz100.zbh.home.appointment.fragment.ApmDetailFragment.RC_HOST;
import static com.gzz100.zbh.home.appointment.fragment.ApmDetailFragment.RC_SUMMARY;

/**
 * Created by Lam on 2018/7/31.
 */

public class TemplateDetailFragment extends BaseBackFragment {
    @BindView(R.id.topbar)
    QMUITopBar mTopbar;
    @BindView(R.id.et_meetingName)
    EditText mEtMeetingName;
    @BindView(R.id.tv_name_count)
    TextView mTvNameCount;
    @BindView(R.id.tv_startTime)
    TextView mTvStartTime;
    @BindView(R.id.tv_endTime)
    TextView mTvEndTime;
    @BindView(R.id.iv_hostPic_detail)
    ImageView mIvHostPic;
    @BindView(R.id.tv_delegateSum)
    TextView mTvDelegateSum;
    @BindView(R.id.rl_delagetePic_detail)
    View mParentViewDelegatePic;
    @BindView(R.id.rl_copypic_detail)
    View mParentViewCopyPic;
    @BindView(R.id.ll_delegateList_detail)
    LinearLayout mLlDelegateListView;
    @BindView(R.id.tv_agendaSum_detail)
    TextView mTvAgendaSum;
    @BindView(R.id.tv_copySum)
    TextView mTvCopySum;
    @BindView(R.id.ll_copyList_detail)
    LinearLayout mLlCopyListView;
    @BindView(R.id.iv_summaryPic_detail)
    ImageView mIvSummaryPic;
    @BindView(R.id.tv_remind)
    TextView mTvRemind;
    @BindView(R.id.btn_delete_template)
    Button mBtnDelete;
    @BindView(R.id.bt_next_detail)
    Button mBtnNext;

    String mMeetingEndTime;
    String mMeetingStartTime;


    Unbinder unbinder;
    private String mRemindTimeStr;
    private List<Staff> mCopyStaffList;
    private List<Staff> mDelegateList;
    private Staff mHostStaff;
    private Staff mSummaryStaff;
    private ArrayList<Agenda> mAddedAgendas;
    private String mTemplateId;
    private ObserverImpl mSaveObserver;
    private String mStartTime;
    private String mEndTime;

    public static TemplateDetailFragment newInstance(String templateId){
        TemplateDetailFragment fragment=new TemplateDetailFragment();
        Bundle bundle=new Bundle();
        bundle.putString("templateId",templateId);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_template_detail, null);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return attachToSwipeBack(view);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVar();
        initView();
        if (!mTemplateId.equals("0")){
            loadData();
        }
    }

    private void loadData() {

        final TemplateRequest request = new TemplateRequest();
        ObserverImpl<HttpResult<UpdateMeetingEntity>> observer=new ObserverImpl<HttpResult<UpdateMeetingEntity>>() {
            @Override
            protected void onResponse(HttpResult<UpdateMeetingEntity> result) {
                if (result!=null) {
                    Logger.i(result.getResult().toString());
                    setTime(result.getResult());
                    onRemindTimeChange(result.getResult().getNotifyTime());
                    setDataToPeople(result.getResult());
                }

            }

            @Override
            protected void onFailure(Throwable e) {
                Toasty.error(_mActivity,e.getMessage()).show();
            }
        };

        request.getTemplateById(observer,mTemplateId);

    }

    private void setTime(UpdateMeetingEntity result) {
        mMeetingStartTime = result.getMeetingStartTime();
        mMeetingEndTime = result.getMeetingEndTime();
        String startTime = TimeFormatUtil.formatTime(Long.parseLong(mMeetingStartTime));
        String endTime = TimeFormatUtil.formatTime(Long.parseLong(mMeetingEndTime));

        mTvStartTime.setText(startTime);
        mTvEndTime.setText(endTime);
    }


    private void initVar() {

        if (getArguments()!=null) {
            mTemplateId = getArguments().getString("templateId");
        }
        mRemindTimeStr="0";
        mAddedAgendas = new ArrayList<>();
        mCopyStaffList = new ArrayList<>();
        mDelegateList = new ArrayList<>();

    }

    private void initView() {
        mTopbar.setTitle("编辑模板");
        if (mTemplateId.equals("0")){
            mBtnDelete.setVisibility(View.GONE);
        }
        mTopbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });

        mEtMeetingName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mTvNameCount.setText(s.length()+"/30");
            }
        });

    }


    @OnClick({R.id.ll_startTime, R.id.rl_host, R.id.rl_delegate_detail,R.id.ll_delegateList_detail, R.id.rl_agenda_detail,
            R.id.rl_copy_detail,R.id.ll_copyList_detail, R.id.rl_summary_detail, R.id.rl_remind,R.id.bt_next_detail,R.id.btn_delete_template})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_startTime:
                selectTime();
                break;
            case R.id.rl_host:
                long hostId = -1;
                if (mHostStaff != null) {
                    hostId = Long.parseLong(mHostStaff.getUserId());
                }
                startFragment(MultiChosePersonFragment.newInstance(MultiChosePersonFragment.SingleChoice, RC_HOST, new long[]{hostId}));
                break;
            case R.id.ll_delegateList_detail:
                startSelectDelegates();
                break;
            case R.id.rl_delegate_detail:
                startSelectDelegates();
                break;
            case R.id.rl_agenda_detail:
                startFragment(AddAgendaFragment.newInstance(mAddedAgendas));
                break;
            case R.id.ll_copyList_detail:
                startSelectCopy();
                break;
            case R.id.rl_copy_detail:
                startSelectCopy();
                break;
            case R.id.rl_summary_detail:
                long summaryId = -1;
                if (mSummaryStaff != null && mSummaryStaff.getUserId() != null) {
                    summaryId = Long.parseLong(mSummaryStaff.getUserId());
                }
                startFragment(MultiChosePersonFragment.newInstance(MultiChosePersonFragment.SingleChoice, RC_SUMMARY, new long[]{summaryId}));
                break;
            case R.id.rl_remind:
                startFragment(RemindTimeFragment.newInstance(mRemindTimeStr));
                break;
            case R.id.bt_next_detail:
                saveTemplate();
                break;
            case R.id.btn_delete_template:
                deleteTemplate();
                break;
        }
    }


    private void selectTime(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_recycler_view, null, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);



        SelectTimeAdapter adapter = new SelectTimeAdapter(getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5, GridLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
//        Dialog dialog = new Dialog(getContext());
//        dialog.setContentView(view);
//        dialog.setTitle("选择时间");
//        dialog.show();

        QMUIDialog qmuiDialog = new QMUIDialog.CustomDialogBuilder(getContext())
                .setCustomView(view)
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        mTvStartTime.setText(mStartTime);
                        mTvEndTime.setText(mEndTime);
                    }
                })
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();

                    }
                })
                .setTitle("选择时间")
                .create();
        qmuiDialog.show();

        long timeInMillis = System.currentTimeMillis();
        timeInMillis += 60000 * 60 * 24;
        String tomorrow = TimeFormatUtil.formatDate(timeInMillis);
        adapter.setAppointmentDate(tomorrow);

        Calendar startCalen = formatTime(mTvStartTime.getText().toString());
        Calendar endCalen = formatTime(mTvEndTime.getText().toString());
        Logger.i(startCalen.getTimeInMillis()+", end ="+endCalen.getTimeInMillis());
        adapter.setSelectTimeBlock(new BookedTime(startCalen,endCalen));

        adapter.notifyDataSetChanged();
        adapter.setOnBookTimeChangeListener(new SelectTimeAdapter.OnBookTimeChangeListener() {
            @Override
            public void onBookTimeChange(long startTime, long endTime) {

                mStartTime = TimeFormatUtil.formatTime(startTime);
                mEndTime = TimeFormatUtil.formatTime(endTime);
                Logger.i("startTime="+mStartTime+",endTime="+mEndTime);

            }
        });


    }

    private Calendar formatTime(String timeClock){

        String[] hourAndMinute = new String[2];
        if (!TextUtils.isEmpty(timeClock)){
            hourAndMinute = timeClock.split(":");
        }else {
            hourAndMinute[0]=""+ Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            hourAndMinute[1]=""+Calendar.getInstance().get(Calendar.MINUTE);
        }

        int hourOfDay=Integer.parseInt(hourAndMinute[0]);
        int minute=Integer.parseInt(hourAndMinute[1]);
        long timeInMillis = System.currentTimeMillis();
        timeInMillis += 60000 * 60 * 24;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);

        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar;
    }





    private void startSelectCopy(){
        long[] copyToIds = new long[mCopyStaffList.size()];
        for (int i = 0; i < mCopyStaffList.size(); i++) {
            copyToIds[i] = Long.parseLong(mCopyStaffList.get(i).getUserId());
        }
        startFragment(MultiChosePersonFragment.newInstance(MultiChosePersonFragment.MultiChoices, RC_COPY, copyToIds));
    }

    private void startSelectDelegates(){
        long[] delegateIds = new long[mDelegateList.size()];
        for (int i = 0; i < mDelegateList.size(); i++) {
            delegateIds[i] = Long.parseLong(mDelegateList.get(i).getUserId());
        }
        startFragment(MultiChosePersonFragment.newInstance(MultiChosePersonFragment.MultiChoices, RC_DELEGATE, delegateIds));
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
                    mParentViewCopyPic.setVisibility(View.GONE);
                } else {
                    mParentViewCopyPic.setVisibility(View.VISIBLE);
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
                    mParentViewDelegatePic.setVisibility(View.GONE);
                } else {
                    mParentViewDelegatePic.setVisibility(View.VISIBLE);
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



    private void setDataToPeople(UpdateMeetingEntity meetingInfo) {
        //主持人
        Staff host = new Staff();
        host.setUserId(meetingInfo.getHostId());
        host.setUserName(meetingInfo.getHostName());
        List<Staff> hostList = new ArrayList<Staff>();
        hostList.add(host);
        StaffWrap hostWrap=new StaffWrap(RC_HOST,hostList);
        onStaffSelectedCallBack(hostWrap);
        //参会人员
        List<UpdateMeetingEntity.StaffBean> delegateInfoList = meetingInfo.getDelegateList();
        List<Staff> delegateList = new ArrayList<>();
        for (UpdateMeetingEntity.StaffBean staffBean : delegateInfoList) {
            Staff staff = new Staff();
            staff.setUserName(staffBean.getUserName());
            staff.setUserId(staffBean.getUserId());
            delegateList.add(staff);
        }
        mLlDelegateListView.setVisibility(View.VISIBLE);
        StaffWrap delegateWrap=new StaffWrap(RC_DELEGATE,delegateList);
        onStaffSelectedCallBack(delegateWrap);
        //抄送人员
        List<UpdateMeetingEntity.StaffBean> copyInfoList = meetingInfo.getCopyList();
        List<Staff> copyList = new ArrayList<>();
        for (UpdateMeetingEntity.StaffBean staffBean : copyInfoList) {
            Staff staff = new Staff();
            staff.setUserName(staffBean.getUserName());
            staff.setUserId(staffBean.getUserId());
            copyList.add(staff);
        }
        mLlCopyListView.setVisibility(View.VISIBLE);
        StaffWrap copyWrap=new StaffWrap(RC_COPY,copyList);
        onStaffSelectedCallBack(copyWrap);

        //纪要人员
        if (meetingInfo.getSummaryPerson()!=null) {
            Staff summaryStaff = new Staff();
            summaryStaff.setUserId(meetingInfo.getSummaryPerson().getUserId());
            summaryStaff.setUserName(meetingInfo.getSummaryPerson().getUserName());
            List<Staff> summaryList =new ArrayList<>();
            summaryList.add(summaryStaff);
            StaffWrap summaryWrap=new StaffWrap(RC_SUMMARY,summaryList);
            onStaffSelectedCallBack(summaryWrap);
        }
        //议程

        List<UpdateMeetingEntity.AgendaListBean> agendaList = meetingInfo.getAgendaList();
        ArrayList<Agenda> agendaArrayList = new ArrayList<>();
        for (UpdateMeetingEntity.AgendaListBean agendaBean : agendaList) {
            Agenda agenda=new Agenda();
            Staff staff = new Staff();
            staff.setUserId(agendaBean.getSpeakerId());
            staff.setUserName(agendaBean.getSpeaker());
            agenda.setAgendaName(agendaBean.getAgendaName());
            agenda.setStaff(staff);
            agendaArrayList.add(agenda);
        }
        onAgendaAddedCallBack(agendaArrayList);

        mEtMeetingName.setText(meetingInfo.getMeetingName());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAgendaAddedCallBack(ArrayList<Agenda> agendas) {
        mAddedAgendas = agendas;
        for (Agenda agenda : mAddedAgendas) {
            Logger.i("agendaName=" + agenda.getAgendaName());

        }
        mTvAgendaSum.setText(mAddedAgendas.size() + "个");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRemindTimeChange(String time){
        mRemindTimeStr = time;
        String tvTimeShow="";
        switch (time) {
            case "15":
                tvTimeShow = "15分钟";
                break;
            case "30":
                tvTimeShow = "30分钟";
                break;
            case "60":
                tvTimeShow = "60分钟";
                break;
            case "120":
                tvTimeShow = "2小时";
                break;
            case "1d":
                tvTimeShow = "1天";
                break;
            case "2d":
                tvTimeShow = "2天";
                break;
            case "7d":
                tvTimeShow = "1周";
                break;
            case "0":
                tvTimeShow = "不提醒";
                break;
        }

        mTvRemind.setText(tvTimeShow);
    }


    private void saveTemplate(){
        if (!checkData()){
            return;
        }

        mSaveObserver = new ObserverImpl<HttpResult>() {
            @Override
            public void onSubscribe(Disposable d) {
                mBtnNext.setEnabled(false);
            }

            @Override
            protected void onResponse(HttpResult result) {
                Toasty.success(_mActivity,"已更新").show();
                pop();
            }

            @Override
            protected void onFailure(Throwable e) {
                mBtnNext.setEnabled(true);
                Toasty.error(_mActivity,e.getMessage()).show();
            }
        };

        TemplateRequest request = new TemplateRequest();




        String startTime = mTvStartTime.getText().toString();
        String endTime = mTvEndTime.getText().toString();
        Logger.i("startTime:"+startTime);
        Logger.i("endTime:"+endTime);

        JSONArray agendaJsonArray = new JSONArray();
        try {
            for (int i = 0; i < mAddedAgendas.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("agendaName", mAddedAgendas.get(i).getAgendaName());
                jsonObject.put("userId", mAddedAgendas.get(i).getStaff().getUserId());
                agendaJsonArray.put(i, jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Logger.i(agendaJsonArray.toString());

        Gson gson=new Gson();
        String[] delegateIdArr=new String[mDelegateList.size()];
        for (int i = 0; i < mDelegateList.size(); i++) {
            delegateIdArr[i] = mDelegateList.get(i).getUserId();
        }
        String delegateIdJson = gson.toJson(delegateIdArr);
        String[] copyIdArr=new String[mCopyStaffList.size()];
        for (int i = 0; i < mCopyStaffList.size(); i++) {
            copyIdArr[i]=mCopyStaffList.get(i).getUserId();

        }

        String copyIdJson = gson.toJson(copyIdArr);

        if(mTemplateId.equals("0")){
            request.addTemplate(mSaveObserver, mEtMeetingName.getText().toString(),
                    startTime, endTime, mHostStaff.getUserId(),
                    mSummaryStaff.getUserId(), copyIdJson, agendaJsonArray.toString(),
                    delegateIdJson,mRemindTimeStr);
        }else {
            request.updateTemplate(mSaveObserver, mTemplateId,mEtMeetingName.getText().toString(),
                    startTime, endTime, mHostStaff.getUserId(),
                    mSummaryStaff.getUserId(), copyIdJson, agendaJsonArray.toString(),
                    delegateIdJson,mRemindTimeStr);

        }


    }

    private void deleteTemplate(){

        TemplateRequest request = new TemplateRequest();

        request.deleteTemplateById(new ObserverImpl<HttpResult>() {
            @Override
            protected void onResponse(HttpResult result) {
                Toasty.success(_mActivity,"已删除").show();
                pop();
            }

            @Override
            protected void onFailure(Throwable e) {
                Toasty.success(_mActivity,e.getMessage()).show();
            }
        },mTemplateId);


    }


    private boolean checkData() {
        if (TextUtils.isEmpty(mEtMeetingName.getText())) {
            mEtMeetingName.setError("会议名称不能为空");
            return false;
        }
        if (TextUtils.isEmpty(mTvStartTime.getText())) {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

}
