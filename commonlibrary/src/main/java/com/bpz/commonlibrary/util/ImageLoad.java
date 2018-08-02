package com.bpz.commonlibrary.util;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bpz.commonlibrary.manager.GlideApp;
import com.bpz.commonlibrary.manager.GlideRequest;
import com.bpz.commonlibrary.net.ProgressCallback;
import com.bpz.commonlibrary.net.ProgressManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.io.File;


/**
 * Created by ZYY
 * on 2018/1/16 21:39.
 * 对Glide加载图片的再封装
 *
 * @author ZYY
 */

public class ImageLoad {

    public static final int CIRCLE = 0;
    public static final int FILLET = 1;
    private static RequestOptions requestOptions;

    public ImageLoad() {
        requestOptions = new RequestOptions();
    }

    /**
     * 从网络加载图片
     *
     * @param url 链接
     */
    @NonNull
    public static GlideRequest<Drawable> load(Context context, String url) {
        return GlideApp.with(context).load(url);
    }

    /**
     * 从文件加载图片
     *
     * @param file 图片文件
     */
    @NonNull
    public static GlideRequest<Drawable> load(Context context, File file) {
        return GlideApp.with(context).load(file);
    }

    /**
     * 从资源目录里加载
     *
     * @param resId 资源id
     */
    @NonNull
    public static GlideRequest<Drawable> load(Context context, @DrawableRes int resId) {
        return GlideApp.with(context).load(resId);
    }

    /**
     * 图片的简单加载
     *
     * @param context   上下文
     * @param url       图片的链接
     * @param imageView 目标控件
     */
    public static void glideLoad(Context context, String url, @NonNull ImageView imageView) {
        load(context, url).into(imageView);
    }

    /**
     * 图片的简单加载
     *
     * @param context   上下文
     * @param resId     资源id
     * @param imageView 目标控件
     */
    public static void glideLoad(Context context, @DrawableRes int resId, @NonNull ImageView imageView) {
        load(context, resId).into(imageView);
    }

    /**
     * 图片的简单加载，从本地文件中
     *
     * @param context   上下文
     * @param file      本地图片
     * @param imageView 目标控件
     */
    public static void glideLoad(Context context, File file, @NonNull ImageView imageView) {
        load(context, file).into(imageView);
    }

    /**
     * 图片带状态监听的加载
     *
     * @param context   上下文
     * @param url       图片的链接
     * @param imageView 目标控件
     */
    public static void glideLoad(Context context, String url, @NonNull ImageView imageView, RequestListener<Drawable> listener) {
        load(context, url).listener(listener).into(imageView);
    }

    /**
     * 图片带状态监听的加载
     *
     * @param context   上下文
     * @param resId     资源id
     * @param imageView 目标控件
     */
    public static void glideLoad(Context context, @DrawableRes int resId, @NonNull ImageView imageView, RequestListener<Drawable> listener) {
        load(context, resId).listener(listener).into(imageView);
    }

    /**
     * 图片带状态监听的加载
     *
     * @param context   上下文
     * @param file      本地文件
     * @param imageView 目标控件
     */
    public static void glideLoad(Context context, File file, @NonNull ImageView imageView, RequestListener<Drawable> listener) {
        load(context, file).listener(listener).into(imageView);
    }

    /**
     * 图片带状态监听和形状变换的加载
     *
     * @param context   上下文
     * @param url       图片的链接
     * @param imageView 目标控件
     * @param shape     要显示的图片的形状
     */
    public static void glideLoad(Context context, String url, @NonNull ImageView imageView, int shape, int roundingRadius) {
        Transformation<Bitmap> transformation;
        GlideRequest<Drawable> load = load(context, url);
        //如有需要其背景也应该为对应的形状
        switch (shape) {
            case CIRCLE:
                //加载圆形图片，
                transformation = new CircleCrop();
                break;
            case FILLET:
                //加载全圆角图片，
                transformation = new RoundedCorners(roundingRadius);
                //transformation = new MyRoundCorners(context,roundingRadius, MyRoundCorners.CornerType.ALL);
                break;
            default:
                //默认无转换
                transformation = null;
                break;
        }
        if (transformation == null) {
            load.into(imageView);
        } else {
            load.transform(transformation)
                    .into(imageView);
        }
    }

