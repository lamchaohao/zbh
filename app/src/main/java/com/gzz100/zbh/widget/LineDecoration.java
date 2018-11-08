package com.gzz100.zbh.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.view.View;

import com.gzz100.zbh.utils.DensityUtil;

/**
 * Created by Lam on 2018/10/8.
 */

public class LineDecoration extends ItemDecoration {

    private int height;

    public LineDecoration(Context context) {
        height = DensityUtil.dp2px(context, 1);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = height;//类似加了一个bottom padding
    }
}
