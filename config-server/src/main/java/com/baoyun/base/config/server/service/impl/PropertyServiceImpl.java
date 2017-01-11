package com.baoyun.base.config.server.service.impl;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baoyun.base.config.server.dto.DataDTO;
import com.baoyun.base.config.server.dto.GroupDTO;
import com.baoyun.base.config.server.dto.PropertyDTO;
import com.baoyun.base.config.server.exception.ParamInvalidException;
import com.baoyun.base.config.server.response.NoPageResultModel;
import com.baoyun.base.config.server.response.RestResponseCode;
import com.baoyun.base.config.server.service.PropertyService;
import com.baoyun.base.config.server.util.DateFormatUtil;
import com.baoyun.base.config.server.util.GroupDTOUtil;
import com.baoyun.base.config.server.util.JsonUtil;
import com.baoyun.base.config.server.util.StringCheckUtils;
import com.baoyun.base.config.server.util.ZkClient;

@Service("propertyService")
public class PropertyServiceImpl implements PropertyService{
	private static Logger log = Logger.getLogger(PropertyServiceImpl.class);
	private int sessionTimeout = 1000 * 60;
	@Value("${config.zookeeperAddress}")
	private String zookeeperAddress = "127.0.0.1:2181";
	@Value("${config.rootPath}")
	private String rootPath = "/linker/config";
	
	
	private String getGroupPath(String groupName){
		groupName = StringCheckUtils.checkGroupName(groupName);
		if(rootPath.endsWith("/"))
			return rootPath + groupName;
		else
			return rootPath + "/" + groupName;
	}
	
	@Override
	public NoPageResultModel getProperty(String txNo, String groupName,
			String propertyKey) {
		log.info("--get property groupName:{} key:{} txNo:{}");
		try {
			ZooKeeper zoo = ZkClient.getZooKeeper(zookeeperAddress);
			
			String groupPath = getGroupPath(groupName);
			Stat groupExists = zoo.exists(groupPath, null);
			if(groupExists != null){
				//模糊查找
				List<PropertyDTO> lists = new ArrayList<PropertyDTO>();
				List<String> childrenNodes = zoo.getChildren(groupPath, null);
				for(String node : childrenNodes){
					if(node.contains(propertyKey)){
						String newPath = groupPath +"/"+node;
						Stat newStat = zoo.exists(newPath, null);
						if(newStat!=null){
							//获取 值  与 描述
							byte[] dataBytes = zoo.getData(newPath, null, newStat);
							String dataString = new String(dataBytes,"UTF-8");
							DataDTO partData = (DataDTO)JsonUtil.json2Object(dataString, DataDTO.class);
							
							PropertyDTO data = new PropertyDTO();
							data.setGroupName(groupName);
							data.setPropertyKey(node);
							data.setPropertyValue(partData.getValue());
							data.setPropertyDesc(partData.getDesc());
							data.setUpdateTime(DateFormatUtil.formatDateTime(new Date(newStat.getMtime())));
							lists.add(data);
						}
					}
				}
				
				//对 结果集排序, 组名称, key,
				sort(lists);
				
				
				return new NoPageResultModel(RestResponseCode.SUCCESS,RestResponseCode.SUCCESS_DESC,lists);
			}else {
				return new NoPageResultModel(RestResponseCode.SUCCESS, RestResponseCode.SUCCESS_DESC, new ArrayList<PropertyDTO>());
			}
		} catch (Exception e) {
			log.error("getProperties err",e);
			return new NoPageResultModel(RestResponseCode.INTERNAL_ERROR, RestResponseCode.INTERNAL_ERROR_DESC, null);
		} 
	}

