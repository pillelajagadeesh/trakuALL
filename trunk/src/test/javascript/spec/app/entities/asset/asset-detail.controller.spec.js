'use strict';

describe('Controller Tests', function() {

    describe('Asset Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockAsset, MockUser, MockAssetType, MockAssetTypeAttributeValue, MockAssetCoordinate;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockAsset = jasmine.createSpy('MockAsset');
            MockUser = jasmine.createSpy('MockUser');
            MockAssetType = jasmine.createSpy('MockAssetType');
            MockAssetTypeAttributeValue = jasmine.createSpy('MockAssetTypeAttributeValue');
            MockAssetCoordinate = jasmine.createSpy('MockAssetCoordinate');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Asset': MockAsset,
                'User': MockUser,
                'AssetType': MockAssetType,
                'AssetTypeAttributeValue': MockAssetTypeAttributeValue,
                'AssetCoordinate': MockAssetCoordinate
            };
            createController = function() {
                $injector.get('$controller')("AssetDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'trakeyeApp:assetUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
