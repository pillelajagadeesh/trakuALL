(function() {
    'use strict';

    angular
    .module('trakeyeApp')
    .controller('CustomerManagementController', CustomerManagementController);

    //CustomerManagementController.$inject = ['Customer','ParseLinks', 'AlertService', '$state', 'pagingParams', 'paginationConstants', 'JhiLanguageService','$stateParams'];
    CustomerManagementController.$inject = ['CustomerDetails', 'CustomerValueSearch', '$state', 'pagingParams', 'paginationConstants','$stateParams', 'Customer', 'AlertService'];
    
    function CustomerManagementController(CustomerDetails, CustomerValueSearch, $state, pagingParams, paginationConstants, $stateParams, Customer, AlertService) {
        var vm = this;

        vm.firstName = null;
        vm.lastName = null;
        vm.loadAll = loadAll;
        vm.setActive = setActive;
        vm.customers = [];
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
        
        function getLocalhour(hour){
        	var d  = new Date();
			d.setUTCHours(hour);
			return d.getHours();
		}

        function setActive (customer, isActivated) {
            customer.activated = isActivated;
            Customer.update(customer, function () {
                vm.loadAll();
                vm.clear();
            });
        }
        
        function filterSearch(){
			if(vm.search!=null && vm.search!="" && !angular.isUndefined(vm.search)){
				CustomerValueSearch.query({customerId:vm.search,
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
			}else{
				loadAll ();
			}

		}
        

        function loadAll () {        	
          if($stateParams.status==undefined){
        	  Customer.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
          }else{ 
        	 
        	CustomersStatusService.query({status:$stateParams.status,
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
          }    
        }

        function onSuccess(data, headers) {
            //hide anonymous user from user management: it's a required user for Spring Security
            for (var i in data) {
                if (data[i]['firstName'] === 'anonymoususer') {
                    data.splice(i, 1);
                }
            }
            //vm.links = ParseLinks.parse(headers('link'));
            vm.totalItems = headers('X-Total-Count');
            vm.queryCount = vm.totalItems;
            vm.page = pagingParams.page;
            vm.customers = data;
           
        }

        function onError(error) {
            AlertService.error(error.data.message);
        }

        function clear () {
            vm.customer = {
                id: null, firstName: null, lastName: null, email: null, mobilePhone: null,
                langKey: null, createdDate: null, status:null, city:null, state:null, country:null
            };
        }

        function sort () {
            var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
            if (vm.predicate !== 'id') {
                result.push('id');
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
