package com.eagle.easyshopping;


import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobGeoPoint;

/**
 * Created by Administrator on 2015/7/21 0021.
 */
public class _User extends BmobObject {
    private String username;
    private BmobGeoPoint lonla;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BmobGeoPoint getLonla() {
        return lonla;
    }

    public void setLonla(BmobGeoPoint lonla) {
        this.lonla = lonla;
    }
}
