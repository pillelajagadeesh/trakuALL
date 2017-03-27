(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('case-type', {
            parent: 'entity',
            url: '/case-type',
            data: {
                authorities: ['ROLE_USER_ADMIN'],
                pageTitle: 'trakeyeApp.caseType.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/case-type/case-types.html',
                    controller: 'CaseTypeController',
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
                    $translatePartialLoader.addPart('caseType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('case-type-detail', {
            parent: 'case-type',
            url: '/case-type/{id}',
            data: {
                authorities: ['ROLE_USER_ADMIN'],
                pageTitle: 'trakeyeApp.caseType.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/case-type/case-type-detail.html',
                    controller: 'CaseTypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('caseType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CaseType', function($stateParams, CaseType) {
                    return CaseType.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'case-type',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        
        .state('case-type.new', {
            parent: 'case-type',
            url: '/create',
            data: {
                authorities: ['ROLE_USER_ADMIN']
            },
			views:{
				'content@':{
					templateUrl: 'app/entities/case-type/case-type-dialog.html',
                    controller: 'CaseTypeDialogController',
                    controllerAs: 'vm'
				}
			},
			resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                createdDate: null,
                                updateDate: null,
                                id: null
                            };
                        }
                    }
        })
		.state('case-type.edit', {
            parent: 'case-type',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER_ADMIN'],
                pageTitle: 'trakeyeApp.caseType.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/case-type/case-type-dialog.html',
                    controller: 'CaseTypeDialogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('caseType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CaseType', function($stateParams, CaseType) {
                    return CaseType.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'case-type',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
      
        .state('case-type.delete', {
            parent: 'case-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/case-type/case-type-delete-dialog.html',
                    controller: 'CaseTypeDeleteController',
                    controllerAs: 'vm',
                    size: 'sm',
                    resolve: {
                        entity: ['CaseType', function(CaseType) {
                            return CaseType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('case-type', null, { reload: 'case-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
