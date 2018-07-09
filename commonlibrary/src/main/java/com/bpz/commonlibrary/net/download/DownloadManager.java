package com.bpz.commonlibrary.net.download;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;


import com.bpz.commonlibrary.entity.ResInfo;
import com.bpz.commonlibrary.net.ProgressManager;
import com.bpz.commonlibrary.net.RetrofitTool;
import com.bpz.commonlibrary.util.LogUtil;
import com.bpz.commonlibrary.util.StringUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Retrofit;

/**
 * Created by Administrator on 2018/1/29.
 * 文件下载管理器
 *
 * @streaming 此注解的作用是一边下载一边往文件写入
 * 使用Retrofit进行文件下载时，通过拦截响应体来获取进度，而下载的本质是文件流是否写入到本地和写入了多少
 */

public class DownloadManager {
    private static final String TAG = "DownloadManager";
    //下载管理器实例
    private static volatile DownloadManager mInstance;
    //Retrofit工具实例
    private RetrofitTool retrofitTool;
    //retrofit实例
    private Retrofit retrofit;
    //线程池
    private ExecutorService executorService = null;
    //数据库操作管理
    //private ResInfoDao resInfoDao;
    //创建一个map管理下载队列
    //private Map<Long, ResInfo> resInfoMap;
    private StateChangeListener listener;

    //指定最大的线程数
    private DownloadManager(int threadCount) {
        //构造内部可以初始化相应参数
        retrofitTool = RetrofitTool.getInstance("");
        //初始化线程池的线程数量,限定在1到cpu核心数
        int maxCount = Runtime.getRuntime().availableProcessors();
        LogUtil.e(TAG, "maxCount....." + maxCount);
        if (threadCount <= 0) {
            threadCount = 1;
        }
        if (threadCount > maxCount) {
            threadCount = maxCount;
        }
        //获取dao
        //resInfoDao = GreenDaoManager.getInstance().getSession().getResInfoDao();
        //创建一个固定数的线程池
        executorService = Executors.newFixedThreadPool(threadCount);
        //获取一个retrofit实例
        retrofit = retrofitTool.getRetrofit();
        //创建一个map
        //resInfoMap = new HashMap<>();
    }

    public static DownloadManager getInstance(int threadCount) {
        if (mInstance == null) {
            synchronized (DownloadManager.class) {
                if (mInstance == null) {
                    mInstance = new DownloadManager(threadCount);
                }
            }
        }
        return mInstance;
    }

    /**
     * 点击下载按钮后的操作
     *
     * @param resInfo  下载的相关信息
     * @param adapter  列表数据适配器
     * @param position 当前的条目索引
     * @param listener 监听器
     */
    public void downLoadClick(ResInfo resInfo, RecyclerView.Adapter adapter,
                              int position, StateChangeListener listener) {
        if (resInfo == null || StringUtil.isSpace(resInfo.getUrl())) {
            //没有信息或URL为空，就不处理
            return;
        }
        setListener(listener);
        int status = resInfo.getStatus();
        switch (status) {
            case State.START:
            case State.FAIL:
            case State.PAUSE:
                download(resInfo, new DownloadObserver(resInfo, adapter, position));
                break;
            case State.LOADING:
                pause(resInfo);
                adapter.notifyItemChanged(position);
                break;
            case State.SUCCESS:
                if (listener != null) {
                    listener.openFile(resInfo);
                }
                break;
        }
    }

    public void setListener(StateChangeListener listener) {
        this.listener = listener;
    }

    /**
     * 下载的方法
     *
     * @param resInfo  下载相关信息
     * @param observer 观察者
     */
    public void download(ResInfo resInfo, DownloadObserver observer) {
        if (resInfo == null || StringUtil.isSpace(resInfo.getUrl())) {
            //没有信息或URL为空，就不处理
            return;
        }
        WriteFileConsumer fileConsumer = new WriteFileConsumer(resInfo);
        //添加监听器
        ProgressManager.addListener(resInfo.getUrl(), observer);
        resInfo.setStatus(State.WAIT);
        //状态改变，通知ui
        if (listener != null) {
            listener.stateChanged(resInfo.getStatus());
        }
        //创建下载任务
        retrofit.create(DownLoadService.class)
                .download("bytes=" + resInfo.getCurrentSize() + "-", resInfo.getUrl())
                //请求在子线程
                .subscribeOn(Schedulers.io())
                //回调在子线程,针对大文件下载,在子线程写入文件（使用自己创建的调度器）
                .observeOn(Schedulers.from(executorService))
                .doOnNext(fileConsumer)
                .observeOn(AndroidSchedulers.mainThread())//在写完文件后，回调到主线程
                .subscribe(observer);

    }

