'use strict';

let expanseApp = angular.module('expanseApp', [])
.controller('PlayController', ['$scope', 'CommandService', function($scope, commandService) {
    // Phaser game engine configuation.
    let gameEngineConfig = {
        type: Phaser.WEBGL,
        width: 640,
        height: 640,
        backgroundColor: "black",
        physics: {
            default: "arcade",
            arcade: {
                Gravity: { x: 0, y: 0 }
            }
        },
        scene: [
            ArcadeScene
        ],
        pixelArt: true,
        roundPixels: true
    };

    $scope.playContext = {
        title : 'Expanse',
        player : {},
        initialize : true
    };

    $scope.movePlayer = function(heading, callback) {
        commandService.movePlayer($scope.playContext.player.name, heading, callback);
    };

    $scope.isMovingPlayer = function() {
        return commandService.isMovingPlayer;
    };

    /**
     * Initializes the game.
     */
    var init = function() {
        if ( $scope.playContext.initialize ) {
            commandService.getPlayer(undefined, function (data) {
                $scope.playContext.player = data;

                let game = new Phaser.Game(gameEngineConfig);
            });

            $scope.playContext.initialize = false;
        }
    };

    init();
}]);