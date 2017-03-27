(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('TrNotificationDetailController', TrNotificationDetailController);

    TrNotificationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TrNotification', 'User', 'TrCase'];

    function TrNotificationDetailController($scope, $rootScope, $stateParams, previousState, entity, TrNotification, User, TrCase) {
        var vm = this;

        vm.trNotification = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('trakeyeApp:trNotificationUpdate', function(event, result) {
            vm.trNotification = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
