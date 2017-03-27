(function() {
    'use strict';
    angular
        .module('trakeyeApp')
        .factory('DashboardService', HomeService);

    HomeService.$inject = ['$http', 'DateUtils'];

    function HomeService ($http, DateUtils) {
    	var service ={};
    	
    	service.loaddata = function (callback){
    		
    		$http.get('api/dashboard/dashboarddata').then(function(response){
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