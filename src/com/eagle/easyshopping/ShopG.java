package com.eagle.easyshopping;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2015/7/15 0015.
 */
public class ShopG extends BmobObject {
	private String shopname;// 搴楅摵鍚嶇О
	private String goodsname;// 鍟嗗搧鍚嶇О
	private String picture;// 鍟嗗搧鍥剧墖
	private String price;// 浠锋牸
	private String description;// 鎻忚堪
	private String gcat;// 鍟嗗搧鍒嗙被
	private Boolean goodssta;// 鍟嗗搧鐘舵�
	private Integer volume;// 鍟嗗搧閿�噺

	public String getShopname() {
		return shopname;
	}

	public void setShopname(String shopname) {
		this.shopname = shopname;
	}

	public String getGoodsname() {
		return goodsname;
	}

	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGcat() {
		return gcat;
	}

	public void setGcat(String gcat) {
		this.gcat = gcat;
	}

	public Boolean getGoodssta() {
		return goodssta;
	}

	public void setGoodssta(Boolean goodssta) {
		this.goodssta = goodssta;
	}

	public Integer getVolume() {
		return volume;
	}

	public void setVolume(Integer volume) {
		this.volume = volume;
	}
}