package com.baoyun.base.config.server.util;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.baoyun.base.config.server.dto.GroupDTO;
import com.baoyun.base.config.server.dto.PropertyDTO;

public class GroupDTOUtilTest {
	GroupDTO g;
	@Before
	public void setUp(){
		g = new GroupDTO();
		g.setGroupName("testGroup");
		
		g.setItems(new ArrayList<PropertyDTO>());
		
		PropertyDTO d = new PropertyDTO(g.getGroupName(),"appId","5","APP ID",null);
		g.getItems().add(d);
		
		d = new PropertyDTO(g.getGroupName(),"baiduUrl","http://www.baidu.com","百度",null);
		g.getItems().add(d);
	}
	@Test
	public void testToProperties(){
		String p = GroupDTOUtil.toProperties(g);
		System.out.println();
		
		String expected = "#APP ID\nappId=5\n#百度\nbaiduUrl=http://www.baidu.com\n";
		Assert.assertEquals(expected, p);
	}
	
	@Test 
	public void testFromProperties(){
		String props = GroupDTOUtil.toProperties(g);
		
		String expected = "#APP ID\nappId=5\n#百度\nbaiduUrl=http://www.baidu.com\n";
		GroupDTO g2 = GroupDTOUtil.fromProperties("testGroup", props);
		
		Assert.assertEquals(g.getGroupName(), g2.getGroupName());
		Assert.assertEquals(g.getItems().size(), g2.getItems().size());
		
		for(int i=0;i<g2.getItems().size();i++){
			PropertyDTO p2 = g2.getItems().get(i);
			PropertyDTO p = g.getItems().get(i);
			Assert.assertEquals(p.toString(),p2.toString());
		}
	}
}
