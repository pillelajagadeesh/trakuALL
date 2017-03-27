(function() {
    'use strict';
    angular
        .module('trakeyeApp')
        .factory('AssetType', AssetType);

    AssetType.$inject = ['$resource'];

    function AssetType ($resource) {
        var resourceUrl =  'api/asset-types/:id';

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
        .factory('AssetTypeSearch', AssetTypeSearch);

    AssetTypeSearch.$inject = ['$resource'];

    function AssetTypeSearch ($resource) {
        var service = $resource('/api/asset-types/searchvalue/:searchvalue',{searchvalue:'@id'},{},{
            'query': {method: 'GET',isArray: true},
        });

        return service;
    }
})();
