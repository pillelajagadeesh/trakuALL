(function() {
    'use strict';
    angular
        .module('trakeyeApp')
        .factory('TrakeyeType', TrakeyeType);

    TrakeyeType.$inject = ['$resource', 'DateUtils'];

    function TrakeyeType ($resource, DateUtils) {
        var resourceUrl =  'api/trakeye-types/:id';

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
        .factory('TrakeyeTypeSearch', TrakeyeTypeSearch);

    TrakeyeTypeSearch.$inject = ['$resource'];

    function TrakeyeTypeSearch ($resource) {
        var service = $resource('/api/trakeye-types/searchvalue/:userId',{userId:'@id'},{},{
            'query': {method: 'GET',isArray: true},
        });

        return service;
    }
})();