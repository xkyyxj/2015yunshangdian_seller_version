package com.eagle.easyshopping;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobGeoPoint;

public class CurrentAdd extends BmobObject {
	public String crtAdd;// 当前实际地址
	public BmobGeoPoint crtGeoAdd;
	public String getCrtAdd() {
		return this.crtAdd;
	}
	public void setCrtAdd(String crtAdd) {
		this.crtAdd = crtAdd;
	}
	public BmobGeoPoint getCrtGeoAdd() {
		return this.crtGeoAdd;
	}
	public void setCrtGeoAdd(BmobGeoPoint crtGeoAdd) {
		this.crtGeoAdd = crtGeoAdd;
	}


	

	}


