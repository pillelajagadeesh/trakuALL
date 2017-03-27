(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('TrNotificationDialogController', TrNotificationDialogController);

    TrNotificationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$state','entity', 'TrNotification', 'User', 'TrCase', 'ActivatedUsers','TrCaseUser','ActivatedUserSearch'];

    function TrNotificationDialogController ($timeout, $scope, $stateParams, $state, entity, TrNotification, User, TrCase,ActivatedUsers,TrCaseUser,ActivatedUserSearch) {
        var vm = this;

        vm.trNotification = entity;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.filtertrcasesid=filtertrcasesid;
        vm.filterActivatedUser = filterActivatedUser;
        var usersMap={};
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
            var toUser = $("#field_toUser").val();
            
            vm.trNotification.toUserId = usersMap[toUser];
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
        
        function filtertrcasesid(){
        	if(vm.trNotification.trCaseId!=null && !angular.isUndefined(vm.trNotification.trCaseId)){
        		
            	TrCaseUser.searchtrcasesuserbyvalue(vm.trNotification.trCaseId,function(response){
            		vm.caseids = response;
            	});
             }
        }
        
        function filterActivatedUser(){
			
			if(vm.trNotification.toUserId!=null && vm.trNotification.toUserId!="" && !angular.isUndefined(vm.trNotification.toUserId)){
				ActivatedUserSearch.query({userId:vm.trNotification.toUserId,
		            }, onSuccess, onError);

         function onSuccess(data, headers) {
             vm.activatedusers = data; 
             
             angular.forEach(vm.activatedusers, function(value, key){
            	 
            	 usersMap[value.login] = value.id; 
            	 
             });
             
         }
         function onError(error) {
             AlertService.error(error.data.message);
         }
			}
		}
    }
})();
