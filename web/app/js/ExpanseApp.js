'use strict';

// Declare app level module which depends on views, and core components
var expanseApp = angular.module('expanseApp', [
    'ngRoute',
    'expanseApp.viewLogin',
    'expanseApp.viewRegister'
]).
config(['$locationProvider', '$routeProvider', function($locationProvider, $routeProvider) {
    $locationProvider.hashPrefix('!');

    $routeProvider.otherwise({redirectTo: '/viewLogin'});
}])


