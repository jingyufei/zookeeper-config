package com.baoyun.base.config.client.bean;

import com.baoyun.base.config.client.annotation.ZookeeperValue;

public class JDBCInfoBean {

	@ZookeeperValue(key = "jdbc.url")
	private String url;
	
	@ZookeeperValue(key = "jdbc.username")
	private String userName;
	
	@ZookeeperValue(key = "jdbc.password")
	private String userPwd;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	@Override
	public String toString() {
		return "JDBCInfoBean [url=" + url + ", userName=" + userName
				+ ", userPwd=" + userPwd + "]";
	}
}
