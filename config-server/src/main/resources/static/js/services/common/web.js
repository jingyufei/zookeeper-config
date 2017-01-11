linker.service('webService', function($http, $q) {
	var get_data = function(url) {
		var deferred = $q.defer();
		$http.get(url).success(function(data) {
			if (!data.reply||data.reply == 1) {
				try{
					if(typeof(data)=="string" && data.indexOf("<!DOCTYPE html>"!=-1)){
						//重定向到登录界面
						location.href="/op-portal/login.html";
					}
				}catch(e){}
				deferred.resolve(data);
			} else {
				deferred.reject(data.replyDesc);
			}
		}).error(function(error) {
			deferred.reject(error.responseText);
		});
		return deferred.promise;
	};
	var post_data = function(url,json) {
		var deferred = $q.defer();
		$http.post(url,json).success(function(data) {
			if (!data.reply||data.reply == 1) {
				try{
					if(typeof(data)=="string" && data.indexOf("<!DOCTYPE html>"!=-1)){
						//重定向到登录界面
						location.href="/op-portal/login.html";
					}
				}catch(e){}
				deferred.resolve(data);
			} else {
				deferred.reject(data.replyDesc);
			}
		}).error(function(error) {
			deferred.reject(error.responseText);
		});
		return deferred.promise;
	};
	return {
		'get_data': get_data,
		'post_data': post_data
	}
});