package com.baoyun.base.config.server.response;

import com.baoyun.base.config.server.dto.PagedListDTO;

/**
 * Created by hli on 2015/1/29.
 * 分页对象
 */
public class PageResultModel<T>  {
    //响应码 (0 成功 1 参数错误 2 无权限 3 内部错误)
    private  int reply;
    //字符串说明
    private String replyDesc;
    //列表数据
    private PagedListDTO<T> pagedListDTO;
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
	public PagedListDTO<T> getPagedListDTO() {
		return pagedListDTO;
	}
	public void setPagedListDTO(PagedListDTO<T> pagedListDTO) {
		this.pagedListDTO = pagedListDTO;
	}
    
    
}
