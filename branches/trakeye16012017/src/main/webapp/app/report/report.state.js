(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
		.state('report', {
            abstract: true,
            parent: 'app'
        })
        .state('generic-report', {
            parent: 'report',
            url: '/generic-report',
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
		
		.state('battery-report', {
            parent: 'report',
            url: '/battery-report',
            data: {
                authorities: ['ROLE_USER','ROLE_USER_ADMIN'],
                pageTitle: 'trakeyeApp.report.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/report/battery-report.html',
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
		.state('distance-travelled',{
			parent: 'report',
			url: '/distance-travelled',
			data: {
				authorities: ['ROLE_USER','ROLE_USER_ADMIN'],
                pageTitle: 'trakeyeApp.report.home.title'
			},
			views: {
				'content@':{ 
					templateUrl: 'app/report/distance-travelled.html',
					controller: 'ReportController',
                    controllerAs: 'vm'
				}
			}
		})
        
        
      
        
    }

})();
