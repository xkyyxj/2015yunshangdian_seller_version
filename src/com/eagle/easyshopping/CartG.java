package com.eagle.easyshopping;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2015/7/13 0013.
 */
public class CartG extends BmobObject {
    private String username;
    private String shopname;
    private String goodsname;
    private int num;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }





    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



}
