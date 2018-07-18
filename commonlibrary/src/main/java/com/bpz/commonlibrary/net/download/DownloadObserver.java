package com.bpz.commonlibrary.net.download;

import android.support.v7.widget.RecyclerView;

import com.bpz.commonlibrary.entity.ResInfo;
import com.bpz.commonlibrary.net.ProgressCallback;
import com.bpz.commonlibrary.util.LogUtil;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2018/1/31.
 * 下载进度观察者
 */

public class DownloadObserver implements Observer<ResponseBody>, ProgressCallback {
    private final String TAG = "DownloadObserver";
    private ResInfo resInfo;
    // 在RxJava 2.x 中，Disposable可以做到切断的操作，让Observer观察者不再接收上游事件，但上游的发送不会停止
    private Disposable disposable;
    private RecyclerView.Adapter mAdapter;
    private int position;

    private MyDownloadListener listener;


    public DownloadObserver(ResInfo resInfo, MyDownloadListener listener) {
        this.resInfo = resInfo;
        this.listener = listener;
    }

    public DownloadObserver(ResInfo resInfo, RecyclerView.Adapter mAdapter, int position) {
        this.resInfo = resInfo;
        this.mAdapter = mAdapter;
        this.position = position;
    }

    @Override
    public void onLoading(long contentLength, long bytesWritten, boolean done) {
        //LogUtil.e(TAG, "progress: " + resInfo.getProgress());
        if (listener != null) {
            listener.onDownloading(resInfo.getUrl(), resInfo.getProgress());
        }
        //把onLoading回调到主线程
        Observable.just(resInfo.getProgress())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressObserver());
    }

    @Override
    public void onSubscribe(Disposable d) {
        //可以通过此字段做一些控制
        this.disposable = d;
        //此处建立订阅关系
        LogUtil.e(TAG, "start......");
    }

    /**
     * 进度观察者，把进度回调到主线程
     */
    public class ProgressObserver implements Observer<Integer> {
        private final String TAG = "ProgressObserver";
        private Disposable disposable;

        @Override
        public void onSubscribe(Disposable d) {
            this.disposable = d;
        }

        @Override
        public void onNext(Integer integer) {
            loading(integer);
        }

        @Override
        public void onError(Throwable e) {
            onFail(e);
        }

        @Override
        public void onComplete() {
            LogUtil.e(TAG, "onComplete");
        }
    }

    @Override
    public void onNext(final ResponseBody body) {
        LogUtil.e(TAG, "onNext()" + Thread.currentThread());
    }

    @Override
    public void onError(Throwable e) {
        onFail(e);
    }

    @Override
    public void onComplete() {
        //完成监听
        LogUtil.e(TAG, "onComplete()");
        File file = new File(resInfo.getAbsolutePath());
        if (resInfo.getTotalSize() == file.length() && resInfo.getStatus() == State.LOADING) {
            //此时已经下载成功了，在主线程
            onSuccess();
        } else if (resInfo.getStatus() == State.PAUSE) {
            //下载暂停了,通知界面
            loading(resInfo.getProgress());
        } else {
            //下载失败了
            onFail(new Exception("download fail !"));
        }
    }

    /**
     * 下载中，回调在主线程
     *
     * @param progress 下载进度
     */
    public void loading(int progress) {
        if (mAdapter != null) {
            mAdapter.notifyItemChanged(position);
        }
    }

    /**
     * 下载失败
     *
     * @param e 异常的信息
     */
    public void onFail(Throwable e) {
        LogUtil.e(TAG, "Exception: " + e);
        resInfo.setStatus(State.FAIL);
        //更新数据库数据
        //downloadManager.updateOne(resInfo);
        if (mAdapter != null) {
            mAdapter.notifyItemChanged(position);
        }
        if (listener != null) {
            listener.onDownloadFail(resInfo.getUrl(), e);
        }
    }

    /**
     * 下载成功
     */
    public void onSuccess() {
        resInfo.setStatus(State.SUCCESS);
        //更新数据库数据
        //downloadManager.updateOne(resInfo);
        if (mAdapter != null) {
            mAdapter.notifyItemChanged(position);
        }
        if (listener != null) {
            listener.onDownloadSuccess(resInfo);
        }
    }


}
