package com.gzz100.zbh.home.appointment.adapter;

import android.content.Context;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.utils.FormatFileSizeUtil;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Lam on 2018/2/8.
 */

public class SelectFileAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private File[] mFiles;
    private File mCurrentFile;
    private String mRootFilePath;
    private OnItemClickListener mOnItemClickListener;
    private List<File> selectedFiles;
    private OnFileDirChange mOnFileDirChange;
    private OnSelectedListener mOnSelectedListener;
    private final DateFormat mSdf;

    public SelectFileAdapter(Context context) {
        mContext = context;
        mCurrentFile = Environment.getExternalStorageDirectory();
        mRootFilePath = mCurrentFile.getAbsolutePath();
        mFiles = mCurrentFile.listFiles();
        mSdf = SimpleDateFormat.getDateInstance();
        selectedFiles = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_file, parent, false);
        return new FileHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final FileHolder viewHolder= (FileHolder) holder;
        final File file = mFiles[position];
        if (file.isDirectory()) {
            //文件夹样式
            viewHolder.ivIcon.setImageResource(R.drawable.ic_folder_yellow_700_36dp);
            viewHolder.cbSelect.setVisibility(View.GONE);
            viewHolder.tvFileName.setText(file.getName());
            viewHolder.tvFileSize.setText("文件:"+file.listFiles().length+" 个");
            viewHolder.tvTime.setVisibility(View.GONE);
            viewHolder.divider.setVisibility(View.GONE);
            viewHolder.flCheck.setVisibility(View.GONE);
        }else {
            viewHolder.ivIcon.setImageResource(R.drawable.ic_insert_drive_file_black_24dp);
            viewHolder.cbSelect.setVisibility(View.VISIBLE);
            viewHolder.tvFileName.setText(file.getName());
            viewHolder.tvFileSize.setText(FormatFileSizeUtil.formatFileSize(file.length()));
            viewHolder.tvTime.setVisibility(View.VISIBLE);
            viewHolder.tvTime.setText(getTime(file.lastModified()));
            viewHolder.divider.setVisibility(View.VISIBLE);
            viewHolder.flCheck.setVisibility(View.VISIBLE);
            if (selectedFiles.contains(file)) {
                viewHolder.cbSelect.setChecked(true);
            }else {
                viewHolder.cbSelect.setChecked(false);
            }
            viewHolder.flCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.cbSelect.callOnClick();
                    viewHolder.cbSelect.setChecked(!viewHolder.cbSelect.isChecked());
                }
            });
            viewHolder.cbSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedFiles.contains(file)) {
                        selectedFiles.remove(file);
                    }else {
                        selectedFiles.add(file);
                    }
                    if (mOnSelectedListener!=null){
                        mOnSelectedListener.onSelect(selectedFiles);
                    }
                }
            });
        }

        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (file.isDirectory()){
                    setCurrentFile(file);
                }else {
                    if (mOnItemClickListener!=null){
                        mOnItemClickListener.onItemClick(file,position);
                    }
                }

            }
        });

    }

    public boolean onBackPress(){

        if (mCurrentFile.getAbsolutePath().equals(mRootFilePath)){
            return false;
        }else {
            File parentFile = mCurrentFile.getParentFile();
            setCurrentFile(parentFile);
            return true;
        }
    }

    public void setCurrentFile(File file){
        mCurrentFile = file;
        mFiles = file.listFiles();
        notifyDataSetChanged();
        if (mOnFileDirChange!=null){
            mOnFileDirChange.onFileChange(mCurrentFile);
        }
    }

    public List<File> getSelectedFiles() {
        return selectedFiles;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        if (mOnSelectedListener!=null){
            mOnSelectedListener.onItemCountChange(mFiles.length);
        }
        return mFiles.length;
    }

    static class FileHolder extends RecyclerView.ViewHolder{
        ImageView ivIcon;
        TextView tvFileName;
        TextView tvFileSize;
        TextView tvTime;
        CheckBox cbSelect;
        FrameLayout flCheck;
        View rootView;
        View divider;
        public FileHolder(View itemView) {
            super(itemView);
            tvFileName = itemView.findViewById(R.id.tv_fileName);
            tvFileSize = itemView.findViewById(R.id.tv_fileSize);
            tvTime = itemView.findViewById(R.id.tv_file_modifyTime);
            cbSelect = itemView.findViewById(R.id.cb_SelectFile);
            ivIcon = itemView.findViewById(R.id.iv_FileIcon);
            rootView = itemView.findViewById(R.id.item_root);
            flCheck = itemView.findViewById(R.id.fl_check);
            divider = itemView.findViewById(R.id.divider);
        }
    }

    private String getTime(long milliSecond){
        Date date = new Date(milliSecond);
        return mSdf.format(date);
    }

    public void setOnSelectedListener(OnSelectedListener onSelectedListener) {
        mOnSelectedListener = onSelectedListener;
    }

    public interface OnFileDirChange{
        void onFileChange(File file);
    }

    public void setOnFileDirChange(OnFileDirChange onFileDirChange) {
        mOnFileDirChange = onFileDirChange;
    }

    public interface OnSelectedListener{
        void onSelect(List<File> fileList);
        void onItemCountChange(int count);
    }

    public interface OnItemClickListener{
        void onItemClick(File file,int position);
    }


}
