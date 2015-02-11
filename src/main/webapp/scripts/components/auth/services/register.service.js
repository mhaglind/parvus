'use strict';

angular.module('parvusApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


