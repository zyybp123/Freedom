package com.bpz.freedom.service;

import com.bpz.freedom.entity.NetEaseEntity;
import com.bpz.freedom.net.NetEaseHost;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface NetEaseService {
    String ARTICLE_PATH = "nc/article/preload/DLFAD94N0528KMHC/full.html";

    @Headers(NetEaseHost.HEADER_NET_EASE)
    @GET(ARTICLE_PATH)
    Observable<NetEaseEntity> preload();
}
