(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('TrCaseDeleteController',TrCaseDeleteController);

    TrCaseDeleteController.$inject = ['$uibModalInstance', 'entity', 'TrCase'];

    function TrCaseDeleteController($uibModalInstance, entity, TrCase) {
        var vm = this;

        vm.trCase = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TrCase.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
