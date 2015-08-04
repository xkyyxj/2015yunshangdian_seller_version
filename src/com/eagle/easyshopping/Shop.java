package com.eagle.easyshopping;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobGeoPoint;
/*
 * ȱ���������ԣ��������Լ�������
 * ����һ�㣬Ӫҵʱ��Ӧ�øĳ������ַ���ʼӪҵʱ�䣬����ʱ�䡣һ��Date���Ͳ����Ա�ʾ��
 * ����ע�����״̬��Ӧ����booleanֵ����Ϊ�������״̬�����֣�����У�δͨ��ͨ�����intֵ��
 * 1��ͨ��2δͨ��3�������
 * 
 */

/**
 * Created by Administrator on 2015/7/20 0020.
 */
public class Shop extends BmobUser {
	// private static final long serialVersionUID = 1L;
	private String shopname; // 店铺名称
	private String susername; // 商家用户名
	private String suserpwd; // 商家密码
	public String getShopname() {
		return shopname;
	}
	public void setShopname(String shopname) {
		this.shopname = shopname;
	}
	public String getSusername() {
		return susername;
	}
	public void setSusername(String susername) {
		this.susername = susername;
	}
	public String getSuserpwd() {
		return suserpwd;
	}
	public void setSuserpwd(String suserpwd) {
		this.suserpwd = suserpwd;
	}
	public String getScat() {
		return scat;
	}
	public void setScat(String scat) {
		this.scat = scat;
	}
	public String getBusiness() {
		return business;
	}
	public void setBusiness(String business) {
		this.business = business;
	}
	public String getShopadd() {
		return shopadd;
	}
	public void setShopadd(String shopadd) {
		this.shopadd = shopadd;
	}
	public String getShoptel() {
		return shoptel;
	}
	public void setShoptel(String shoptel) {
		this.shoptel = shoptel;
	}
	public Boolean getOperatesta() {
		return operatesta;
	}
	public void setOperatesta(Boolean operatesta) {
		this.operatesta = operatesta;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getTotalgood() {
		return totalgood;
	}
	public void setTotalgood(String totalgood) {
		this.totalgood = totalgood;
	}
	public String getTotalbad() {
		return totalbad;
	}
	public void setTotalbad(String totalbad) {
		this.totalbad = totalbad;
	}
	public BmobGeoPoint getSlonla() {
		return slonla;
	}
	public void setSlonla(BmobGeoPoint slonla) {
		this.slonla = slonla;
	}
	public Double getSendprice() {
		return sendprice;
	}
	public void setSendprice(Double sendprice) {
		this.sendprice = sendprice;
	}
	public Double getDistributeprice() {
		return distributeprice;
	}
	public void setDistributeprice(Double distributeprice) {
		this.distributeprice = distributeprice;
	}
	public String getSpicture() {
		return spicture;
	}
	public void setSpicture(String spicture) {
		this.spicture = spicture;
	}
	public Number getCheckstate() {
		return checkstate;
	}
	public void setCheckstate(Number checkstate) {
		this.checkstate = checkstate;
	}
	private String scat; // 店铺分类
	private String business; // 主营业务
	private String shopadd; // 店铺地址
	private String shoptel;// 店铺联系方式
	private Boolean operatesta; // 营业状态
	private String starttime;
	private String endtime;// 营业时间
	private String totalgood; // 总赞数
	private String totalbad; // 总踩数
	private BmobGeoPoint slonla; // 店铺经纬度
	private Double sendprice; // 起送价
	private Double distributeprice; // 配送价
	private String spicture; // 商家头像图片
	private Number checkstate; // 店铺注册审核状态

}
