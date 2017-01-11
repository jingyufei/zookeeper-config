package com.baoyun.base.config.server.dto;


public class PropertyDTO {
	private String groupName;
	private String propertyKey;
	private String propertyValue;
	private String propertyDesc;
	private String updateTime;
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getPropertyKey() {
		return propertyKey;
	}
	public void setPropertyKey(String propertyKey) {
		this.propertyKey = propertyKey;
	}
	public String getPropertyValue() {
		return propertyValue;
	}
	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}
	public String getPropertyDesc() {
		return propertyDesc;
	}
	public void setPropertyDesc(String propertyDesc) {
		this.propertyDesc = propertyDesc;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public PropertyDTO(String groupName, String propertyKey,
			String propertyValue, String propertyDesc, String updateTime) {
		super();
		this.groupName = groupName;
		this.propertyKey = propertyKey;
		this.propertyValue = propertyValue;
		this.propertyDesc = propertyDesc;
		this.updateTime = updateTime;
	}
	public PropertyDTO() {
		super();
		// TODO Auto-generated constructor stub
	} 
}
