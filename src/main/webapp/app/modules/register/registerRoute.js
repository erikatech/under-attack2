'use strict';

/**
 * @ngdoc function
 * @name app.route:registerRoute
 * @description
 * # registerRoute
 * Route for registering a new student
 */

angular.module('register')
	.config(['$stateProvider', function ($stateProvider) {

		$stateProvider
			.state('register', {
				url:'/register',
				templateUrl: 'app/modules/register/register.html',
				controller: 'RegisterCtrl',
				controllerAs: '$registerCtrl'
			});
	}]);
