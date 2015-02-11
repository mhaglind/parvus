'use strict';

angular.module('parvusApp')
    .controller('MessageDetailController', function ($scope, $stateParams, Message) {
        $scope.message = {};
        $scope.load = function (id) {
            Message.get({id: id}, function(result) {
              $scope.message = result;
            });
        };
        $scope.load($stateParams.id);
    });
