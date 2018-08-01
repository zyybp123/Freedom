package com.bpz.commonlibrary.entity;

public class WebParamEntity {
    /**
     * naviType : 0
     * animeType : -1
     * sbttid : fliggy.router.sb.0
     * url : page://home
     * protocal : page
     * page : home
     * alipayDownload : false
     * evokeType : 1
     * apk : https://download.alicdn.com/nbdev-client/client4trip/channel      /trip_10003300@travel_android.apk
     * packagename : com.taobao.trip
     */

    private int naviType;
    private int animeType;
    private String sbttid;
    private String url;
    private String protocal;
    private String page;
    private boolean alipayDownload;
    private String evokeType;
    private String apk;
    private String packagename;

    public int getNaviType() {
        return naviType;
    }

    public void setNaviType(int naviType) {
        this.naviType = naviType;
    }

    public int getAnimeType() {
        return animeType;
    }

    public void setAnimeType(int animeType) {
        this.animeType = animeType;
    }

    public String getSbttid() {
        return sbttid;
    }

    public void setSbttid(String sbttid) {
        this.sbttid = sbttid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProtocal() {
        return protocal;
    }

    public void setProtocal(String protocal) {
        this.protocal = protocal;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public boolean isAlipayDownload() {
        return alipayDownload;
    }

    public void setAlipayDownload(boolean alipayDownload) {
        this.alipayDownload = alipayDownload;
    }

    public String getEvokeType() {
        return evokeType;
    }

    public void setEvokeType(String evokeType) {
        this.evokeType = evokeType;
    }

    public String getApk() {
        return apk;
    }

    public void setApk(String apk) {
        this.apk = apk;
    }

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }
    //params={"naviType":0,
    // "animeType":-1,
    // "sbttid":"fliggy.router.sb.0",
    // "url":"page://home","protocal":"page",
    // "page":"home","alipayDownload":false,
    // "evokeType":"1",
    // "apk":"https://download.alicdn.com/nbdev-client/client4trip/channel
    // /trip_10003300@travel_android.apk","packagename":"com.taobao.trip"} }


}
