(function() {
    'use strict';
    angular
        .module('trakeyeApp')
        .factory('ServiceType', ServiceType);

    ServiceType.$inject = ['$resource', 'DateUtils'];

    function ServiceType ($resource, DateUtils) {
        var resourceUrl =  'api/service-types/:id';

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
        .factory('ServiceTypeSearch', ServiceTypeSearch);

    ServiceTypeSearch.$inject = ['$resource'];

    function ServiceTypeSearch ($resource) {
        var service = $resource('/api/service-types/searchvalue/:userId',{userId:'@id'},{},{
            'query': {method: 'GET',isArray: true},
        });

        return service;
    }
})();