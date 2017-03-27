(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('CustomerManagementDetailController', CustomerManagementDetailController);

    CustomerManagementDetailController.$inject = ['$stateParams', '$state', 'CustomerDetails', 'CustomerUpdate'];

    function CustomerManagementDetailController ($stateParams, $state, CustomerDetails, CustomerUpdate) {
        var vm = this;
        
        vm.save = save;
        vm.load = load;
        vm.customer = {};

        vm.load($stateParams.id);

        function load (id) {
        	CustomerDetails.get({customerId: id}, function(result) {
                vm.customer = result;
            });
        }

        function onSaveSuccess (result) {
            //vm.isSaving = false;
            $state.go('customer-management');
        }

        function onSaveError () {
            //vm.isSaving = false;
        }
        
        function save () {
       
            if (vm.customer.id !== null) {
                CustomerUpdate.update(vm.customer, onSaveSuccess, onSaveError);
            } else {
                CustomerUpdate.save(vm.customer, onSaveSuccess, onSaveError);
            }
        }
    }
})();

