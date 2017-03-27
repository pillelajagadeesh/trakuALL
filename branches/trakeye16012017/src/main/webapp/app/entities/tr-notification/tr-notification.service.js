(function() {
    'use strict';
    angular
        .module('trakeyeApp')
        .factory('TrNotification', TrNotification);

    TrNotification.$inject = ['$resource', 'DateUtils'];

    function TrNotification ($resource, DateUtils) {
        var resourceUrl =  'api/tr-notifications/:id';

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
        .factory('TrNotificationSearch', TrNotificationSearch);

    TrNotificationSearch.$inject = ['$resource'];

    function TrNotificationSearch ($resource) {
        var service = $resource('/api/tr-notifications/searchvalue/:userId',{userId:'@id'},{},{
            'query': {method: 'GET',isArray: true},
        });

        return service;
    }
})();