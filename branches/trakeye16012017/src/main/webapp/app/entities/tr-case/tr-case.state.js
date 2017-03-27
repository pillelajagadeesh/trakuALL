(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tr-case', {
            parent: 'app',
            url: '/tr-case',
            data: {
                authorities: ['ROLE_USER_ADMIN','ROLE_USER'],
                pageTitle: 'trakeyeApp.trCase.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tr-case/tr-cases.html',
                    controller: 'TrCaseController',
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
                    $translatePartialLoader.addPart('trCase');
                    $translatePartialLoader.addPart('caseStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('tr-case-detail', {
            parent: 'tr-case',
            url: '/tr-case/{id}',
            data: {
                authorities: ['ROLE_USER_ADMIN','ROLE_USER'],
                pageTitle: 'trakeyeApp.trCase.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tr-case/tr-case-detail.html',
                    controller: 'TrCaseDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('trCase');
                    $translatePartialLoader.addPart('caseStatus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TrCase', function($stateParams, TrCase) {
                    return TrCase.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'tr-case',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('tr-case.priority', {
            parent: 'tr-case',
            url: '/tr-case/{priority}',
            data: {
                authorities: ['ROLE_USER_ADMIN','ROLE_USER'],
                pageTitle: 'trakeyeApp.trCase.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tr-case/tr-cases.html',
                    controller: 'TrCaseController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('trCase');
                    $translatePartialLoader.addPart('caseStatus');
                    return $translate.refresh();
                }]
            }
        })
        
        .state('tr-case.new', {
            parent: 'tr-case',
            url: '/create',
            data: {
                authorities: ['ROLE_USER_ADMIN','ROLE_USER']
            },
            views: {
                'content@': {
                	templateUrl: 'app/entities/tr-case/tr-case-dialog.html',
                    controller: 'TrCaseDialogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: function () {
                    return {
                        description: null,
                        createDate: null,
                        updateDate: null,
                        pinLat: null,
                        pinLong: null,
                        address: null,
                        escalated: null,
                        status: null,
                        id: null,
                        caseImages:[]
                    };
                }
            }
           
        })
        .state('tr-case.import', {
            parent: 'tr-case',
            url: '/import',
            data: {
            	 authorities: ['ROLE_USER','ROLE_USER_ADMIN']
            },
			views:{
				'content@':{
					templateUrl: 'app/entities/tr-case/tr-case-import.html',
                    controller: 'TrCaseDialogController',
                    controllerAs: 'vm'
				}
			},
			 resolve: {
                        entity: function () {
                            return {
                            	 description: null,
                                 createDate: null,
                                 updateDate: null,
                                 pinLat: null,
                                 pinLong: null,
                                 address: null,
                                 escalated: null,
                                 status: null,
                                 id: null,
                                 caseImages:[]
                            };
                        }
                    }
            
        })
        .state('tr-case.edit', {
            parent: 'tr-case',
            url: '/Edit/{id}',
            data: {
                authorities: ['ROLE_USER_ADMIN','ROLE_USER'],
                pageTitle: 'trakeyeApp.trCase.detail.title'
            },
            views: {
                'content@': {
                	templateUrl: 'app/entities/tr-case/tr-case-dialog.html',
                    controller: 'TrCaseDialogController',
                    controllerAs: 'vm',
                }
            },
            resolve: {
            	
            	entity: ['$stateParams', 'TrCase', function($stateParams, TrCase) {
                    return TrCase.get({id : $stateParams.id}).$promise;
                }],
              
                
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'tr-case',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
       
        .state('tr-case.delete', {
            parent: 'tr-case',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tr-case/tr-case-delete-dialog.html',
                    controller: 'TrCaseDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TrCase', function(TrCase) {
                            return TrCase.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tr-case', null, { reload: 'tr-case' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
