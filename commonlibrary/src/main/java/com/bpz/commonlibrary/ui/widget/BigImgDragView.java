package com.bpz.commonlibrary.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.bpz.commonlibrary.util.BarUtil;
import com.bpz.commonlibrary.util.LogUtil;
import com.bpz.commonlibrary.util.gesture.CustomGestureDetector;
import com.bpz.commonlibrary.util.gesture.MoveGestureDetector;
import com.bpz.commonlibrary.util.gesture.OnGestureListener;

import java.io.File;
import java.io.IOException;

public class BigImgDragView extends SurfaceView implements SurfaceHolder.Callback,OnGestureListener {
    private static final String TAG = "BigImgDragView";
    private static final BitmapFactory.Options options = new BitmapFactory.Options();

    static {
        options.inPreferredConfig = Bitmap.Config.RGB_565;
    }

    private int mImageWidth;
    private int mImageHeight;
    private Rect mRscRect;
    /**
     * 控制图片的显示位置
     */
    private float mDestLeft, mDestTop;
    private BitmapRegionDecoder mDecoder;
    private float paintOffsetY = 0.0f;
    // 绘制事的X偏移（去掉marginLeft的宽度）
    private float paintOffsetX = 0.0f;
    private int canvasWidth;
    private int canvasHeight;
    private float lastX, lastY;
    private Bitmap mBitmap;
    private CustomGestureDetector detector;

    public BigImgDragView(Context context) {
        this(context, null);
    }

    public BigImgDragView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BigImgDragView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        LogUtil.e(TAG, "----------init----------");
        ///sdcard/MagazineUnlock/blues-13-2.3.001-bigpicture_13_1.jpg
        File file = new File(Environment.getExternalStorageDirectory(),
                //"/MagazineUnlock/blues-13-2.3.001-bigpicture_13_1.jpg"
                //"/xysx.com.tzq/download/world.jpg"
                "/MagazineUnlock/blues-13-2.3.001-bigpicture_13_1_land.jpg"
                //"/xysx.com.tzq/upload/uploadUtil.jpg"
        );
        try {
            mDecoder = BitmapRegionDecoder.newInstance(file.getAbsolutePath(), false);
            //获取图片的宽高
            mImageWidth = mDecoder.getWidth();//* 2;
            mImageHeight = mDecoder.getHeight();//* 2;
            LogUtil.e(TAG, "mImageWidth: " + mImageWidth + ", mImageHeight: " + mImageHeight);
            detector = new CustomGestureDetector(getContext(),this);
            getHolder().addCallback(this);
        } catch (Exception e) {
            LogUtil.e(TAG, "load exception !");
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!checkCanDrag()) {
            return true;
        }
        detector.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        paintOffsetX = left;
        paintOffsetY = BarUtil.getStatusBarHeight() + top;
    }

    /**
     * 检查是否可以拖拽
     *
     * @return true为可以拖拽
     */
    public boolean checkCanDrag() {
        LogUtil.e(TAG, "check: " + (mBitmap.getWidth() > canvasWidth || mBitmap.getHeight() > canvasHeight));
        return mImageWidth > canvasWidth || mImageHeight > canvasHeight;
    }

    private void onDragStart(float touchX, float touchY) {
        lastX = touchX;
        lastY = touchY;
    }

    public void onDrag(float touchX, float touchY) {
        if (mImageWidth > canvasWidth) {
            mRscRect.offset((int)-(touchX), 0);
            checkWidth();
        }
        if (mImageHeight > canvasHeight) {
            mRscRect.offset(0, (int)- touchY);
            checkHeight();
        }
        drawBitmap();

    }

    @Override
    public void onFling(float startX, float startY, float velocityX, float velocityY) {

    }

    @Override
    public void onScale(float scaleFactor, float focusX, float focusY) {

    }

    private void checkWidth() {
        Rect rect = mRscRect;
        int imageWidth = mImageWidth;
        if (rect.right > imageWidth) {
            rect.right = imageWidth;
            rect.left = imageWidth - canvasWidth;
        }
        if (rect.left < 0) {
            rect.left = 0;
            rect.right = canvasWidth;
        }
    }

    private void checkHeight() {
        Rect rect = mRscRect;
        int imageHeight = mImageHeight;
        if (rect.bottom > imageHeight) {
            rect.bottom = imageHeight;
            rect.top = imageHeight - canvasHeight;
        }
        if (rect.top < 0) {
            rect.top = 0;
            rect.bottom = canvasHeight;
        }
    }

    private void drawBitmap() {
        synchronized (BigImgDragView.class) {
            Canvas canvas = getHolder().lockCanvas();
            if (canvas == null) {
                LogUtil.e(TAG, "canvas is null! ");
                return;
            }
            //获取要显示的bitmap
            mBitmap = mDecoder.decodeRegion(mRscRect, options);
            if (mBitmap == null) {
                LogUtil.e(TAG, "mBitmap is null!");
                return;
            }
            //绘制bitmap
            canvas.drawColor(Color.WHITE);
            canvas.drawBitmap(mBitmap, mDestLeft, mDestTop, null);
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void onDragEnd(float touchX, float touchY) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        canvasHeight = getMeasuredHeight();
        LogUtil.e(TAG, "height: " + canvasHeight);
        canvasWidth = getMeasuredWidth();
        LogUtil.e(TAG, "width: " + canvasWidth);
        int left, top, right, bottom;
        if (mImageWidth >= canvasWidth) {
            left = (mImageWidth - canvasWidth) / 2;
            right = left + canvasWidth;
            mDestLeft = 0;
        } else {
            left = 0;
            right = left + mImageWidth;
            mDestLeft = (canvasWidth - mImageWidth) / 2;
        }

        if (mImageHeight >= canvasHeight) {
            top = (mImageHeight - canvasHeight) / 2;
            bottom = top + canvasHeight;
            mDestTop = 0;
        } else {
            top = 0;
            bottom = top + mImageHeight;
            mDestTop = (canvasHeight - mImageHeight) / 2;
        }
        LogUtil.e(TAG, "surfaceCreated");
        mRscRect = new Rect(left, top, right, bottom);
        drawBitmap();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        LogUtil.e(TAG, "surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        LogUtil.e(TAG, "surfaceDestroyed");
    }

}
