package com.bpz.commonlibrary.manager;

import android.content.Context;

import java.io.File;

/**
 * Created by Administrator on 2018/3/8.
 * 文件管理类
 * 1.对path的整合
 * 2.7.0的适配
 * 3.文件的创建删除
 */

public class FileManager {
    private Context mContext;

    public FileManager(Context mContext) {
        this.mContext = mContext;
    }

    public void getInternalDir(){
        if (mContext != null){
            File filesDir = mContext.getFilesDir();
        }
    }
}
