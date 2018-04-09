package com.gzz100.zbh.home.appointment.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseBackFragment;
import com.gzz100.zbh.data.entity.Staff;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.AppointmentRequest;
import com.gzz100.zbh.home.appointment.adapter.SearchResultAdapter;
import com.gzz100.zbh.home.appointment.adapter.SelectedStaffAdapter;
import com.gzz100.zbh.home.appointment.adapter.StaffAdapter;
import com.gzz100.zbh.home.appointment.entity.Department;
import com.gzz100.zbh.home.appointment.entity.StaffWrap;
import com.mypopsy.widget.FloatingSearchView;
import com.qmuiteam.qmui.widget.QMUITopBar;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

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

    private List<Department> mDepartmentList;
    private List<Staff> mSelectedStaffs;
    private StaffAdapter mStaffAdapter;
    private SearchResultAdapter mSearchResultAdapter;
    private SelectedStaffAdapter mSelectedAdapter;
    private long[] mSelectedIds;
    private boolean isMultiChoiceMode;
    private int mRequestCode;
    public static final String MultiChoices = "MultiChoice_Mode";
    public static final String SingleChoice = "SingleChoice_Mode";

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
        loadStaffData();
        return attachToSwipeBack(view);
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
            Button rightButton = mTopbar.addRightTextButton("保存", R.id.imageButtonId);
            rightButton.setTextColor(Color.WHITE);
            rightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postMessageToResult(mSelectedAdapter.getSelectedStaffList());
                }
            });
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


    private void loadStaffData(){
        mDepartmentList = new ArrayList<>();

        AppointmentRequest request=new AppointmentRequest();
        Observer observer=new Observer<HttpResult<List<Staff>>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HttpResult<List<Staff>> result) {
                Log.i("onNext","Observer------onNext");
                List<Staff> staffList = result.getResult();
                for (Staff staff : staffList) {
                    boolean isExistDepart =false;
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
                    for (Department department1 : mDepartmentList) {
                        if (department1.getDepartmentId()==Long.parseLong(staff.getDepartmentId())) {
                            isExistDepart = true;
                            department1.getStaffs().add(staff);
                            break;
                        }
                    }
                    if (!isExistDepart) {
                        Department department = new Department();
                        department.setDepartmentId(Long.parseLong(staff.getDepartmentId()));
                        department.setDepartmentName(staff.getDepartmentName());
                        List<Staff> staffs=new ArrayList<>();
                        staffs.add(staff);
                        department.setStaffs(staffs);
                        mDepartmentList.add(department);
                    }
                }

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                initView();
            }
        };
        request.getAllStaffs(observer);
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
            for (Department department : mDepartmentList) {
                if (department.getDepartmentId() == Long.parseLong(staff.getDepartmentId())) {
                    for (Staff departStaff : department.getStaffs()) {
                        if (staff.getUserId() == departStaff.getUserId()) {
                            departStaff.setSelect(staff.isSelect());
                            if (staff.isSelect()) {
                                mSelectedAdapter.add(staff);
                            }else {
                                mSelectedAdapter.remove(staff);
                            }
                            mStaffAdapter.notifyDataSetChanged();
                            return;
                        }
                    }
                }
            }
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
}
