(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('dashboard', {
            parent: 'app',
            url: '/dashboard',            
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/dashboard/dashboard.html',
                    controller: 'DashboardController',
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
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('dashboard');
                    return $translate.refresh();
                }]
           
            }
        });
    }
})();
