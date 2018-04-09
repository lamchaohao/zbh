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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DocHolder viewHolder = (DocHolder) holder;
        final File file = docList.get(position);
        FilePickerConst.FILE_TYPE fileType = FileUtils.getFileType(file.getAbsolutePath());
        switch (fileType){
            case PDF:
                viewHolder.docIcon.setImageResource(droidninja.filepicker.R.drawable.icon_file_pdf);
                break;
            case PPT:
                viewHolder.docIcon.setImageResource(droidninja.filepicker.R.drawable.icon_file_ppt);
                break;
            case TXT:
                viewHolder.docIcon.setImageResource(droidninja.filepicker.R.drawable.icon_file_unknown);
            case WORD:
                viewHolder.docIcon.setImageResource(droidninja.filepicker.R.drawable.icon_file_doc);
                break;
            case EXCEL:
                viewHolder.docIcon.setImageResource(droidninja.filepicker.R.drawable.icon_file_xls);
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
    }

    @Override
    public int getItemCount() {
        return docList.size();
    }

    public interface OnItemClickListener{
        void onItemClick(File file);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    static class DocHolder extends RecyclerView.ViewHolder{
        ImageView docIcon;
        TextView tvDocName;
        View rootView;
        public DocHolder(View itemView) {
            super(itemView);
            docIcon = itemView.findViewById(R.id.iv_docIcon);
            tvDocName = itemView.findViewById(R.id.tv_docName);
            rootView = itemView.findViewById(R.id.rootView);
        }
    }

}
