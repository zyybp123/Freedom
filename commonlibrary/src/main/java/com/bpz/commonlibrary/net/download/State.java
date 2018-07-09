package com.bpz.commonlibrary.net.download;

/**
 * Created by Administrator on 2018/1/30.
 * 状态值的定义
 */

public interface State {
    int START = 0;
    int LOADING = 1;
    int PAUSE = 2;
    int WAIT = 3;
    int SUCCESS = 4;
    int FAIL = 5;
}
