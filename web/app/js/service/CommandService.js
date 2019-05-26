'use strict';

expanseApp.service('CommandService', ['$http','UserService', function($http, userService) {
    this.movePlayer = function(playerName, heading, callback) {
        var params = { "heading":heading };

        $http({
            method: 'PUT',
            params : params,
            headers: { 'Authorization' : userService.getJWTBearerToken() },
            url: 'http://localhost:9091/command/player/'+playerName+'/position'
        }).then(function(response) {
            callback(response.data, response.status);
        },
            function(response) { console.log('error');
        });
    };
}]);