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

            $scope.onCommand = function($event, command) {
                var heading;

                if ( command === 'press' ) {
                    if ($scope.commandContext.command === 'w') {
                        heading = 'north';
                    }
                    if ($scope.commandContext.command === 's') {
                        heading = 'south';
                    }
                    if ($scope.commandContext.command === 'a') {
                        heading = 'west';
                    }
                    if ($scope.commandContext.command === 'd') {
                        heading = 'east';
                    }

                    // Hard coded until registration works properly.
                    commandService.movePlayer('buttercup001', heading, function(response, status) {
                        if ( status === 200 ) {
                            console.log("command:onCommand " + heading);

                            $scope.position.x = response.x;
                            $scope.position.y = response.y;
                        }

                        $scope.commandContext.command = '';
                    });
                }
            }
        }],
        templateUrl: 'js/directive/commandTemplate.html'
    };
}]);
