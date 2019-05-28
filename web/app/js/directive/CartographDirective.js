'use strict';

expanseApp.directive('cartograph', function() {
    return {
        restrict:'E',
        scope: {
            position : '=position'
        },
        controller : ['$scope', 'CartographService', function($scope, cartographService) {
            var that = this;
            that.areaSize = 1;
            that.viewSize = 11;

            that.fullMap = [500];

            $scope.cartographContext = {
                map :[that.viewSize]
            }

            $scope.getGameMap = function() {
                cartographService.getMapGridRange($scope.position.x, $scope.position.y, that.areaSize, that.areaSize, function (mapGridRange, status) {
                    console.log('cartograph: getMapGridRange');
                    if (200 === status) {

                        // Copy the data into back buffer fullMap
                        var upperLeftX = $scope.position.x - that.areaSize;
                        var upperLeftY = $scope.position.y - that.areaSize ;
                        var lowerRightX = $scope.position.x + that.areaSize;
                        var lowerRightY = $scope.position.y + that.areaSize;

                        // add the data to the full map
                        var index = 0;
                        for ( var y = upperLeftY; y <= lowerRightY; y ++) {
                            for (var x = upperLeftX; x <= lowerRightX; x++) {
                                that.fullMap[y][x] = mapGridRange[index].tile;
                                index ++;
                            }
                        }

                        // Build the view map from the fullMap
                        var half = 5;

                        upperLeftX = $scope.position.x - half;
                        lowerRightX = $scope.position.x + half;
                        upperLeftY = $scope.position.y - half;
                        lowerRightY = $scope.position.y + half;
                        for ( var y = upperLeftY; y <= lowerRightY; y++ ) {
                            for ( var x = upperLeftX; x <= lowerRightX; x ++ ) {
                                var columnIndex = y - upperLeftY;
                                var rowIndex = x - upperLeftX;

                                $scope.cartographContext.map[columnIndex][rowIndex] = that.fullMap[y][x];
                            }
                        }
                    }
                });
            };

            var init = function() {
                $scope.$watch('position.x', $scope.getGameMap);
                $scope.$watch('position.y', $scope.getGameMap);

                // Initialize the map with 0 data.
                var mapSize = that.fullMap.length;

                for ( var c = 0; c < 500; c++) {
                    that.fullMap[c] = new Array(500);
                    that.fullMap[c].fill("0");
                }

                for ( var c = 0; c < that.viewSize ; c++) {
                    $scope.cartographContext.map[c] = new Array(that.viewSize);
                    $scope.cartographContext.map[c].fill("0");
                }

                $scope.getGameMap();
            };

            init();
        }],
        templateUrl: 'js/directive/cartographTemplate.html'
    };
});
