package com.bpz.freedom.entity;

import android.text.TextUtils;

import com.bpz.commonlibrary.util.StringUtil;

/**
 * 服务器返回数据的通用实体
 *
 * @param <T> 具体数据的类型
 */
public class ResultEntity<T> {
    /**
     * "result":"success"，获取结果
     * "code":"1"，返回码
     * "describe":"成功"，返回描述
     * "data": ，具体的数据
     * "version":""，版本号
     */
    private String result;
    private int code;
    private String describe;
    private T data;
    private String version;

    public String getResult() {
        return StringUtil.getNotNullStr(result);
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescribe() {
        return StringUtil.getNotNullStr(describe);
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getVersion() {
        return StringUtil.getNotNullStr(version);
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
