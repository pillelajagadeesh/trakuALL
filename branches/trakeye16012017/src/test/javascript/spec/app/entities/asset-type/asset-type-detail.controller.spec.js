'use strict';

describe('Controller Tests', function() {

    describe('AssetType Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockAssetType, MockUser, MockAssetTypeAttribute;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockAssetType = jasmine.createSpy('MockAssetType');
            MockUser = jasmine.createSpy('MockUser');
            MockAssetTypeAttribute = jasmine.createSpy('MockAssetTypeAttribute');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'AssetType': MockAssetType,
                'User': MockUser,
                'AssetTypeAttribute': MockAssetTypeAttribute
            };
            createController = function() {
                $injector.get('$controller')("AssetTypeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'trakeyeApp:assetTypeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
