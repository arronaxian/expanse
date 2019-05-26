'use strict';

expanseApp.directive('command', ['CommandService', function(commandService) {
    return {
        restrict:'E',
        transclude: true,
        scope:{ position : '=' },
        controller:['$scope', function CommandController($scope) {
            $scope.commandContext = {
                command : ''
            };

            var heading;
            $scope.onCommand = function($event, command) {
                if ( command === 'press' ) {
                    if ($scope.commandContext.command === 'w') {
                        $scope.commandContext.command = '';
                        heading = 'north';
                    }
                    if ($scope.commandContext.command === 's') {
                        $scope.commandContext.command = '';
                        heading = 'south';
                    }
                    if ($scope.commandContext.command === 'a') {
                        $scope.commandContext.command = '';
                        heading = 'west';
                    }
                    if ($scope.commandContext.command === 'd') {
                        $scope.commandContext.command = '';
                        heading = 'east';
                    }
                }

                commandService.movePlayer('buttercup001', heading, function(response, status) {
                    if ( status === 200 ) {
                        console.log("command:onCommand " + heading);

                        $scope.position.x = response.x;
                        $scope.position.y = response.y;
                    }
                });
            }
        }],
        templateUrl: 'js/directive/commandTemplate.html'
    };
}]);
