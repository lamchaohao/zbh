package com.gzz100.zbh.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Lam on 2018/5/30.
 */

public class DividerView extends View {


    public DividerView(Context context) {
        super(context);
    }

    public DividerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DividerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final int left;
        final int right;

        left = getPaddingLeft();
        right = getWidth() - getPaddingRight();
        canvas.clipRect(left, getPaddingTop(), right,
                getHeight() - getPaddingBottom());
        Paint paint =new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setColor(Color.parseColor("#9C27B0"));
        canvas.drawLine(left,0,right,getHeight(),paint);
    }
}
