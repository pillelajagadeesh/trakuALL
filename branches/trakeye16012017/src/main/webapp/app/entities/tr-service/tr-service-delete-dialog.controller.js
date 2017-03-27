(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('TrServiceDeleteController',TrServiceDeleteController);

    TrServiceDeleteController.$inject = ['$uibModalInstance', 'entity', 'TrService','$scope'];

    function TrServiceDeleteController($uibModalInstance, entity, TrService,$scope) {
        var vm = this;

        vm.trService = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
        	$scope.deletefail = true;
            TrService.delete({id: id},
                function () {
            	
           	 $scope.deletefail = false;
                    $uibModalInstance.close(true);
                });
            
        }
    }
})();
