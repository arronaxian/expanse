'use strict';

expanseApp.service('PlayerService', ['$http','UserService', function($http, userService) {
    this.command = function(command, callback) {
        $http({
            method: 'GET',
            params: params,
            headers: { 'Authorization' : userService.getJWTBearerToken() },
            url: 'http://localhost:9090/command'
        }).then(function(response) {
                callback(response.data, response.status);
            },
            function(response) { console.log('error');
            });
    };
}]);