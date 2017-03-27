'use strict';

describe('Controller Tests', function() {

    describe('TrNotification Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockTrNotification, MockUser, MockTrCase;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockTrNotification = jasmine.createSpy('MockTrNotification');
            MockUser = jasmine.createSpy('MockUser');
            MockTrCase = jasmine.createSpy('MockTrCase');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'TrNotification': MockTrNotification,
                'User': MockUser,
                'TrCase': MockTrCase
            };
            createController = function() {
                $injector.get('$controller')("TrNotificationDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'trakeyeApp:trNotificationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
