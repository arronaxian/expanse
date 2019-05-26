'use strict';

angular.module('expanseApp.play', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/play', {
      templateUrl: 'view/play/play.html',
      controller: 'PlayController'
    });
}]).controller('PlayController', ['$scope', function($scope) {
    $scope.playContext = {
        title : 'Expanse',
        player : {
            name : 'Miss Kitty',
            position : { x : 50, y : 50 }
        }
    };
}]);