(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('RequestResetController', RequestResetController);

    RequestResetController.$inject = ['$timeout', 'Auth'];

    function RequestResetController ($timeout, Auth) {
        var vm = this;

        vm.error = null;
        vm.errorEmailNotExists = null;
        vm.requestReset = requestReset;
        vm.resetAccount = {};
        vm.success = null;
        vm.errorUserDeactivate= null;
        vm.countExceeded = null;

        $timeout(function (){angular.element('#email').focus();});

        function requestReset () {

            vm.error = null;
            vm.errorEmailNotExists = null;

            Auth.resetPasswordInit(vm.resetAccount.login).then(function (response) {
                vm.success = 'OK';
            }).catch(function (response) {
                vm.success = null;
                if (response.status === 400 && response.data === 'username not registered') {
                    vm.errorEmailNotExists = 'ERROR';
                   
                } else if(response.status === 400 && response.data === 'user is deactivated'){
                	 vm.errorUserDeactivate = 'ERROR';
                    
                } else if(response.status === 400 && response.data === 'Reset password count exceeded for the day'){
               	 vm.countExceeded = 'ERROR';
                 
                }else {
                    vm.error = 'ERROR';//User has not yet set password once. So forget password email can not be sent. Check your email to set the password
                }
            });
        }
    }
})();