	@Override
	public NoPageResultModel editProperty(String txNo, String groupName,
			String propertyKey, String propertyValue, String propertyDesc) {
		log.info("--edit property groupName:{} key:{} txNo:{}");
		try {
			ZooKeeper zoo = ZkClient.getZooKeeper(zookeeperAddress);
			
			String groupPath = getGroupPath(groupName);
			Stat groupExists = zoo.exists(groupPath, null);
			if(groupExists != null){
				//更新属性
				String newPath = groupPath + "/" + propertyKey;
				if(zoo.exists(newPath, null) == null)
					return new NoPageResultModel(RestResponseCode.NO_THE_PROPERTY, RestResponseCode.No_THE_PROPERTY_DESC, false);
				
				//组织配置项的配置信息和描述    json 字符串
				DataDTO dataDTO = new DataDTO(propertyValue, propertyDesc);
				String jsonData = JsonUtil.write2JsonStr(dataDTO);
				Stat resultString = zoo.setData(newPath, jsonData.getBytes("UTF-8"), -1);
				
				if(resultString != null)
					return new NoPageResultModel(RestResponseCode.SUCCESS, RestResponseCode.SUCCESS_DESC, true);
				return new NoPageResultModel(RestResponseCode.FAILURE, RestResponseCode.FAILURE_DESC, false);
			}else {
				return new NoPageResultModel(RestResponseCode.NO_THE_GROUP, RestResponseCode.No_THE_GROUP_DESC, false);
			}
		} catch (Exception e) {
			log.error("editProperty err, txNo:{"+txNo+"}",e);
			return new NoPageResultModel(RestResponseCode.INTERNAL_ERROR, RestResponseCode.INTERNAL_ERROR_DESC, false);
		} 
	}

	@Override
	public NoPageResultModel delProperty(String txNo, String groupName, String propertyKey) throws ParamInvalidException{
		log.info("--delte property groupName:{} key:{} txNo:{}");
		try {
			groupName = StringCheckUtils.checkGroupName(groupName);
			propertyKey = StringCheckUtils.checkPropertyKey(propertyKey);
			ZooKeeper zoo = ZkClient.getZooKeeper(zookeeperAddress);
			
			String groupPath = getGroupPath(groupName);
			Stat groupExists = zoo.exists(groupPath, null);
			if(groupExists != null){
				//删除
				String newPath = groupPath + "/" + propertyKey;
				if(zoo.exists(newPath, null) == null)
					return new NoPageResultModel(RestResponseCode.NO_THE_PROPERTY, RestResponseCode.No_THE_PROPERTY_DESC, false);
				
				zoo.delete(newPath, -1);
				return new NoPageResultModel(RestResponseCode.SUCCESS,RestResponseCode.SUCCESS_DESC,true);
			}else {
				return new NoPageResultModel(RestResponseCode.NO_THE_GROUP, RestResponseCode.No_THE_GROUP_DESC, false);
			}
		} catch (Exception e) {
			return new NoPageResultModel(RestResponseCode.INTERNAL_ERROR, RestResponseCode.INTERNAL_ERROR_DESC, false);
		} 
	}

