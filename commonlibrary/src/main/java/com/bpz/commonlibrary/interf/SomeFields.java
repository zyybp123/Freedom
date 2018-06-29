package com.bpz.commonlibrary.interf;

/**
 * 专门存放固定值字段的接口
 */
public interface SomeFields {
    String URL_FLAG = "frUrlFlag";
    String GLIDE_DISK_CACHE_NAME = "appImageCache";

    String CONNECTION_ERROR = "网络连接异常";
    String TIMEOUT_ERROR = "网络连接超时";
    String NETWORK_ERROR = "网络工作异常";
    String HOST_UNKNOWN_ERROR = "主机解析异常";
    String FR_CACHE_CONTROL = "frCacheControl";
    String GET = "GET";
    String POST = "POST";
    String CACHE_CONTROL = "Cache-Control";
    String FR_CACHE = FR_CACHE_CONTROL + ":" + "true";
}
