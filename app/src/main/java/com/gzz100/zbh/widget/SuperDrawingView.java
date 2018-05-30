package com.gzz100.zbh.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.gzz100.zbh.home.meetingadmin.drawbean.DrawPath;
import com.gzz100.zbh.mimc.SyncCanvasBean;

import java.util.ArrayList;
import java.util.List;

public class SuperDrawingView extends View {
    private String TAG = "Super";
    private Paint paint;
    private Path path;
    private float downX,downY;
    private float tempX,tempY;
    private int paintWidth = 10;
    private List<DrawPath> drawPathList;
    private List<DrawPath> savePathList;
    private List<SyncCanvasBean> syncPathList;
    private boolean clearMode;
    private OnDrawListener mOnDrawListener;

    public SuperDrawingView(Context context) {
        super(context);
        init();
    }

    public SuperDrawingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SuperDrawingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setOnDrawListener(OnDrawListener onDrawListener) {
        mOnDrawListener = onDrawListener;
    }

    private void init() {
        drawPathList = new ArrayList<>();
        savePathList = new ArrayList<>();
        syncPathList = new ArrayList<>();
        initPaint();
    }

    public void onSyncCanvasMsgReceived(List<SyncCanvasBean> syncList){
        Log.i(TAG,getWidth()+","+getHeight());
        int width = getWidth();
        int height = getHeight();
        for (SyncCanvasBean syncBean : syncList) {
            float realX = width*syncBean.getxPiot();
            float realY = height*syncBean.getyPiot();
            drawpath(syncBean.getAction(),realX,realY);
        }
    }

    public void onCommandReceived(){

    }

    private void initPaint() {

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(paintWidth);
        paint.setStyle(Paint.Style.STROKE);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (clearMode ){
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            canvas.drawPaint(paint);
        }
        if(drawPathList!=null){
            for(DrawPath drawPath:drawPathList){
                if(drawPath.path!=null){
                    canvas.drawPath(drawPath.path,drawPath.paint);
                }
            }
        }
    }

    public static interface OnDrawListener{
        void onDrawCompleted(List<SyncCanvasBean> syncPointList);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();
        int height = getHeight();
        int width = getWidth();
        float relativeX = x/width;
        float relativeY = y/height;
        Log.i(TAG,"height->"+height+" ,width->"+width);
        Log.i(TAG, "onTouchEvent: relativeX->"+relativeX+",relativeY->"+relativeY);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                syncPathList.clear();//每次都要归零
                syncPathList.add(new SyncCanvasBean(event.getAction(), relativeX, relativeY));
                break;
            case MotionEvent.ACTION_MOVE:
                syncPathList.add(new SyncCanvasBean(event.getAction(), relativeX, relativeY));
                break;
            case MotionEvent.ACTION_UP:
                syncPathList.add(new SyncCanvasBean(event.getAction(), relativeX, relativeY));
                if (mOnDrawListener!=null) {
                    mOnDrawListener.onDrawCompleted(syncPathList);
                }
                break;
        }


        drawpath(event.getAction(),event.getX(),event.getY());

        return true;
    }

    private void drawpath(int action,float xPiot,float yPiot) {
        switch (action){
            case MotionEvent.ACTION_DOWN:
                downX = xPiot;
                downY = yPiot;
                path = new Path();//每次手指下去都是一条新的路径
                path.moveTo(downX,downY);
                DrawPath drawPath = new DrawPath();
                drawPath.paint = paint;
                drawPath.path = path;
                drawPathList.add(drawPath);
                invalidate();
                tempX = downX;
                tempY = downY;
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = xPiot;
                float moveY = yPiot;
                path.quadTo(tempX,tempY,moveX,moveY);
                invalidate();
                tempX = moveX;
                tempY = moveY;
                break;
            case MotionEvent.ACTION_UP:
                //每次手指抬起都要重置下画笔,不然画笔会保存了之前的设置什么画笔的属性会引起bug
//                initPaint();
                break;
        }
    }

    /**
     * 撤销功能
     */
    public void undo() {
        if(drawPathList!=null&&drawPathList.size()>=1){
            savePathList.add(drawPathList.get(drawPathList.size()-1));
            drawPathList.remove(drawPathList.size()-1);
            invalidate();
        }
    }
    /**
     * 反撤销功能
     */
    public void reundo() {
        if (savePathList != null && !savePathList.isEmpty()) {
            drawPathList.add(savePathList.get(savePathList.size() - 1));
            savePathList.remove(savePathList.size() - 1);
            invalidate();
        }
    }
    /**
     * 改变画笔颜色
     * @param color
     */
    public void resetPaintColor(int color) {
        paint.setColor(color);
    }
    /**
     * 放大就是改变画笔的宽度
     */
    public void resetPaintWidth() {
        paintWidth+=2;
        paint.setStrokeWidth(paintWidth);
    }
    /**
     * 橡皮擦功能 把画笔的颜色和view的背景颜色一样就ok,然后把画笔的宽度变大了,擦除的时候显得擦除范围大点
     */
    public void eraser() {
        paint.setColor(Color.WHITE);//这是view背景的颜色
        paint.setStrokeWidth(paintWidth+6);
    }
    public void clearAll(){
        if (savePathList != null && drawPathList!=null) {
            drawPathList.clear();
//            savePathList.clear();
            clearMode=true;
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            invalidate();
            clearMode=false;
            initPaint();
        }
    }
}
