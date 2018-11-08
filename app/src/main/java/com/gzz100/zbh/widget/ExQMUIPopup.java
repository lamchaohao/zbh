package com.gzz100.zbh.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.support.annotation.IntDef;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.gzz100.zbh.R;
import com.qmuiteam.qmui.widget.popup.QMUIBasePopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Lam on 2018/7/20.
 */

public class ExQMUIPopup extends QMUIBasePopup {
    public static final int ANIM_GROW_FROM_LEFT = 1;
    public static final int ANIM_GROW_FROM_RIGHT = 2;
    public static final int ANIM_GROW_FROM_CENTER = 3;
    public static final int ANIM_AUTO = 4;

    public static final int DIRECTION_TOP = 0;
    public static final int DIRECTION_BOTTOM = 1;
    public static final int DIRECTION_NONE = 2;
    protected ImageView mArrowUp;
    protected ImageView mArrowDown;
    protected int mAnimStyle;
    protected int mDirection;
    protected int mX = -1;
    protected int mY = -1;
    protected int mArrowCenter;
    // 该PopupWindow的View距离屏幕左右的最小距离
    private int mPopupLeftRightMinMargin = 0;
    // 该PopupWindow的View距离屏幕上下的最小距离
    private int mPopupTopBottomMinMargin = 0;
    private int mPreferredDirection;
    // 计算位置后的偏移x值
    private int mOffsetX = 0;
    // 计算位置后的偏移y值，当浮层在View的上方时使用
    private int mOffsetYWhenTop = 0;
    // 计算位置后的偏移y值，当浮层在View的下方时使用
    private int mOffsetYWhenBottom = 0;
    @SuppressLint("WrongConstant")
    public ExQMUIPopup(Context context) {
        this(context, DIRECTION_NONE);
    }

    public ExQMUIPopup(Context context, @QMUIPopup.Direction int preferredDirection) {
        super(context);
        mAnimStyle = ANIM_AUTO;
        mPreferredDirection = preferredDirection;
        mDirection = mPreferredDirection;
    }

    public void setPopupLeftRightMinMargin(int popupLeftRightMinMargin) {
        mPopupLeftRightMinMargin = popupLeftRightMinMargin;
    }

    public void setPopupTopBottomMinMargin(int popupTopBottomMinMargin) {
        mPopupTopBottomMinMargin = popupTopBottomMinMargin;
    }

    /**
     * 设置根据计算得到的位置后的偏移值
     */
    public void setPositionOffsetX(int offsetX) {
        mOffsetX = offsetX;
    }

    /**
     * 设置根据计算得到的位置后的偏移值
     *
     * @param offsetYWhenTop mDirection!=DIRECTION_BOTTOM 时的 offsetY
     */
    public void setPositionOffsetYWhenTop(int offsetYWhenTop) {
        mOffsetYWhenTop = offsetYWhenTop;
    }

    /**
     * 设置根据计算得到的位置后的偏移值
     *
     * @param offsetYWhenBottom mDirection==DIRECTION_BOTTOM 时的 offsetY
     */
    public void setPositionOffsetYWhenBottom(int offsetYWhenBottom) {
        mOffsetYWhenBottom = offsetYWhenBottom;
    }

    public void setPreferredDirection(int preferredDirection) {
        mPreferredDirection = preferredDirection;
    }

    @Override
    protected Point onShow(View attachedView) {
        calculatePosition(attachedView);

        showArrow();

        setAnimationStyle(mScreenSize.x, mArrowCenter);

        int offsetY = 0;
        if (mDirection == DIRECTION_TOP) {
            offsetY = mOffsetYWhenTop;
        } else if (mDirection == DIRECTION_BOTTOM) {
            offsetY = mOffsetYWhenBottom;
        }
        return new Point(mX + mOffsetX, mY + offsetY);
    }

    private void calculatePosition(View attachedView) {
        if (attachedView != null) {
            int[] attachedViewLocation = new int[2];
            attachedView.getLocationOnScreen(attachedViewLocation);
            mArrowCenter = attachedViewLocation[0] + attachedView.getWidth() / 2;
            if (mArrowCenter < mScreenSize.x / 2) {//描点在左侧
                if (mArrowCenter - mWindowWidth / 2 > mPopupLeftRightMinMargin) {
                    mX = mArrowCenter - mWindowWidth / 2;
                } else {
                    mX = mPopupLeftRightMinMargin;
                }
            } else {//描点在右侧
                if (mArrowCenter + mWindowWidth / 2 < mScreenSize.x - mPopupLeftRightMinMargin) {
                    mX = mArrowCenter - mWindowWidth / 2;
                } else {
                    mX = mScreenSize.x - mPopupLeftRightMinMargin - mWindowWidth;
                }
            }
            //实际的方向和期望的方向可能不一致，每次都需要重新
            mDirection = mPreferredDirection;
            switch (mPreferredDirection) {
                case DIRECTION_TOP:
                    mY = attachedViewLocation[1] - mWindowHeight;
                    if (mY < mPopupTopBottomMinMargin) {
                        mY = attachedViewLocation[1] + attachedView.getHeight();
                        mDirection = DIRECTION_BOTTOM;
                    }
                    break;
                case DIRECTION_BOTTOM:
                    mY = attachedViewLocation[1] + attachedView.getHeight();
                    if (mY > mScreenSize.y - mPopupTopBottomMinMargin) {
                        mY = attachedViewLocation[1] - mWindowHeight;
                        mDirection = DIRECTION_TOP;
                    }
                    break;
                case DIRECTION_NONE:
                    // 默认Y值与attachedView的Y值相同
                    mY = attachedViewLocation[1];
                    break;
            }
        } else {
            mX = (mScreenSize.x - mWindowWidth) / 2;
            mY = (mScreenSize.y - mWindowHeight) / 2;
            mDirection = DIRECTION_NONE;
        }
    }