    /**
     * 从网络加载图片
     *
     * @param url 链接
     */
    @NonNull
    public static GlideRequest<Drawable> load(Fragment fragment, String url) {
        return GlideApp.with(fragment).load(url);
    }

    /**
     * 从文件加载图片
     *
     * @param file 图片文件
     */
    @NonNull
    public static GlideRequest<Drawable> load(Fragment fragment, File file) {
        return GlideApp.with(fragment).load(file);
    }

    /**
     * 从资源目录里加载
     *
     * @param resId 资源id
     */
    @NonNull
    public static GlideRequest<Drawable> load(Fragment fragment, @DrawableRes int resId) {
        return GlideApp.with(fragment).load(resId);
    }

    /**
     * 图片的简单加载
     *
     * @param fragment  跟随fragment
     * @param url       图片的链接
     * @param imageView 目标控件
     */
    public static void glideLoad(Fragment fragment, String url, @NonNull ImageView imageView) {
        load(fragment, url).into(imageView);
    }

    /**
     * 图片的简单加载
     *
     * @param fragment  跟随fragment
     * @param resId     资源id
     * @param imageView 目标控件
     */
    public static void glideLoad(Fragment fragment, @DrawableRes int resId, @NonNull ImageView imageView) {
        load(fragment, resId).into(imageView);
    }

    /**
     * 图片的简单加载，从本地文件中
     *
     * @param fragment  跟随fragment
     * @param file      本地图片
     * @param imageView 目标控件
     */
    public static void glideLoad(Fragment fragment, File file, @NonNull ImageView imageView) {
        load(fragment, file).into(imageView);
    }

    /**
     * 图片带状态监听的加载
     *
     * @param fragment  跟随fragment
     * @param url       图片的链接
     * @param imageView 目标控件
     */
    public static void glideLoad(Fragment fragment, String url, @NonNull ImageView imageView, RequestListener<Drawable> listener) {
        load(fragment, url).listener(listener).into(imageView);
    }

    /**
     * 图片带状态监听的加载
     *
     * @param fragment  跟随fragment
     * @param resId     资源id
     * @param imageView 目标控件
     */
    public static void glideLoad(Fragment fragment, @DrawableRes int resId, @NonNull ImageView imageView, RequestListener<Drawable> listener) {
        load(fragment, resId).listener(listener).into(imageView);
    }

    /**
     * 图片带状态监听的加载
     *
     * @param fragment  跟随fragment
     * @param file      本地文件
     * @param imageView 目标控件
     */
    public static void glideLoad(Fragment fragment, File file, @NonNull ImageView imageView, RequestListener<Drawable> listener) {
        load(fragment, file).listener(listener).into(imageView);
    }

    /**
     * 图片带状态监听和形状变换的加载
     *
     * @param fragment  跟随fragment
     * @param url       图片的链接
     * @param imageView 目标控件
     * @param shape     要显示的图片的形状
     */
    public static void glideLoad(Fragment fragment, String url, @NonNull ImageView imageView, int shape, int roundingRadius) {
        Transformation<Bitmap> transformation;
        GlideRequest<Drawable> load = load(fragment, url);
        //如有需要其背景也应该为对应的形状
        switch (shape) {
            case CIRCLE:
                //加载圆形图片，
                transformation = new CircleCrop();
                break;
            case FILLET:
                //加载全圆角图片，
                transformation = new RoundedCorners(roundingRadius);
                //transformation = new MyRoundCorners(context,roundingRadius, MyRoundCorners.CornerType.ALL);
                break;
            default:
                //默认无转换
                transformation = null;
                break;
        }
        if (transformation == null) {
            load.into(imageView);
        } else {
            load.transform(transformation)
                    .into(imageView);
        }
    }

    public static void ss(Fragment context, final String url, ImageView imageView) {
        ProgressManager.addListener(url, new ProgressCallback() {
            @Override
            public void onLoading(long contentLength, long bytesWritten, boolean done) {
                LogUtil.e("progress......" + (int) (bytesWritten * 100f / contentLength + 0.5f) + "%");
            }
        });
        GlideApp.with(context)
                .load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        //加载失败，移除监听器
                        ProgressManager.removeListener(url);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        //加载成功，移除监听器
                        ProgressManager.removeListener(url);
                        return false;
                    }
                })
                .into(imageView);
    }


}
