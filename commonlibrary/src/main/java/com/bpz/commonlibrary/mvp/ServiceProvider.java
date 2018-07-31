package com.bpz.commonlibrary.mvp;

import com.bpz.commonlibrary.interf.ConfigFields;
import com.bpz.commonlibrary.net.RetrofitTool;

public class ServiceProvider<T> {
    private static volatile ServiceProvider mInstance;

    private ServiceProvider() {
    }

    public static ServiceProvider getInstance() {
        if (mInstance == null) {
            synchronized (ServiceProvider.class) {
                if (mInstance == null) {
                    mInstance = new ServiceProvider();
                }
            }
        }
        return mInstance;
    }

    /**
     * @param serviceClass api的class
     * @return 返回 api的对象
     */
    public T getService(Class<T> serviceClass) {
        return RetrofitTool
                .getInstance(ConfigFields.DEFAULT_BASE_URL)
                .getRetrofit()
                .create(serviceClass);
    }
}
