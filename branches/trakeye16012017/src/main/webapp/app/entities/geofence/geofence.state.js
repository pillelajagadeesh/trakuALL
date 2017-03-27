(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('geofence', {
            parent: 'entity',
            url: '/geofence?page&sort&search',
            data: {
                authorities: ['ROLE_USER_ADMIN'],
                pageTitle: 'trakeyeApp.geofence.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/geofence/geofences.html',
                    controller: 'GeofenceController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'modified_date,desc',
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
                    $translatePartialLoader.addPart('geofence');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('geofence-detail', {
            parent: 'entity',
            url: '/geofence/{id}',
            data: {
                authorities: ['ROLE_USER_ADMIN'],
                pageTitle: 'trakeyeApp.geofence.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/geofence/geofence-detail.html',
                    controller: 'GeofenceDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('geofence');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Geofence', function($stateParams, Geofence) {
                    return Geofence.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'geofence',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        
        .state('geofence.new', {
            parent: 'geofence',
            url: '/create',
            data: {
                authorities: ['ROLE_USER_ADMIN']
            },
			views:{
				'content@':{
					templateUrl: 'app/entities/geofence/geofence-dialog.html',
                    controller: 'GeofenceDialogController',
                    controllerAs: 'vm'
				}
			},
			resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                coordinates: null,
                                createdDate: null,
                                modifiedDate: null,
                                id: null
                            };
                        }
                    }
            
        })
		.state('geofence.edit', {
            parent: 'entity',
            url: '/geofence/edit/{id}',
            data: {
                authorities: ['ROLE_USER_ADMIN']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/geofence/geofence-dialog.html',
                    controller: 'GeofenceDialogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('geofence');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Geofence', function($stateParams, Geofence) {
                    return Geofence.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'geofence',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
		
       
        .state('geofence.delete', {
            parent: 'geofence',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/geofence/geofence-delete-dialog.html',
                    controller: 'GeofenceDeleteController',
                    controllerAs: 'vm',
                    size: 'sm',
                    resolve: {
                        entity: ['Geofence', function(Geofence) {
                            return Geofence.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('geofence', null, { reload: 'geofence' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
