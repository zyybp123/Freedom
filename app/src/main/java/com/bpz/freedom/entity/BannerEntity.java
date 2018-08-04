package com.bpz.freedom.entity;

import com.bpz.commonlibrary.interf.listener.BannerData;
import com.bpz.commonlibrary.util.StringUtil;

public class BannerEntity implements BannerData {
    private String imgUrl;
    private String html;
    private boolean selected;

    public BannerEntity(String imgUrl, String html) {
        this.imgUrl = imgUrl;
        this.html = html;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    @Override
    public String getBannerImgUrl() {
        return StringUtil.getNotNullStr(imgUrl);
    }

    @Override
    public String getBannerClickUrl() {
        return StringUtil.getNotNullStr(html);
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }
}
