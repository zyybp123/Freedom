package com.bpz.commonlibrary.net.download;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2018/1/30.
 * 下载方法接口
 */

public interface DownLoadService {

    /**
     * 断点下载的方法
     *
     * @param range range头，记录从哪开始下载，格式为 （key）Range : （value）bytes=（0:起始进度）- （结尾进度，不传则到文件末尾）
     * @param url   下载路径
     */
    @GET
    @Streaming
    Observable<ResponseBody> download(@Header("Range") String range, @Url String url);

    /**
     * 不断点续传的文件下载的方法
     *
     * @param url 下载的路径
     */
    @GET
    @Streaming
    Observable<ResponseBody> download(@Url String url);
}
