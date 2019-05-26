'use strict';

expanseApp.service('UserService', function($http, $httpParamSerializerJQLike, $window) {
    this.login = function(username, password) {
        var data = { "username":username,"password":password };
        $http({
            method: 'POST',
            data: data,
            url: 'http://localhost:8080/login'
        }).then(function(response) {
            console.log("Success " + response.headers('Authorization'));
        },
            function(response) { console.log('error');
        });

    };

    this.loginUrlEncoded = function(username, password) {
        var data = { "username":username,"password":password };

        $http({
            method: 'POST',
            headers: { 'Content-Type':'application/x-www-form-urlencoded' },
            data: $httpParamSerializerJQLike(data),
            url: 'http://localhost:8080/login'
        }).then(loginSuccessHandler,
            function(response) { console.log('error');
        });
   };

    this.loginBasic = function(username, password) {
        var auth = 'Basic ' + window.btoa(username + ':' + password);
        $http({
            method: 'POST',
            headers: { 'Authorization':auth },
            url: 'http://localhost:8080/login'
        }).then(function(response) {
                console.log("success " + response);
                console.log(response.headers()['access-token']);
                console.log(response.headers('access-token'));
                console.log(response.headers('Authorization'));
            },
            function(response) { console.log('error'); });

    };

    function loginSuccessHandler(response) {
        var auth = response.headers("Authorization");
        $window.localStorage.setItem("com.ds.expanse.jwt", auth);

        console.log("login success " + auth);
    };

    this.getJWTBearerToken = function() {
        var auth = $window.localStorage.getItem("com.ds.expanse.jwt");
        return auth;
    };
});