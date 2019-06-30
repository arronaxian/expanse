'use strict';

// Declare app level module which depends on views, and core components
var expanseApp = angular.module('expanseApp', [
    'ngRoute',
    'expanseApp.viewLogin',
    'expanseApp.viewRegister',
    'expanseApp.play',
    'expanseApp.version'
]).
config(['$locationProvider', '$routeProvider', function($locationProvider, $routeProvider) {
    $locationProvider.hashPrefix('!');

    $routeProvider.otherwise({redirectTo: '/viewLogin'});
}]).
controller(['$scope', function($scope) {
}]);



