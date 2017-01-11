function configResource($http,$q){
	return {
		query : function(conditions){
			    var url="/getProperty";
				var params = {
                    	        "groupName":conditions.groupName,"propertyKey":conditions.propertyKey
                               };
			    var request = {
				        url : url,
				        params : params
				    };
			    
				var deferred = $q.defer();
				$http(request).success(function(response){					
					 deferred.resolve(response);
				}).error(function(error){
					deferred.reject(error);
				});
			return deferred.promise;
		},
		getAllGroup : function(conditions){
			    var url="/getAllGroup";
				var params = {};
			    var request = {
				        url : url,
				        params : params
				};
			    
				var deferred = $q.defer();
				$http(request).success(function(response){					
					 deferred.resolve(response);
				}).error(function(error){
					deferred.reject(error);
				});
			return deferred.promise;
		},
		del : function(item){
		    var url="/delProperty";
			var params = {"groupName":item.groupName,"propertyKey":item.propertyKey};
		    var request = {
			        url : url,
			        params : params
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
linker.factory('configService', ['$http','$q', configResource]);