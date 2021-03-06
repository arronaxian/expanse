'use strict';

angular.module('expanseApp.viewLogin', ['ngRoute'])
.config(['$routeProvider','$httpProvider', function($routeProvider, $httpProvider) {
    $httpProvider.defaults.withCredentials = true;

    $routeProvider.when('/viewLogin', {
        templateUrl: 'view/login/login.html',
        controller: 'ViewLoginCtrl'
    });
}])
.controller('ViewLoginCtrl', ['$scope','UserService', function($scope, userService) {
    $scope.data = {};
    $scope.login = function() {
        userService.loginUrlEncoded($scope.data.username, $scope.data.password);
    }
}]);