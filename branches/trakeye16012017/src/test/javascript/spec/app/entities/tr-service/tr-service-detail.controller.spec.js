'use strict';

describe('Controller Tests', function() {

    describe('TrService Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockTrService, MockUser, MockTrCase;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockTrService = jasmine.createSpy('MockTrService');
            MockUser = jasmine.createSpy('MockUser');
            MockTrCase = jasmine.createSpy('MockTrCase');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'TrService': MockTrService,
                'User': MockUser,
                'TrCase': MockTrCase
            };
            createController = function() {
                $injector.get('$controller')("TrServiceDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'trakeyeApp:trServiceUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
