'use strict';

angular.module('parvusApp')
    .factory('Message', function ($resource) {
        return $resource('api/messages/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.created = new Date(data.created);
                    return data;
                }
            }
        });
    });
