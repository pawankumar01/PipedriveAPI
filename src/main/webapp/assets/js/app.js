'use strict';

var app = angular.module('app', [
        'ngSanitize',
        'ngRoute',
        'ngAnimate',
        'ngCookies',
        'ngResource',
        'ngStorage',
        'ngMaterial',
        'md.data.table'
    ])
    .config(['$mdThemingProvider', function ($mdThemingProvider) {
        'use strict';
        $mdThemingProvider.theme('default').primaryPalette('blue');
    }]);

app.run(function($rootScope) {
    $rootScope.$on("$locationChangeSuccess", function(event, next, current) {


    });
});


