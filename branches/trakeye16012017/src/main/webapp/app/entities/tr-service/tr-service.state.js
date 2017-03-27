(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tr-service', {
            parent: 'app',
            url: '/tr-service',
            data: {
                authorities: ['ROLE_USER_ADMIN','ROLE_USER'],
                pageTitle: 'trakeyeApp.trService.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tr-service/tr-services.html',
                    controller: 'TrServiceController',
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
                    $translatePartialLoader.addPart('trService');
                    $translatePartialLoader.addPart('serviceStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('tr-service-detail', {
            parent: 'tr-service',
            url: '/tr-service/{id}',
            data: {
                authorities: ['ROLE_USER_ADMIN','ROLE_USER'],
                pageTitle: 'trakeyeApp.trService.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tr-service/tr-service-detail.html',
                    controller: 'TrServiceDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('trService');
                    $translatePartialLoader.addPart('serviceStatus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TrService', function($stateParams, TrService) {
                    return TrService.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'tr-service',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
       
        .state('tr-service.new', {
            parent: 'tr-service',
            url: '/create',
            data: {
                authorities: ['ROLE_USER_ADMIN']
            },
          views :{
        	  'content@':{
        		  templateUrl: 'app/entities/tr-service/tr-service-dialog.html',
                  controller: 'TrServiceDialogController',
                  controllerAs: 'vm'
        	  }
          },
            resolve: {
                entity: function () {
                    return {
                        createdDate: null,
                        modifiedDate: null,
                        description: null,
                        serviceDate: null,
                        status: null,
                        notes: null,
                        id: null,
                        serviceTypeAttributeValues:[],
                        serviceImages:[]
                      
                    };
                }
            }              
            
        })
        
        .state('tr-service.edit', {
            parent: 'tr-service',
            url: '/Edit/{id}',
            data: {
                authorities: ['ROLE_USER_ADMIN','ROLE_USER'],
                pageTitle: 'trakeyeApp.trService.detail.title'
            },
            views: {
                'content@': {
                	 templateUrl: 'app/entities/tr-service/tr-service-dialog.html',
                     controller: 'TrServiceDialogController',
                     controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('trService');
                    $translatePartialLoader.addPart('serviceStatus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TrService', function($stateParams, TrService) {
                    return TrService.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'tr-service',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        
        
        .state('tr-service.delete', {
            parent: 'tr-service',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tr-service/tr-service-delete-dialog.html',
                    controller: 'TrServiceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                   
                    resolve: {
                        entity: ['TrService', function(TrService) {
                            return TrService.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tr-service', null, { reload: 'tr-service' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
