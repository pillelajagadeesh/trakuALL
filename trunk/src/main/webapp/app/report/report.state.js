(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('report', {
            parent: 'app',
            url: '/report',
            data: {
                authorities: ['ROLE_USER','ROLE_USER_ADMIN'],
                pageTitle: 'trakeyeApp.report.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/report/report.html',
                    controller: 'ReportController',
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
