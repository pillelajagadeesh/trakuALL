(function() {
    'use strict';
    angular
        .module('trakeyeApp')
        .factory('Upload', Upload);

    Upload.$inject = ['$http'];

    function Upload ($http) {
       
        var service ={};
    	service.upload = function (data,callback){
    		
    		$http.post('api/upload',data,{
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
             }).then(function(response){
    			
        		callback(response,response.headers);
        	}, function(error){
        		callback(error, error.headers);
        	});
    	};
    	
    	 service.findAllFiles= function (callback){
    			$http.get('api/listfiles').then(function(response){
    	    	    callback(response.data);
    	    	});
    		};
    		
         service.findAllIosFiles= function (callback){
    				$http.get('api/listfiles-ios').then(function(response){
    		    	    callback(response.data);
    		    	});
         };		
    	return service;
    }
    
})();

