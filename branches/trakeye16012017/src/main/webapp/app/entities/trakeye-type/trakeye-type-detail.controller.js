(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('TrakeyeTypeDetailController', TrakeyeTypeDetailController);

    TrakeyeTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TrakeyeType', 'User'];

    function TrakeyeTypeDetailController($scope, $rootScope, $stateParams, previousState, entity, TrakeyeType, User) {
        var vm = this;

        vm.trakeyeType = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('trakeyeApp:trakeyeTypeUpdate', function(event, result) {
            vm.trakeyeType = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
