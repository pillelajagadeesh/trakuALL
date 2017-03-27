(function() {
    'use strict';
    angular
        .module('trakeyeApp')
        .factory('DashboardService', HomeService);

    HomeService.$inject = ['$http', 'DateUtils'];

    function HomeService ($http, DateUtils) {
    	var service ={};
    	
    	service.notifications = function (callback){
    		
    		$http.get('api/dashboard/notifications').then(function(response){
        		callback(response.data);
        	});
    	};
    	service.users = function (callback){
    		
    		$http.get('api/dashboard/users').then(function(response){
        		callback(response.data);
        	});
    	};
    	service.casesbypriority = function (callback){
    		
    		$http.get('api/dashboard/casepriority').then(function(response){
    			callback(response.data);
        	});
    	};
		service.cases = function (callback){
		    		
		    		$http.get('api/dashboard/cases').then(function(response){
		        		callback(response.data);
		        	});
		    	};
		service.services = function (callback){
		    		
		    		$http.get('api/dashboard/services').then(function(response){
		        		callback(response.data);
		        	});
		    	};	
		service.geofences = function (callback){
				    		
				    		$http.get('api/dashboard/geofences').then(function(response){
				        		callback(response.data);
				        	});
				    	};	
    	
    	return service;
    }
})();
(function () {
    'use strict';

    angular
        .module('trakeyeApp')
        .factory('UserListAndDistance', UserListAndDistance);

    UserListAndDistance.$inject = ['$resource'];

    function UserListAndDistance ($resource) {
        var service = $resource('api/dashboard/userlist',{},{
            'query': {method: 'GET',isArray: true},
        });

        return service;
    }
})();

(function () {
    'use strict';

    angular
        .module('trakeyeApp')
        .factory('UserListAndDistanceSearch', UserListAndDistanceSearch);

    UserListAndDistanceSearch.$inject = ['$resource'];

    function UserListAndDistanceSearch ($resource) {
        var service = $resource('api/dashboard/userlist/:login',{login:'@login'},{},{
            'query': {method: 'GET',isArray: true},
        });

        return service;
    }
})();