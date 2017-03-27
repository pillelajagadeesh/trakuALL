(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('user-management', {
            parent: 'admin',
            url: '/user-management?page&sort$search',
            data: {
                authorities: ['ROLE_SUPER_ADMIN','ROLE_USER_ADMIN'],
                pageTitle: 'userManagement.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/admin/user-management/user-management.html',
                    controller: 'UserManagementController',
                    controllerAs: 'vm'
                }
            },            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'last_modified_date,desc',
                    squash: true
                }
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort)
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('user-management');
                    return $translate.refresh();
                }]

            }       
			})
			
        
        .state('user-management.status', {
            parent: 'user-management',
            url: '/user-management/{status}?page&sort$search',
            data: {
                authorities: ['ROLE_SUPER_ADMIN','ROLE_USER_ADMIN'],
                pageTitle: 'userManagement.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/admin/user-management/user-management.html',
                    controller: 'UserManagementController',
                    controllerAs: 'vm'
                }
            },            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                }
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort)
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('user-management');
                    return $translate.refresh();
                }]

            }       
			})
        
		//Active Users
       /* .state('active-users', {
            parent: 'admin',
            url: '/active-users',
            data: {
                authorities: ['ROLE_SUPER_ADMIN','ROLE_USER_ADMIN'],
                pageTitle: 'userManagement.home.active-title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/admin/active-users/active-users.html',
                    controller: 'UserManagementController',
                    controllerAs: 'vm'
                }
            },            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                }
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort)
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('user-management');
                    return $translate.refresh();
                }]

            }       
			})*/
	//Inactive users
//		.state('inactive-users', {
//            parent: 'admin',
//            url: '/inactive-users',
//            data: {
//                authorities: ['ROLE_SUPER_ADMIN','ROLE_USER_ADMIN'],
//                pageTitle: 'userManagement.home.inactive-title'
//            },
//            views: {
//                'content@': {
//                    templateUrl: 'app/admin/inactive-users/inactive-users.html',
//                    controller: 'UserManagementController',
//                    controllerAs: 'vm'
//                }
//            },            params: {
//                page: {
//                    value: '1',
//                    squash: true
//                },
//                sort: {
//                    value: 'id,asc',
//                    squash: true
//                }
//            },
//            resolve: {
//                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
//                    return {
//                        page: PaginationUtil.parsePage($stateParams.page),
//                        sort: $stateParams.sort,
//                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
//                        ascending: PaginationUtil.parseAscending($stateParams.sort)
//                    };
//                }],
//                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
//                    $translatePartialLoader.addPart('user-management');
//                    return $translate.refresh();
//                }]
//
//            }       
//			})
			//Idle Users
//			.state('idle-users', {
//            parent: 'admin',
//            url: '/idle-users',
//            data: {
//                authorities: ['ROLE_SUPER_ADMIN','ROLE_USER_ADMIN'],
//                pageTitle: 'userManagement.home.idle-title'
//            },
//            views: {
//                'content@': {
//                    templateUrl: 'app/admin/idle-users/idle-users.html',
//                    controller: 'UserManagementController',
//                    controllerAs: 'vm'
//                }
//            },            params: {
//                page: {
//                    value: '1',
//                    squash: true
//                },
//                sort: {
//                    value: 'id,asc',
//                    squash: true
//                }
//            },
//            resolve: {
//                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
//                    return {
//                        page: PaginationUtil.parsePage($stateParams.page),
//                        sort: $stateParams.sort,
//                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
//                        ascending: PaginationUtil.parseAscending($stateParams.sort)
//                    };
//                }],
//                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
//                    $translatePartialLoader.addPart('user-management');
//                    return $translate.refresh();
//                }]
//
//            }       
//			})
        .state('user-management-detail', {
            parent: 'admin',
            url: '/user/:login',
            data: {
            	 authorities: ['ROLE_SUPER_ADMIN','ROLE_USER_ADMIN'],
                pageTitle: 'user-management.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/admin/user-management/user-management-detail.html',
                    controller: 'UserManagementDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('user-management');
                    return $translate.refresh();
                }]
            }
        })
        .state('user-management.new', {
            parent: 'user-management',
            url: '/create',
            data: {
            	 authorities: ['ROLE_SUPER_ADMIN','ROLE_USER_ADMIN']
            },
			views:{
				'content@':{
					templateUrl: 'app/admin/user-management/user-management-dialog.html',
                    controller: 'UserManagementDialogController',
                    controllerAs: 'vm'
				}
			},
			 resolve: {
                        entity: function () {
                            return {
                                id: null, login: null, firstName: null, lastName: null, email: null, phone: null, imsi: null,
                                activated: true, langKey: null, createdBy: null, createdDate: null,
                                lastModifiedBy: null, lastModifiedDate: null, resetDate: null,
                                resetKey: null, authorities: null, geofences: null
                            };
                        }
                    }
            
        })
		
        .state('user-management.edit', {
            parent: 'user-management',
            url: '/{login}/edit',
            data: {
            	 authorities: ['ROLE_SUPER_ADMIN','ROLE_USER_ADMIN']
            },
			views:{
				'content@':{
					templateUrl: 'app/admin/user-management/user-management-dialog.html',
                    controller: 'UserManagementDialogController',
                    controllerAs: 'vm'
				}
			},
			resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('user-management');
                    return $translate.refresh();
                }],
				entity: ['$stateParams', 'User', function($stateParams, User) {
                    return User.get({login : $stateParams.login}).$promise;
                }],
            }
			
            
        })
        .state('user-management.delete', {
            parent: 'user-management',
            url: '/{login}/delete',
            data: {
            	 authorities: ['ROLE_SUPER_ADMIN','ROLE_USER_ADMIN'],
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/admin/user-management/user-management-delete-dialog.html',
                    controller: 'UserManagementDeleteController',
                    controllerAs: 'vm',
					size: 'sm',
                    resolve: {
                        entity: ['User', function(User) {
                            return User.get({login : $stateParams.login});
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-management', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }
})();
