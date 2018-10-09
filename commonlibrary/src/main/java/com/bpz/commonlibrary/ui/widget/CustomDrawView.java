package com.bpz.commonlibrary.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.bpz.commonlibrary.util.LogUtil;

public class CustomDrawView extends View {
    private static final String TAG = "CustomDrawView";
    private Bitmap bitmap;
    private Canvas mCanvas;

    public CustomDrawView(Context context) {
        this(context,null);
    }

    public CustomDrawView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomDrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        mCanvas = new Canvas();
        mCanvas.drawColor(Color.RED);
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        LogUtil.e(TAG,"onMeasure");
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);
        LogUtil.e(TAG,"layout");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        LogUtil.e(TAG,"onLayout");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        LogUtil.e(TAG,"onSizeChanged");
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCanvas.setBitmap(bitmap);
        canvas.drawBitmap(bitmap,0,0,null);

        LogUtil.e(TAG,"onDraw");
       // canvas.drawColor(Color.BLUE);
    }


}
