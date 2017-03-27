(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('TrCaseDialogController', TrCaseDialogController)
    .directive("fileread", [function () {
    	'use strict';

        return {
            restrict: "A",

            link: function($scope, element){

                element.change(function(event){
                    $scope.$apply(function(){
                      $scope.myFile =element[0].files[0];
                      
                    });
                });
            }

        };
}]);

    TrCaseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$q', 'entity', 'TrCase', 'User', 'CaseType','Principal','$state', 'ActivatedUsers', 'UserValueSearch', 'AlertService', 'ActivatedUserSearch','TrCaseUpload'];

    function TrCaseDialogController ($timeout, $scope, $stateParams, $q, entity, TrCase, User, CaseType,Principal,$state, ActivatedUsers, UserValueSearch, AlertService,ActivatedUserSearch,TrCaseUpload) {
        var vm = this;

        vm.trCase = entity;
        
        vm.upload= upload;
        
        var entity_old = angular.copy(vm.trCase);
        vm.caseImages=entity_old.caseImages;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
		vm.decodeImage = decodeImage;
    	vm.filterUser=filterUser;
    	vm.filterActivatedUser=filterActivatedUser;
    	var usersMap={};
		$scope.allimages = false;
		 $scope.toggle = function() {
			$scope.allimages = !$scope.allimages;
		};
		vm.trCase.caseImages=[];
		
		 var formData;
	        function upload(){
	        	formData=new FormData();
	        	formData.append('file',$scope.myFile);
	        
	        	TrCaseUpload.trcaseupload(formData,function(response,headers){
	        		if(response.status == 200){
	             		vm.error=null;
	             		vm.success='OK';
	             		
	            		$timeout(function(){
	            			vm.success= '';
	            		},4000);
	            		
	            		
	             	} 
	             	if(response.status == 400){
	             		vm.success=null;
	             		vm.error='error';
	             			
	             	} 
	             });
	        	
	        }
		
/*        Principal.identity().then(function(identity){
        	if(identity.authorities && identity.authorities.indexOf('ROLE_USER_ADMIN') !==- 1){
        		//vm.users = User.query();
        	    ActivatedUsers.getactivatedusers(function(response){
        	    	vm.users = response;
        	    });
        	}
        });*/
		
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
            var assignedToUser = $("#field_assignedToUser").val();
            vm.trCase.assignedTo = usersMap[assignedToUser];
            
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
		 

			function filterUser(){
			if(vm.trCase.assignedToUser!=null && vm.trCase.assignedToUser!="" && !angular.isUndefined(vm.trCase.assignedToUser)){
				UserValueSearch.query({userId:vm.trCase.assignedToUser,
		            }, onSuccess, onError);

         function onSuccess(data, headers) {
             vm.users = data; 
             angular.forEach(vm.users, function(value, key){
            	 usersMap[value.login] = value.id; 
             });
         }
         function onError(error) {
             AlertService.error(error.data.message);
         }
			}
		}
			
			function filterActivatedUser(){
				
				if(vm.trCase.assignedToUser!=null && vm.trCase.assignedToUser!="" && !angular.isUndefined(vm.trCase.assignedToUser)){
					ActivatedUserSearch.query({userId:vm.trCase.assignedToUser,
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
