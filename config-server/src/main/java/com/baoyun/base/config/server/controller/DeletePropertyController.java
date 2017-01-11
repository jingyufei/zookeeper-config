package com.baoyun.base.config.server.controller;

import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baoyun.base.config.server.exception.ParamInvalidException;
import com.baoyun.base.config.server.response.NoPageResultModel;
import com.baoyun.base.config.server.response.RestResponseCode;
import com.baoyun.base.config.server.service.PropertyService;
import com.baoyun.base.config.server.util.JsonUtil;
import com.baoyun.base.config.server.util.StringCheckUtils;

@RestController
public class DeletePropertyController {

	private static Logger log = Logger.getLogger(DeletePropertyController.class);
	@Autowired
	PropertyService propertyService;

	@RequestMapping(value = "/delProperty", method = RequestMethod.GET)
	@ResponseBody
	public NoPageResultModel deleteProperty(
			@RequestParam(value = "groupName", required = true) String groupName,
			@RequestParam(value = "propertyKey", required = true) String propertyKey,
			HttpServletRequest request) {
		String txNo = "del property: " + UUID.randomUUID().toString();
		log.info("txNo: {}, groupName: {}, propertyKey: {}");

		try {
			groupName = StringCheckUtils.checkGroupName(groupName);
			propertyKey = StringCheckUtils.checkPropertyKey(propertyKey);
			// 成功返回true, 失败返回false
			NoPageResultModel resultModel = propertyService.delProperty(txNo, groupName,propertyKey);
			if ((boolean) resultModel.getData()) 
				log.info("delete property success, txNo: {}, groupName: {}, propertyKey: {}");
			else 
				log.error("delete property failed, txNo: {}, groupName: {}, propertyKey: {}");
			return resultModel;
		} catch (ParamInvalidException e) {
	        log.error("delProperty groupName:{} propertyKey:{} error:{} ");
	        return new NoPageResultModel(RestResponseCode.PARAMETER_ERROR, RestResponseCode.PARAMETER_ERROR_DESC + e.getFieldName()+" "+e.getMessage());
	    } catch (Exception e) {
	    	log.error("delProperty groupName:{} propertyKey:{} error:{} ");
	        return new NoPageResultModel(RestResponseCode.INTERNAL_ERROR, RestResponseCode.INTERNAL_ERROR_DESC);
	    }
	}
	
	@RequestMapping(value = "/delPropertyJson", method = RequestMethod.POST)
	@ResponseBody
	public NoPageResultModel delPropertyJson(
			@RequestBody String body,
			HttpServletRequest request) {
		String txNo = "del property: " + UUID.randomUUID().toString();
		log.info("txNo: {},body:{}");

		try {
			Map<String,Object> data = JsonUtil.json2Map(body);
			String groupName = (String)data.get("groupName");
			groupName = StringCheckUtils.checkGroupName(groupName);
			
			String propertyKey = (String)data.get("propertyKey");
			propertyKey = StringCheckUtils.checkPropertyKey(propertyKey);
			// 成功返回true, 失败返回false
			NoPageResultModel resultModel = propertyService.delProperty(txNo, groupName,propertyKey);
			if ((boolean) resultModel.getData()) 
				log.info("delete property success, txNo: {}, groupName: {}, propertyKey: {}");
			else 
				log.error("delete property failed, txNo: {}, groupName: {}, propertyKey: {}");
			return resultModel;
		} catch (ParamInvalidException e) {
	        log.error("delProperty txNo:{} error:{} ");
	        return new NoPageResultModel(RestResponseCode.PARAMETER_ERROR, RestResponseCode.PARAMETER_ERROR_DESC + e.getFieldName()+" "+e.getMessage());
	    } catch (Exception e) {
	    	log.error("delProperty txNo:{} error:{} ");
	        return new NoPageResultModel(RestResponseCode.INTERNAL_ERROR, RestResponseCode.INTERNAL_ERROR_DESC);
	    }
	}
}