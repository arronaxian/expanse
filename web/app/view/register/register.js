'use strict';

angular.module('expanseApp.viewRegister', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/register', {
    templateUrl: 'view/register/register.html',
    controller: 'ViewRegisterCtrl'
  });
}])

.controller('ViewRegisterCtrl', [function() {
}]);