    /**
     * Set animation style
     *
     * @param screenWidth screen width
     * @param requestedX  distance from left edge
     */
    private void setAnimationStyle(int screenWidth, int requestedX) {
        int arrowPos = requestedX;
        if (mArrowUp != null) {
            arrowPos -= mArrowUp.getMeasuredWidth() / 2;
        }
        boolean onTop = mDirection == DIRECTION_TOP;
        switch (mAnimStyle) {
            case ANIM_GROW_FROM_LEFT:
                mWindow.setAnimationStyle(onTop ? com.qmuiteam.qmui.R.style.QMUI_Animation_PopUpMenu_Left : com.qmuiteam.qmui.R.style.QMUI_Animation_PopDownMenu_Left);
                break;

            case ANIM_GROW_FROM_RIGHT:
                mWindow.setAnimationStyle(onTop ? com.qmuiteam.qmui.R.style.QMUI_Animation_PopUpMenu_Right : com.qmuiteam.qmui.R.style.QMUI_Animation_PopDownMenu_Right);
                break;

            case ANIM_GROW_FROM_CENTER:
                mWindow.setAnimationStyle(onTop ? com.qmuiteam.qmui.R.style.QMUI_Animation_PopUpMenu_Center : com.qmuiteam.qmui.R.style.QMUI_Animation_PopDownMenu_Center);
                break;
            case ANIM_AUTO:
                if (arrowPos <= screenWidth / 4) {
                    mWindow.setAnimationStyle(onTop ? com.qmuiteam.qmui.R.style.QMUI_Animation_PopUpMenu_Left : com.qmuiteam.qmui.R.style.QMUI_Animation_PopDownMenu_Left);
                } else if (arrowPos > screenWidth / 4 && arrowPos < 3 * (screenWidth / 4)) {
                    mWindow.setAnimationStyle(onTop ? com.qmuiteam.qmui.R.style.QMUI_Animation_PopUpMenu_Center : com.qmuiteam.qmui.R.style.QMUI_Animation_PopDownMenu_Center);
                } else {
                    mWindow.setAnimationStyle(onTop ? com.qmuiteam.qmui.R.style.QMUI_Animation_PopUpMenu_Right : com.qmuiteam.qmui.R.style.QMUI_Animation_PopDownMenu_Right);
                }

                break;
        }
    }

    /**
     * 显示箭头（上/下）
     */
    private void showArrow() {
        View showArrow = null;
        switch (mDirection) {
            case DIRECTION_BOTTOM:
                setViewVisibility(mArrowUp, false);
                setViewVisibility(mArrowDown, false);
                showArrow = mArrowUp;
                break;
            case DIRECTION_TOP:
                setViewVisibility(mArrowDown, false);
                setViewVisibility(mArrowUp, false);
                showArrow = mArrowDown;
                break;
            case DIRECTION_NONE:
                setViewVisibility(mArrowDown, false);
                setViewVisibility(mArrowUp, false);
                break;
        }

        if (showArrow != null) {
            final int arrowWidth = mArrowUp.getMeasuredWidth();
            ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams) showArrow.getLayoutParams();
            param.leftMargin = mArrowCenter - mX - arrowWidth / 2;
        }
    }

    /**
     * 菜单弹出动画
     *
     * @param mAnimStyle 默认是 ANIM_AUTO
     */
    public void setAnimStyle(int mAnimStyle) {
        this.mAnimStyle = mAnimStyle;
    }

    @Override
    public void setContentView(View root) {
        @SuppressLint("InflateParams") FrameLayout layout = (FrameLayout) LayoutInflater.from(mContext)
                .inflate(R.layout.ex_popup, null, false);
        mArrowDown = (ImageView) layout.findViewById(R.id.arrow_down);
        mArrowUp = (ImageView) layout.findViewById(R.id.arrow_up);
        FrameLayout box = (FrameLayout) layout.findViewById(R.id.popup_parent);
        box.addView(root);

        super.setContentView(layout);
    }

    private void setViewVisibility(View view, boolean visible) {
        if (view != null) {
            view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public ViewGroup.LayoutParams generateLayoutParam(int width, int height) {
        return new FrameLayout.LayoutParams(width, height);
    }

    @IntDef({DIRECTION_NONE, DIRECTION_TOP, DIRECTION_BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Direction {
    }

}
