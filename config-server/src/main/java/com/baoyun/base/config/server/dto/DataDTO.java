package com.baoyun.base.config.server.dto;

public class DataDTO {

	private String value;
	private String desc;
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public DataDTO(String value, String desc) {
		super();
		this.value = value;
		this.desc = desc;
	}
	public DataDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "DataDTO [value=" + value + ", desc=" + desc + "]";
	}
	
}
