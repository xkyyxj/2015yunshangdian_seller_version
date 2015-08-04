package com.eagle.easyshopping;
import java.io.Serializable;

/**
 * Created by Administrator on 2015/7/6 0006.
 */
public class FoodBean implements Serializable{
    private String pid;
    private String name;
    private String price;
    private String description;
    private int num;

    public FoodBean(String pid, String name, String price,int num,String description) {
        this.pid = pid;
        this.name = name;
        this.price = price;
        this.num = num;
        this.description=description;
    }

    @Override
    public String toString() {
        return "FoodBean{" +
                "resid=" + pid +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                '}';
    }

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

   
}
