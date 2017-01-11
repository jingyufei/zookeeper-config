package com.baoyun.base.config.server.dto;

import java.util.List;


public class GroupDTO {
	private String groupName;
	private List<PropertyDTO> items;
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public List<PropertyDTO> getItems() {
		return items;
	}
	public void setItems(List<PropertyDTO> items) {
		this.items = items;
	}
	public GroupDTO(String groupName, List<PropertyDTO> items) {
		super();
		this.groupName = groupName;
		this.items = items;
	}
	public GroupDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "GroupDTO [groupName=" + groupName + ", items=" + items + "]";
	}
	
}
