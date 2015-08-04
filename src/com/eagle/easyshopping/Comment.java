package com.eagle.easyshopping;
import cn.bmob.v3.BmobObject;

/**
 * Created by Albert on 2015/7/15.
 */
public class Comment extends BmobObject {

    private String content;
    private Shop desti;
    private String send_speed;
    private User user;
    private Boolean state;//true��������false������

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Shop getDesti() {
        return desti;
    }

    public void setDesti(Shop desti) {
        this.desti = desti;
    }

    public String getSend_speed() {
        return send_speed;
    }

    public void setSend_speed(String send_speed) {
        this.send_speed = send_speed;
    }

    public Boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
