function responseService($window){
	var successResponse = function(response){
		if(_.isString(response) && response.indexOf("<!DOCTYPE html>") != -1 ){
	 	    $window.location = "/op-portal/login.html";
	 	    return false;
	 	}else if(response.reply != 1){
	 	   	layer.alert(response.replyDesc, {    	 
			 	icon: 2
			});
			return false;
	 	}else {
	 	    return true;
	 	}
	};
	
	var errorResponse = function(message){
		layer.alert(message, {    	 
			icon: 2
		});
	};
	
	var checkSession = function(){
		if(_.isUndefined(sessionStorage.userName) || _.isEmpty(sessionStorage.userName)){
			$window.location = "/op-portal/login.html";
			return false;
		}else{
			//if(sessionStorage.userType=="admin")sessionStorage.appId=0;
		    return true;	   
		}
	};
	
	return {
		"successResponse" : successResponse,
		"errorResponse" : errorResponse,
		"checkSession" : checkSession
	}
}
   
linker.factory('responseService', ['$window',responseService]);