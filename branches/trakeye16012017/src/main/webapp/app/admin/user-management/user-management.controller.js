(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('UserManagementController', UserManagementController);

    UserManagementController.$inject = ['$scope', 'Principal', 'User', 'ParseLinks', 'AlertService', '$state', 'pagingParams', 'paginationConstants', 'JhiLanguageService','UserValueSearch','UsersStatusService','$stateParams', 'UserStatusSearch'];

    function UserManagementController($scope, Principal, User, ParseLinks, AlertService, $state, pagingParams, paginationConstants, JhiLanguageService,UserValueSearch, UsersStatusService,$stateParams, UserStatusSearch) {
        var vm = this;

        vm.authorities = ['ROLE_USER', 'ROLE_USER_ADMIN'];
        vm.currentAccount = null;
        vm.languages = null;
        vm.loadAll = loadAll;
        vm.setActive = setActive;
        vm.users = [];
        vm.page = 1;
        vm.totalItems = null;
        vm.clear = clear;
        vm.links = null;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.transition = transition;
        vm.getLocalhour = getLocalhour;
        vm.filterSearch=filterSearch;

        vm.loadAll();
        
        JhiLanguageService.getAll().then(function (languages) {
            vm.languages = languages;
        });
        Principal.identity().then(function(account) {
            vm.currentAccount = account;
        });
        
        function getLocalhour(hour){
        	var d  = new Date();
			d.setUTCHours(hour);
			return d.getHours();
		}

        function setActive (user, isActivated) {
            user.activated = isActivated;
            User.update(user, function () {
                vm.loadAll();
                vm.clear();
            });
        }
        
        function filterSearch(){
        	
			if($scope.search!=null && $scope.search!="" && !angular.isUndefined($scope.search)){
			if($stateParams.status==undefined){
				UserValueSearch.query({userId:$scope.search,
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
                }, onSuccess, onError);
			}else{
					UserStatusSearch.query({status:$stateParams.status,searchtext:$scope.search,
		                page: pagingParams.page - 1,
		                size: vm.itemsPerPage,
		                sort: sort()
		            }, onSuccess, onError);
			}	
           /* function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                   result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.users = data;
                vm.page = pagingParams.page;
        
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }*/
			}else{
				loadAll ();
			}
		}
        

        function loadAll () {
        	
          if($stateParams.status==undefined){
        	User.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
          }else{ 
        	UsersStatusService.query({status:$stateParams.status,
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
          }    
        }

        function onSuccess(data, headers) {
            //hide anonymous user from user management: it's a required user for Spring Security
            for (var i in data) {
                if (data[i]['login'] === 'anonymoususer') {
                    data.splice(i, 1);
                }
            }
            vm.links = ParseLinks.parse(headers('link'));
            vm.totalItems = headers('X-Total-Count');
            vm.queryCount = vm.totalItems;
            vm.page = pagingParams.page;
            vm.users = data;
           
        }

        function onError(error) {
            AlertService.error(error.data.message);
        }

        function clear () {
            vm.user = {
                id: null, login: null, firstName: null, lastName: null, email: null, phone: null, imsi: null,
                activated: null, langKey: null, createdBy: null, createdDate: null,
                lastModifiedBy: null, lastModifiedDate: null, resetDate: null,
                resetKey: null, authorities: null
            };
        }

        function sort () {
            var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
            if (vm.predicate !== 'id') {
              //  result.push('id');
            }
            return result;
        }

        function loadPage (page) {
            vm.page = page;
            vm.transition();
        }

        function transition () {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }
    }
})();
