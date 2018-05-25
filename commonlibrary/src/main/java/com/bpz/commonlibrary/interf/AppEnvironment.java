package com.bpz.commonlibrary.interf;

import com.bpz.commonlibrary.BuildConfig;

/**
 * app所处环境标识，用来动态匹配对应url
 */
public interface AppEnvironment {
    int APP_DEV = 0;
    int APP_TEST = 1;
    int APP_ON_LINE = 2;
    int APP_URL_FLAG = BuildConfig.URL_FLAG;
}
