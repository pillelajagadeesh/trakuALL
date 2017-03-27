(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('AssetTypeDetailController', AssetTypeDetailController);

    AssetTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AssetType', 'User'];

    function AssetTypeDetailController($scope, $rootScope, $stateParams, previousState, entity, AssetType, User) {
        var vm = this;

        vm.assetType = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('trakeyeApp:assetTypeUpdate', function(event, result) {
            vm.assetType = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
