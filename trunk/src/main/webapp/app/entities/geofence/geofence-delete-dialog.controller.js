(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('GeofenceDeleteController',GeofenceDeleteController);

    GeofenceDeleteController.$inject = ['$uibModalInstance', 'entity', 'Geofence'];

    function GeofenceDeleteController($uibModalInstance, entity, Geofence) {
        var vm = this;

        vm.geofence = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Geofence.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
