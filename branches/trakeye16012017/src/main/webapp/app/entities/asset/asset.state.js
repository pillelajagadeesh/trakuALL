(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('asset', {
            parent: 'app',
            url: '/asset?page&sort&search',
            data: {
                authorities: ['ROLE_USER_ADMIN','ROLE_USER'],
                pageTitle: 'trakeyeApp.asset.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/asset/assets.html',
                    controller: 'AssetController',
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
                    $translatePartialLoader.addPart('asset');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('asset-detail', {
            parent: 'asset',
            url: '/asset/{id}',
            data: {
                authorities: ['ROLE_USER_ADMIN','ROLE_USER'],
                pageTitle: 'trakeyeApp.asset.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/asset/asset-detail.html',
                    controller: 'AssetDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('asset');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Asset', function($stateParams, Asset) {
                    return Asset.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'asset',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        
        
        
        
        .state('asset-detail.edit', {
            parent: 'asset-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER_ADMIN','ROLE_USER'],
                pageTitle: 'trakeyeApp.asset.detail.title'
            },
            views: {
                'content@': {
                	templateUrl: 'app/entities/asset/asset-dialog.html',
                    controller: 'AssetDialogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('asset');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Asset', function($stateParams, Asset) {
                    return Asset.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'asset',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        
        
        
        .state('asset.new', {
            parent: 'asset',
            url: '/create',
            data: {
            	 authorities: ['ROLE_USER','ROLE_USER_ADMIN']
            },
			views:{
				'content@':{
					templateUrl: 'app/entities/asset/asset-dialog.html',
                    controller: 'AssetDialogController',
                    controllerAs: 'vm'
				}
			},
			 resolve: {
                        entity: function () {
                            return {
                                id: null,name: null,
                                description: null,
                                createDate: null,
                                updateDate: null,
                                userId   :null,
                                assetType:null,
                                assetTypeAttributeValues:[],
                                assetCoordinates:[]
                            };
                        }
                    }
            
        })
       
	   .state('asset.import', {
            parent: 'asset',
            url: '/import',
            data: {
            	 authorities: ['ROLE_USER','ROLE_USER_ADMIN']
            },
			views:{
				'content@':{
					templateUrl: 'app/entities/asset/asset-import.html',
                    controller: 'AssetDialogController',
                    controllerAs: 'vm'
				}
			},
			 resolve: {
                        entity: function () {
                            return {
                                id: null,name: null,
                                description: null,
                                createDate: null,
                                updateDate: null,
                                userId   :null,
                                assetType:null,
                                assetTypeAttributeValues:[],
                                assetCoordinates:[]
                            };
                        }
                    }
            
        })
	   
        .state('asset.edit', {
            parent: 'asset',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER_ADMIN','ROLE_USER'],
                pageTitle: 'trakeyeApp.asset.detail.title'
            },
            views: {
                'content@': {
                	templateUrl: 'app/entities/asset/asset-dialog.html',
                    controller: 'AssetDialogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('asset');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Asset', function($stateParams, Asset) {
                    return Asset.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'asset',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        
        
        .state('asset.delete', {
            parent: 'asset',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/asset/asset-delete-dialog.html',
                    controller: 'AssetDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Asset', function(Asset) {
                            return Asset.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('asset', null, { reload: 'asset' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
