package com.bpz.commonlibrary.net;

/**
 * Created by Administrator on 2017/12/22.
 * 上传或下载进度回调接口
 */

public interface ProgressCallback {
    /**
     * 进度回调
     *
     * @param contentLength 文件总长
     * @param bytesWritten  已经读写的文件长度
     * @param done          是否已上传或下载完成
     *                      进度计算 (int) ((bytesWritten * 100f / contentLength) + 0.5f);
     */
    void onLoading(long contentLength, long bytesWritten, boolean done);
}
