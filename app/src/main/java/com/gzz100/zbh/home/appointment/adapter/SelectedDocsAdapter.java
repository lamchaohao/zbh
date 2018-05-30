package com.gzz100.zbh.home.appointment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gzz100.zbh.R;

import java.io.File;
import java.util.List;

import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.utils.FileUtils;

/**
 * Created by Lam on 2018/2/28.
 */

public class SelectedDocsAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<File> docList;

    private OnItemClickListener mOnItemClickListener;

    private static final int TYPE_ADD=1;
    private static final int TYPE_NORMAL=2;

    public SelectedDocsAdapter(Context context, List<File> docList) {
        mContext = context;
        this.docList = docList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_doc, parent, false);
        return new DocHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        DocHolder viewHolder = (DocHolder) holder;

        int itemViewType = getItemViewType(position);
        if (itemViewType==TYPE_ADD){
            viewHolder.tvDocName.setText("添加");
            viewHolder.docIcon.setImageResource(R.drawable.ic_add_circle_outline_black_24dp);
            viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener!=null){
                        mOnItemClickListener.onItemClick(null);
                    }
                }
            });
            viewHolder.ivClose.setVisibility(View.GONE);
            return;
        }
        final File file = docList.get(position);
        FilePickerConst.FILE_TYPE fileType = FileUtils.getFileType(file.getAbsolutePath());
        switch (fileType){
            case PDF:
                viewHolder.docIcon.setImageResource(R.drawable.pdf_icon);
                break;
            case PPT:
                viewHolder.docIcon.setImageResource(R.drawable.power_point_2013);
                break;
            case TXT:
                viewHolder.docIcon.setImageResource(droidninja.filepicker.R.drawable.icon_file_unknown);
            case WORD:
                viewHolder.docIcon.setImageResource(R.drawable.word_2013);
                break;
            case EXCEL:
                viewHolder.docIcon.setImageResource(R.drawable.excel_2013);
                break;
            case UNKNOWN:
                viewHolder.docIcon.setImageResource(droidninja.filepicker.R.drawable.icon_file_unknown);
                break;
        }
        viewHolder.tvDocName.setText(file.getName());

        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClick(file);
                }
            }
        });
        viewHolder.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClose(position);
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if (position==docList.size()){
            return TYPE_ADD;
        }else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return docList.size()+1;
    }

    public interface OnItemClickListener{
        void onItemClick(File file);
        void onItemClose(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    static class DocHolder extends RecyclerView.ViewHolder{
        ImageView docIcon;
        TextView tvDocName;
        ImageView ivClose;
        View rootView;
        public DocHolder(View itemView) {
            super(itemView);
            docIcon = itemView.findViewById(R.id.iv_docIcon);
            ivClose = itemView.findViewById(R.id.ivClose);
            tvDocName = itemView.findViewById(R.id.tv_docName);
            rootView = itemView.findViewById(R.id.rootView);
        }
    }

}
