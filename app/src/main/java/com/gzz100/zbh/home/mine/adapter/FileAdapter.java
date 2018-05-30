package com.gzz100.zbh.home.mine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.utils.FormatFileSizeUtil;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.utils.FileUtils;

/**
 * Created by Lam on 2018/5/3.
 */

public class FileAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private File[] mFiles;
    private File mCurrentFile;
    private String mRootFilePath;
    private OnItemClickListener mOnItemClickListener;
    private OnItemCountChangeListener mOnItemCountChangeListener;
    private final DateFormat mSdf;

    public FileAdapter(Context context,File rootFile) {
        mContext = context;
        mCurrentFile = rootFile;
        mRootFilePath = mCurrentFile.getAbsolutePath();
        mFiles = mCurrentFile.listFiles();
        mSdf = SimpleDateFormat.getDateInstance();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_local_file, parent, false);
        return new FileHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final FileHolder viewHolder= (FileHolder) holder;
        final File file = mFiles[position];
        if (file.isDirectory()) {
            //文件夹样式
            viewHolder.ivIcon.setImageResource(R.drawable.ic_folder_yellow_700_36dp);
            viewHolder.tvFileName.setText(file.getName());
            viewHolder.tvFileSize.setText("文件:"+file.listFiles().length+" 个");
            viewHolder.tvTime.setVisibility(View.GONE);
        }else {

            FilePickerConst.FILE_TYPE fileType = FileUtils.getFileType(file.getAbsolutePath());
            switch (fileType){
                case PDF:
                    viewHolder.ivIcon.setImageResource(R.drawable.pdf_icon);
                    break;
                case PPT:
                    viewHolder.ivIcon.setImageResource(R.drawable.power_point_2013);
                    break;
                case TXT:
                    viewHolder.ivIcon.setImageResource(R.drawable.ic_insert_drive_file_black_24dp);
                case WORD:
                    viewHolder.ivIcon.setImageResource(R.drawable.word_2013);
                    break;
                case EXCEL:
                    viewHolder.ivIcon.setImageResource(R.drawable.excel_2013);
                    break;
                case UNKNOWN:
                    viewHolder.ivIcon.setImageResource(R.drawable.ic_insert_drive_file_black_24dp);
                    break;
            }

            viewHolder.tvFileName.setText(file.getName());
            viewHolder.tvFileSize.setText(FormatFileSizeUtil.formatFileSize(file.length()));
            viewHolder.tvTime.setVisibility(View.VISIBLE);
            viewHolder.tvTime.setText(getTime(file.lastModified()));

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
        viewHolder.ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onItemShareClick(file,position);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        if (mOnItemCountChangeListener!=null){
            mOnItemCountChangeListener.onChange(mFiles.length);
        }
        return mFiles.length;
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

    private void setCurrentFile(File file){
        mCurrentFile = file;
        mFiles = file.listFiles();
        notifyDataSetChanged();
    }

    public void refresh(){
        setCurrentFile(mCurrentFile);
    }

    private String getTime(long milliSecond){
        Date date = new Date(milliSecond);
        return mSdf.format(date);
    }

    static class FileHolder extends RecyclerView.ViewHolder{
        ImageView ivIcon;
        ImageView ivMore;
        TextView tvFileName;
        TextView tvFileSize;
        TextView tvTime;
        View rootView;
        public FileHolder(View itemView) {
            super(itemView);
            tvFileName = itemView.findViewById(R.id.tv_fileName);
            tvFileSize = itemView.findViewById(R.id.tv_fileSize);
            tvTime = itemView.findViewById(R.id.tv_file_modifyTime);
            ivIcon = itemView.findViewById(R.id.iv_FileIcon);
            rootView = itemView.findViewById(R.id.item_root);
            ivMore = itemView.findViewById(R.id.iv_more);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemCountChangeListener(OnItemCountChangeListener onItemCountChangeListener) {
        mOnItemCountChangeListener = onItemCountChangeListener;
    }

    public interface OnItemClickListener{
        void onItemClick(File file,int position);
        void onItemShareClick(File file,int position);
    }

    public interface OnItemCountChangeListener{
        void onChange(int count);
    }

}
