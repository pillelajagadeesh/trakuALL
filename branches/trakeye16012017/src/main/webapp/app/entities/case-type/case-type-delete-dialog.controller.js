(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('CaseTypeDeleteController',CaseTypeDeleteController);

    CaseTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'CaseType'];

    function CaseTypeDeleteController($uibModalInstance, entity, CaseType) {
        var vm = this;

        vm.caseType = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CaseType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
