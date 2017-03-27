(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('TenantDeleteController',TenantDeleteController);

    TenantDeleteController.$inject = [ 'entity', 'Tenant'];

    function TenantDeleteController( entity, Tenant) {
        var vm = this;

        vm.tenant = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
          //  $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Tenant.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
