package com.bpz.commonlibrary.interf.listener;

import okhttp3.Request;

public interface OnHeaderOptionListener {
    void onHeaderOption(Request.Builder builder, Request newRequest, String headerValue);
}
