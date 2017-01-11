package com.baoyun.base.config.server.response;


public class NoPageResultModel {
    /**
     * 响应码(1成功)
     */
    private int reply;
    /**
     * 返回说明
     */
    private String replyDesc;

    /**
     * 返回数据
     */
    private Object data;
    
    
    
    public int getReply() {
		return reply;
	}


	public void setReply(int reply) {
		this.reply = reply;
	}


	public String getReplyDesc() {
		return replyDesc;
	}


	public void setReplyDesc(String replyDesc) {
		this.replyDesc = replyDesc;
	}


	public Object getData() {
		return data;
	}


	public void setData(Object data) {
		this.data = data;
	}


	public NoPageResultModel(int reply, String replyDesc) {
        this.reply = reply;
        this.replyDesc = replyDesc;
        this.data = "";
    }


	public NoPageResultModel() {
		super();
	}


	public NoPageResultModel(int reply, String replyDesc, Object data) {
		super();
		this.reply = reply;
		this.replyDesc = replyDesc;
		this.data = data;
	}
}
