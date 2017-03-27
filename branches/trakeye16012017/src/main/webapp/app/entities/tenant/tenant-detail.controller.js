(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('TenantDetailController', TenantDetailController);

    TenantDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Tenant'];

    function TenantDetailController($scope, $rootScope, $stateParams, previousState, entity, Tenant) {
        var vm = this;

        vm.tenant = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('trakeyeApp:tenantUpdate', function(event, result) {
            vm.tenant = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
