package com.baoyun.base.config.server.util;

import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import com.baoyun.base.config.server.dto.GroupDTO;
import com.baoyun.base.config.server.dto.PropertyDTO;

public class GroupDTOUtil {
	public static String toProperties(GroupDTO group){
		StringBuilder sb = new StringBuilder();
		
		for(PropertyDTO pro:group.getItems()){
			if(pro.getPropertyDesc()!=null){
				//拼接注释
				sb.append("#")
					.append(pro.getPropertyDesc())
					.append("\n");
			}
			sb.append(pro.getPropertyKey())
				.append("=")
				.append(pro.getPropertyValue())
				.append("\n");
		}
		
		return sb.toString();
	}
	
	public static GroupDTO fromProperties(String groupName,String props){
		GroupDTO result = new GroupDTO();
		result.setGroupName(groupName);
		
		List<PropertyDTO> items = new ArrayList<PropertyDTO>();
		result.setItems(items);
		
		LineNumberReader lr = null;
		try{
			lr = new LineNumberReader(new StringReader(props));
			
			String key = "";
			String value = "";
			String desc = "";
			
			String line = null;
			while((line = lr.readLine()) !=null){
				if(line.trim().equals("")){
					continue;
				}
				
				if(line.startsWith("#")){
					desc += line.substring(1);
				}else{
					key = line.substring(0,line.indexOf("="));
					if(line.indexOf("#") ==-1){
						value = line.substring(line.indexOf("=")+1,line.length());
					}else{
						value = line.substring(line.indexOf("=")+1,line.indexOf("#"));
					}
					PropertyDTO item = new PropertyDTO();
					item.setGroupName(groupName);
					item.setPropertyDesc(desc);
					item.setPropertyKey(key);
					item.setPropertyValue(value);
					items.add(item);
					
					key = "";
					desc = "";
					value = "";
				}
			}
		}catch(Exception e){
//			log.info("fromProperties err",e);
		}
		return result;
	}
}
