(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('TrCaseDeleteController',TrCaseDeleteController);

    TrCaseDeleteController.$inject = ['$uibModalInstance', 'entity', 'TrCase','$scope'];

    function TrCaseDeleteController($uibModalInstance, entity, TrCase,$scope) {
        var vm = this;

        vm.trCase = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
        	$scope.deletefail = true;
            TrCase.delete({id: id},
                function () {
            	
            	 $scope.deletefail = false;
                    $uibModalInstance.close(true);
                });
           
        }
    }
})();
