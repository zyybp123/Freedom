package com.bpz.freedom.ui;

import com.bpz.commonlibrary.mvp.BasePresenterImpl;
import com.bpz.commonlibrary.net.RetrofitTool;
import com.bpz.commonlibrary.util.LogUtil;
import com.bpz.freedom.entity.ResultEntity;
import com.bpz.freedom.entity.tzq.LoginInfo;
import com.bpz.freedom.service.LoginService;

import java.util.HashMap;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class LoginPresenter extends BasePresenterImpl<LoginView> {
    public LoginPresenter() {
        RetrofitTool retrofitTool = RetrofitTool
                .getInstance("", new HashMap<String, String>());
        LoginService service = retrofitTool
                .getRetrofit()
                .create(LoginService.class);
        service
                .doLogin("17600108092","a123456")
                .compose(RetrofitTool.<ResultEntity<LoginInfo>>setMainThread())
                .subscribe(new Observer<ResultEntity<LoginInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResultEntity<LoginInfo> loginInfoResultEntity) {
                        mView.onLoginSuccess(loginInfoResultEntity.getData());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void doLogin(String accountName, String pwd) {

    }
}
