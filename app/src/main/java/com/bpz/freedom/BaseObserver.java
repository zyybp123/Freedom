package com.bpz.freedom;

import android.accounts.NetworkErrorException;
import android.support.annotation.NonNull;

import com.bpz.commonlibrary.interf.SomeFields;
import com.bpz.commonlibrary.mvp.BaseView;
import com.bpz.commonlibrary.util.LogUtil;
import com.bpz.commonlibrary.util.StringUtil;
import com.bpz.freedom.entity.ResultEntity;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class BaseObserver<T> implements Observer<ResultEntity<T>> {
    private static final String TAG = "BaseObserver";
    private BaseView mView;
    private String methodTag;

    public BaseObserver(BaseView mView, String methodTag) {
        this.mView = mView;
        this.methodTag = methodTag;
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (mView != null) {
            mView.onSubscribe(methodTag, d);
        }
    }

    @Override
    public void onNext(ResultEntity<T> tResultEntity) {
        if (tResultEntity != null) {
            LogUtil.e(TAG, "......response body......" + tResultEntity);
            //数据预处理
            String version = tResultEntity.getVersion();
            String result = tResultEntity.getResult();
            String describe = tResultEntity.getDescribe();
            int code = tResultEntity.getCode();
            switch (code) {
                case -1:
                case 0:
                    //操作失败
                    onFailure(methodTag, code, describe);
                    break;
                case 1:
                    //获取数据成功
                    T data = tResultEntity.getData();
                    if (data != null) {
                        onSuccess(methodTag, result, data);
                    } else {
                        if (mView != null) {
                            mView.onEmpty(methodTag);
                        }
                    }
                    break;
                case 1000:
                    //重新登录

                    break;
                default:
                    //其他情况由后台提示

                    break;
            }
        }
    }

    public abstract void onFailure(String methodTag, int code, String describe);

    public abstract void onSuccess(String methodTag, String result, @NonNull T data);


    @Override
    public void onError(Throwable e) {
        String msg = "...error...";
        if (e == null) {
            if (mView != null) {
                mView.onError(methodTag, msg);
            }
            return;
        }
        LogUtil.e(TAG, "onError: " + e);//这里可以打印错误信息
        try {
            if (e instanceof ConnectException) {
                msg = SomeFields.CONNECTION_ERROR;
                if (mView != null) {
                    mView.noNet();
                }
                return;
            } else if (e instanceof TimeoutException) {
                msg = SomeFields.TIMEOUT_ERROR;
            } else if (e instanceof NetworkErrorException) {
                msg = SomeFields.NETWORK_ERROR;
            } else if (e instanceof UnknownHostException) {
                msg = SomeFields.HOST_UNKNOWN_ERROR;
            } else {
                msg = StringUtil.getNotNullStr(e.getMessage());
            }
        } catch (Exception e1) {
            msg = e1.getMessage();
            LogUtil.e(TAG, "其它异常信息" + e1);
        }
        if (mView != null) {
            mView.onError(methodTag, msg);
        }

    }

    @Override
    public void onComplete() {
        //onRequestEnd();
    }
}
