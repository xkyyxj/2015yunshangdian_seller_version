package com.eagle.easyshopping;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobGeoPoint;

public class User extends BmobObject {

	private String userpwd;
	private String username;
	private BmobGeoPoint lonla;
	public String getUserpwd() {
		return userpwd;
	}
	public void setUserpwd(String userpwd) {
		this.userpwd = userpwd;
	}
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
