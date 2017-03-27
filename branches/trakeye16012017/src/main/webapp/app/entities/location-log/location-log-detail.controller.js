(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('LocationLogDetailController', LocationLogDetailController);

    LocationLogDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'LocationLog', 'User'];

    function LocationLogDetailController($scope, $rootScope, $stateParams, previousState, entity, LocationLog, User) {
        var vm = this;

        vm.locationLog = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('trakeyeApp:locationLogUpdate', function(event, result) {
            vm.locationLog = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
