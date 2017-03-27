(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('UploadController', UploadController)
        .directive('agentFile', [function() {
        'use strict';

        return {
            restrict: "A",

            link: function($scope, element){

                element.change(function(event){
                    $scope.$apply(function(){
                      $scope.myFile =element[0].files[0];
                      $scope.versionNo = $scope.version;
                      
                    });
                });
            }

        };
    }]);

    UploadController.$inject = ['$scope', '$state', 'Auth', 'Principal','Upload' ,'ProfileService', 'LoginService','$rootScope','$timeout','$window'];

    function UploadController ($scope, $state, Auth, Principal,Upload, ProfileService, LoginService, $rootScope,$timeout, $window) {
    	$scope.FILE_EXT_REGEXP =/^(.*?)\.(apk||ipa)$/;
    	
        var vm = this;
        vm.upload = upload;
        vm.success= null;
        vm.fileempty = null;
        vm.error = null;
        vm.files=[];
        vm.iosfiles=[];
        vm.showiosfiles=showiosfiles;
        vm.showandroidfiles=showandroidfiles;
        
      
//        vm.changedFile =fileselected;
        var formData;
        function upload () {
        formData=new FormData();
       
        formData.append("version", $scope.version);
        formData.append('file',$scope.myFile);
        
               Upload.upload(formData,function(response,headers){
            	    if(response.status == 200){
                		vm.error=null;
                		vm.success='OK';
                		$scope.version='';
                		
               		$timeout(function(){
               			vm.success= '';
               		},4000);
               		
               		Upload.findAllFiles(function(data){
                        	vm.files=data;
                        });
                		
               		Upload.findAllIosFiles(function(data){
                			vm.iosfiles=data;
                		});
                	} 
                	if(response.status == 400){
                		vm.success=null;
                		vm.error='error';
                			
                	} 
                });
        
        }   
        //Added for upload styles
        $(":file").filestyle({buttonBefore: true});      
        //$(":file").filestyle('placeholder', 'Choose File');
        $(":file").filestyle('buttonText', 'Browse File');
        
        function showiosfiles(){
          Upload.findAllIosFiles(function(data){
        	  vm.iosfiles=data;
          });
        }
        
        vm.showandroidfiles();
        
        function showandroidfiles(){
        	Upload.findAllFiles(function(data){
          	  vm.files=data;
            });
        }
        $scope.tab = 1;

		$scope.setTab = function(newTab){
		  $scope.tab = newTab;
		  if( $scope.tab==1){
			  vm.showandroidfiles();
		  }
		  if( $scope.tab==2){
			  vm.showiosfiles();
		  }
		};

		$scope.isSet = function(tabNum){
		  return $scope.tab === tabNum;
		};
    }
   
})();
