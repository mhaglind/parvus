'use strict';

angular.module('parvusApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
