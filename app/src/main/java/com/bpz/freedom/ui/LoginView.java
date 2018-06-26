package com.bpz.freedom.ui;

import com.bpz.commonlibrary.mvp.BaseView;
import com.bpz.freedom.entity.tzq.LoginInfo;

public interface LoginView extends BaseView {
    void onLoginSuccess(LoginInfo loginInfo);

    void onLoginFailure(int code, String describe);

}
