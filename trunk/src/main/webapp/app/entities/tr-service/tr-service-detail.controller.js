(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('TrServiceDetailController', TrServiceDetailController);

    TrServiceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity'];

    function TrServiceDetailController($scope, $rootScope, $stateParams, previousState, entity) {
        var vm = this;

        vm.trService = entity;
        vm.previousState = previousState.name;
        vm.decodeImage = decodeImage;
        var unsubscribe = $rootScope.$on('trakeyeApp:trServiceUpdate', function(event, result) {
            vm.trService = result;
        });
        $scope.$on('$destroy', unsubscribe);
        
        
        function decodeImage(img){
      	   return window.atob(img);
         }
    }
})();
