package com.gzz100.zbh.home.meetingadmin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.data.entity.DocumentEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.utils.FileUtils;

/**
 * Created by Lam on 2018/4/23.
 */

public class CloudFileAdapter extends RecyclerView.Adapter {


    private Context mContext;
    private List<DocumentEntity> mDocumentList;
    OnItemClickListener mOnItemClickListener;
    public CloudFileAdapter(Context context, List<DocumentEntity> documentList) {
        mContext = context;
        mDocumentList = documentList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_cloud_file, parent, false);
        return new CloudHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        CloudHolder viewHolder= (CloudHolder) holder;
        final DocumentEntity documentEntity = mDocumentList.get(position);
        FilePickerConst.FILE_TYPE fileType = FileUtils.getFileType(documentEntity.getDocumentName());
        switch (fileType){
            case PDF:
                viewHolder.ivFileIcon.setImageResource(R.drawable.pdf_icon);
                break;
            case PPT:
                viewHolder.ivFileIcon.setImageResource(R.drawable.power_point_2013);
                break;
            case TXT:
                viewHolder.ivFileIcon.setImageResource(droidninja.filepicker.R.drawable.icon_file_unknown);
            case WORD:
                viewHolder.ivFileIcon.setImageResource(R.drawable.word_2013);
                break;
            case EXCEL:
                viewHolder.ivFileIcon.setImageResource(R.drawable.excel_2013);
                break;
            case UNKNOWN:
                viewHolder.ivFileIcon.setImageResource(droidninja.filepicker.R.drawable.icon_file_unknown);
                break;
        }
        viewHolder.tvDocName.setText(documentEntity.getDocumentName());
        viewHolder.ivRemoveFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onRemoveClick(position,documentEntity);
                }
            }
        });
        viewHolder.ivFileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClick(position,documentEntity);
                }
            }
        });
        viewHolder.tvDocName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClick(position,documentEntity);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDocumentList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onRemoveClick(int position,DocumentEntity documentEntity);
        void onItemClick(int position,DocumentEntity documentEntity);
    }

    static class CloudHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_FileIcon)
        ImageView ivFileIcon;
        @BindView(R.id.iv_removeFile)
        ImageView ivRemoveFile;
        @BindView(R.id.tv_docName)
        TextView tvDocName;
        public CloudHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
