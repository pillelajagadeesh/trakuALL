(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('AssetDetailController', AssetDetailController);

    AssetDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Asset', 'User', 'AssetType'];

    function AssetDetailController($scope, $rootScope, $stateParams, previousState, entity, Asset, User, AssetType) {
        var vm = this;

        vm.asset = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('trakeyeApp:assetUpdate', function(event, result) {
            vm.asset = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
