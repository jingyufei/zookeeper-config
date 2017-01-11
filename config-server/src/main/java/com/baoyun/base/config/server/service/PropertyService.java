package com.baoyun.base.config.server.service;

import java.io.OutputStream;
import java.util.List;

import com.baoyun.base.config.server.exception.ParamInvalidException;
import com.baoyun.base.config.server.response.NoPageResultModel;

public interface PropertyService {

	/*
	 * 查看属性
	 */
	public NoPageResultModel getProperty(String txNo,String groupName,String propertyKey) throws ParamInvalidException;
	
	/*
	 * 更新属性
	 */
	public NoPageResultModel editProperty(String txNo, String groupName, String propertyKey, String propertyValue, String propertyDesc);
	
	/*
	 * 删除属性
	 */
	public NoPageResultModel delProperty(String txNo, String groupName,String propertyKey)  throws ParamInvalidException;
	
	/*
	 * 新增属性
	 */
	public NoPageResultModel addProperty(String txNo, String groupName, String propertyKey, String propertyValue, String propertyDesc) throws ParamInvalidException;
	
	/*
	 * 创建组
	 */
	public NoPageResultModel addGroup(String txNo, String groupName) throws ParamInvalidException;
	
	/*
	 * 获取组列表
	 */
	public List<String> getAllGroup(String txNo);
	
	/*
	 * 导入属性
	 */
	public NoPageResultModel importProperty(String txNo,String groupName,String json);
	
	/*
	 * 导出属性
	 */
	public OutputStream exportProperty(String txNo,String groupName,OutputStream os) throws ParamInvalidException;

	/*
	 * 查询组别是否存在
	 */
	public boolean existGroupName(String txNo, String groupName);
}
