(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('asset-type', {
            parent: 'entity',
            url: '/asset-type?page&sort&search',
            data: {
                authorities: ['ROLE_USER_ADMIN','ROLE_USER'],
                pageTitle: 'trakeyeApp.assetType.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/asset-type/asset-types.html',
                    controller: 'AssetTypeController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'update_date,desc',
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
                    $translatePartialLoader.addPart('assetType');
                    $translatePartialLoader.addPart('layout');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('asset-type-detail', {
            parent: 'entity',
            url: '/asset-type/{id}',
            data: {
                authorities: ['ROLE_USER_ADMIN'],
                pageTitle: 'trakeyeApp.assetType.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/asset-type/asset-type-detail.html',
                    controller: 'AssetTypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('assetType');
                    $translatePartialLoader.addPart('layout');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'AssetType', function($stateParams, AssetType) {
                    return AssetType.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'asset-type',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        
        
        .state('asset-type-detail.edit', {
            parent: 'asset-type-detail',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER_ADMIN'],
                pageTitle: 'trakeyeApp.assetType.detail.title'
            },
            views: {
                'content@': {
                	templateUrl: 'app/entities/asset-type/asset-type-dialog.html',
                    controller: 'AssetTypeDialogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('assetType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'AssetType', function($stateParams, AssetType) {
                    return AssetType.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'asset-type',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        
       
        
        .state('asset-type.new', {
            parent: 'asset-type',
            url: '/new',
            data: {
            	 authorities: ['ROLE_USER_ADMIN']
            },
			views:{
				'content@':{
					 templateUrl: 'app/entities/asset-type/asset-type-dialog.html',
	                    controller: 'AssetTypeDialogController',
	                    controllerAs: 'vm',
				}
			},
			 resolve: {
                        entity: function () {
                            return {
                            	name: null,
                                description: null,
                                layout: null,
                                colorcode: null,
                                createDate: null,
                                updateDate: null,
                                id: null
                            };
                        }
                    }
            
        })
        
        .state('asset-type.edit', {
            parent: 'asset-type',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER_ADMIN'],
                pageTitle: 'trakeyeApp.assetType.detail.title'
            },
            views: {
                'content@': {
                	templateUrl: 'app/entities/asset-type/asset-type-dialog.html',
                    controller: 'AssetTypeDialogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('assetType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'AssetType', function($stateParams, AssetType) {
                    return AssetType.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'asset-type',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        
        
        .state('asset-type.delete', {
            parent: 'asset-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/asset-type/asset-type-delete-dialog.html',
                    controller: 'AssetTypeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AssetType', function(AssetType) {
                            return AssetType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('asset-type', null, { reload: 'asset-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
