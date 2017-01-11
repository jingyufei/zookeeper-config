package com.baoyun.base.config.server.dto;

import java.io.Serializable;
import java.util.List;

import com.baoyun.base.config.server.exception.ParamInvalidException;

/**
 * 分页查询结果
 * @author MorningSheep
 *
 * @param <T>
 */
public class PagedListDTO <T> implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * 该页的记录
     */
    private List<T> records;
    /**
     * 分页信息
     */
    private PageDTO page;
    /**
     * 记录总数
     */
    private int total;
    
    /**
     * 检查分页信息是否有效
     * @throws ParamInvalidException    参数无效时抛出异常
     */
    public void checkInvalid()throws ParamInvalidException{
        if(total<0)
            throw new ParamInvalidException("total","总记录数必须大于或等于0");
        page.checkInvalid();
    }
    @Override
	public String toString() {
		return "PagedListDTO [records=" + records + ", page=" + page
				+ ", total=" + total + "]";
	}
	/**
     * 总页数
     * @return 总页数
     */
    public int getTotalPage(){
        return total/page.getPageSize() + (total%page.getPageSize()>0?1:0);
    }
}
