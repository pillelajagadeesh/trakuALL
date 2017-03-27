(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('TrCaseDialogController', TrCaseDialogController);

    TrCaseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$q', 'entity', 'TrCase', 'User', 'CaseType','Principal','$state', 'ActivatedUsers'];

    function TrCaseDialogController ($timeout, $scope, $stateParams, $q, entity, TrCase, User, CaseType,Principal,$state, ActivatedUsers) {
        var vm = this;

        vm.trCase = entity;
        
        var entity_old = angular.copy(vm.trCase);
        vm.caseImages=entity_old.caseImages;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
		vm.decodeImage = decodeImage;
		$scope.allimages = false;
		 $scope.toggle = function() {
			$scope.allimages = !$scope.allimages;
		};
		vm.trCase.caseImages=[];
		
        Principal.identity().then(function(identity){
        	if(identity.authorities && identity.authorities.indexOf('ROLE_USER_ADMIN') !==- 1){
        		//vm.users = User.query();
        	    ActivatedUsers.getactivatedusers(function(response){
        	    	vm.users = response;
        	    });
        	}
        });
        $(".select2").select2(); //Added search for select box
        // Added for upload
        $(":file").filestyle({buttonBefore: true});      
        $(":file").filestyle('buttonText', 'Browse File');
        vm.caseTypes = CaseType.query();
        vm.selectattributes = selectedCaseTypeAttributes;
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

      
        function save () {
            vm.isSaving = true;
            if (vm.trCase.id !== null) {
                TrCase.update(vm.trCase, onSaveSuccess, onSaveError);
            } else {
                TrCase.save(vm.trCase, onSaveSuccess, onSaveError);                
            }
        }
			$("#caseImages").change(function(){

				 var filesSelected = document.getElementById("caseImages").files;
				 for(var i=0; i<filesSelected.length; i++){
					var fileReader = new FileReader();

				      fileReader.onload = function(fileLoadedEvent) {
				    	  vm.trCase.caseImages.push({image:window.btoa(fileLoadedEvent.target.result),trCase:{id:entity_old.id}});
				      }
				      fileReader.readAsBinaryString(filesSelected[i]);
				}
			    
			});
        function onSaveSuccess (result) {
            $scope.$emit('trakeyeApp:trCaseUpdate', result);
            $state.go('tr-case');
           // $uibModalInstance.close(result);
            vm.isSaving = false;
        }
        vm.caseType =entity.caseType;
        function selectedCaseTypeAttributes(){
        	if(vm.caseType){
    			vm.trCase.caseType=vm.caseType;
        		if(entity_old.caseType && vm.caseType.id===entity_old.caseType.id){
        			vm.trCase.caseTypeAttributeValues = [];
        			vm.trCase.caseTypeAttributeValues = entity_old.caseTypeAttributeValues;
        		}else{
        			vm.trCase.caseTypeAttributeValues = [];
        			$.each(vm.caseType.caseTypeAttribute,function(key,value){
            			vm.trCase.caseTypeAttributeValues.push({caseTypeAttribute:vm.caseType.caseTypeAttribute[key]});
            		});
        		}
        	}
        	
        }
        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createDate = false;
        vm.datePickerOpenStatus.updateDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
		 function decodeImage(img){
     	   return window.atob(img);
        }
    }
})();
