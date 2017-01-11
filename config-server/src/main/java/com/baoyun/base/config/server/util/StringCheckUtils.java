package com.baoyun.base.config.server.util;

import java.util.regex.Pattern;

import com.baoyun.base.config.server.exception.ParamInvalidException;


public class StringCheckUtils {
	public static String checkGroupName(String groupName) throws ParamInvalidException {
		if(groupName==null){
			throw new ParamInvalidException("groupName", "组名为null");
		}
		groupName = groupName.trim();
		
		Pattern pattern = Pattern.compile("[\\d\\w_\\\\.\\\\-]+");
		if(!pattern.matcher(groupName).matches()){
			throw new ParamInvalidException("groupName", "组名只能是字母数字下划线点号");
		}
		

		return groupName;
	}
	public static String checkPropertyKey(String propertyName) throws ParamInvalidException {
		if(propertyName==null){
			throw new ParamInvalidException("propertyName", "组名为null");
		}
		propertyName = propertyName.trim();
		
		Pattern pattern = Pattern.compile("[\\d\\w_\\\\.\\\\-]+");
		if(!pattern.matcher(propertyName).matches()){
			throw new ParamInvalidException("propertyName", "组名只能是字母数字下划线点号");
		}
		

		return propertyName;
	}
}
