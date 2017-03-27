(function() {
    'use strict';
    angular
        .module('trakeyeApp')
        .factory('Tenant', Tenant);

    Tenant.$inject = ['$resource'];

    function Tenant ($resource) {
        var resourceUrl =  'api/tenants/:id';

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
        .factory('TenantSearch', TenantSearch);

    TenantSearch.$inject = ['$resource'];

    function TenantSearch ($resource) {
        var service = $resource('/api/tenants/searchvalue/:searchvalue',{searchvalue:'@id'},{},{
            'query': {method: 'GET',isArray: true},
        });

        return service;
    }
})();
