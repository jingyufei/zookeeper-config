var login = angular.module('login',['ngRoute']);
var linker = angular.module('linker',['ngRoute']);

//route
login.config(function($routeProvider, $locationProvider) {
	$routeProvider
		.when('/', {
    		templateUrl: 'templates/login/signin.html',
    		controller: 'LoginController'
  		})
  		.when('/forgetpwd', {
    		templateUrl: 'templates/login/forgetpwd.html',
    		controller: 'LoginController'
  		})
  		.otherwise({  
            redirectTo: '/'
        });
});

linker.config(function($routeProvider, $locationProvider) {
	$routeProvider
  		.when('/config',{
  			templateUrl:'templates/main/config.html',
  			controller:'ConfigController'
  		})
  		.otherwise({  
            redirectTo: '/orders'
        });
});