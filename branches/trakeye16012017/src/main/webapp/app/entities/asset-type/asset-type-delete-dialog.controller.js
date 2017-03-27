(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('AssetTypeDeleteController',AssetTypeDeleteController);

    AssetTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'AssetType'];

    function AssetTypeDeleteController($uibModalInstance, entity, AssetType) {
        var vm = this;

        vm.assetType = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AssetType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
