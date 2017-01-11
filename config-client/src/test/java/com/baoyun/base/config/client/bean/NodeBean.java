package com.baoyun.base.config.client.bean;

public class NodeBean {

    private String path;

    private NodeDataBean valueData;
    
    public NodeBean(){
        
    }
    
    public NodeBean(String path, NodeDataBean valueData){
        this.path = path;
        this.valueData = valueData;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public NodeDataBean getValueData() {
        return valueData;
    }

    public void setValueData(NodeDataBean valueData) {
        this.valueData = valueData;
    }

}
