linker.controller('MainController', ['$scope','$location','logoutService','$window', function($scope,$location,logoutService,$window) {
  	//for test
	sessionStorage.userName = "匿名";
	sessionStorage.userType = "admin";
	sessionStorage.agentName = "";
	sessionStorage.enterNameshow = "";
	
	$scope.currentUser = {
  			"name" : sessionStorage.userName,
  	        "type" : sessionStorage.userType,
  	        "agentName":sessionStorage.agentName,
  	        "enterNameshow":sessionStorage.enterNameshow,
  	};
  	$scope.display = $scope.currentUser.type == "admin" ? false : true;
	$scope.navigators = [
	    {"name" : "配置管理","image":"agent","href":"/config","ngclass":"","display":!$scope.display}
  	];
  	$scope.changeshow=function(type){
  		
  	}
  	$scope.$on('$locationChangeStart', function(evt, next, current) {
  		if (current) {
  			
			if (sessionStorage.userType == "admin") {
				
			}
			
		}
	});
  	$scope.selectthis = function(name){
  		_.each($scope.navigators,function(item){
  			if(item.name != name){
  				item.ngclass = "";
  			}else{
  				item.ngclass = "active";
  			}
  		})

  	}
  	
  	function forRefresh(){
  		var path = $location.path();
  		_.each($scope.navigators,function(item){
  			if(path != ""){
  				if(item.href != path){
	  				item.ngclass = "";
	  			}else{
	  				item.ngclass = "active";
	  			}
  			}
  		})
  	}
  	
  	forRefresh();
 
  	$scope.logout = function(){
  		logoutService.logout().then(function(response){
 	    	sessionStorage.name = "";
 	    	sessionStorage.type = "";
 	    	$window.location="/op-portal/login.html";
 	    },function(errorMessage){
 	       layer.alert('退出失败', {    	 
 	    	  icon: 2 //failure icon; icon 1: success icon; icon 0: alert icon;
 	    	});
 	    });
  		
  	};
	
}]);