    /**
     * 下载暂停的方法
     *
     * @param resInfo 下载的信息数据实体
     */
    public void pause(ResInfo resInfo) {
        if (resInfo == null) {
            return;
        }
        //将状态置为暂停
        resInfo.setStatus(State.PAUSE);
        //状态改变，停止写文件
        if (listener != null) {
            listener.stateChanged(resInfo.getStatus());
        }
    }

    /**
     * 下载状态监听接口
     */
    public interface StateChangeListener {
        /**
         * 状态变化的回调
         *
         * @param state 此时的状态
         */
        void stateChanged(int state);

        /**
         * 文件下载成功后的回调
         *
         * @param info 文件的类型，可根据类型对文件做不同的处理
         */
        void openFile(ResInfo info);
    }

    /**
     * 写文件的操作
     */
    public class WriteFileConsumer implements Consumer<ResponseBody> {
        /**
         * 下载相关信息的封装
         */
        private ResInfo resInfo;

        WriteFileConsumer(ResInfo resInfo) {
            this.resInfo = resInfo;
        }

        @Override
        public void accept(ResponseBody body) throws Exception {
            //执行写文件的操作
            if (resInfo == null || body == null) {
                //下载信息为空或响应体为空，抛出空指针异常
                throw new NullPointerException();
            }
            String path = resInfo.getFileDir();
            File dir = new File(path);
            if (!dir.exists()) {
                //目标文件夹不存在，创建目标文件夹
                boolean mkdirs = dir.mkdirs();
                LogUtil.e(TAG, "dir create: " + mkdirs);
            }
            //获取文件全名
            String fulName = resInfo.getFullName();
            //获取对应文件
            File file = new File(dir, fulName);
            if (!file.exists()) {
                //文件不存在，从0下载
                resInfo.setCurrentSize(0);
            }
            if (file.length() != resInfo.getCurrentSize() || resInfo.getStatus() == State.FAIL) {
                //无效文件，删除无效文件，重置下载状态
                boolean delete = file.delete();
                LogUtil.e(TAG, "delete file: " + delete);
                resInfo.setCurrentSize(0);
            }
            //记录文件总长和类型,只记录一次
            if (resInfo.getCurrentSize() == 0) {
                resInfo.setTotalSize(body.contentLength());
                MediaType mediaType = body.contentType();
                if (mediaType != null) {
                    resInfo.setType(mediaType.toString());
                    LogUtil.e(TAG, "type is " + resInfo.getType());
                }
                LogUtil.e(TAG, "...mediaType is null...");
            }
            //下载状态置为下载中
            resInfo.setStatus(State.LOADING);
            //文件存在且长度和记录一致，开始下载
            InputStream is;
            byte[] buf = new byte[1024 * 100];
            int len;
            FileOutputStream fos;
            long totalSize = resInfo.getTotalSize();
            long currentSize = resInfo.getCurrentSize();
            //从响应体里面获取输入流
            is = body.byteStream();
            //获取文件输出流,允许追加
            fos = new FileOutputStream(file, true);
            //写文件
            while (resInfo.getStatus() == State.LOADING && (len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
                LogUtil.e("curSize...." + currentSize);
                currentSize += len;
                //不断记录当前已经写的长度
                resInfo.setCurrentSize(currentSize);
                resInfo.setProgress((int) (100f * currentSize / totalSize + 0.5f));
                //当前文件长度大于总长度就终止记录
                if (currentSize > totalSize) {
                    break;
                }
            }
            fos.flush();
            //关流
            is.close();
            fos.close();
        }
    }

}
