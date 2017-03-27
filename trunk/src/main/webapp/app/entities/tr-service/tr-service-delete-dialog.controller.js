(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('TrServiceDeleteController',TrServiceDeleteController);

    TrServiceDeleteController.$inject = ['$uibModalInstance', 'entity', 'TrService'];

    function TrServiceDeleteController($uibModalInstance, entity, TrService) {
        var vm = this;

        vm.trService = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TrService.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
