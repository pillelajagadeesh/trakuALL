(function() {
    'use strict';
    angular
        .module('trakeyeApp')
        .factory('Report', ['$rootScope','$http',function($rootScope,$http){
        	var service ={};
        	
        	service.getUsers = function (name,callback){
        		
        		$http.get('api/userslist').then(function(response){
            		callback(response.data);
            	});
        	};
        	service.getGeofences = function (name,callback){
        		
        		$http.get('api/geofenceslist').then(function(response){
            		callback(response.data);
            	});
        	};
        	service.getReport = function (data,callback){
        		
        		$http.post('api/reports/view',data).then(function(response){
            		callback(response.data);
            	});
        	};
			service.getDetailedReport = function (data,callback){
        		
        		$http.post('api/reports/detailed-report',data).then(function(response){
            		callback(response.data);
            	},function(error){
            		callback({counts:{}});
            	})
        	};
        	
        	return service;
        }]);

    
    
})();