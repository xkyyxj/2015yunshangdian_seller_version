package com.eagle.easyshopping;

/**
 * Created by Administrator on 2015/7/14 0014.
 */
public class CommentBean {
    private Boolean gresid;
    private String username;    
    private String comment;

    
    public Boolean getGresid() {
		return gresid;
	}

	public void setGresid(Boolean gresid) {
		this.gresid = gresid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	
    public CommentBean(Boolean gresid, String username, String comment) {
        this.gresid = gresid;
        this.username = username;
        this.comment = comment;
    }



    

}
