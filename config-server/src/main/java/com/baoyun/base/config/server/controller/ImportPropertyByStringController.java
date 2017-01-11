package com.baoyun.base.config.server.controller;

import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baoyun.base.config.server.exception.ParamInvalidException;
import com.baoyun.base.config.server.response.NoPageResultModel;
import com.baoyun.base.config.server.response.RestResponseCode;
import com.baoyun.base.config.server.service.PropertyService;
import com.baoyun.base.config.server.util.JsonUtil;

@RestController
public class ImportPropertyByStringController {	
	private static Logger log = Logger.getLogger(ImportPropertyByStringController.class);
	@Autowired
	PropertyService propertyService;
	
	@RequestMapping(value = "/importPropertyByString", method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public NoPageResultModel importProperty(
			@RequestBody String body,
			HttpServletRequest request) {

		String txNo = "import property: " + UUID.randomUUID().toString();
		log.info("txNo: {}, json: {}");

		try {
			Map<String,Object> data = JsonUtil.json2Map(body);
			// 成功导入返回true,不成功返回false
			NoPageResultModel restModel = propertyService.importProperty(txNo,(String)data.get("groupName"), (String)data.get("json"));
			if ((boolean) restModel.getData()) 
				log.info("import property success, txNo: {} ");
			 else 
				log.error("import property failed, txNo: {}");
			return restModel;
		} catch (ParamInvalidException e) {
			log.error("ParamInvalidException, txNo: {}, {}");
			return new NoPageResultModel(RestResponseCode.PARAMETER_ERROR,
					RestResponseCode.PARAMETER_ERROR_DESC + e.getMessage(),
					false);
		} catch (Exception e) {
			log.error("Exception, txNo: {}, {}");
			return new NoPageResultModel(RestResponseCode.INTERNAL_ERROR,
					RestResponseCode.INTERNAL_ERROR_DESC, false);
		}
	}
}
