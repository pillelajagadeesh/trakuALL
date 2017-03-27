(function () {
    'use strict';

    angular
        .module('trakeyeApp')
        .factory('User', User);

    User.$inject = ['$resource'];

    function User ($resource) {
        var service = $resource('api/users/:login', {}, {
            'query': {method: 'GET', isArray: true},
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
})();
(function () {
    'use strict';

    angular
        .module('trakeyeApp')
        .factory('UserSearch', ['$rootScope','$http',function($rootScope,$http){

       var service ={};
	   service.getuserbyname = function (data,callback){
		$http.get('api/users-details/'+data).then(function(response){
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
        .factory('UserDetails', ['$rootScope','$http',function($rootScope,$http){

       var service ={};
	   service.getuserbystatus= function (data,callback){
		$http.get('api/user/userdetails/'+data).then(function(response){
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
        .factory('ActivatedUsers', ['$rootScope','$http',function($rootScope,$http){

       var service ={};
	   service.getactivatedusers = function (callback){
		$http.get('api/activateduserlist').then(function(response){
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
        .factory('UserValueSearch', UserValueSearch);

    UserValueSearch.$inject = ['$resource'];

    function UserValueSearch ($resource) {
    	var service = $resource('/api/users/searchvalue/:userId',{userId:'@id'},{},{
            'query': {method: 'GET',isArray: true},
        });

        return service;
    }
})();



(function () {
    'use strict';

    angular
        .module('trakeyeApp')
        .factory('UsersStatusService', UsersStatusService);

    UsersStatusService.$inject = ['$resource'];

    function UsersStatusService ($resource) {
        var service = $resource('api/userdetails/:status',{},{
            'query': {method: 'GET',isArray: true},
        });

        return service;
    }
})();

(function () {
    'use strict';

    angular
        .module('trakeyeApp')
        .factory('UserStatusSearch', UserStatusSearch);

    UserStatusSearch.$inject = ['$resource'];

    function UserStatusSearch ($resource) {
    	var service = $resource('/api/userdetails/statussearch/:status/:searchtext',{status:'@status',searchtext:'@searchtext'},{
            'query': {method: 'GET',isArray: true},
        });

        return service;
    }
})();

(function () {
    'use strict';

    angular
        .module('trakeyeApp')
        .factory('ActivatedUserSearch', ActivatedUserSearch);

    ActivatedUserSearch.$inject = ['$resource'];

    function ActivatedUserSearch ($resource) {
    	var service = $resource('/api/users/searchactivatedusers/:userId',{userId:'@id'},{},{
            'query': {method: 'GET',isArray: true},
        });

        return service;
    }
})();

