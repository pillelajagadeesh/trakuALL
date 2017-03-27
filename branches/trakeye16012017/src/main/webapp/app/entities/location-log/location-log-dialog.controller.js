(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('LocationLogDialogController', LocationLogDialogController);

    LocationLogDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'LocationLog', 'User'];

    function LocationLogDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, LocationLog, User) {
        var vm = this;

        vm.locationLog = entity;
        vm.locationLog.createdDateTime = (new Date()).getTime();
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
       // vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
        	vm.isSaving = true;
            if (vm.locationLog.id !== null) {
                LocationLog.update(vm.locationLog, onSaveSuccess, onSaveError);
            } else {
                LocationLog.save(vm.locationLog, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('trakeyeApp:locationLogUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createdDateTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
