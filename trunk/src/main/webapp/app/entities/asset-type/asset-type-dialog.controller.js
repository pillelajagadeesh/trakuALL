(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('AssetTypeDialogController', AssetTypeDialogController);

    AssetTypeDialogController.$inject = ['$timeout', '$scope', '$stateParams',  'entity', 'AssetType', 'User', '$state', '$q'];

    function AssetTypeDialogController ($timeout, $scope, $stateParams,  entity, AssetType, User, $state, $q) {
        var vm = this;
        $scope.IMAGE_EXT_REGEXP =/^(.*?)\.(jpg||jpeg||png)$/;

        vm.assetType = entity;
        vm.clear = clear;
        vm.colorcode = colorcode;
        vm.assetImageName = assetImageName;
        vm.decodeImage = decodeImage;
		$scope.allimages = false;
		 $scope.toggle = function() {
			$scope.allimages = !$scope.allimages;
		};
		vm.save = save;
		 $(":file").filestyle({buttonBefore: true});      
	     $(":file").filestyle('buttonText', 'Browse File');
       
        
        vm.items=[{name: ''}];
        
        if(vm.assetType.assetTypeAttributes!=null && vm.assetType.assetTypeAttributes.length!=0){
        	vm.items=vm.assetType.assetTypeAttributes;
        }
        
        function colorcode(color) {
        	vm.assetType.colorcode = color;
            
        }
        function assetImageName(image) {
        	vm.assetType.image = image;
        }
        
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.assetType.id !== null) {
            	vm.assetType.assetTypeAttributes=[];
            	for(var i = 0; i < vm.items.length; i++){
            		var attributes ={"name" :  vm.items[i]};
            	    vm.assetType.assetTypeAttributes.push({"name" :  vm.items[i].name});
            	}
                AssetType.update(vm.assetType, onSaveSuccess, onSaveError);
            } else {
                vm.assetType.assetTypeAttributes=[];
            	
            	for(var i = 0; i < vm.items.length; i++){
            		var attributes ={"name" :  vm.items[i]};
            	    vm.assetType.assetTypeAttributes.push({"name" :  vm.items[i].name});
            	}
                AssetType.save(vm.assetType, onSaveSuccess, onSaveError);
            }
        }
       
       /* $scope.fileNameChanged = function() {
        	
        	 var filesSelected = document.getElementById("assetImage").files;
        	 for(var i=0; i<filesSelected.length; i++){
 				var fileReader = new FileReader();
                  fileReader.onload = function(fileLoadedEvent) {
 			    	var test=window.btoa(fileLoadedEvent.target.result);
 			    	 vm.assetType.image= test;
 			    }
 			      fileReader.readAsBinaryString(filesSelected[i]);
 			}
 		   
        	}*/
        
        $("#assetImage").change(function(){
        	var filesSelected = document.getElementById("assetImage").files;
       	 for(var i=0; i<filesSelected.length; i++){
				var fileReader = new FileReader();
                 fileReader.onload = function(fileLoadedEvent) {
			    	var test=window.btoa(fileLoadedEvent.target.result);
			    	 vm.assetType.image= test;
			    	
			    }
			      fileReader.readAsBinaryString(filesSelected[i]);
			}
		    
		});
        
        

        function onSaveSuccess (result) {
            $scope.$emit('trakeyeApp:assetTypeUpdate', result);
            vm.isSaving = false;
            $state.go('asset-type');
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
          function decodeImage(img){
        	   return window.atob(img);
           }

    }
})();
