package com.eagle.easyshopping;

import java.util.ArrayList;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by Administrator on 2015/7/17 0017.
 */
public class Ord extends BmobObject{
    private String username;//用户名
    private String shopname;//店铺名称
    private ArrayList<String> goodsname;//商品信息
    private ArrayList<String> nums;
    private ArrayList<String> price;
    private String totalprice;//总价
    //private BmobDate ordtime;//订单时间
    private String ordtime;
    private String receiver;//收货人
    private String useradd;//收货地址
    private String usertel; //联系方式
    private String payway; //支付方式
    private String jystate;
    private Boolean good; //赞
    private Boolean bad; //踩
    private Integer comm_state;
    private String com;


    public Integer getComm_state() {
		return comm_state;
	}

	public void setComm_state(Integer comm_state) {
		this.comm_state = comm_state;
	}

	public String getCom() {
		return com;
	}

	public void setCom(String com) {
		this.com = com;
	}

	public ArrayList<String> getPrice() {
        return price;
    }

    public void setPrice(ArrayList<String> price) {
        this.price = price;
    }

    public ArrayList<String> getNums() {
        return nums;
    }

    public void setNums(ArrayList<String> nums) {
        this.nums = nums;
    }



    public ArrayList<String> getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(ArrayList<String> goodsname) {
        this.goodsname = goodsname;
    }

    public String getJystate() {
        return jystate;
    }

    public void setJystate(String jystate) {
        this.jystate = jystate;
    }

    public Boolean getBad() {
        return bad;
    }

    public void setBad(Boolean bad) {
        this.bad = bad;
    }

    public Boolean getGood() {
        return good;
    }

    public void setGood(Boolean good) {
        this.good = good;
    }

    public String getPayway() {
        return payway;
    }

    public void setPayway(String payway) {
        this.payway = payway;
    }

    public String getUsertel() {
        return usertel;
    }

    public void setUsertel(String usertel) {
        this.usertel = usertel;
    }

    public String getUseradd() {
        return useradd;
    }

    public void setUseradd(String useradd) {
        this.useradd = useradd;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getOrdtime() {
        return ordtime;
    }

    public void setOrdtime(String ordtime) {
        this.ordtime = ordtime;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    /*public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }*/

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


