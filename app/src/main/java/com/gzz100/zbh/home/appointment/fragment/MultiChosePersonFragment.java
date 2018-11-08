package com.gzz100.zbh.home.appointment.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseBackFragment;
import com.gzz100.zbh.data.ObserverImpl;
import com.gzz100.zbh.data.entity.Staff;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.AppointmentRequest;
import com.gzz100.zbh.home.appointment.adapter.SearchResultAdapter;
import com.gzz100.zbh.home.appointment.adapter.SelectedStaffAdapter;
import com.gzz100.zbh.home.appointment.adapter.StaffAdapter;
import com.gzz100.zbh.home.appointment.entity.Department;
import com.gzz100.zbh.home.appointment.entity.StaffWrap;
import com.mypopsy.widget.FloatingSearchView;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;

import static com.gzz100.zbh.home.appointment.fragment.ApmDetailFragment.RC_COPY;
import static com.gzz100.zbh.home.appointment.fragment.ApmDetailFragment.RC_DELEGATE;
import static com.gzz100.zbh.home.appointment.fragment.ApmDetailFragment.RC_HOST;
import static com.gzz100.zbh.home.appointment.fragment.ApmDetailFragment.RC_SUMMARY;

public class MultiChosePersonFragment extends BaseBackFragment implements StaffAdapter.OnStaffSelectListener,
        StaffAdapter.OnDepartmentSelectListener,
        SearchResultAdapter.OnSearchStaffClickListener ,
        SelectedStaffAdapter.OnSelectCountChangeListener{


    @BindView(R.id.expandableListView)
    ExpandableListView mExpandableListView;
    Unbinder unbinder;
    @BindView(R.id.rl_multiSelect)
    RelativeLayout rlSelected;
    @BindView(R.id.floatingSearchView)
    FloatingSearchView mSearchView;
    @BindView(R.id.topbar)
    QMUITopBar mTopbar;
    @BindView(R.id.tvSelected)
    TextView mTvSelected;
    @BindView(R.id.rcv_selected)
    RecyclerView mRcvSelected;
    @BindView(R.id.cb_selectAll)
    CheckBox mCbSelectAll;
    @BindView(R.id.tv_comfirm)
    TextView mTvComfirm;

    private List<Department> mDepartmentList;
    private List<Staff> mSelectedStaffs;
    private StaffAdapter mStaffAdapter;//人员列表
    private SearchResultAdapter mSearchResultAdapter;//搜索结果列表
    private SelectedStaffAdapter mSelectedAdapter;//选择结果列表
    private long[] mSelectedIds;
    private boolean isMultiChoiceMode;
    private int mRequestCode;
    public static final String MultiChoices = "MultiChoice_Mode";
    public static final String SingleChoice = "SingleChoice_Mode";
    private ObserverImpl<HttpResult<List<Department>>> mDataObserver;
    private QMUITipDialog mLoadingDialog;

    public static MultiChosePersonFragment newInstance(String mode, int requestCode,long[] selectedIds) {
        Bundle args = new Bundle();
        MultiChosePersonFragment fragment = new MultiChosePersonFragment();
        args.putString("mode", mode);
        args.putInt("requestCode", requestCode);
        args.putLongArray("selectedIds",selectedIds);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_multichoice_person, null);
        unbinder = ButterKnife.bind(this, view);
        if (getArguments() != null) {
            String mode = getArguments().getString("mode", SingleChoice);
            mRequestCode = getArguments().getInt("requestCode");
            mSelectedIds = getArguments().getLongArray("selectedIds");
            if (mode.equals(MultiChoices)) {
                isMultiChoiceMode = true;
            }
        }
        initTopBar();
        initSelectedIfNeed();
        initSearchView();
        initTipDialog();
        loadStaffData();
        return attachToSwipeBack(view);
    }

    private void initTipDialog() {
        mLoadingDialog = new QMUITipDialog.Builder(getContext())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("获取中")
                .create();
        mLoadingDialog.show();

    }

    private void initSelectedIfNeed() {
        if (isMultiChoiceMode) {
            mTvSelected.setVisibility(View.VISIBLE);
            mRcvSelected.setVisibility(View.VISIBLE);
            mCbSelectAll.setVisibility(View.VISIBLE);
            mSelectedAdapter = new SelectedStaffAdapter(getContext());
            mSelectedStaffs = new ArrayList<>();
            mRcvSelected.setLayoutManager(new GridLayoutManager(getContext(),1,GridLayoutManager.HORIZONTAL,false));
            mRcvSelected.setAdapter(mSelectedAdapter);
            mSelectedAdapter.setOnItemClickListener(new SelectedStaffAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, Staff staff) {
                    //departmentList也需要更改
                    staff.setSelect(false);
                    onSearchStaffCheck(staff);
                }
            });
            mSelectedAdapter.setOnSelectCountChange(this);
            //全选按钮
            mCbSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    for (Department department : mDepartmentList) {
                        department.setSelect(isChecked);
                        for (Staff staff : department.getStaffs()) {
                            staff.setSelect(isChecked);
                        }
                    }
                    if (isChecked) {
                        mSelectedAdapter.clearAll();
                        for (Department department : mDepartmentList) {
                            mSelectedAdapter.addAll(department.getStaffs());
                        }
                    }else {
                        mSelectedAdapter.clearAll();
                    }

                    mStaffAdapter.notifyDataSetChanged();
                }
            });

        }
    }

    private void initTopBar() {
        if (isMultiChoiceMode) {
//            Button rightButton = mTopbar.addRightTextButton("保存", R.id.imageButtonId);
            rlSelected.setVisibility(View.VISIBLE);
            mTvComfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postMessageToResult(mSelectedAdapter.getSelectedStaffList());
                }
            });
        }else {
            rlSelected.setVisibility(View.GONE);
        }
        String title;
        switch (mRequestCode) {
            case RC_HOST:
                title="选择主持人";
                break;
            case RC_COPY:
                title="选择抄送人员";
                break;
            case RC_DELEGATE:
                title="选择参会人员";
                break;
            case RC_SUMMARY:
                title="选择纪要人员";
                break;
            default:
                title="选择人员";
                break;
        }
        mTopbar.setTitle(title);
        mTopbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
    }

    private void initSearchView() {

        mSearchResultAdapter = new SearchResultAdapter(getContext());
        mSearchView.setAdapter(mSearchResultAdapter);
        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSearchAction(CharSequence text) {
                doSearch(text.toString());
            }
        });
        mSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = s.toString();
                doSearch(keyword);
            }
        });

        mSearchResultAdapter.setOnSearchStaffClickListener(this);

    }

    private void doSearch(String keywords) {
        List<Staff> suggestions = new ArrayList<>();
        for (Department department : mDepartmentList) {
            for (Staff staff : department.getStaffs()) {
                boolean contains = staff.getUserName().contains(keywords);
                if (contains && !TextUtils.isEmpty(keywords)) {
                    suggestions.add(staff);
                }
            }
        }
        mSearchResultAdapter.setDataSet(suggestions);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.action_search);

    }

    private void initView() {
        mStaffAdapter = new StaffAdapter(getContext(),mDepartmentList,isMultiChoiceMode);
        mStaffAdapter.setOnDepartmentSelectListener(this);
        mStaffAdapter.setOnStaffSelectListener(this);
        mExpandableListView.setAdapter(mStaffAdapter);
        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Staff staff = mDepartmentList.get(groupPosition).getStaffs().get(childPosition);
                onStaffCheck(!staff.isSelect(), groupPosition, childPosition);
                return true;
            }
        });

    }

    /**
     * 通过EventBus发送消息返回给原fragment,并pop自身
     *
     * @param staffs 需要返回的staffList
     */
    private void postMessageToResult(List<Staff> staffs) {
        StaffWrap staffWrap = new StaffWrap(mRequestCode, staffs);
        EventBus.getDefault().post(staffWrap);
        pop();
    }

    @Override
    public boolean onBackPressedSupport() {
        if (mLoadingDialog.isShowing()){
            mLoadingDialog.dismiss();
            pop();
            return true;
        }else {
            return false;
        }
    }

    private void loadStaffData(){
        mDepartmentList = new ArrayList<>();

        AppointmentRequest request=new AppointmentRequest();
        //用于显示已选人员
        mDataObserver = new ObserverImpl<HttpResult<List<Department>>>() {

            @Override
            public void onComplete() {
                initView();
            }

            @Override
            protected void onResponse(HttpResult<List<Department>> result) {

                List<Department> departments = result.getResult();

                for (Department department : departments) {
                    for (Staff staff : department.getStaffs()) {
                        for (long selectedId : mSelectedIds) {
                            if (Long.parseLong(staff.getUserId())==selectedId) {
                                staff.setSelect(true);
                                if (isMultiChoiceMode){
                                    mSelectedStaffs.add(staff);//用于显示已选人员
                                }
                                break;
                            }
                        }
                        if (isMultiChoiceMode){
                            mSelectedAdapter.setStaffList(mSelectedStaffs);
                        }
                    }

                }
                mDepartmentList.addAll(departments);
                mLoadingDialog.dismiss();

            }

            @Override
            protected void onFailure(Throwable e) {
                mLoadingDialog.dismiss();
                Toasty.error(_mActivity,e.getMessage()).show();
            }
        };
        request.getAllStaffs(mDataObserver);
    }


    /**
     * 单选和多选的终极处理方法都在此
     *
     * @param isCheck       是否选择
     * @param groupPosition departmentList的index
     * @param childPosition staffList中的index
     */
    @Override
    public void onStaffCheck(boolean isCheck, int groupPosition, int childPosition) {
        if (isMultiChoiceMode) {
            Staff staff = mDepartmentList.get(groupPosition).getStaffs().get(childPosition);
            staff.setSelect(isCheck);
            mStaffAdapter.notifyDataSetChanged();
            if (isCheck) {
                mSelectedAdapter.add(staff);
            }else {
                mSelectedAdapter.remove(staff);
            }

        } else {
            //返回选择结果并pop
            Staff staff = mDepartmentList.get(groupPosition).getStaffs().get(childPosition);
            List<Staff> postObject = new ArrayList<>();
            postObject.add(staff);
            postMessageToResult(postObject);
        }
    }

    /**
     * 多选模式下才会执行此方法
     *
     * @param isCheck       是否选择
     * @param groupPosition departmentList的index
     */
    @Override
    public void onDepartmentCheck(boolean isCheck, int groupPosition) {
        mDepartmentList.get(groupPosition).setSelect(isCheck);
        List<Staff> staffs = mDepartmentList.get(groupPosition).getStaffs();
        for (Staff staff : staffs) {
            staff.setSelect(isCheck);
        }
        if (isCheck){
            mSelectedAdapter.addAll(staffs);
        }else {
            mSelectedAdapter.removeAll(staffs);
        }
        mStaffAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onSearchStaffCheck(Staff staff) {
        if (isMultiChoiceMode) {
            Logger.i(staff.toString());
            if (staff.isSelect()) {
                mSelectedAdapter.add(staff);
            }else {
                mSelectedAdapter.remove(staff);
            }

//            ArrayList<Staff> selected =new ArrayList();
//            selected.addAll(mSelectedAdapter.getSelectedStaffList());
//            for (Staff selectedStaff : selected) {
//                if (selectedStaff.getUserId().equals(staff.getUserId())) {
//                    if (selectedStaff.isSelect()) {
//                        mSelectedAdapter.add(staff);
//                    }else {
//                        mSelectedAdapter.remove(staff);
//                    }
//                    mStaffAdapter.notifyDataSetChanged();
//                }
//            }

        } else {
            //单选模式下直接返回所选中的staff并pop
            List<Staff> postObject = new ArrayList<>();
            postObject.add(staff);
            postMessageToResult(postObject);
        }

    }

    @Override
    public void onSelectCountChange(int count) {
        mTvSelected.setText("已选 "+count+" 人");
        for (Staff staff : mSearchResultAdapter.getStaffList()) {
            boolean isSelect=false;
            for (Staff selected : mSelectedAdapter.getSelectedStaffList()) {
                if (selected.getUserId()==staff.getUserId()) {
                    isSelect=true;
                }
            }
            staff.setSelect(isSelect);
        }
        mSearchResultAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDataObserver.cancleRequest();
    }
}
