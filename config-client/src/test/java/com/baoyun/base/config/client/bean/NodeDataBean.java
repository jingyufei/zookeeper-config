package com.baoyun.base.config.client.bean;

public class NodeDataBean {

    /**
     * 值
     */
    private String value;

    /**
     * 描述
     * 
     */
    private String desc;

    public NodeDataBean() {

    }

    public NodeDataBean(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

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

}
