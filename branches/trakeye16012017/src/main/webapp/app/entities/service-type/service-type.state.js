(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('service-type', {
            parent: 'entity',
            url: '/service-type',
            data: {
                authorities: ['ROLE_USER_ADMIN'],
                pageTitle: 'trakeyeApp.serviceType.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/service-type/service-types.html',
                    controller: 'ServiceTypeController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'updated_date,desc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('serviceType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('service-type-detail', {
            parent: 'entity',
            url: '/service-type/{id}',
            data: {
                authorities: ['ROLE_USER_ADMIN'],
                pageTitle: 'trakeyeApp.serviceType.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/service-type/service-type-detail.html',
                    controller: 'ServiceTypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('serviceType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ServiceType', function($stateParams, ServiceType) {
                    return ServiceType.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'service-type',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('service-type.new', {
            parent: 'service-type',
            url: '/create',
            data: {
                authorities: ['ROLE_USER_ADMIN']
            },
			views:{
				'content@':{
					templateUrl: 'app/entities/service-type/service-type-dialog.html',
                    controller: 'ServiceTypeDialogController',
                    controllerAs: 'vm'
				}
			},
			resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                createdDate: null,
                                updatedDate: null,
                                id: null
                            };
                        }
                    }            
        })
		.state('service-type.edit', {
            parent: 'entity',
            url: '/service-type/Edit/{id}',
            data: {
                authorities: ['ROLE_USER_ADMIN']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/service-type/service-type-dialog.html',
                    controller: 'ServiceTypeDialogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('serviceType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ServiceType', function($stateParams, ServiceType) {
                    return ServiceType.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'service-type',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        
        .state('service-type.delete', {
            parent: 'service-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service-type/service-type-delete-dialog.html',
                    controller: 'ServiceTypeDeleteController',
                    controllerAs: 'vm',
                    size: 'sm',
                    resolve: {
                        entity: ['ServiceType', function(ServiceType) {
                            return ServiceType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('service-type', null, { reload: 'service-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
