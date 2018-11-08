package com.gzz100.zbh.home.mine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.home.mine.bean.FileEntity;
import com.gzz100.zbh.utils.FormatFileSizeUtil;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.utils.FileUtils;

/**
 * Created by Lam on 2018/5/3.
 */

public class FileAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private File mCurrentFile;
    private List<FileEntity> fileList;
    private String mRootFilePath;
    private OnItemClickListener mOnItemClickListener;
    private OnItemCountChangeListener mOnItemCountChangeListener;
    private final DateFormat mSdf;
    private boolean isEditMode;

    public FileAdapter(Context context,File rootFile) {
        mContext = context;
        mCurrentFile = rootFile;
        mRootFilePath = mCurrentFile.getAbsolutePath();
//        mFiles = mCurrentFile.listFiles();
        mSdf = SimpleDateFormat.getDateInstance();
        fileList = new ArrayList<>();
        for (File file : mCurrentFile.listFiles()) {
            fileList.add(new FileEntity(file));
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_local_file, parent, false);
        return new FileHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final FileHolder viewHolder= (FileHolder) holder;
        final File file = fileList.get(position).getFile();
        if (file.isDirectory()) {
            //文件夹样式
            viewHolder.ivIcon.setImageResource(R.drawable.ic_folder_yellow_700_36dp);
            viewHolder.tvFileName.setText(file.getName());
            viewHolder.tvFileSize.setText("文件:"+file.listFiles().length+" 个");
            viewHolder.tvTime.setVisibility(View.GONE);
            viewHolder.ivMore.setVisibility(View.GONE);
        }else {

            FilePickerConst.FILE_TYPE fileType = FileUtils.getFileType(file.getAbsolutePath());
            switch (fileType){
                case PDF:
                    viewHolder.ivIcon.setImageResource(R.drawable.ic_pdf);
                    break;
                case PPT:
                    viewHolder.ivIcon.setImageResource(R.drawable.ic_power_point);
                    break;
                case TXT:
                    viewHolder.ivIcon.setImageResource(R.drawable.ic_insert_drive_file_black_24dp);
                case WORD:
                    viewHolder.ivIcon.setImageResource(R.drawable.ic_word);
                    break;
                case EXCEL:
                    viewHolder.ivIcon.setImageResource(R.drawable.ic_excel);
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

        if (isEditMode){
            viewHolder.cbFile.setVisibility(View.VISIBLE);
            viewHolder.cbFile.setChecked(fileList.get(position).isSelect());
        }else {
            viewHolder.cbFile.setVisibility(View.GONE);
        }

            viewHolder.cbFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fileList.get(position).setChange();
                }
            });

        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditMode){
                    //如果为编辑模式
                    FileEntity fileEntity = fileList.get(position);
                    fileEntity.setChange();
                    viewHolder.cbFile.setChecked(fileEntity.isSelect());
                }else {
                    if (file.isDirectory()){
                        setCurrentFile(file);
                    }else {
                        if (mOnItemClickListener!=null){
                            mOnItemClickListener.onItemClick(file,position);
                        }
                    }
                }

            }
        });

        viewHolder.rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!isEditMode){
                    fileList.get(position).setSelect(true);
                    setEditMode(true);

                }
                return true;
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

        return fileList.size();
    }

    /**
     * 设置是否为多选模式
     * @param enable true为多选编辑模式,false为正常模式
     */
    public void setEditMode(boolean enable){
        isEditMode = enable;
        //只有在取消多选时候才重置所有选项
        if (!enable) {
            for (FileEntity fileEntity : fileList) {
                fileEntity.setSelect(false);
            }
        }

        if (mOnItemClickListener!=null){
            mOnItemClickListener.onEditModeChange(enable);
        }
        if (mOnItemCountChangeListener!=null){
            mOnItemCountChangeListener.onChange(fileList.size());
        }
        notifyDataSetChanged();
    }

    public boolean isEditMode(){
        return isEditMode;
    }

    public String getCurrentPath(){
        return mCurrentFile.getAbsolutePath();
    }

    public List<File> getSelectedFiles(){
        List<File> selectedList = new ArrayList<>();
        for (FileEntity fileEntity : fileList) {
            if (fileEntity.isSelect()) {
                selectedList.add(fileEntity.getFile());
            }
        }
        return selectedList;
    }

    /**
     * 返回操作处理
     * @return 是否消耗此次点击
     */
    public boolean onBackPress(){

        if (isEditMode){
            setEditMode(false);
            return true;
        }

        if (mCurrentFile.getAbsolutePath().equals(mRootFilePath)){
            return false;
        }else {
            File parentFile = mCurrentFile.getParentFile();
            setCurrentFile(parentFile);
            return true;
        }
    }

    /**
     * 设置当前目录的文件夹
     * @param file 需要设置的当前目录
     */
    private void setCurrentFile(File file){
        mCurrentFile = file;
        fileList.clear();
        for (File tempFile : mCurrentFile.listFiles()) {
            fileList.add(new FileEntity(tempFile));
        }
        notifyDataSetChanged();
        if (mOnItemCountChangeListener!=null){
            mOnItemCountChangeListener.onChange(fileList.size());
        }
    }

    public void refresh(){
        setCurrentFile(mCurrentFile);
    }

    private String getTime(long milliSecond){
        Date date = new Date(milliSecond);
        return mSdf.format(date);
    }

    public List<FileEntity> getFileList() {
        return fileList;
    }

    static class FileHolder extends RecyclerView.ViewHolder{
        ImageView ivIcon;
        ImageView ivMore;
        CheckBox cbFile;
        TextView tvFileName;
        TextView tvFileSize;
        TextView tvTime;
        View rootView;
        public FileHolder(View itemView) {
            super(itemView);
            cbFile = itemView.findViewById(R.id.cb_file);
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
        void onEditModeChange(boolean isEditMode);
    }

    public interface OnItemCountChangeListener{
        void onChange(int count);
    }

}
