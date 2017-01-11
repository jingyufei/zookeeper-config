linker.controller('ConfigController', ['$scope','$window', '$routeParams', '$location' ,'configService', 'responseService','popupService', function($scope,$window,$routeParams,$location,configService,responseService,popupService) {  	

	$scope.page=1;
	$scope.num=10;
	$scope.totalPage=1;
	
	$scope.groupName = "";
	$scope.propertyKey = "";
	
	$scope.popupdata = popupService.init();
	
	var initial = function(){
		if(responseService.checkSession()){
			$scope.getAllGroup();
			
			//查询
			 $scope.$watch('page', function() {
		         $scope.getPagesAndRecords();
		    });
		}
	};
	
	$scope.getAllGroup = function(arg){
		configService.getAllGroup({
		}).then(
			function(response){
		    	if(responseService.successResponse(response)){
		    		$scope.groups = response.data;
		    		if(response.data!=null && response.data.length>0){
		    			$scope.groupName = response.data[0];
		    			$scope.getPagesAndRecords();
		    		}
		    	}
			},
	    	function(errorMessage){
				responseService.errorResponse("读取配置分组列表失败");
			}
		);
	}
	$scope.freshAllGroup = function(arg){
		configService.getAllGroup({
		}).then(
			function(response){
		    	if(responseService.successResponse(response)){
		    		$scope.groups = response.data;
		    	}
			},
	    	function(errorMessage){
				responseService.errorResponse("读取配置分组列表失败");
			}
		);
	}

  	$scope.getPagesAndRecords = function (arg) {				
  		configService.query({
				groupName:$scope.groupName,
				propertyKey:$scope.propertyKey
	     }).then(function(response){
 	    	if(responseService.successResponse(response)){
 	    		$scope.configs = response.data;
 	    	}
 	    },function(errorMessage){
 	    	responseService.errorResponse("读取配置信息失败");
 	    });
  
     };
  	$scope.queryConfig = function() {
  		$scope.getPagesAndRecords();
	};
    
    $scope.exportConfig = function () {	
    	$scope.queryConfig();
    	$window.location = "/order/export?appId="+$scope.appId+"&mo=" + $scope.mo;
    };
    
    $scope.del = function(item){
    	$scope.popupdata = popupService.set("删除配置项"+item.propertyKey, true, "/delPropertyJson");
		$scope.popupdata.default = [
		       item.groupName,item.propertyKey
		];

		$scope.popupdata.datas = [
		               		   {
		               				"name": "配置组名",
		               				"blur": "empty",
		               				"disabled":true,
		               				"field":"groupName"
		               			},{
		               			"name": "配置项",
		               			"blur": "empty",
		               			"disabled":true,
		               			"field":"propertyKey",
		               		}];
		               		
		$scope.popupdata.method='POST';
		$scope.popupdata.afterSuccess = $scope.queryConfig;
//    	configService.del(item).then(
//			function(response){
//		    	if(responseService.successResponse(response)){
//		    		$scope.getPagesAndRecords();
//		    	}
//			},
//	    	function(errorMessage){
//				responseService.errorResponse("删除配置失败");
//			}
//		);
    }
    
    $scope.exportConfig = function(){
    	$window.location = "/exportProperty?groupName="+$scope.groupName;
    }
    
    $scope.addGroup = function(){
    	$scope.popupdata = popupService.set("添加配置组", true, "/addGroup?groupName=");
		$scope.popupdata.default = [
			""
		];

		$scope.popupdata.datas = [{
			"name": "配置组名",
			"blur": "empty"
		}];
		
		$scope.popupdata.afterSuccess = $scope.freshAllGroup;
    }
    
    $scope.importProperty = function(){
    	$scope.popupdata = popupService.set("导入", true, "/importPropertyByString");
		$scope.popupdata.default = [
			$scope.groupName,""
		];

		$scope.popupdata.datas = [
		   {
				"name": "配置组名",
				"blur": "empty",
				"disabled":true,
				"field":"groupName"
			},{
			"name": "导入数据",
			"blur": "empty",
			"field":"json",
			"url": "<textarea cols=\"120\" rows=\"20\"  name=\"{{$index}}\" ng-model=\"data[3*$index]\" ng-blur=\"check(item.blur,$event)\" style=\"display: block;width: 100%; \" />"
		}];
		
		$scope.popupdata.method='POST';
		
		$scope.popupdata.afterSuccess = function(){
			$scope.queryConfig();
			//$scope.getPagesAndRecords();
		}
    }
    
    $scope.addProperty = function(){
    	$scope.popupdata = popupService.set("添加配置项", true, "/addProperty?groupName=&propertyKey=&propertyValue=&propertyDesc=");
		$scope.popupdata.default = [
			$scope.groupName,"","",""
		];

		$scope.popupdata.datas = [
			{
				"name": "配置组名",
				"blur": "empty",
				"disabled":true
			},
			{
				"name": "配置项名",
				"blur": "empty"
			},
			{
				"name": "配置项值",
				"blur": "empty"
			},
			{
				"name": "配置项描述",
				"blur": "empty"
			}
		];
		
		$scope.popupdata.afterSuccess = $scope.queryConfig;
    }
    
    $scope.edit = function(item){
    	$scope.popupdata = popupService.set("编辑配置项", true, "/editProperty?groupName=&propertyKey=&propertyValue=&propertyDesc=");
		$scope.popupdata.default = [
			item.groupName,item.propertyKey,item.propertyValue,item.propertyDesc
		];

		$scope.popupdata.datas = [
			{
				"name": "配置组名",
				"blur": "empty",
				"disabled":true
			},
			{
				"name": "配置项名",
				"blur": "empty",
				"disabled":true
			},
			{
				"name": "配置项值",
				"blur": "empty"
			},
			{
				"name": "配置项描述",
				"blur": "empty"
			}
		];
		
		$scope.popupdata.afterSuccess = $scope.queryConfig;
    }
    
    
    
    
	$scope.makeOrder = function(type, direction) {
		if ($scope.orderBy == type) {
			if (direction == "up") {
				$scope.orderByValue="1";
			}else{
				$scope.orderByValue="-1";
			}
		} else {
			$scope.orderBy = type;
			if (direction == "up") {
				$scope.orderByValue = "1";
			} else {
				$scope.orderByValue = "-1";
			}
		}
		$scope.currentPage = 1;
		$scope.getPagesAndRecords();
	};
   	
    $scope.getupSortClass = function(type) {
		var spanClass = "no-sort-up";
		if ($scope.orderBy == type) {
			if ($scope.orderByValue == "-1") {
				spanClass = "no-sort-up";
			} else {
				spanClass = "sort-up";
			}
		}
		return spanClass;
	}
	$scope.getdownSortClass = function(type) {
		var spanClass = "no-sort-down";
		if ($scope.orderBy == type) {
			if ($scope.orderByValue == "-1") {
				spanClass = "sort-down";
			} else {
				spanClass = "no-sort-down";
			}
		}
		return spanClass;
	}
    initial();
 
}]);