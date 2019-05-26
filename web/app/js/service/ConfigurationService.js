'use strict';

expanseApp.service('ConfigurationService', [function($http) {
    this.getProtocol = function() {
        return "https";
    };

    this.getBaseURL = function() {
        return "www.nomad.com";
    };
}]);