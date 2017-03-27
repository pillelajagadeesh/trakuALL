(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('ServiceTypeDetailController', ServiceTypeDetailController);

    ServiceTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ServiceType', 'User'];

    function ServiceTypeDetailController($scope, $rootScope, $stateParams, previousState, entity, ServiceType, User) {
        var vm = this;

        vm.serviceType = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('trakeyeApp:serviceTypeUpdate', function(event, result) {
            vm.serviceType = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
