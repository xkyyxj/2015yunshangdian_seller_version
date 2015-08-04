package com.eagle.easyshopping;

import cn.bmob.v3.BmobObject;

/**
 * Created by Albert on 2015/7/21.
 */
public class ShopNews extends BmobObject {

    private String text;
    private Shop shop;

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
