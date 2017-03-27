(function () {
    'use strict';

    angular
        .module('trakeyeApp')
        .factory('Register', Register);

    Register.$inject = ['$resource'];

    function Register ($resource) {
        var service = $resource('api/customer', {}, {
            'save': { method:'POST' }
        });
        return service;
    }
})();
