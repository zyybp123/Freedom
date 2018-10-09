package com.bpz.commonlibrary.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.bpz.commonlibrary.R;


/**
 * Created by ZYY
 * 索引条
 */

public class SortBar extends View {
    private Paint mPanit;
    private int mNomalColor = Color.WHITE;
    private int mPressColor = Color.BLACK;
    private String[] indexArr = {"#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"
            , "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    public SortBar(Context context) {
        this(context, null);
    }

    public SortBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SortBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //setBackgroundColor(getResources().getColor(R.color.white_dark));
        setBackgroundColor(getResources().getColor(android.R.color.white));
        mPanit = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPanit.setColor(mNomalColor);
        mPanit.setTextAlign(Paint.Align.CENTER);
        mPanit.setTextSize(50);
    }

    public void setTextColor(int... color) {
        switch (color.length) {
            case 1:
                mNomalColor = color[0];
                break;
            case 2:
                mNomalColor = color[0];
                mPressColor = color[1];
                break;
        }
    }

    private float cellHeight;
    private float mWidth;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        cellHeight = getMeasuredHeight() * 1f / indexArr.length;//盒子高度
        mWidth = getMeasuredWidth() / 2;//总高度
    }

    //记录索引
    int position = -1;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float downY = event.getY();
                int tempPosition = (int) (downY / cellHeight);
                if (position != tempPosition) {
                    position = tempPosition;
                    //回调，传出索引和对应的字符串
                    if (onTouchListener != null) {
                        if (position >= 0 && position < indexArr.length) {
                            String s = indexArr[position];
                            onTouchListener.onTouch(position, s);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                position = -1;
                break;
        }
        invalidate();
        return true;
    }

    private int mHeight;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < indexArr.length; i++) {
            String temp = indexArr[i];
            mHeight = (int) (cellHeight / 2 + cellHeight * i + getTextHeight(temp) / 2);
            mPanit.setColor(i == position ? mPressColor : mNomalColor);
            canvas.drawText(indexArr[i], mWidth, mHeight, mPanit);
        }
    }

    private int getTextHeight(String str) {
        Rect rect = new Rect();
        mPanit.getTextBounds(str, 0, str.length(), rect);
        return rect.height();
    }

    private OnTouchListener onTouchListener;

    public void setOnTouchListener(OnTouchListener Listener) {
        this.onTouchListener = Listener;
    }

    //回调接口
    public interface OnTouchListener {
        void onTouch(int position, String s);
    }

}
