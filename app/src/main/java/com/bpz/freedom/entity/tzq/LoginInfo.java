package com.bpz.freedom.entity.tzq;

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
}
