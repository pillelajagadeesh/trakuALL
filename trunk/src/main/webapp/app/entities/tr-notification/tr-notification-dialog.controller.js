(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('TrNotificationDialogController', TrNotificationDialogController);

    TrNotificationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$state','entity', 'TrNotification', 'User', 'TrCase', 'ActivatedUsers'];

    function TrNotificationDialogController ($timeout, $scope, $stateParams, $state, entity, TrNotification, User, TrCase,ActivatedUsers) {
        var vm = this;

        vm.trNotification = entity;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        //vm.users = User.query();
        ActivatedUsers.getactivatedusers(function(response){
	    	vm.users = response;
	    });
        vm.trcases = TrCase.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });
      
        function save () {
            vm.isSaving = true;
            if (vm.trNotification.id !== null) {
                TrNotification.update(vm.trNotification, onSaveSuccess, onSaveError);
            } else {
                TrNotification.save(vm.trNotification, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('trakeyeApp:trNotificationUpdate', result);           
            vm.isSaving = false;
            $state.go('tr-notification');
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createdDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
