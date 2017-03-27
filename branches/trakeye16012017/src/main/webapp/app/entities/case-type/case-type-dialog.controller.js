(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('CaseTypeDialogController', CaseTypeDialogController);

    CaseTypeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$state', 'entity', 'CaseType', 'User','CaseTypeSearch'];

    function CaseTypeDialogController ($timeout, $scope, $stateParams, $state, entity, CaseType, User,CaseTypeSearch) {
        var vm = this;

        vm.caseType = entity;
        
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
//        vm.users = User.query();
       vm.items=[{name: ''}];
        
        if(vm.caseType.caseTypeAttribute!=null && vm.caseType.caseTypeAttribute.length!=0){
        	vm.items=vm.caseType.caseTypeAttribute;
        }
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        
       
        function save () {
            vm.isSaving = true;
             if (vm.caseType.id !== null) {
            	vm.caseType.caseTypeAttribute=[];
            	for(var i = 0; i < vm.items.length; i++){
            		var attributes ={"name" :  vm.items[i]};
            	    vm.caseType.caseTypeAttribute.push({"name" :  vm.items[i].name});
            	}
            	CaseType.update(vm.caseType, onSaveSuccess, onSaveError);
            } else {
            	vm.caseType.caseTypeAttribute=[];
            	
            	for(var i = 0; i < vm.items.length; i++){
            		var attributes ={"name" :  vm.items[i]};
            	    vm.caseType.caseTypeAttribute.push({"name" :  vm.items[i].name});
            	}
            	CaseType.save(vm.caseType, onSaveSuccess, onSaveError);
            }
           
        }

        function onSaveSuccess (result) {
            $scope.$emit('trakeyeApp:caseTypeUpdate', result);
            vm.isSaving = false;
			$state.go('case-type');
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.addmore = function() {
            vm.items.push({name: ''});
          };
            
       vm.remove = function(index) {
            vm.items.splice(index, 1);
          };
          
        vm.datePickerOpenStatus.createdDate = false;
        vm.datePickerOpenStatus.updateDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
