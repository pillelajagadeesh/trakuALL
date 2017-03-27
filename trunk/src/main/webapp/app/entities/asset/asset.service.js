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
