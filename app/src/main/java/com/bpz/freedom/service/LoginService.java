package com.bpz.freedom.service;

import com.bpz.freedom.entity.ResultEntity;
import com.bpz.freedom.entity.tzq.LoginInfo;
import com.bpz.freedom.interf.ParamNames;
import com.bpz.freedom.net.TzqHost;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface LoginService {
    String LOGIN_PATH = "mobile/login/dologin";


    @Headers(TzqHost.HEADER_TZQ)
    @FormUrlEncoded
    @POST(LOGIN_PATH)
    Observable<ResultEntity<LoginInfo>> doLogin(@Field(ParamNames.ACCOUNT_NAME) String accountName,
                                                @Field(ParamNames.PASSWORD) String password);

}
