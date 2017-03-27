(function() {
    'use strict';
    angular
        .module('trakeyeApp')
        .factory('LocationLog', LocationLog);

    LocationLog.$inject = ['$resource', 'DateUtils'];

    function LocationLog ($resource, DateUtils) {
        var resourceUrl =  'api/location-logs/:id';

        return $resource(resourceUrl, {}, {
        'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                      
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
        
        
        
    }
    
    
    
})();


(function() {
    'use strict';
    angular
        .module('trakeyeApp')
        .factory('LocationLogMapPath', ['$rootScope','$http',function($rootScope,$http){
        	var service ={};
        	
        	service.getmaplogs = function (data,callback){
        		
        		$http.get('api/location-logs/userpath/'+data.userId+'/'+data.fromDate+'/'+data.toDate).then(function(response){
            		callback(response.data);
            	});
        	};
        	service.locateUsers = function (callback){
        		$http.get('api/dashboard/agentdashboard').then(function(response){
        			callback(response.data);
            	});
        	};
        	
           /* service.locateInActiveUsers = function (callback){
            	$http.get('api/location-logs/latestinactive').then(function(response){
        			callback(response.data);
            	});
        	};*/
        	
        	return service;
        }]);

    
    
})();

(function() {
    'use strict';
    angular
        .module('trakeyeApp')
        .factory('LocationLogMap', ['$rootScope','$http',function($rootScope,$http){
        	var service ={};
        	
        	service.getlogsbydate = function (data,callback){
        		
        		$http.get('api/location-logs/'+data.fromDate+'/'+data.toDate).then(function(response){
            		callback(response.data,response.headers);
            	});
        	};
        	
        	return service;
        }]);

    
    
})();










