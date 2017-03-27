(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('TrServiceDialogController', TrServiceDialogController);

    TrServiceDialogController.$inject = ['$timeout', '$scope','$http', '$stateParams', 'entity', '$state', 'TrService', 'User', 'TrCase', 'ServiceType','UserSearch','$sce', '$q','$rootScope','TrCaseUser'];

    function TrServiceDialogController ($timeout, $scope,$http, $stateParams, entity, $state, TrService, User, TrCase, ServiceType,UserSearch,$sce, $q,$rootScope, TrCaseUser) {
        var vm = this;

        vm.trService = entity;
        var entity_old = angular.copy(vm.trService)
       vm.serviceImages=entity_old.serviceImages;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.filtertrcasesid=filtertrcasesid;
        if($rootScope.has_ROLE_USER_ADMIN==true){
        	vm.users = User.query();
        }
        vm.trcases = TrCase.query();
        if($rootScope.has_ROLE_USER_ADMIN==true){
        vm.serviceTypes= ServiceType.query();
        }
        vm.selectattributes = selectedServiceTypeAttributes;
        vm.serviceType={};
        vm.trService.serviceImages=[];
        $scope.allimages = false;
		 $scope.toggle = function() {
			$scope.allimages = !$scope.allimages;
		};
        
        $('.select2').select2(); //Added for selectbox search
        
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });
        if(vm.trService.id && vm.trService.id!=null){
        	vm.serviceType=vm.trService.serviceType;
        	var date=vm.trService.serviceDate;
        	var newdate = new Date(date);
        	vm.trService.serviceDate=newdate;
        }
       
        function save () {
            vm.isSaving = true;
            if (vm.trService.id !== null) {
            	var date=vm.trService.serviceDate;
            	vm.trService.serviceDate=date.getTime();
                TrService.update(vm.trService, onSaveSuccess, onSaveError);
            } else {
            	$scope.date=vm.trService.serviceDate.getTime();
            	vm.trService.serviceDate=$scope.date;
                TrService.save(vm.trService, onSaveSuccess, onSaveError);               
            }
        }

        $("#serviceImages").change(function(){

			 var filesSelected = document.getElementById("serviceImages").files;
			 for(var i=0; i<filesSelected.length; i++){
				var fileReader = new FileReader();

			      fileReader.onload = function(fileLoadedEvent) {
			    	  vm.trService.serviceImages.push({image:window.btoa(fileLoadedEvent.target.result),trService:{id:entity_old.id}});
			      }
			      fileReader.readAsBinaryString(filesSelected[i]);
			}
		    
		});
        function onSaveSuccess (result) {
            $scope.$emit('trakeyeApp:trServiceUpdate', result);
            //$uibModalInstance.close(result);           
            vm.isSaving = false;
            $state.go('tr-service');
        }
        vm.serviceType =entity.serviceType;
        function selectedServiceTypeAttributes(){
        	if(vm.serviceType){
        		vm.trService.serviceType=vm.serviceType;
            		if(entity_old.serviceType && vm.serviceType.id===entity_old.serviceType.id){
            			vm.trService.serviceTypeAttributeValues = [];
            			vm.trService.serviceTypeAttributeValues = entity_old.serviceTypeAttributeValues;
            		}else{
            			vm.trService.serviceTypeAttributeValues = [];
            			$.each(vm.serviceType.serviceTypeAttribute,function(key,value){
                			vm.trService.serviceTypeAttributeValues.push({serviceTypeAttribute:vm.serviceType.serviceTypeAttribute[key]});
                		});
            		}
        	}
        	
        }
        function onSaveError () {
            vm.isSaving = false;
        }
        
        function filtertrcasesid(){
        	if(vm.trService.trCase.id!=null && !angular.isUndefined(vm.trService.trCase.id)){
        		
            	TrCaseUser.searchtrcasesuserbyvalue(vm.trService.trCase.id,function(response){
            		vm.caseids = response;
            	});
             }
        }

        vm.datePickerOpenStatus.createdDate = false;
        vm.datePickerOpenStatus.modifiedDate = false;
        vm.datePickerOpenStatus.serviceDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
		
		  //Added for upload styles
        $(":file").filestyle({buttonBefore: true});      
        //$(":file").filestyle('placeholder', 'Choose File');
        $(":file").filestyle('buttonText', 'Browse File');
               
    }
})();

