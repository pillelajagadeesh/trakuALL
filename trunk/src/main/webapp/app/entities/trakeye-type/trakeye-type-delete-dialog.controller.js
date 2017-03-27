(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('TrakeyeTypeDeleteController',TrakeyeTypeDeleteController);

    TrakeyeTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'TrakeyeType'];

    function TrakeyeTypeDeleteController($uibModalInstance, entity, TrakeyeType) {
        var vm = this;

        vm.trakeyeType = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TrakeyeType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
