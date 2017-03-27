(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('customer-management', {
            parent: 'admin',
            //url: '/customer-management?page&sort$search',
            url: '/customer-management',
            data: {
            	authorities: [],
                pageTitle: 'customerManagement.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/admin/customer-management/customer-management.html',
                    controller: 'CustomerManagementController',
                    controllerAs: 'vm'
                }
            },            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                }
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort)
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('customer-management');
                    return $translate.refresh();
                }]

            }       
			})
    
    .state('customer-management.edit', {
        parent: 'customer-management',
        url: '/{id}/edit',
        data: {
        	 //authorities: ['ROLE_SUPER_ADMIN','ROLE_USER_ADMIN']
        },
		views:{
			'content@':{
				templateUrl: 'app/admin/customer-management/customer-management-dialog.html',
                controller: 'CustomerDetails',
                controllerAs: 'vm'
			}
		},
		resolve: {
            translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                $translatePartialLoader.addPart('customer-management');
                return $translate.refresh();
            }],
			entity: ['$stateParams', 'CustomerDetails', function($stateParams, CustomerDetails) {
                return CustomerDetails.get({id : $stateParams.id}).$promise;
            }],
        }		        
    })
    
    .state('customer-management-detail', {
        parent: 'admin',
        url: '/customer/:id',
        data: {
        	 //authorities: ['ROLE_SUPER_ADMIN','ROLE_USER_ADMIN'],
            pageTitle: 'customer-management.detail.title'
        },
        views: {
            'content@': {
                templateUrl: 'app/admin/customer-management/customer-management-dialog.html',
                controller: 'CustomerManagementDetailController',
                controllerAs: 'vm'
            }
        },
        resolve: {
            translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                $translatePartialLoader.addPart('customer-management');
                return $translate.refresh();
            }]
        }
    });
    }
})();
