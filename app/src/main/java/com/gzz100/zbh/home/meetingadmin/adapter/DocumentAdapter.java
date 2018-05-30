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

import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.utils.FileUtils;

/**
 * Created by Lam on 2018/3/27.
 */

public class DocumentAdapter extends RecyclerView.Adapter{

    private List<DocumentEntity> mDocumentList;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public DocumentAdapter(Context context, List<DocumentEntity> documentList) {
        mDocumentList = documentList;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_document,parent,false);
        return new DocHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        DocHolder docHolder = (DocHolder) holder;

        if (position==mDocumentList.size()){
            docHolder.ivIcon.setImageResource(R.drawable.ic_add_circle_outline_black_24dp);
            docHolder.tvName.setText("添加文件");
            docHolder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener!=null) {
                        mOnItemClickListener.onAddFileClick();
                    }
                }
            });
        }else {
            final DocumentEntity documentEntity = mDocumentList.get(position);
            FilePickerConst.FILE_TYPE fileType = FileUtils.getFileType(documentEntity.getDocumentName());
            switch (fileType){
                case PDF:
                    docHolder.ivIcon.setImageResource(R.drawable.pdf_icon);
                    break;
                case PPT:
                    docHolder.ivIcon.setImageResource(R.drawable.power_point_2013);
                    break;
                case TXT:
                    docHolder.ivIcon.setImageResource(droidninja.filepicker.R.drawable.icon_file_unknown);
                case WORD:
                    docHolder.ivIcon.setImageResource(R.drawable.word_2013);
                    break;
                case EXCEL:
                    docHolder.ivIcon.setImageResource(R.drawable.excel_2013);
                    break;
                case UNKNOWN:
                    docHolder.ivIcon.setImageResource(droidninja.filepicker.R.drawable.icon_file_unknown);
                    break;
            }

            docHolder.tvName.setText(documentEntity.getDocumentName());
            docHolder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener!=null) {
                        mOnItemClickListener.onClick(position,documentEntity);
                    }
                }
            });
        }



    }

    @Override
    public int getItemCount() {
        return mDocumentList.size()+1;
    }



    public interface OnItemClickListener{
        void onClick(int pos,DocumentEntity documentEntity);
        void onAddFileClick();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    static class DocHolder extends RecyclerView.ViewHolder{
        ImageView ivIcon;
        TextView tvName;
        View rootView;
        public DocHolder(View itemView) {
            super(itemView);
            rootView = itemView.findViewById(R.id.rootView);
            ivIcon = itemView.findViewById(R.id.iv_document_icon);
            tvName = itemView.findViewById(R.id.tv_document_name);
        }
    }


}
