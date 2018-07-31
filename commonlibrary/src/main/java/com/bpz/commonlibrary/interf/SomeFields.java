package com.bpz.commonlibrary.interf;

/**
 * 专门存放固定值字段的接口
 */
public interface SomeFields {
    String URL_FLAG = "frUrlFlag";
    String GLIDE_DISK_CACHE_NAME = "appImageCache";
    String UTF_8 = "UTF-8";
    String GBK = "GBK";

    String CONNECTION_ERROR = "网络连接异常";
    String TIMEOUT_ERROR = "网络连接超时";
    String NETWORK_ERROR = "网络工作异常";
    String HOST_UNKNOWN_ERROR = "主机解析异常";
    String FR_CACHE_CONTROL = "frCacheControl";
    String GET = "GET";
    String POST = "POST";
    String CACHE_CONTROL = "Cache-Control";
    String CONTENT_DISPOSITION = "Content-Disposition";
    String FR_CACHE = FR_CACHE_CONTROL + ":" + "true";

    //常用软件的SD卡下载路径
    String BROWSER_DOWNLOAD = "/Download";
    //QQ接收的文件
    String QQ_FILE_RECEIVE = "/tencent/QQfile_recv";
    //WX接收的文件
    String TENCENT_DOWNLOAD = "/tencent/MicroMsg/Download";
    String WX_FILE ="/tencent/MicroMsg/WeiXin";
    //百度云下载的文件
    String BD_FILE = "/BaiduNetdisk";
    //UC浏览器下载文件
    String UC_FILE = "/UCDownloads";

}
