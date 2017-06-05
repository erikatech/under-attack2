'use strict';

/**
 * @ngdoc function
 * @name app.route:homeRoute
 * @description
 * # homeRoute
 * Route of the module Home
 */

angular.module('home')
	.config(['$stateProvider', function ($stateProvider) {
		$stateProvider
            .state('authenticated.home', {
                url:'/home',
                templateUrl: 'app/modules/home/home.html',
                controller: 'HomeCtrl',
                controllerAs: '$home'

            })
	}]);
