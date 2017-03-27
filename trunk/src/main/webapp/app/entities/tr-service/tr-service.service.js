(function() {
    'use strict';
    angular
        .module('trakeyeApp')
        .factory('TrService', TrService);

    TrService.$inject = ['$resource', 'DateUtils'];

    function TrService ($resource, DateUtils) {
        var resourceUrl =  'api/tr-services/:id';

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
        .factory('TrServiceSearch', TrServiceSearch);

    TrServiceSearch.$inject = ['$resource'];
    
    function TrServiceSearch ($resource) {
        var service = $resource('/api/tr-services/searchvalue/:userId',{userId:'@id'},{},{
            'query': {method: 'GET',isArray: true},
        });

        return service;
    }
})();
