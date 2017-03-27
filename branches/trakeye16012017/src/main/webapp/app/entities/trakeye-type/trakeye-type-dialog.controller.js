(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('TrakeyeTypeDialogController', TrakeyeTypeDialogController);

    TrakeyeTypeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$state', 'entity', 'TrakeyeType', 'User'];

    function TrakeyeTypeDialogController ($timeout, $scope, $stateParams, $state, entity, TrakeyeType, User) {
        var vm = this;

        vm.trakeyeType = entity;       
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();
        vm.items=[{"id":null}];
        
        if(vm.trakeyeType.trakeyeTypeAttribute!=null && vm.trakeyeType.trakeyeTypeAttribute.length!=0){
        	vm.items=vm.trakeyeType.trakeyeTypeAttribute;
        }
        
        
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });
       
        function save () {
            vm.isSaving = true;
        	vm.trakeyeType.trakeyeTypeAttribute=[];
            if (vm.trakeyeType.id !== null) {
            	for(var i = 0; i < vm.items.length; i++){
            		var attributes ={"name" :  vm.items[i]};
            	    vm.trakeyeType.trakeyeTypeAttribute.push({"name" :  vm.items[i].name});
            	}
            	TrakeyeType.update(vm.trakeyeType, onSaveSuccess, onSaveError);
            } else {
            	for(var i = 0; i < vm.items.length; i++){
            		var attributes ={"name" :  vm.items[i]};
            	    vm.trakeyeType.trakeyeTypeAttribute.push({"name" :  vm.items[i].name});
            	}
            	TrakeyeType.save(vm.trakeyeType, onSaveSuccess, onSaveError);
            }
        }
       

        function onSaveSuccess (result) {
            $scope.$emit('trakeyeApp:trakeyeTypeUpdate', result);
            vm.isSaving = false;
            $state.go('trakeye-type');
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createdDate = false;
        vm.datePickerOpenStatus.updatedDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
        
        vm.addmore = function() {
            var newItemNo = vm.items.length+1;
            vm.items.push({'id':newItemNo});
          };
            
       vm.remove = function() {
            var lastItem = vm.items.length-1;
            vm.items.splice(lastItem);
          };
        
    }
})();
