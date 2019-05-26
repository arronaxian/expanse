'use strict';

angular.module('expanseApp.version', [
  'expanseApp.version.interpolate-filter',
  'expanseApp.version.version-directive'
])

.value('version', '0.1');
