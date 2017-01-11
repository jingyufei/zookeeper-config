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
import org.springframework.web.multipart.MultipartFile;

import com.baoyun.base.config.server.exception.ParamInvalidException;
import com.baoyun.base.config.server.response.NoPageResultModel;
import com.baoyun.base.config.server.response.RestResponseCode;
import com.baoyun.base.config.server.service.PropertyService;

@RestController
public class ImportPropertyController {	
	private static Logger log = Logger.getLogger(ImportPropertyController.class);
	@Autowired
	PropertyService propertyService;
	
	@RequestMapping(value = "/importProperty", method = RequestMethod.GET)
	@ResponseBody
	public NoPageResultModel importProperty(
			@RequestParam("file_import") MultipartFile file,
			HttpServletRequest request) {

		String txNo = "import property: " + UUID.randomUUID().toString();
		log.info("txNo: {}, fileName: {}");

		try {
			// 成功导入返回true,不成功返回false
			NoPageResultModel restModel = null;//propertyService.importProperty(txNo, file.getInputStream());
			if ((boolean) restModel.getData()) 
				log.info("import property success, txNo: {} fileName");
			 else 
				log.error("import property failed, txNo: {}, fileName: {}");
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
