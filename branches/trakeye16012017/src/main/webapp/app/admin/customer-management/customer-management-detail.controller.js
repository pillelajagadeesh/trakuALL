(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('CustomerManagementDetailController', CustomerManagementDetailController);

    CustomerManagementDetailController.$inject = ['$stateParams', 'CustomerDetails'];

    function CustomerManagementDetailController ($stateParams, CustomerDetails) {
        var vm = this;

        vm.load = load;
        vm.customer = {};

        vm.load($stateParams.id);

        function load (id) {
        	CustomerDetails.get({customerId: id}, function(result) {
                vm.customer = result;
            });
        }
       
    }
})();

