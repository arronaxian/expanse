'use strict';

expanseApp.directive('cartograph', function() {
    return {
        restrict:'E',
        scope: {
            position : '=position'
        },
        controller : ['$scope', 'CartographService', function($scope, cartographService) {
            $scope.cartographContext = {
                map :[],
            }

            $scope.getGameMap = function() {
                cartographService.getMapGridRange($scope.position.x, $scope.position.y, 4, 4, function (mapGridRange, status) {
                    console.log('cartograph: getMapGridRange');
                    if (200 === status) {
                        var map = [];
                        var row = [];
                        for (var i = 0; i < mapGridRange.length; i++) {
                            if (i != 0 && i % 8 == 0) {
                                map.push(row);
                                row = [];
                            }

                            row.push(mapGridRange[i].tile);
                        }

                        $scope.cartographContext.map = map;

                        console.log('cartograph:map built ' + (map.length + 1));
                    }
                });
            };



            var init = function() {
                $scope.$watch('position.x', $scope.getGameMap);
                $scope.$watch('position.y', $scope.getGameMap);
            };

            init();
        }],
        templateUrl: 'js/directive/cartographTemplate.html'
    };
});