	@Override
	public NoPageResultModel addProperty(String txNo, String groupName,
			String propertyKey, String propertyValue, String propertyDesc) throws ParamInvalidException {
		
		log.info("--add property groupName:{} key:{} txNo:{}");
		try {
			groupName = StringCheckUtils.checkGroupName(groupName);
			propertyKey = StringCheckUtils.checkPropertyKey(propertyKey);
			
			ZooKeeper zoo = ZkClient.getZooKeeper(zookeeperAddress);
			//新增属性
			//判断原来组的节点是否存在
			String groupPath = getGroupPath(groupName);
			Stat groupExists = zoo.exists(groupPath, null);
			
			if(groupExists == null){
				throw new ParamInvalidException("groupName", "配置组不存在");
			}
			//组织配置项的配置信息和描述    json 字符串
			DataDTO dataDTO = new DataDTO(propertyValue, propertyDesc);
			String jsonData = JsonUtil.write2JsonStr(dataDTO);
			String newPath = groupPath + "/" + propertyKey;
			
			if(zoo.exists(newPath, null) == null) {
				String resultString = zoo.create(newPath, jsonData.getBytes("UTF-8"), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				if(resultString!=null)
					return new NoPageResultModel(RestResponseCode.SUCCESS, RestResponseCode.SUCCESS_DESC, true);
				else
					return new NoPageResultModel(RestResponseCode.FAILURE, RestResponseCode.FAILURE_DESC, false);
			} else 
				return new NoPageResultModel(RestResponseCode.REPEATED_PROPERTY, RestResponseCode.REPEATED_PROPERTY_DESC, false);
		} catch (Exception e) {
			log.info("--add property fail, groupName:{} key:{} txNo:{}");
			log.error("",e);
			return new NoPageResultModel(RestResponseCode.INTERNAL_ERROR, RestResponseCode.INTERNAL_ERROR_DESC, false);
		} 
	}

	@Override
	public NoPageResultModel addGroup(String txNo, String groupName) {
		log.info("--getAllGroup groupName:{} txNo:{}");

		try {
			ZooKeeper zoo = ZkClient.getZooKeeper(zookeeperAddress);
			
			groupName = StringCheckUtils.checkGroupName(groupName);
			String groupPath = getGroupPath(groupName);
			Stat groupExists = zoo.exists(groupPath, null);
			
			if(groupExists == null){
				zoo.create(groupPath, "group".getBytes("UTF-8"), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				return new NoPageResultModel(RestResponseCode.SUCCESS, RestResponseCode.SUCCESS_DESC, true);
			}else {
				return new NoPageResultModel(RestResponseCode.REPEATED_GROPU, RestResponseCode.REPEATED_GROPU_DESC, false);
			}
		} catch (Exception e) {
			throw new ParamInvalidException("addGroup", e.getMessage());
		} 
	}

	@Override
	public List<String> getAllGroup(String txNo) throws ParamInvalidException{
		log.info("--getAllGroup txNo:{}");
		try {
			ZooKeeper zoo = ZkClient.getZooKeeper(zookeeperAddress);
			
			List<String> nodes = zoo.getChildren(rootPath, null);
			if(nodes!=null && !nodes.isEmpty()){
				Collections.sort(nodes);
			}
			
			return nodes;
		} catch (Exception e) {
			throw new ParamInvalidException("getAllGroup", e.getMessage());
		} 
	}
	
	@Override
	public NoPageResultModel importProperty(String txNo,String groupName, String json) {
		log.info("--importProperty txNo:{},json:{}");
		try {
			ZooKeeper zoo = ZkClient.getZooKeeper(zookeeperAddress);
			//查看组别是否存在，不存在创建
			groupName = StringCheckUtils.checkGroupName(groupName);
			String groupPath = getGroupPath(groupName);
			
			//init group
			Stat groupExists = zoo.exists(groupPath, null);
			if(groupExists == null){
				zoo.create(groupPath, "group".getBytes("UTF-8"), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			}
			
			GroupDTO dto = GroupDTOUtil.fromProperties(groupName, json);//(GroupDTO)JsonUtil.json2Object(json, GroupDTO.class);
			for(PropertyDTO p : dto.getItems()){
				p.setPropertyKey(p.getPropertyKey().trim());
				p.setPropertyValue(p.getPropertyValue().trim());
				
				//add or update properties
				String node = groupPath + "/" + p.getPropertyKey();
				DataDTO data = new DataDTO(p.getPropertyValue(),p.getPropertyDesc());
				String datajson = JsonUtil.write2JsonStr(data);
				
				Stat zooStat = zoo.exists(node, null);
				if(zooStat == null)
					zoo.create(node, datajson.getBytes("UTF-8"), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				else
					zoo.setData(node, datajson.getBytes("UTF-8"), -1);	
			}
			return new NoPageResultModel(RestResponseCode.SUCCESS,RestResponseCode.SUCCESS_DESC,true);
		} catch (Exception e) {
			throw new ParamInvalidException("importProperty", e.getMessage());
		} 
	}


	@Override
	public OutputStream exportProperty(String txNo, String groupName,OutputStream os)throws ParamInvalidException{
		log.info("--exportProperty groupName:{} txNo:{}");
		try {
			ZooKeeper zoo = ZkClient.getZooKeeper(zookeeperAddress);
			
			String groupPath = getGroupPath(groupName);
			Stat statExists = zoo.exists(groupPath, null);

			List<PropertyDTO> props = new ArrayList<PropertyDTO>();
			
			if(statExists != null){
				List<String> childNodes = zoo.getChildren(groupPath, null);
				for(String node : childNodes){
					Stat stat = zoo.exists(groupPath+"/"+node, null);
					if(stat != null){
						byte[] bytesValue = zoo.getData(groupPath+"/"+node, null, stat);
						String jsonValue = new String(bytesValue, "UTF-8");
						DataDTO item = (DataDTO)JsonUtil.json2Object(jsonValue, DataDTO.class);
						
						PropertyDTO dto = new PropertyDTO();
						dto.setGroupName(groupName);
						dto.setPropertyKey(node);
						dto.setPropertyValue(item.getValue());
						dto.setPropertyDesc(item.getDesc());
						dto.setUpdateTime(DateFormatUtil.formatDateTime(new Date(stat.getMtime())));
						props.add(dto);
					}
				}
			}
			
			//对 结果集排序, 组名称, key,
            sort(props);
			
			String resultJson = GroupDTOUtil.toProperties(new GroupDTO(groupName,props));//JSONObject.fromObject(new GroupDTO(groupName,props)).toString();
			os.write(resultJson.getBytes("UTF-8"));
			os.flush();
		} catch (Exception e) {
			log.error("exportProperty err,txNo:{"+txNo+"}",e);
		} finally {
			if (os != null) {
				try {
					os.close();
				}
				catch (Exception e) {
				
				}
			}
		}
		return os;
	}
	
    /**
     * Watcher事件转化为 String
     * 
     * @param event
     * @return
     */
    private static String watchedEventToString(WatchedEvent event) {
        StringBuffer stringBuffer = new StringBuffer();
        if (event == null) {
            stringBuffer.append("event is null");
        } else {
            stringBuffer.append(" type=" + event.getType());
            stringBuffer.append(", state=" + event.getState());
            stringBuffer.append(", path=" + event.getPath());
        }
        return stringBuffer.toString();
    }

	@Override
	public boolean existGroupName(String txNo, String groupName) {
		log.info("check groupName:{} exist or not txNo:{}");
		try {
			ZooKeeper zoo = ZkClient.getZooKeeper(zookeeperAddress);
			Stat existStat = zoo.exists(groupName, null);
			if(existStat!=null)
				return true;
			return false;
		} catch (Exception e) {
			throw new ParamInvalidException("GroupName exist or not", e.getMessage());
		} 
	}
	
	
    /**
     * 对配置结果排序
     * @param lists
     */
    private void sort(List<PropertyDTO> lists) {
        // 对 结果集排序, 组名称, key,
        if (lists != null && lists.size() > 0) {
            Collections.sort(lists, new Comparator<PropertyDTO>() {

                @Override
                public int compare(PropertyDTO o1, PropertyDTO o2) {
                    int compare = 0;
                    if (o1 != null && o2 != null) {
                        compare = o1.getGroupName().compareTo(o2.getGroupName());
                        if (compare == 0) {
                            compare = o1.getPropertyKey().compareTo(o2.getPropertyKey());
                            if (compare == 0) {
                                compare = o1.getPropertyValue().compareTo(o2.getPropertyValue());
                            }
                        }
                    }

                    return compare;
                }

            });
        }
    }

	

}
