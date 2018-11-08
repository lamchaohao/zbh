package com.gzz100.zbh.home.mine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.data.entity.StarFileEntity;

import java.util.List;

import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.utils.FileUtils;

/**
 * Created by Lam on 2018/5/16.
 */

public class StarFileAdapter extends RecyclerView.Adapter {

    private List<StarFileEntity> mFileList;
    private OnItemClickListener mOnItemClickListener;
    private Context mContext;

    public StarFileAdapter(List<StarFileEntity> fileList, Context context) {
        mFileList = fileList;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_star_file, parent, false);
        return new StarFileHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        StarFileHolder viewHolder = (StarFileHolder) holder;
        final StarFileEntity fileEntity= mFileList.get(position);
        FilePickerConst.FILE_TYPE fileType = FileUtils.getFileType(fileEntity.getDocumentType());
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

        viewHolder.tvFileName.setText(fileEntity.getDocumentName());

        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClick(position,fileEntity);
                }
            }
        });

        viewHolder.ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onShareClick(position,fileEntity);
                }
            }
        });
    }

    public void removeStar(StarFileEntity fileEntity){
        if (mFileList!=null) {
            mFileList.remove(fileEntity);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return mFileList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    static class StarFileHolder extends RecyclerView.ViewHolder{

        ImageView ivIcon;
        TextView tvFileName;
        View rootView;
        ImageView ivMore;
        public StarFileHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_FileIcon);
            tvFileName = itemView.findViewById(R.id.tv_fileName);
            ivMore = itemView.findViewById(R.id.iv_more);
            rootView = itemView.findViewById(R.id.rootView);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int pos,StarFileEntity fileEntity);
        void onShareClick(int pos,StarFileEntity fileEntity);
    }
}
