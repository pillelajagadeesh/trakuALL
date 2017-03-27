(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('TrCaseDetailController', TrCaseDetailController);

    TrCaseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TrCase', 'User', 'CaseType'];

    function TrCaseDetailController($scope, $rootScope, $stateParams, previousState, entity, TrCase, User, CaseType) {
        var vm = this;

        vm.trCase = entity;
        vm.previousState = previousState.name;
        vm.decodeImage = decodeImage;
        var unsubscribe = $rootScope.$on('trakeyeApp:trCaseUpdate', function(event, result) {
            vm.trCase = result;
        });
        $scope.$on('$destroy', unsubscribe);
        
        function decodeImage(img){
     	   return window.atob(img);
        }
    }
})();
