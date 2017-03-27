(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('location-log', {
            parent: 'app',
            url: '/live-monitoring?page&sort&search',
            data: {
                authorities: ['ROLE_USER','ROLE_USER_ADMIN'],
                pageTitle: 'trakeyeApp.locationLog.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/location-log/location-logs.html',
                    controller: 'LocationLogController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
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
                    $translatePartialLoader.addPart('locationLog');
                    $translatePartialLoader.addPart('logSource');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('location-log.login', {
            parent: 'app',
            url: '/live-monitoring/{login}?page&sort&search',
            data: {
                authorities: ['ROLE_USER','ROLE_USER_ADMIN'],
                pageTitle: 'trakeyeApp.locationLog.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/location-log/location-logs.html',
                    controller: 'LocationLogController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
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
                    $translatePartialLoader.addPart('locationLog');
                    $translatePartialLoader.addPart('logSource');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('location-log-detail', {
            parent: 'entity',
            url: '/location-log/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'trakeyeApp.locationLog.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/location-log/location-log-detail.html',
                    controller: 'LocationLogDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('locationLog');
                    $translatePartialLoader.addPart('logSource');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'LocationLog', function($stateParams, LocationLog) {
                    return LocationLog.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'location-log',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('location-log-detail.edit', {
            parent: 'location-log-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/location-log/location-log-dialog.html',
                    controller: 'LocationLogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    //size: 'lg',
                    resolve: {
                        entity: ['LocationLog', function(LocationLog) {
                            return LocationLog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('location-log.new', {
            parent: 'location-log',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/location-log/location-log-dialog.html',
                    controller: 'LocationLogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                   // size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                latitude: null,
                                longitude: null,
                                address: null,
                                logSource: null,
                                createdDateTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('location-log', null, { reload: 'location-log' });
                }, function() {
                    $state.go('location-log');
                });
            }]
        })
        .state('location-log.edit', {
            parent: 'location-log',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/location-log/location-log-dialog.html',
                    controller: 'LocationLogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                   // size: 'lg',
                    resolve: {
                        entity: ['LocationLog', function(LocationLog) {
                            return LocationLog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('location-log', null, { reload: 'location-log' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        
         
        
        
        .state('location-log.delete', {
            parent: 'location-log',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/location-log/location-log-delete-dialog.html',
                    controller: 'LocationLogDeleteController',
                    controllerAs: 'vm',
                    //size: 'md',
                    resolve: {
                        entity: ['LocationLog', function(LocationLog) {
                            return LocationLog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('location-log', null, { reload: 'location-log' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
