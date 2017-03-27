(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('trakeye-type', {
            parent: 'entity',
            url: '/trakeye-type',
            data: {
                authorities: ['ROLE_USER_ADMIN'],
                pageTitle: 'trakeyeApp.trakeyeType.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/trakeye-type/trakeye-types.html',
                    controller: 'TrakeyeTypeController',
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
                    $translatePartialLoader.addPart('trakeyeType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('trakeye-type-detail', {
            parent: 'entity',
            url: '/trakeye-type/{id}',
            data: {
                authorities: ['ROLE_USER_ADMIN'],
                pageTitle: 'trakeyeApp.trakeyeType.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/trakeye-type/trakeye-type-detail.html',
                    controller: 'TrakeyeTypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('trakeyeType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TrakeyeType', function($stateParams, TrakeyeType) {
                    return TrakeyeType.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'trakeye-type',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
       
        .state('trakeye-type.new', {
            parent: 'trakeye-type',
            url: '/create',
            data: {
                authorities: ['ROLE_USER_ADMIN']
            },
            views:{
            	'content@':{
            		 templateUrl: 'app/entities/trakeye-type/trakeye-type-dialog.html',
                     controller: 'TrakeyeTypeDialogController',
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
        .state('trakeye-type.edit', {
            parent: 'entity',
            url: '/trakeye-type/Edit/{id}',
            data: {
                authorities: ['ROLE_USER_ADMIN'],
                pageTitle: 'trakeyeApp.trakeyeType.detail.title'
            },
            views: {
                'content@': {
                	templateUrl: 'app/entities/trakeye-type/trakeye-type-dialog.html',
                    controller: 'TrakeyeTypeDialogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('trakeyeType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TrakeyeType', function($stateParams, TrakeyeType) {
                    return TrakeyeType.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'trakeye-type',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        
        .state('trakeye-type.delete', {
            parent: 'trakeye-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/trakeye-type/trakeye-type-delete-dialog.html',
                    controller: 'TrakeyeTypeDeleteController',
                    controllerAs: 'vm',
                    size: 'sm',
                    resolve: {
                        entity: ['TrakeyeType', function(TrakeyeType) {
                            return TrakeyeType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('trakeye-type', null, { reload: 'trakeye-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
