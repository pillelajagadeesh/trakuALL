(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tr-notification', {
            parent: 'app',
            url: '/tr-notification?page&sort&search',
            data: {
                authorities: ['ROLE_USER_ADMIN','ROLE_USER'],
                pageTitle: 'trakeyeApp.trNotification.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tr-notification/tr-notifications.html',
                    controller: 'TrNotificationController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'created_date,desc',
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
                    $translatePartialLoader.addPart('trNotification');
                    $translatePartialLoader.addPart('notificationStatus');
                    $translatePartialLoader.addPart('alertType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('tr-notification-detail', {
            parent: 'tr-notification',
            url: '/tr-notification/{id}',
            data: {
                authorities: ['ROLE_USER_ADMIN','ROLE_USER'],
                pageTitle: 'trakeyeApp.trNotification.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tr-notification/tr-notification-detail.html',
                    controller: 'TrNotificationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('trNotification');
                    $translatePartialLoader.addPart('notificationStatus');
                    $translatePartialLoader.addPart('alertType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TrNotification', function($stateParams, TrNotification) {
                    return TrNotification.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'tr-notification',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('tr-notification-detail.edit', {
            parent: 'tr-notification-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tr-notification/tr-notification-dialog.html',
                    controller: 'TrNotificationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    //size: 'lg',
                    resolve: {
                        entity: ['TrNotification', function(TrNotification) {
                            return TrNotification.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tr-notification.new', {
            parent: 'tr-notification',
            url: '/create',
            data: {
                authorities: ['ROLE_USER_ADMIN']
            },
            views:{
            	'content@':{
            		 templateUrl: 'app/entities/tr-notification/tr-notification-dialog.html',
                     controller: 'TrNotificationDialogController',
                     controllerAs: 'vm'
            	}
            },
            resolve: {
                entity: function () {
                    return {
                        createdDate: null,
                        description: null,
                        status: null,
                        subject: null,
                        alertType: null,
                        id: null
                    };
                }
            }            
        })
         .state('tr-notification.edit', {
            parent: 'tr-notification',
            url: '/edit/{id}',
            data: {
                authorities: ['ROLE_USER_ADMIN'],
                pageTitle: 'trakeyeApp.trNotification.detail.title'
            },
            views: {
                'content@': {
                	templateUrl: 'app/entities/tr-notification/tr-notification-dialog.html',
                    controller: 'TrNotificationDialogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('trNotification');
                    $translatePartialLoader.addPart('notificationStatus');
                    $translatePartialLoader.addPart('alertType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TrNotification', function($stateParams, TrNotification) {
                    return TrNotification.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'tr-notification',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
       
        .state('tr-notification.delete', {
            parent: 'tr-notification',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tr-notification/tr-notification-delete-dialog.html',
                    controller: 'TrNotificationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TrNotification', function(TrNotification) {
                            return TrNotification.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tr-notification', null, { reload: 'tr-notification' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
