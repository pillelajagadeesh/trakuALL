(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('TenantDialogController', TenantDialogController);

    TenantDialogController.$inject = ['$timeout', '$scope', '$stateParams', 'entity','$state', 'Tenant'];

    function TenantDialogController ($timeout, $scope, $stateParams, entity, $state, Tenant) {
    	$scope.EMAIL_REGEXP =/^[_A-Za-z0-9-\+]+(\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\.[A-Za-z0-9]+)*(\.[A-Za-z]{2,})$/;
        $scope.PHONE_REGEXP =/^[0-9]+$/;
        var vm = this;

        vm.tenant = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.tenant.id !== null) {
                Tenant.update(vm.tenant, onSaveSuccess, onSaveError);
            } else {
                Tenant.save(vm.tenant, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('trakeyeApp:tenantUpdate', result);
            vm.isSaving = false;
            $state.go('tenant');
         }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
