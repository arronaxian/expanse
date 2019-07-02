'use strict';

expanseApp.service('CommandService', ['$http','UserService', function($http, userService) {
    /**
     * Flag to ensure only one move request can be made at a time.
     * @type {boolean}
     */
    this.isMovingPlayer = false;
    this.isGettingNonPlayerNearMe = false;

    /**
     * Gets the Player by name.
     * @param playerName (Optional) player name.
     * @param callback The callback.
     */
    this.getPlayer = function(playerName, callback) {
        let params = {};

        let url = 'http://localhost:9091/command/player/';
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


    /**
     * Move the Player in the direction of heading.
     * @param playerName The Player's name.
     * @param heading The heading (north, south, east, west).
     * @param callback The response data - {x:###,y:###}
     */
    this.movePlayer = function(playerName, heading, callback) {
        if ( this.isMovingPlayer ) {
            return;
        } else {
            this.isMovingPlayer = true;
        }

        let params = { "heading":heading };
        let service = this;
        $http({
            method: 'PUT',
            params : params,
            headers: { 'Authorization' : userService.getJWTBearerToken() },
            url: 'http://localhost:9091/command/player/'+playerName+'/position'
        }).then(function(response) {
            callback(response.data, response.status);
            service.isMovingPlayer = false;
        },function(response) {
            service.isMovingPlayer = false;
        });
    };

    /**
     * Gets an array of Non-Players near me
     * @param callback The response data is an repeat array of type, x and y - [type,x,y...]
     */
    this.getNonPlayersNearMe = function(callback) {
        if ( this.isGettingNonPlayerNearMe ) {
            return;
        } else {
            this.isGettingNonPlayerNearMe = true;
        }

        let service = this;
        $http({
            method: 'GET',
            headers: { 'Authorization' : userService.getJWTBearerToken() },
            url: 'http://localhost:9091/command/nonplayer/near/me'
        }).then(function(response) {
            callback(response.data, response.status);
            service.isGettingNonPlayerNearMe = false;
        },function(response) {
            service.isGettingNonPlayerNearMe = false;
        });
    };
}]);