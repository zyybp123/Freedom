package com.bpz.commonlibrary.interf;

public interface ConfigFields {
    int READ_TIME_OUT = 60;
    int WRITE_TIME_OUT = 60;
    int CONNECT_TIME_OUT = 2;
    String DEFAULT_BASE_URL = "";
    /**
     * cookie 是否持久化
     */
    boolean IS_PERSISTENCE = true;
    /**
     * 默认硬盘缓存大小
     */
    long DEFAULT_DISK_CACHE_SIZE = 1024 * 1024 * 100;
    /**
     * 默认缓存文件夹名称
     */
    String DEFAULT_CACHE_NAME = "/fr_net";

}
