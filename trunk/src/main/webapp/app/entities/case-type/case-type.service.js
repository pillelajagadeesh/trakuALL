(function() {
    'use strict';
    angular
        .module('trakeyeApp')
        .factory('CaseType', CaseType);

    CaseType.$inject = ['$resource', 'DateUtils'];

    function CaseType ($resource, DateUtils) {
        var resourceUrl =  'api/case-types/:id';

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
        .factory('CaseTypeSearch', ['$rootScope','$http',function($rootScope,$http){

       var service ={};
	   service.getcasetypebyname = function (data,callback){
		$http.get('api/case-types-details/'+data).then(function(response){
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
        .factory('CaseTypeSearch', CaseTypeSearch);

    CaseTypeSearch.$inject = ['$resource'];

    function CaseTypeSearch ($resource) {
        var service = $resource('/api/case-types/searchvalue/:userId',{userId:'@id'},{},{
            'query': {method: 'GET',isArray: true},
        });

        return service;
    }
})();