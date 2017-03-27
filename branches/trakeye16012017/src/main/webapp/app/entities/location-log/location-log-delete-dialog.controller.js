(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('LocationLogDeleteController',LocationLogDeleteController);

    LocationLogDeleteController.$inject = ['$uibModalInstance', 'entity', 'LocationLog'];

    function LocationLogDeleteController($uibModalInstance, entity, LocationLog) {
        var vm = this;

        vm.locationLog = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            LocationLog.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
