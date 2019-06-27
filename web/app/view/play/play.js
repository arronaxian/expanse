'use strict';

angular.module('expanseApp.play', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/play', {
      templateUrl: 'view/play/play.html',
      controller: 'PlayController'
    });
}]).controller('PlayController', ['$scope', 'CommandService', function($scope, commandService) {
    var gameEngineConfig = {
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

    /**
     * Initializes the game.
     */
    var init = function() {
        if ( $scope.playContext.initialize ) {
            commandService.getPlayer(undefined, function (data) {
                $scope.playContext.player = data;
            });

            $scope.playContext.initialize = false;
        }

        if ( !$scope.gameController ) {
            $scope.gameController = new Phaser.Game(gameEngineConfig);
        }
    };

    init();
}]);