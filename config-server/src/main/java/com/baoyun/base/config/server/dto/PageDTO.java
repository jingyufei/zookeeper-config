package com.baoyun.base.config.server.dto;

import java.io.Serializable;

import com.baoyun.base.config.server.exception.ParamInvalidException;


public class PageDTO implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * 页码大小
     */
    private int pageSize;
    /**
     * 第几页，起始页是1
     */
    private int pageIndex;
    
    /**
     * 跳过的记录数
     * @return 跳过的记录数
     */
    public int getSkip(){
        int skip = (pageIndex-1)*pageSize;
        if(skip<0)
            return 0;
        else
            return skip;
    }
    
    
    
    public int getPageSize() {
		return pageSize;
	}



	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}



	public int getPageIndex() {
		return pageIndex;
	}



	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}



	/**
     * 检查分页信息是否有效
     * @throws ParamInvalidException    参数无效时抛出异常
     */
    public void checkInvalid()throws ParamInvalidException{
        if(pageIndex<=0)
            throw new ParamInvalidException("pageIndex","页码必须大于0");
        if(pageSize<=0)
            throw new ParamInvalidException("pageSize","每页条数必须大于0");
    }
    
    public PageDTO(int pageIndex,int pageSize) {
        super();
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }
    
    public PageDTO(){}
}
