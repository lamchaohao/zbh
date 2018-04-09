package com.gzz100.zbh.home.meetingadmin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.utils.GlideApp;

import java.util.List;

/**
 * Created by Lam on 2018/3/30.
 */

public class PPTAdapter extends RecyclerView.Adapter {

    private List<String> picUrlList;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private int positivePPt;

    public PPTAdapter(List<String> picUrlList, Context context) {
        this.picUrlList = picUrlList;
        mContext = context;
    }


    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_imageview_full, parent, false);
        return new PicHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        PicHolder picHolder = (PicHolder) holder;
        GlideApp.with(mContext)
                .load(picUrlList.get(position))
                .encodeQuality(100)
                .into(picHolder.imageView);
        if (positivePPt==position) {
            picHolder.rootView.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
        }else {
            picHolder.rootView.setBackgroundColor(0);
        }
        if (mOnItemClickListener!=null){
            picHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(position,picUrlList.get(position));
                }
            });
        }
    }


    public void setPositivePPt(int positivePos) {
        notifyItemChanged(positivePPt);
        this.positivePPt = positivePos;
        notifyItemChanged(positivePPt);
    }

    @Override
    public int getItemCount() {
        return picUrlList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(int pos, String url);
    }

    static class PicHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        View rootView;
        public PicHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            rootView = itemView.findViewById(R.id.rootView);
        }
    }

}
