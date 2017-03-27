(function() {
    'use strict';
    angular
        .module('trakeyeApp')
        .factory('Asset', Asset);

    Asset.$inject = ['$resource'];

    function Asset ($resource) {
        var resourceUrl =  'api/assets/:id';

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

(function () {
    'use strict';

    angular
        .module('trakeyeApp')
        .factory('AssetSearch', AssetSearch);

    AssetSearch.$inject = ['$resource'];

    function AssetSearch ($resource) {
        var service = $resource('/api/assets/searchvalue/:searchvalue',{searchvalue:'@id'},{},{
            'query': {method: 'GET',isArray: true},
        });

        return service;
    }
    
    
})();

(function () {
    'use strict';

    angular
        .module('trakeyeApp')
        .factory('AssetSearchForMap', ['$rootScope','$http',function($rootScope,$http){

       var service ={};
	   service.searchassetsformap = function (search,callback){
			$http.get('api/assets/searchformap/'+search).then(function(response){
	    	    callback(response.data);
	    	});
		   };
		   
	   
				 
	return service;
    }]);
})();




(function () {
    'use strict';

    angular
        .module('trakeyeApp')
        .factory('AssetUser', ['$rootScope','$http',function($rootScope,$http){

       var service ={};
	   service.gettrcasesuser = function (callback){
		$http.get('api/location-logs/latest').then(function(response){
    	    callback(response.data);
    	});
	   };
	 			 
	return service;
    }]);
})();

(function () {
    'use strict';

    angular
        .module('trakeyeApp')
        .factory('AssetsForMap', ['$rootScope','$http',function($rootScope,$http){

       var service ={};
	   service.assetsformap = function (callback){
		$http.get('api/assetsformap').then(function(response){
    	    callback(response.data);
    	});
	   };
	 			 
	return service;
    }]);
})();


/*(function () {
    'use strict';

    angular
        .module('trakeyeApp')
        .factory('AssetUpload', ['$rootScope','$http',function($rootScope,$http){

       var service ={};
	   
	   service.assetupload = function (assetname,callback){
		        $http.post('api/assetsimport/'+assetname,{
	                  
	             }).then(function(response){
	    			
	        		callback(response,response.headers);
	        	}, function(error){
	        		
	        		callback(error, error.headers);
	        	});
	    
		   };
		   
	 		 
	return service;
    }]);
})();*/

(function() {
    'use strict';
    angular
        .module('trakeyeApp')
        .factory('AssetUpload', AssetUpload);

    AssetUpload.$inject = ['$http'];

    function AssetUpload ($http) {
       
        var service ={};
    	service.assetupload = function (data,callback){
    		
    		$http.post('api/assetsimport',data,{
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
             }).then(function(response){
    			
        		callback(response,response.headers);
        	}, function(error){
        		callback(error, error.headers);
        	});
    	};
    	
    	 	
    	return service;
    }
    
})();

/*(function () {
    'use strict';

    angular
        .module('trakeyeApp')
        .factory('AssetTypeList', AssetTypeList);

    AssetTypeList.$inject = ['$resource'];

    function AssetTypeList ($resource) {
        var service = $resource('/api/asset-typelist',{},{
            'query': {method: 'GET',isArray: true},
        });

        return service;
    }
    
    
})();*/
