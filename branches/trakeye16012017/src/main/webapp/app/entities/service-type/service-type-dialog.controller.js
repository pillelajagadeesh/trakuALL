(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('ServiceTypeDialogController', ServiceTypeDialogController);

    ServiceTypeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$state', 'entity', 'ServiceType', 'User'];

    function ServiceTypeDialogController ($timeout, $scope, $stateParams, $state, entity, ServiceType, User) {
        var vm = this;
        vm.serviceType = entity;       
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();
        vm.items=[{"id":null}];
        
        if(vm.serviceType.serviceTypeAttribute!=null && vm.serviceType.serviceTypeAttribute.length!=0){
        	vm.items=vm.serviceType.serviceTypeAttribute;
        }

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function save () {
            vm.isSaving = true;
            
            if (vm.serviceType.id !== null) {
            	vm.serviceType.serviceTypeAttribute=[];
            	for(var i = 0; i < vm.items.length; i++){
            		var attributes ={"name" :  vm.items[i]};
            	    vm.serviceType.serviceTypeAttribute.push({"name" :  vm.items[i].name});
            	}
                ServiceType.update(vm.serviceType, onSaveSuccess, onSaveError);
            } else {
            	vm.serviceType.serviceTypeAttribute=[];
            	
            	for(var i = 0; i < vm.items.length; i++){
            		var attributes ={"name" :  vm.items[i]};
            	    vm.serviceType.serviceTypeAttribute.push({"name" :  vm.items[i].name});
            	}
                ServiceType.save(vm.serviceType, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('trakeyeApp:serviceTypeUpdate', result);
            vm.isSaving = false;
			$state.go('service-type');
        }

        function onSaveError () {
            vm.isSaving = false;
        }
        
       vm.addmore = function() {
            var newItemNo = vm.items.length+1;
            vm.items.push({'id':newItemNo});
          };
            
       vm.remove = function() {
            var lastItem = vm.items.length-1;
            vm.items.splice(lastItem);
          };

        vm.datePickerOpenStatus.createdDate = false;
        vm.datePickerOpenStatus.updatedDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
