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
        final DocumentEntity documentEntity = mDocumentList.get(position);
        docHolder.ivIcon.setImageResource(R.drawable.ic_message_primary_24dp);
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

    @Override
    public int getItemCount() {
        return mDocumentList.size();
    }


    public interface OnItemClickListener{
        void onClick(int pos,DocumentEntity documentEntity);
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
