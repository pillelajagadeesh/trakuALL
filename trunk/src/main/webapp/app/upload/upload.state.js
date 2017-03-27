(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('upload', {
            parent: 'app',
            url: '/upload',
            data: {
                authorities: ['ROLE_SUPER_ADMIN','ROLE_USER_ADMIN'],
                pageTitle: 'trakeyeApp.upload.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/upload/uploadagent.html',
                    controller: 'UploadController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            	translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('locationLog');
                    $translatePartialLoader.addPart('logSource');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
           
           
        })
        
        
      
        
    }

})();
