(function() {
    'use strict';
    angular
        .module('trakeyeApp')
        .factory('TrCase', TrCase);

    TrCase.$inject = ['$resource', 'DateUtils'];

    function TrCase ($resource, DateUtils) {
        var resourceUrl =  'api/tr-cases/:id';

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
        .factory('TrCasePriority', TrCasePriority);

    TrCasePriority.$inject = ['$resource'];

    function TrCasePriority ($resource) {
        var service = $resource('/api/tr-cases/priority/:priority',{},{
            'query': {method: 'GET',isArray: true},
        });

        return service;
    }
})();




(function () {
    'use strict';

    angular
        .module('trakeyeApp')
        .factory('TrCaseSearch', TrCaseSearch);

    TrCaseSearch.$inject = ['$resource'];

    function TrCaseSearch ($resource) {
        var service = $resource('/api/tr-cases/searchvalue/:userId',{userId:'@id'},{},{
            'query': {method: 'GET',isArray: true},
        });

        return service;
    }
    
    
})();

(function () {
    'use strict';

    angular
        .module('trakeyeApp')
        .factory('TrCasePrioritySearch', TrCasePrioritySearch);

    TrCasePrioritySearch.$inject = ['$resource'];

    function TrCasePrioritySearch ($resource) {
    	var service = $resource('/api/tr-cases/prioritysearch/:priority/:searchtext',{priority:'@priorirty',searchtext:'@searchtext'},{
            'query': {method: 'GET',isArray: true},
        });

        return service;
    }
})();

(function () {
    'use strict';

    angular
        .module('trakeyeApp')
        .factory('TrCaseUser', ['$rootScope','$http',function($rootScope,$http){

       var service ={};
	   service.gettrcasesuser = function (callback){
		$http.get('api/tr-cases/live-logs').then(function(response){
    	    callback(response.data);
    	});
	   };
	   
	   service.gettrcasesuserbyid = function (id,callback){
			$http.get('api/tr-cases/live-logs/'+id).then(function(response){
	    	    callback(response.data);
	    	});
		   };
		   
	   service.searchtrcasesuserbyvalue = function (id,callback){
				$http.get('api/tr-cases/search/'+id).then(function(response){
		    	    callback(response.data);
		    	});
			   };
	    service.gettrcasesanduserbypriority = function (priority,callback){
					$http.get('api/tr-cases/live-logs/priority/'+priority).then(function(response){
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
        .factory('TrCaseEdit', ['$rootScope','$http',function($rootScope,$http){

       var service ={};
	   service.getalltrcases = function (callback){
		$http.get('api/tr-allcases').then(function(response){
    	    callback(response.data);
    	});
	   };
	   
	   service.edittrcase = function (data,callback){
		        $http.post('api/tr-caseedit/'+data.userId+'/'+data.caseId,{
	                  
	             }).then(function(response){
	    			
	        		callback(response,response.headers);
	        	}, function(error){
	        		
	        		callback(error, error.headers);
	        	});
	    
			/*$http.post('api/tr-caseedit/'+data.userId+'/'+data.caseId).then(function(response){
	    	    callback(response.data);
	    	});*/
		   };
		   
	 		 
	return service;
    }]);
})();

(function() {
    'use strict';
    angular
        .module('trakeyeApp')
        .factory('TrCaseUpload', TrCaseUpload);

    TrCaseUpload.$inject = ['$http'];

    function TrCaseUpload ($http) {
       
        var service ={};
    	service.trcaseupload = function (data,callback){
    		
    		$http.post('api/caseimport',data,{
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
             }).then(function(response){
    			
        		callback(response,response.headers);
        	}, function(error){
        		callback(error, error.headers);
        	});
    	};
    	
    	 	
    	return service;
    }
    
})();

/*(function () {
    'use strict';

    angular
        .module('trakeyeApp')
        .factory('CaseTypeList', CaseTypeList);

    CaseTypeList.$inject = ['$resource'];

    function CaseTypeList ($resource) {
        var service = $resource('/api/case-typelist',{},{
            'query': {method: 'GET',isArray: true},
        });

        return service;
    }
    
    
})();*/