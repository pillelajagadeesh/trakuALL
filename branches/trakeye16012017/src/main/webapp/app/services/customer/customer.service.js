(function () {
    'use strict';

    angular
        .module('trakeyeApp')
        .factory('Customer', Customer);

    Customer.$inject = ['$resource'];

    function Customer ($resource) {
        var service = $resource('api/customers', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            }
        });

        return service;
    }
})();

(function () {
    'use strict';

    angular
        .module('trakeyeApp')
        .factory('CustomerValueSearch', CustomerValueSearch);

    CustomerValueSearch.$inject = ['$resource'];

    function CustomerValueSearch ($resource) {
    	var service = $resource('/api/customers/searchvalue/:customerId',{customerId:'@id'},{},{
            'query': {method: 'GET',isArray: true},
        });

        return service;
    }
})();

(function () {
    'use strict';

    angular
        .module('trakeyeApp')
        .factory('CustomerDetails', CustomerDetails);

    CustomerDetails.$inject = ['$resource'];

    function CustomerDetails ($resource) {
    	var service = $resource('/api/customer/:customerId',{customerId:'@id'},{},{
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'save': { method:'POST' },
            'update': { method:'PUT' },
            'delete':{ method:'DELETE'}
        });

        return service;
    }
}) ();

(function () {
    'use strict';

    angular
        .module('trakeyeApp')
        .factory('CustomerUpdate', CustomerUpdate);

    CustomerUpdate.$inject = ['$resource'];

    function CustomerUpdate ($resource) {
    	var service = $resource('/api/customer',{},{
            'update': { method:'PUT' },
            'save': { method:'POST' },
        });

        return service;
    }
})();

