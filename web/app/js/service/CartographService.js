'use strict';

expanseApp.service('CartographService', ['$http','UserService', function($http, userService) {
    this.getMapGrid = function(x, y, callback) {
    };

    this.getMapGridCompass = function(x, y, direction, callback) {
    };

    this.getMapGridRange = function(posX, posY, rangeX, rangeY, callback) {
        var params = {
            "cx":  posX,
            "cy":  posY,
            "ax": rangeX,
            "ay": rangeY
        };

        $http({
            method: 'GET',
            params: params,
            headers: { 'Authorization' : userService.getJWTBearerToken() },
            url: 'http://localhost:9092/cartograph/area'
        }).then(function(response) {
            callback(response.data, response.status);
        },
        function(response) { console.log('error');
        });
    };
}]);