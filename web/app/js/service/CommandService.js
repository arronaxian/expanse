'use strict';

expanseApp.service('CommandService', ['$http','UserService', function($http, userService) {
    this.getPlayer = function(playerName, callback) {
        var params = {};

        var url = 'http://localhost:9091/command/player/';
        if ( playerName ) {
            url += playerName;
        }
        $http({
            method: 'GET',
            params : params,
            headers: { 'Authorization' : userService.getJWTBearerToken() },
            url: url
        }).then(function(response) {
                callback(response.data, response.status);
        },
        function(response) {
            console.log('error');
        });
    };


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
            function(response) {
                console.log('error');
        });
    };
}]);