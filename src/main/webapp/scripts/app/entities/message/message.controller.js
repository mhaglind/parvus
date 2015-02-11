'use strict';

angular.module('parvusApp')
    .controller('MessageController', function ($scope, Message) {
        $scope.messages = [];
        $scope.loadAll = function() {
            Message.query(function(result) {
               $scope.messages = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            Message.save($scope.message,
                function () {
                    $scope.loadAll();
                    $('#saveMessageModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.message = Message.get({id: id});
            $('#saveMessageModal').modal('show');
        };

        $scope.delete = function (id) {
            $scope.message = Message.get({id: id});
            $('#deleteMessageConfirmation').modal('show');
        };

        $scope.confirmDelete = function (id) {
            Message.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteMessageConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.message = {created: null, payload: null, id: null};
        };
    });
