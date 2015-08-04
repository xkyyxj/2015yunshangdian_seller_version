package com.eagle.easyshopping;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2015/7/19 0019.
 */
public class Add extends BmobObject {
	private String username;
    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	private String receiver;//收货人
    private String useradd;//收货地址
    private String usertel; //联系方式

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


}
