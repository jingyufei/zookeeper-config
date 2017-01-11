package com.baoyun.base.config.server.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baoyun.base.config.server.exception.ParamInvalidException;
import com.baoyun.base.config.server.response.NoPageResultModel;
import com.baoyun.base.config.server.response.RestResponseCode;
import com.baoyun.base.config.server.service.PropertyService;
import com.baoyun.base.config.server.util.StringCheckUtils;

@RestController
public class AddPropertyController {

	private static Logger log = Logger.getLogger(AddPropertyController.class);
	@Autowired
	PropertyService propertyService;

	@RequestMapping(value = "/addProperty", method = RequestMethod.GET)
	@ResponseBody
	public NoPageResultModel addProperty(
			@RequestParam(value = "groupName", required = true) String groupName,
			@RequestParam(value = "propertyKey", required = true) String propertyKey,
			@RequestParam(value = "propertyValue", required = true) String propertyValue,
			@RequestParam(value = "propertyDesc", required = true) String propertyDesc,
			HttpServletRequest request) {
	
		String txNo = "add property: " + UUID.randomUUID().toString();
		log.info(
				"txNo: {}, groupName: {}, propertyKey: {}, propertyValue: {}, propertyDesc: {}");
		
		try {

			groupName = StringCheckUtils.checkGroupName(groupName);
			propertyKey = StringCheckUtils.checkPropertyKey(propertyKey);
			NoPageResultModel resultModel =  propertyService.addProperty(txNo, groupName, propertyKey, propertyValue, propertyDesc);
			if ((boolean) resultModel.getData()) 
				log.info("add property success, txNo: {}, groupName: {}, propertyKey: {}, propertyValue: {}, propertyDesc: {}");
			 else 
				log.error("add property failed, txNo: {}, groupName: {}, propertyKey: {}, propertyValue: {}, propertyDesc: {}");
			return resultModel;
		} catch (ParamInvalidException e) {
	        log.error("addProperty groupName:{} propertyKey:{} error:{} ");
	        return new NoPageResultModel(RestResponseCode.PARAMETER_ERROR, RestResponseCode.PARAMETER_ERROR_DESC + e.getFieldName() +" "+e.getErrorInfo());
	    } catch (Exception e) {
	    	log.error("addProperty groupName:{} propertyKey:{} error:{} ");
	        return new NoPageResultModel(RestResponseCode.INTERNAL_ERROR, RestResponseCode.INTERNAL_ERROR_DESC);
	    }
	}
}