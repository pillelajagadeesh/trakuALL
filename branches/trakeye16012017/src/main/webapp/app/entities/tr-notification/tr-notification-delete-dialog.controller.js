(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('TrNotificationDeleteController',TrNotificationDeleteController);

    TrNotificationDeleteController.$inject = ['$uibModalInstance', 'entity', 'TrNotification'];

    function TrNotificationDeleteController($uibModalInstance, entity, TrNotification) {
        var vm = this;

        vm.trNotification = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TrNotification.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
