'use strict';
let expanseApp = angular.module('expanseApp', [])
.controller('ArcadeController', ['ArcadeService', function(arcadeService) {
    arcadeService.init();
}])
.service('ArcadeService',[function() {
    var game;
    let arcadeConfig = {
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

    this.init = function() {
        if ( !game ) {
            console.log('service.init called');
            game = new Phaser.Game(arcadeConfig);
        }

        return game;
    }
}]);

