(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('ResetFinishController', ResetFinishController);

    ResetFinishController.$inject = ['$stateParams', '$timeout', 'Auth', 'LoginService'];

    function ResetFinishController ($stateParams, $timeout, Auth, LoginService) {
        var vm = this;

        vm.keyMissing = angular.isUndefined($stateParams.key);
        vm.confirmPassword = null;
        vm.doNotMatch = null;
        vm.error = null;
        vm.finishReset = finishReset;
        vm.login = LoginService.open;
        vm.resetAccount = {};
        vm.success = null;
        vm.emaillinkinvalid = null;
        vm.verifyaccount=null;
        
        Auth.verify($stateParams.key ,function (response) {
        	vm.verifyaccount = response.data;
            
        });
        $timeout(function (){angular.element('#password').focus();});

        function finishReset() {
            vm.doNotMatch = null;
            vm.error = null;
            if (vm.resetAccount.password !== vm.confirmPassword) {
                vm.doNotMatch = 'ERROR';
            } else {
                Auth.resetPasswordFinish({key: $stateParams.key, newPassword: vm.resetAccount.password}).then(function () {
                    vm.success = 'OK';
                }).catch(function (response) {
                    vm.success = null;
                    if(response.status === 400 && response.data === 'email link invalid'){
                   	 vm.emaillinkinvalid = 'ERROR';
                    }else{
                    	vm.error = 'ERROR';
                    }
                    
                });
            }
        }
    }
})();
