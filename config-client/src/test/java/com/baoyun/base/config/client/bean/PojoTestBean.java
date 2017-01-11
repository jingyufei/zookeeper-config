package com.baoyun.base.config.client.bean;

import org.springframework.beans.factory.annotation.Value;

import com.baoyun.base.config.client.annotation.ZookeeperValue;

public class PojoTestBean {

    @ZookeeperValue(key = "test.info", realtime = false)
    @Value("test.info")
    private String info;

    @ZookeeperValue(key = "test.addr")
    private String addr;

    @ZookeeperValue(key = "test.tel.phoneno", realtime = true)
    private String telPhoneNo;

    @ZookeeperValue(key = "test.weburl")
    private String webUrl;

    @ZookeeperValue(key = "test.blogurl")
    private String blogUrl;

    @ZookeeperValue(key = "test.isSuccess1")
    private boolean isSuccess1;
    
    @ZookeeperValue(key = "test.isSuccess2")
    private boolean isSuccess2;
    
    @ZookeeperValue(key = "test.isSuccess3")
    private boolean isSuccess3;

    @ZookeeperValue(key = "test.countLong")
    private long countLong;

    @ZookeeperValue(key = "test.countInt")
    private int countInt;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getTelPhoneNo() {
        return telPhoneNo;
    }

    public void setTelPhoneNo(String telPhoneNo) {
        this.telPhoneNo = telPhoneNo;
    }

}
