package com.gzz100.zbh.home.appointment.fragment;

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
import com.gzz100.zbh.home.appointment.adapter.SelectFileAdapter;
import com.gzz100.zbh.utils.FragmentBackHandler;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.tencent.smtt.sdk.QbSdk;

import java.io.File;
import java.util.HashMap;
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

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_select_file, null);
        unbinder = ButterKnife.bind(this, view);
        initTopbar();
        initView();
        return attachToSwipeBack(view);
    }

    private void initTopbar() {
        rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        mTopbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
        View view = LayoutInflater.from(getContext()).inflate(R.layout.button_save, null);
        mSaveButton = view.findViewById(R.id.buttonSave);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<File> selectedFiles = mAdapter.getSelectedFiles();
                for (File selectedFile : selectedFiles) {
                    Logger.i("selectedFile ="+selectedFile.getName());
                }
            }
        });
        mTopbar.addRightView(view,R.id.fl_buttonSave);
        mTopbar.setTitle("手机存储");

    }

    private void initView() {
        mRcvSelectFile.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new SelectFileAdapter(getContext());
        mRcvSelectFile.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        mRcvSelectFile.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new SelectFileAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(File file, int position) {
                HashMap<String,String> configValue = new HashMap<>();
                //“true”表示是进入打开方式选择界面，如果不设置或设置为“false” ，则进入 miniqb 浏览器模式。
                configValue.put("style","1");
                configValue.put("local","false");
                configValue.put("topBarBgColor","#2196F3");
                QbSdk.openFileReader(getContext(),file.getAbsolutePath(),configValue,null );

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
                mSaveButton.setText("("+fileList.size()+")"+" 上传");
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
