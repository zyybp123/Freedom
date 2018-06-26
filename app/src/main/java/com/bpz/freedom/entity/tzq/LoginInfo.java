package com.bpz.freedom.entity.tzq;

import com.bpz.commonlibrary.util.StringUtil;

import java.util.List;

public class LoginInfo {
    /**
     * {"accountid":"283142",
     * "rongyuntoken":"",
     * "xytoken":"6B284432224E0B54427DCF7F441D4DEE",
     * "roleid":1,
     * "userimage":"http://localhost:8080/tzq/data/default/staff.png",
     * "orglist":[],
     * "layer":1,
     * "username":"未命名"},
     */

    private String accountid;
    private String rongyuntoken;
    private String xytoken;
    private int roleid;
    private String userimage;
    private List<OrgInfo> orglist;
    private int layer;
    private String username;

    public String getAccountid() {
        return StringUtil.getNotNullStr(accountid);
    }

    public void setAccountid(String accountid) {
        this.accountid = accountid;
    }

    public String getRongyuntoken() {
        return StringUtil.getNotNullStr(rongyuntoken);
    }

    public void setRongyuntoken(String rongyuntoken) {
        this.rongyuntoken = rongyuntoken;
    }

    public String getXytoken() {
        return StringUtil.getNotNullStr(xytoken);
    }

    public void setXytoken(String xytoken) {
        this.xytoken = xytoken;
    }

    public int getRoleid() {
        return roleid;
    }

    public void setRoleid(int roleid) {
        this.roleid = roleid;
    }

    public String getUserimage() {
        return StringUtil.getNotNullStr(userimage);
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public List<OrgInfo> getOrglist() {
        return orglist;
    }

    public void setOrglist(List<OrgInfo> orglist) {
        this.orglist = orglist;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public String getUsername() {
        return StringUtil.getNotNullStr(username);
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
