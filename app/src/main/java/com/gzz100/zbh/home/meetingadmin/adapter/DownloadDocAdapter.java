package com.gzz100.zbh.home.meetingadmin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.data.entity.DownloadFileEntity;

import java.util.ArrayList;
import java.util.List;

import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.utils.FileUtils;

/**
 * Created by Lam on 2018/5/17.
 */

public class DownloadDocAdapter extends RecyclerView.Adapter {

    private List<DownloadFileEntity> mFileEntityList ;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public DownloadDocAdapter(List<DownloadFileEntity> fileEntityList, Context context) {
        mFileEntityList = fileEntityList;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_download_doc, parent, false);
        return new DownloadHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final DownloadHolder viewHolder = (DownloadHolder) holder;
        final DownloadFileEntity file = mFileEntityList.get(position);
        viewHolder.cbSelect.setChecked(file.isSelected());
        viewHolder.cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                file.setSelected(isChecked);
            }
        });
        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                viewHolder.cbSelect.setChecked(!file.isSelected());
                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClick(position,file);
                }
            }
        });
        viewHolder.tvDocName.setText(file.getDocumentName());

        FilePickerConst.FILE_TYPE fileType = FileUtils.getFileType(file.getDocumentType());
        switch (fileType){
            case PDF:
                viewHolder.ivIcon.setImageResource(R.drawable.ic_pdf);
                break;
            case PPT:
                viewHolder.ivIcon.setImageResource(R.drawable.ic_power_point);
                break;
            case TXT:
                viewHolder.ivIcon.setImageResource(droidninja.filepicker.R.drawable.icon_file_unknown);
            case WORD:
                viewHolder.ivIcon.setImageResource(R.drawable.ic_word);
                break;
            case EXCEL:
                viewHolder.ivIcon.setImageResource(R.drawable.ic_excel);
                break;
            case UNKNOWN:
                viewHolder.ivIcon.setImageResource(droidninja.filepicker.R.drawable.icon_file_unknown);
                break;
        }
    }

    public List<DownloadFileEntity> getSelectedFile(){
        List<DownloadFileEntity> selectedList=new ArrayList<>();
        for (DownloadFileEntity fileEntity : mFileEntityList) {
            if (fileEntity.isSelected()) {
                selectedList.add(fileEntity);
            }
        }
        return selectedList;
    }

    public interface OnItemClickListener{
        void onItemClick(int pos,DownloadFileEntity fileEntity);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return mFileEntityList.size();
    }

    static class DownloadHolder extends RecyclerView.ViewHolder{

        CheckBox cbSelect;
        ImageView ivIcon;
        TextView tvDocName;
        ImageView ivMore;
        View rootView;
        public DownloadHolder(View itemView) {
            super(itemView);
            cbSelect = itemView.findViewById(R.id.cb_select);
            ivIcon = itemView.findViewById(R.id.iv_document_icon);
            ivMore = itemView.findViewById(R.id.iv_more);
            tvDocName = itemView.findViewById(R.id.tv_document_name);
            rootView = itemView.findViewById(R.id.rootView);
        }
    }
}
