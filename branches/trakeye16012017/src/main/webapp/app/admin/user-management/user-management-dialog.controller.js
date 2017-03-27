(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('UserManagementDialogController',UserManagementDialogController);

    UserManagementDialogController.$inject = ['$rootScope','$scope','$stateParams', '$state', 'entity', 'User', 'JhiLanguageService','Geofence','TrakeyeType','Principal'];
    
    function UserManagementDialogController ($rootScope ,$scope,$stateParams, $state, entity, User, JhiLanguageService,Geofence,TrakeyeType,Principal) {
    	$scope.EMAIL_REGEXP =/^[_A-Za-z0-9-\+]+(\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\.[A-Za-z0-9]+)*(\.[A-Za-z]{2,})$/;
        $scope.PHONE_REGEXP =/^[0-9]+$/;
        $scope.IMEI_REGEXP =/^[0-9]+$/;
       $scope.IMAGE_EXT_REGEXP =/^(.*?)\.(jpg||jpeg||png)$/;
        var vm = this;
        
        vm.languages = null;
        vm.save = save;
        vm.user = entity;
        vm.decodeImage = decodeImage;
		$scope.allimages = false;
		$(":file").filestyle({buttonBefore: true});      
        $(":file").filestyle('buttonText', 'Browse File');
		
		 $scope.toggle = function() {
			$scope.allimages = !$scope.allimages;
			
		};
		
		 $("#userImage").change(function(){
			 			 var filesSelected = document.getElementById("userImage").files;
			 			 for(var i=0; i<filesSelected.length; i++){
			 				var fileReader = new FileReader();

			 			      fileReader.onload = function(fileLoadedEvent) {
			 			    	 var test=window.btoa(fileLoadedEvent.target.result);
			 			    	  vm.user.userImage=test;
			 			    }
			 			      fileReader.readAsBinaryString(filesSelected[i]);
			 			}
			 		    
			 		});
        
        var entity_old = angular.copy(vm.user)
        Principal.identity().then(function(identity){
        	/*if(identity.authorities && identity.authorities.indexOf('ROLE_SUPER_ADMIN') !==- 1){
        		vm.authorities = ['ROLE_USER_ADMIN'];
   			 
        	}else if(identity.authorities && identity.authorities.indexOf('ROLE_USER_ADMIN') !==- 1){*/
        		vm.authorities = ['ROLE_USER','ROLE_USER_ADMIN'];
        		vm.selectattributes = selectedTrakeyeTypeAttributes;
   			 vm.geofences = Geofence.query();
   			 vm.trakeyeTypes=TrakeyeType.query();
   			 
        	//}
        	if(vm.user.id ==null){
        		vm.user.langKey='en';
        		//vm.user.authorities=vm.authorities;
        		vm.user.fromTime=$rootScope.getUTChour(8);
        		vm.user.toTime=$rootScope.getUTChour(20);
        	
        	}
        
        });
		
        function selectedTrakeyeTypeAttributes(){
        	if(vm.user.trakeyeType){
            		if(entity_old.trakeyeType && vm.user.trakeyeType.id===entity_old.trakeyeType.id){
            			vm.user.trakeyeTypeAttributeValues = [];
            			vm.user.trakeyeTypeAttributeValues = entity_old.trakeyeTypeAttributeValues;
            		}else{
            			vm.user.trakeyeTypeAttributeValues = [];
            			$.each(vm.user.trakeyeType.trakeyeTypeAttribute,function(key,value){
                			vm.user.trakeyeTypeAttributeValues.push({trakeyeTypeAttribute:vm.user.trakeyeType.trakeyeTypeAttribute[key]});
                		});
            		}
        	}
        	
        }    
        
        vm.selectfrom = [0,1, 2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23];

        JhiLanguageService.getAll().then(function (languages) {
            vm.languages = languages;
        });
        
       

        function onSaveSuccess (result) {
            vm.isSaving = false;
            $state.go('user-management');
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        function save () {
        	
        	vm.isSaving = true;
            if (vm.user.id !== null) {
            	User.update(vm.user, onSaveSuccess, onSaveError);
            } else {
            	User.save(vm.user, onSaveSuccess, onSaveError);
            }
        }
        function decodeImage(img){
     	   return window.atob(img);
        }
    }
})();
