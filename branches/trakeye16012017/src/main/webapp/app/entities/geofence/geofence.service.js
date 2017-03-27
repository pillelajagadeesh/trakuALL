(function() {
    'use strict';
    angular
        .module('trakeyeApp')
        .factory('Geofence', Geofence);

    Geofence.$inject = ['$resource', 'DateUtils'];

    function Geofence ($resource, DateUtils) {
        var resourceUrl =  'api/geofences/:id';

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
        .factory('GeofenceSearch', GeofenceSearch);

    GeofenceSearch.$inject = ['$resource'];

    function GeofenceSearch ($resource) {
        var service = $resource('/api/geofences/searchvalue/:userId',{userId:'@id'},{},{
            'query': {method: 'GET',isArray: true},
        });

        return service;
    }
})();
