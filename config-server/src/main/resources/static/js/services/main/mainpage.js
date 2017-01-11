function logoutResource($http,$q){
	return {
		logout : function(){		
				var url="/logout";
			    var request = {
				        url : url,
				    };
				var deferred = $q.defer();
				$http(request).success(function(response){					
					 deferred.resolve(response);
				}).error(function(error){
					deferred.reject(error);
				});
			return deferred.promise;
		}
	}
}
linker.factory('logoutService', ['$http','$q', logoutResource]);