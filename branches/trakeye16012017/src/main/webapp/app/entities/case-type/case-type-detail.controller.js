(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('CaseTypeDetailController', CaseTypeDetailController);

    CaseTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CaseType', 'User'];

    function CaseTypeDetailController($scope, $rootScope, $stateParams, previousState, entity, CaseType, User) {
        var vm = this;

        vm.caseType = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('trakeyeApp:caseTypeUpdate', function(event, result) {
            vm.caseType = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
