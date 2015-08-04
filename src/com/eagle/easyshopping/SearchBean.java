package com.eagle.easyshopping;

/**
 * Created by Administrator on 2015/7/15 0015.
 */
public class SearchBean {
    private String shopName;
    private String goodName;
    private String totalGood;
    private Number sendPrice;
    private String spicture;
    private Boolean operatesta;
    private String distance;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getSpicture() {
        return spicture;
    }

    public void setSpicture(String spicture) {
        this.spicture = spicture;
    }

    public Boolean getOperatesta() {
        return operatesta;
    }

    public void setOperatesta(Boolean operatesta) {
        this.operatesta = operatesta;
    }

    public SearchBean(String shopName, String totalGood, Number sendPrice,String spicture,Boolean operatesta,String distance) {
        this.shopName = shopName;
        this.totalGood = totalGood;
        this.sendPrice = sendPrice;
        this.spicture = spicture;
        this.operatesta = operatesta;
        this.distance=distance;
    }

    public SearchBean(String goodName,String shopName, String totalGood, Number sendPrice,String spicture,Boolean operatesta,String distance) {
        this.goodName = goodName;
        this.shopName = shopName;
        this.totalGood = totalGood;
        this.sendPrice = sendPrice;
        this.spicture = spicture;
        this.operatesta = operatesta;
        this.distance=distance;
    }



    public String getTotalGood() {
        return totalGood;
    }

    public void setTotalGood(String totalGood) {
        this.totalGood = totalGood;
    }

    public Number getSendPrice() {
        return sendPrice;
    }

    public void setSendPrice(Number sendPrice) {
        this.sendPrice = sendPrice;
    }

    public SearchBean(String shopName) {
        this.shopName = shopName;
    }

    public SearchBean(String goodName, String shopName) {
        this.goodName = goodName;
        this.shopName = shopName;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }


}
