package com.bpz.freedom.ui;

import android.support.annotation.NonNull;

import com.bpz.commonlibrary.mvp.BasePresenterImpl;
import com.bpz.commonlibrary.net.RetrofitTool;
import com.bpz.freedom.BaseObserver;
import com.bpz.freedom.entity.ResultEntity;
import com.bpz.freedom.entity.tzq.LoginInfo;
import com.bpz.freedom.net.TzqHost;
import com.bpz.freedom.service.LoginService;

import java.util.HashMap;

public class LoginPresenter extends BasePresenterImpl<LoginView> {
    private LoginService service;

    public LoginPresenter() {
        service = RetrofitTool
                .getInstance(TzqHost.BASE_URL_TZQ)
                .getRetrofit()
                .create(LoginService.class);
    }


    public void doLogin(String accountName, String pwd) {
        service.doLogin(accountName, pwd)
                .compose(RetrofitTool.<ResultEntity<LoginInfo>>setMainThread())
                .subscribe(new BaseObserver<LoginInfo>(mView, LoginService.LOGIN_PATH) {
                    @Override
                    public void onFailure(String methodTag, int code, String describe) {
                        if (mView != null) {
                            mView.onLoginFailure(code, describe);
                        }
                    }

                    @Override
                    public void onSuccess(String methodTag, String result, @NonNull LoginInfo data) {
                        if (mView != null){
                            mView.onLoginSuccess(data);
                        }
                    }
                });
    }

}
