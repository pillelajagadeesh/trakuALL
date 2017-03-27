(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('LoginControllers', LoginControllers);

    LoginControllers.$inject = ['$rootScope', '$state', '$timeout', 'Auth', 'Principal'];

    function LoginControllers ($rootScope, $state, $timeout, Auth, Principal) {
        var vm = this;

        vm.authenticationError = false;
        vm.cancel = cancel;
        vm.credentials = {};
        vm.login = login;
        vm.password = null;
        vm.register = register;
        vm.rememberMe = true;
        vm.requestResetPassword = requestResetPassword;
        vm.username = null;

        $timeout(function (){angular.element('#username').focus();});

        function cancel () {
            vm.credentials = {
                username: null,
                password: null,
                rememberMe: true
            };
            vm.authenticationError = false;
          //  $uibModalInstance.dismiss('cancel');
        }

        function login (event) {
            event.preventDefault();
            Auth.login({
                username: vm.username,
                password: vm.password,
                rememberMe: vm.rememberMe
            }).then(function () {
                vm.authenticationError = false;
                $rootScope.has_ROLE_SUPER_ADMIN=false;
				$rootScope.has_ROLE_USER_ADMIN=false;
				$rootScope.has_ROLE_USER=false;   
				   Principal.identity().then(function(identity){
					if(identity && identity.authorities && identity.authorities.indexOf('ROLE_SUPER_ADMIN') !==- 1){
					   $rootScope.has_ROLE_SUPER_ADMIN=true;
					   $state.go('tenant');
					}else if(identity && identity.authorities && identity.authorities.indexOf('ROLE_USER_ADMIN') !==- 1){
					   $rootScope.has_ROLE_USER_ADMIN=true;
					   $state.go('dashboard');
					}else if(identity && identity.authorities && identity.authorities.indexOf('ROLE_USER') !==- 1){
						$rootScope.has_ROLE_USER=true;  
						$state.go('location-log');
					}
					if(identity){
					   $rootScope.userName = identity.login;
					   $rootScope.image = identity.userImage;
					}
				});

                $rootScope.$broadcast('authenticationSuccess');

                // previousState was set in the authExpiredInterceptor before being redirected to login modal.
                // since login is succesful, go to stored previousState and clear previousState
				
		///////////////////No use with this code ///////////////////
				/*if (Auth.getPreviousState()) {
                    var previousState = Auth.getPreviousState();
                    Auth.resetPreviousState();
                    $state.go(previousState.name, previousState.params);
                }*/
            }).catch(function () {
                vm.authenticationError = true;
            });
        }

        function register () {
            //$uibModalInstance.dismiss('cancel');
            $state.go('register');
        }

        function requestResetPassword () {
            //$uibModalInstance.dismiss('cancel');
            $state.go('requestReset');
        }
    }
})();
