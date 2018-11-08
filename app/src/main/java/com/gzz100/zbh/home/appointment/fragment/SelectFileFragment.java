package com.gzz100.zbh.home.appointment.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseBackFragment;
import com.gzz100.zbh.data.eventEnity.FileInfoEntity;
import com.gzz100.zbh.home.appointment.adapter.SelectFileAdapter;
import com.gzz100.zbh.utils.FragmentBackHandler;
import com.gzz100.zbh.utils.QbSdkFileUtil;
import com.qmuiteam.qmui.widget.QMUITopBar;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Lam on 2018/2/8.
 */

public class SelectFileFragment extends BaseBackFragment implements FragmentBackHandler {
    @BindView(R.id.topbar)
    QMUITopBar mTopbar;
    @BindView(R.id.rcv_selectFile)
    RecyclerView mRcvSelectFile;
    @BindView(R.id.emptyView)
    TextView emptyView;
    Unbinder unbinder;
    private String rootPath;
    private SelectFileAdapter mAdapter;
    private Button mSaveButton;
    private ArrayList<String> mSelectedList;

    public static SelectFileFragment newInstance(ArrayList<String> selectList){
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("selectList",selectList);
        SelectFileFragment fragment = new SelectFileFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_select_file, null);
        unbinder = ButterKnife.bind(this, view);
        initVar();
        initTopbar();
        initView();
        return attachToSwipeBack(view);
    }

    private void initVar() {
        if (getArguments()!=null) {
            mSelectedList = getArguments().getStringArrayList("selectList");
            if (mSelectedList==null){
                mSelectedList = new ArrayList<>();
            }
        }else {
            mSelectedList = new ArrayList<>();
        }

    }

    private void initTopbar() {
        rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        mTopbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!onBackPressed()) {
                    pop();
                }
            }
        });
        View view = LayoutInflater.from(getContext()).inflate(R.layout.button_save, null);
        mSaveButton = view.findViewById(R.id.buttonSave);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<File> selectedFiles = mAdapter.getSelectedFiles();
                ArrayList<String> filePathList = new ArrayList<>();
                for (File selectedFile : selectedFiles) {
                   filePathList.add(selectedFile.getAbsolutePath());
                }
                FileInfoEntity fileInfoEntity = new FileInfoEntity();
                fileInfoEntity.setFileNameList(filePathList);
                EventBus.getDefault().post(fileInfoEntity);
                pop();

            }
        });
        mTopbar.addRightView(view,R.id.fl_buttonSave);
        mTopbar.setTitle("手机存储");

    }

    private void initView() {
        mRcvSelectFile.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new SelectFileAdapter(getContext());

        ArrayList<File> selecteFiles=new ArrayList<>();
        for (String fileName : mSelectedList) {
            selecteFiles.add(new File(fileName));
        }
        mAdapter.setSelectedFiles(selecteFiles);

        mRcvSelectFile.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        mRcvSelectFile.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new SelectFileAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(File file, int position) {
                QbSdkFileUtil.openFile(getContext(),file);

            }
        });

        mAdapter.setOnFileDirChange(new SelectFileAdapter.OnFileDirChange() {
            @Override
            public void onFileChange(File file) {
                String filePath = file.getAbsolutePath().replace(rootPath,"");
                mTopbar.setSubTitle(filePath);
            }
        });
        mAdapter.setOnSelectedListener(new SelectFileAdapter.OnSelectedListener() {
            @Override
            public void onSelect(List<File> fileList) {
                mSaveButton.setText("("+fileList.size()+")"+" 完成");
            }

            @Override
            public void onItemCountChange(int count) {
                if (count==0){
                    emptyView.setVisibility(View.VISIBLE);
                }else {
                    emptyView.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public boolean onBackPressed() {
        boolean canBack = mAdapter.onBackPress();
        return canBack;
    }
}
